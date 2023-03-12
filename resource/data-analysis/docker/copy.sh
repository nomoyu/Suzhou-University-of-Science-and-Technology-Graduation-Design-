#!/bin/sh

# 复制项目的文件到对应docker路径，便于一键生成镜像。
usage() {
	echo "Usage: sh copy.sh"
	exit 1
}


# copy sql
echo "begin copy sql "
cp ../sql/ry_20220814.sql ./mysql/db
cp ../sql/ry_config_20220510.sql ./mysql/db

# copy html
echo "begin copy html "
cp -r ../data-analysis -ui/dist/** ./nginx/html/dist


# copy jar
echo "begin copy data-analysis -gateway "
cp ../data-analysis -gateway/target/data-analysis -gateway.jar ./data-analysis /gateway/jar

echo "begin copy data-analysis -auth "
cp ../data-analysis -auth/target/data-analysis -auth.jar ./data-analysis /auth/jar

echo "begin copy data-analysis -visual "
cp ../data-analysis -visual/data-analysis -monitor/target/data-analysis -visual-monitor.jar  ./data-analysis /visual/monitor/jar

echo "begin copy data-analysis -modules-system "
cp ../data-analysis -modules/data-analysis -system/target/data-analysis -modules-system.jar ./data-analysis /modules/system/jar

echo "begin copy data-analysis -modules-file "
cp ../data-analysis -modules/data-analysis -file/target/data-analysis -modules-file.jar ./data-analysis /modules/file/jar

echo "begin copy data-analysis -modules-job "
cp ../data-analysis -modules/data-analysis -job/target/data-analysis -modules-job.jar ./data-analysis /modules/job/jar

echo "begin copy data-analysis -modules-gen "
cp ../data-analysis -modules/data-analysis -gen/target/data-analysis -modules-gen.jar ./data-analysis /modules/gen/jar

