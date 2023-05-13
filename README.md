4个redis

1个mysql

### 主机配置

更改各模块注册中心ip地址

jar包移动到docker目录下





# graduate-project

### docker 配置

1. 安装Docker的依赖库。

```
yum install -y yum-utils device-mapper-persistent-data lvm2
```

2. 添加Docker CE的软件源信息。

```
yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
```

3. 安装Docker CE。

```
yum makecache fast &&
yum -y install docker-ce
```

4. 启动Docker服务。

```
systemctl start docker
```

5. 配DockerHub镜像加速器。

```
sudo mkdir -p /etc/docker
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://8yyenugx.mirror.aliyuncs.com"]
}
EOF
sudo systemctl daemon-reload
sudo systemctl restart docker
```

6. 重启Docker服务。

```
systemctl restart docker
```

### docker compose 配置

```
sudo curl -L "https://github.com/docker/compose/releases/download/1.26.2/dockercompose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose 

curl -L https://get.daocloud.io/docker/compose/releases/download/1.25.5/dockercompose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose
```

设置权限

sudo chomd +x docker-compose



### docker-compose.yml

```yaml
version : '1.0'
services:
  dataAnalysis-nacos:
    container_name: dataAnalysis-nacos
    image: nacos/nacos-server
    build:
      context: ./nacos
    environment:
      - MODE=standalone
    volumes:
      - ./nacos/logs/:/home/nacos/logs
      - ./nacos/conf/application.properties:/home/nacos/conf/application.properties
    ports:
      - "0.0.0.0:8848:8848"
      - "0.0.0.0:9848:9848"
      - "0.0.0.0:9849:9849"
  dataAnalysis-nginx:
    container_name: dataAnalysis-nginx
    image: nginx
    build:
      context: ./nginx
    ports:
      - "80:80"
    volumes:
      - ./nginx/html/dist:/home/dataAnalysis/projects/dataAnalysis-ui
      - ./nginx/conf/nginx.conf:/etc/nginx/nginx.conf
      - ./nginx/logs:/var/log/nginx
      - ./nginx/conf.d:/etc/nginx/conf.d
    depends_on:
      - dataAnalysis-gateway
    links:
      - dataAnalysis-gateway
  dataAnalysis-gateway:
    container_name: dataAnalysis-gateway
    build:
      context: ./dataAnalysis/gateway
      dockerfile: dockerfile
    ports:
      - "8080:8080"
  dataAnalysis-auth:
    container_name: dataAnalysis-auth
    build:
      context: ./dataAnalysis/auth
      dockerfile: dockerfile
    ports:
      - "9200:9200"
  dataAnalysis-modules-system:
    container_name: dataAnalysis-modules-system
    build:
      context: ./dataAnalysis/modules/system
      dockerfile: dockerfile
    ports:
      - "9201:9201"
  dataAnalysis-modules-gen:
    container_name: dataAnalysis-modules-gen
    build:
      context: ./dataAnalysis/modules/gen
      dockerfile: dockerfile
    ports:
      - "9202:9202"
  dataAnalysis-modules-job:
    container_name: dataAnalysis-modules-job
    build:
      context: ./dataAnalysis/modules/job
      dockerfile: dockerfile
    ports:
      - "9203:9203"
  dataAnalysis-modules-file:
    container_name: dataAnalysis-modules-file
    build:
      context: ./dataAnalysis/modules/file
      dockerfile: dockerfile
    ports:
      - "9300:9300"
    volumes:
    - ./dataAnalysis/uploadPath:/home/dataAnalysis/uploadPath
  dataAnalysis-visual-monitor:
    container_name: dataAnalysis-visual-monitor
    build:
      context: ./dataAnalysis/visual/monitor
      dockerfile: dockerfile
    ports:
      - "9100:9100"

```





### 关联html文件

mkdir -p /home/data-analysis/projects/data-analysis-ui





**修改服务注册ip地址**

文件模块不需要改ip地址（docker容器搭建）



文件模块

\# 本地文件上传    

file:

​    domain: http://127.0.0.1:9300

​    path: D:/dataAnalysis/uploadPath      /home/data-analysis/uploadPath

​    prefix: /statics



\# FastDFS配置

fdfs:

  domain: http://8.129.231.12

  soTimeout: 3000

  connectTimeout: 2000

  trackerList: 8.129.231.12:22122



\# Minio配置

minio:

  url: http://8.129.231.12:9000

  accessKey: minioadmin

  secretKey: minioadmin

  bucketName: test

### python模块说明

1. 启动项目

   终端输入命令

   ```shell
   uvicorn main:app --reload
   ```

