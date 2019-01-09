# 部署流程

- 查看物理机器上是否存在名字为【tdl-seckill】的容器名称，并且状态为UP，如果不是UP就删除，重新创建容器tdl-seckill，并启动。
- 查看本地是否存在名为从【tdl-seckill】的代码仓库，有的话删除；从代码库拉取对应分支的代码，maven编译打包。
- 进入容器tdl-seckill 杀掉java进程。
- 删除容器里边的jar包。
- 使用docker cp app.jar tdl-seckill:/home/app/ 拷贝文件进入容器。
- 启动容器内的app.jar 暴露对外端口。

