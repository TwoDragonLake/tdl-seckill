#!/bin/sh

# author: ceaserwang@outlook.com

#config var
host_work_dir="/data/"
docker_container_name="tdl-seckill"
docker_image_name="tdl-seckill"
docker_image_tag="v1"
docker_user_name="tdlceaserwang"
docker_user_password="*****"
push_docker_image_to_docker_hub=0
git_repository_name=tdl-seckill
git_repository_url=https://github.com/TwoDragonLake/
git_repository_branch=tdl-seckill-2.0
#temp var

has_rebuild_docker_image=0

containerStatusIsUp(){
    docker_container_status=`docker container ls -a | awk '{print $5 $7}' | grep  $docker_container_name | awk '{print $5}'`
    if [[ -n $docker_container_status ]]; then
        docker_container_status=`echo $docker_container_status | cut -b 1-2`
        if [[ "UP" -eq  $docker_container_status ]]; then
             return 1
        fi
    fi
    return 0
}

cloneCode(){
    cd $host_work_dir
    if [ ! -d "$host_work_dir$git_repository_name" ]; then
        git clone $git_repository_url$git_repository_name
        cd $git_repository_name
        git checkout $git_repository_branch
        return 1
    else
        rm -rf $git_repository_name
        git clone $git_repository_url$git_repository_name
        cd $git_repository_name
        git checkout $git_repository_branch
        return 1
    fi
    return 0
}

mavenPackage(){
   cd $host_work_dir$git_repository_name
   maven_build_result=`mvn clean install -DskipTests -U > maven_build.log | tail -7 maven_build.log | awk  '{if($3!="SUCCESS") {print $3}'`
   if [[ $maven_build_result -eq "SUCCESS" ]]; then
        mv $host_work_dir$git_repository_name
   fi


}


startContainer(){
    if [[ 1 -eq $has_rebuild_docker_image ]]; then
        containerStatusIsUp
        if [ $? -eq "1" ]; then
            docker stop  $docker_container_name
        fi
        cleanContainers
        docker run -d --name $docker_container_name $docker_image_name:$docker_image_tag
        containerStatusIsUp
        if [ $? -eq "1" ]; then
            echo "container $docker_container_name is start ok."
            return 1
        else
            echo "container $docker_container_name is start faild."
             return 0
        fi
    else
        containerStatusIsUp
        if [ $? -eq "1" ]; then
            return 1
        else
            cleanContainers
            docker run -d --name $docker_container_name $docker_image_name:$docker_image_tag
            containerStatusIsUp
            if [ $? -eq "1" ]; then
                echo "container $docker_container_name is start ok."
                return 1
            else
                echo "container $docker_container_name is start faild."
                return 0
            fi
        fi
    fi
}

# clean all unavliable containers
cleanContainers(){
expect -c "set timeout -1;
    spawn docker container prune;
    expect {
      *[y/N]* {send \"y\r\";exp_continue;}
      eof   {exit 0;}
    }";
}


# clean all unavliable images
cleanImages(){
expect -c "set timeout -1;
    spawn docker image prune;
    expect {
      *[y/N]* {send \"y\r\";exp_continue;}
      eof   {exit 0;}
    }";
}


# if container is exist and status is Up, then execute clone app  code to the local for  maven package
docker_container_status=`docker container ls -a | awk '{print $5 $7}' | grep  $docker_container_name | awk '{print $5}'`
if [[ -n $docker_container_status ]]; then
    docker_container_status=`echo $docker_container_status | cut -b 1-2`
    if [[ "UP" -eq  $docker_container_status ]]; then
         cloneCode
         if [ $? -eq "1" ]; then
            mavenPackage
         fi
    fi
else
    #
    concurrent_docker_image_name=`dokcer images | awk '{if($1!="<none>") {print $1 $2}' | grep  $docker_image_name | awk '{print $1}'`
    if [[ -z $concurrent_docker_image_name ]]; then
        cd $host_work_dir$git_repository_name/deploy/docker/
        docker build -t $docker_image_name:$docker_image_tag .
        concurrent_docker_image_name=`dokcer images | awk '{if($1!="<none>") {print $1 $2}' | grep  $docker_image_name | awk '{print $1}'`
        if [[ -z $concurrent_docker_image_name ]]; then
            echo "dokcer image $docker_image_name build faid,build command: docker build -t $docker_image_name:$docker_image_tag ."
            exit 1
        fi
        has_rebuild_docker_image=1
        if [[ 1 -eq $push_docker_image_to_docker_hub ]]; then
            docker tag $docker_image_name:$docker_image_tag $docker_user_name/$docker_image_name:$docker_image_tag
        fi
    fi
    startContainer
    if [ $? -eq "1" ]; then
         cloneCode
         if [ $? -eq "1" ]; then
            mavenPackage
         fi
    fi

fi


