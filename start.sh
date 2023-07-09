#!/usr/bin/env bash

if [ ! -n "$1" ]; then
echo "you should input packege module name!"
exit 1
fi

basedir=`cd $(dirname $0); pwd -P`
echo $basedir

if [ "$1" == "common" ]; then
echo "*****************************************************************$1**************************************************************"
cd  ./common  && mvn clean deploy  -Dmaven.test.skip=true -U  && cd  ${basedir}
exit 1
fi

if [ "$1" == "binance-infra-2.0" -o "$1" == "2.0" ]; then
echo "*****************************************************************$1**************************************************************"
cd  ./binance-infra-2.0  && mvn clean deploy  -Dmaven.test.skip=true -U  && cd  ${basedir}
exit 1
fi

if [ "$1" == "api-base" ]; then
echo "*****************************************************************$1**************************************************************"
cd  ./common/api-base  && mvn clean deploy  -Dmaven.test.skip=true -U  && cd  ${basedir}
exit 1
fi

if [ "$1" == "common-lib" ]; then
echo "*****************************************************************$1**************************************************************"
cd  ./common/api-base  && mvn clean deploy  -Dmaven.test.skip=true -U  && cd  ${basedir}
exit 1
fi

if [ "$1" == "apollo" -o "$1" == "infra-apollo" ]; then
echo "*****************************************************************$1**************************************************************"
cd  ./binance-infra-2.0/binance-infra/binance-infra-apollo  && mvn clean deploy  -Dmaven.test.skip=true -U  && cd  ${basedir}
exit 1
fi

if [ "$1" == "infra-common" ]; then
echo "*****************************************************************$1**************************************************************"
cd  ./binance-infra-2.0/binance-infra/binance-infra-common  && mvn clean deploy  -Dmaven.test.skip=true -U  && cd  ${basedir}
exit 1
fi

if [ "$1" == "eureka" -o "$1" == "infra-apollo" ]; then
echo "*****************************************************************$1**************************************************************"
cd  ./binance-infra-2.0/binance-infra/binance-infra-eureka  && mvn clean deploy  -Dmaven.test.skip=true -U  && cd  ${basedir}
exit 1
fi

if [ "$1" == "monitor" -o "$1" == "infra-apollo" ]; then
echo "*****************************************************************$1**************************************************************"
cd  ./binance-infra-2.0/binance-infra/binance-infra-monitor  && mvn clean deploy  -Dmaven.test.skip=true -U  && cd  ${basedir}
exit 1
fi

if [ "$1" == "mybatis" -o "$1" == "infra-mybatis"  ]; then
echo "*****************************************************************$1**************************************************************"
cd  ./binance-infra-2.0/binance-infra/binance-infra-mybatis  && mvn clean deploy  -Dmaven.test.skip=true -U  && cd  ${basedir}
exit 1
fi

if [ "$1" == "openfeign" -o "$1" == "infra-openfeign" ]; then
echo "*****************************************************************$1**************************************************************"
cd  ./binance-infra-2.0/binance-infra/binance-infra-openfeign  && mvn clean deploy  -Dmaven.test.skip=true -U  && cd  ${basedir}
exit 1
fi

if [ "$1" == "rabbit" -o "$1" == "infra-rabbit" ]; then
echo "*****************************************************************$1**************************************************************"
cd  ./binance-infra-2.0/binance-infra/binance-infra-rabbit  && mvn clean deploy  -Dmaven.test.skip=true -U  && cd  ${basedir}
exit 1
fi

if [ "$1" == "resilience4j" -o "$1" == "infra-resilience4j"  ]; then
echo "*****************************************************************$1**************************************************************"
cd  ./binance-infra-2.0/binance-infra/binance-infra-resilience4j  && mvn clean deploy  -Dmaven.test.skip=true -U  && cd  ${basedir}
exit 1
fi

if [ "$1" == "swagger" -o "$1" == "infra-swagger"  ]; then
echo "*****************************************************************$1**************************************************************"
cd  ./binance-infra-2.0/binance-infra/binance-infra-swagger  && mvn clean deploy  -Dmaven.test.skip=true -U  && cd  ${basedir}
exit 1
fi

echo "you should input correct packege module name!"