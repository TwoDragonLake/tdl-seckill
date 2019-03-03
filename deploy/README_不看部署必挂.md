# 部署流程

- 查看物理机器上是否存在名字为【tdl-seckill】的容器名称，并且状态为UP，如果不是UP就删除，重新创建容器tdl-seckill，并启动。
- 查看本地是否存在名为从【tdl-seckill】的代码仓库，有的话删除；从代码库拉取对应分支的代码，maven编译打包。
- 进入容器tdl-seckill 杀掉java进程。
- 删除容器里边的jar包。
- 使用docker cp app.jar tdl-seckill:/home/app/ 拷贝文件进入容器。
- 启动容器内的app.jar 暴露对外端口。


tdl-deploy-auto项目：
- 在部署tdl-deploy-auto项目的物理机器上检查仓库【tdl-seckill】是够存在，存在的话先删除，然后重新clone下来，或者直接git pull一下。
- 查看物理机器上是否存在名字为【tdl-seckill】的容器名称，并且状态为UP，如果不是UP就删除，重新创建容器tdl-seckill，并启动。
- maven编译目标工程
- 使用docker cp app.jar tdl-seckill:/home/app/ 拷贝文件进入容器。
- 使用docker cp restart.sh tdl-seckill:/home/app/ 拷贝文件进入容器。restart.sh的只要内容是杀掉之前的java进程，重新启动新的jar包。
- 执行docker exec -it $DOCKER_ID /bin/bash -c '/home/app/exec restart.sh'


