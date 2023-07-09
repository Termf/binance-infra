#!/usr/bin/env bash

basedir=`cd $(dirname $0); pwd -P`
echo $basedir
cd  ./binance-infra-build                          && mvn install -U -DskipTests -Dmaven.javadoc.skip=true && cd  ${basedir}
cd  ./binance-infra/binance-infra-swagger          && mvn install -U -DskipTests -Dmaven.javadoc.skip=true && cd  ${basedir}
cd  ./binance-infra/binance-infra-common           && mvn install -U -DskipTests -Dmaven.javadoc.skip=true && cd  ${basedir}
cd  ./binance-infra/binance-infra-monitor          && mvn install -U -DskipTests -Dmaven.javadoc.skip=true && cd  ${basedir}
cd  ./binance-infra/binance-infra-apollo           && mvn install -U -DskipTests -Dmaven.javadoc.skip=true && cd  ${basedir}
cd  ./binance-infra/binance-infra-eureka           && mvn install -U -DskipTests -Dmaven.javadoc.skip=true && cd  ${basedir}
cd  ./binance-infra/binance-infra-openfeign        && mvn install -U -DskipTests -Dmaven.javadoc.skip=true && cd  ${basedir}
cd  ./binance-infra/binance-infra-redis            && mvn install -U -DskipTests -Dmaven.javadoc.skip=true && cd  ${basedir}
cd  ./binance-infra/binance-infra-mybatis          && mvn install -U -DskipTests -Dmaven.javadoc.skip=true && cd  ${basedir}
cd  ./binance-infra/binance-infra-rabbit           && mvn install -U -DskipTests -Dmaven.javadoc.skip=true && cd  ${basedir}
cd  ./binance-infra/binance-infra-amazons3         && mvn install -U -DskipTests -Dmaven.javadoc.skip=true && cd  ${basedir}
cd  ./binance-infra/binance-infra-web              && mvn install -U -DskipTests -Dmaven.javadoc.skip=true && cd  ${basedir}
cd  ./binance-infra/binance-infra-resilience4j     && mvn install -U -DskipTests -Dmaven.javadoc.skip=true && cd  ${basedir}
cd  ./binance-infra/binance-infra-mbx              && mvn install -U -DskipTests -Dmaven.javadoc.skip=true && cd  ${basedir}
cd  ./binance-infra/binance-infra-websocket        && mvn install -U -DskipTests -Dmaven.javadoc.skip=true && cd  ${basedir}
cd  ./binance-infra/binance-infra-mongo            && mvn install -U -DskipTests -Dmaven.javadoc.skip=true && cd  ${basedir}
cd  ./binance-infra-release                        && mvn install -U -DskipTests -Dmaven.javadoc.skip=true && cd  ${basedir}
cd  ./binance-infra/binance-infra-sapi             && mvn install -U -DskipTests -Dmaven.javadoc.skip=true && cd  ${basedir}

