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
echo "begin copy data-analysis-gateway "
cp ../dataAnalysis-gateway/target/dataAnalysis-gateway.jar ./data-analysis/gateway/jar

echo "begin copy data-analysis-auth "
cp ../dataAnalysis-auth/target/dataAnalysis-auth.jar ./data-analysis/auth/jar

echo "begin copy data-analysis-visual "
cp ../dataAnalysis-visual/dataAnalysis-visual-monitor/target/dataAnalysis-visual-monitor.jar  ./data-analysis/visual/monitor/jar

echo "begin copy data-analysis -modules-system "
cp ../dataAnalysis-modules/dataAnalysis-modules-system/target/dataAnalysis-modules-system.jar ./data-analysis/modules/system/jar

echo "begin copy dataAnalysis-modules-file "
cp ../dataAnalysis-modules/dataAnalysis-modules-file/target/dataAnalysis-modules-file.jar ./data-analysis/modules/file/jar

echo "begin copy dataAnalysis-modules-job "
cp ../dataAnalysis-modules/dataAnalysis-modules-job/target/dataAnalysis-modules-job.jar ./data-analysis/modules/job/jar

echo "begin copy dataAnalysis-modules-gen "
cp ../dataAnalysis-modules/dataAnalysis-modules-gen/target/dataAnalysis-modules-gen.jar ./data-analysis/modules/gen/jar

echo "begin copy dataAnalysis-modules-business "
cp ../dataAnalysis-modules/dataAnalysis-modules-business/target/dataAnalysis-modules-business.jar ./data-analysis/modules/business/jar

