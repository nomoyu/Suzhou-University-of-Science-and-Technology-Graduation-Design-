#!/bin/sh

# 复制项目的文件到对应docker路径，便于一键生成镜像。
usage() {
	echo "Usage: sh copy.sh"
	exit 1
}


# copy sql
# echo "begin copy sql "
# cp ../sql/ry_20220814.sql ./mysql/db
# cp ../sql/ry_config_20220510.sql ./mysql/db

# copy html
echo "begin copy html "
cp -r ../dataAnalysis-ui/dist/** ./nginx/html/dist


# copy jar
echo "begin copy dataAnalysis-gateway "
cp ../dataAnalysis-gateway/target/dataAnalysis-gateway.jar ./dataAnalysis/gateway/jar

echo "begin copy dataAnalysis-auth "
cp ../dataAnalysis-auth/target/dataAnalysis-auth.jar ./dataAnalysis/auth/jar

echo "begin copy dataAnalysis-visual "
cp ../dataAnalysis-visual/dataAnalysis-visual-monitor/target/dataAnalysis-visual-monitor.jar  ./dataAnalysis/visual/monitor/jar

echo "begin copy dataAnalysis -modules-system "
cp ../dataAnalysis-modules/dataAnalysis-modules-system/target/dataAnalysis-modules-system.jar ./dataAnalysis/modules/system/jar

echo "begin copy dataAnalysis-modules-file "
cp ../dataAnalysis-modules/dataAnalysis-modules-file/target/dataAnalysis-modules-file.jar ./dataAnalysis/modules/file/jar

echo "begin copy dataAnalysis-modules-job "
cp ../dataAnalysis-modules/dataAnalysis-modules-job/target/dataAnalysis-modules-job.jar ./dataAnalysis/modules/job/jar

echo "begin copy dataAnalysis-modules-gen "
cp ../dataAnalysis-modules/dataAnalysis-modules-gen/target/dataAnalysis-modules-gen.jar ./dataAnalysis/modules/gen/jar

echo "begin copy dataAnalysis-modules-business "
cp ../dataAnalysis-modules/dataAnalysis-modules-business/target/dataAnalysis-modules-business.jar ./dataAnalysis/modules/business/jar

