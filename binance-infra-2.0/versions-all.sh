#!/usr/bin/env bash

basedir=`cd $(dirname $0); pwd -P`
echo $basedir
cd  ./binance-infra/binance-infra-common           && mvn versions:set -DnewVersion=$1 && mvn versions:commit && cd  ${basedir}
cd  ./binance-infra/binance-infra-apollo           && mvn versions:set -DnewVersion=$1 && mvn versions:commit && cd  ${basedir}
cd  ./binance-infra/binance-infra-eureka           && mvn versions:set -DnewVersion=$1 && mvn versions:commit && cd  ${basedir}
cd  ./binance-infra/binance-infra-openfeign        && mvn versions:set -DnewVersion=$1 && mvn versions:commit && cd  ${basedir}
cd  ./binance-infra/binance-infra-redis            && mvn versions:set -DnewVersion=$1 && mvn versions:commit && cd  ${basedir}
cd  ./binance-infra/binance-infra-mybatis          && mvn versions:set -DnewVersion=$1 && mvn versions:commit && cd  ${basedir}
cd  ./binance-infra/binance-infra-rabbit           && mvn versions:set -DnewVersion=$1 && mvn versions:commit && cd  ${basedir}
cd  ./binance-infra/binance-infra-monitor          && mvn versions:set -DnewVersion=$1 && mvn versions:commit && cd  ${basedir}
cd  ./binance-infra/binance-infra-amazons3         && mvn versions:set -DnewVersion=$1 && mvn versions:commit && cd  ${basedir}
cd  ./binance-infra/binance-infra-web              && mvn versions:set -DnewVersion=$1 && mvn versions:commit && cd  ${basedir}

