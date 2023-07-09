#!/usr/bin/env bash

basedir=`cd $(dirname $0); pwd -P`
echo $basedir
cd  ./binance-infra-build                          && mvn clean deploy -Pserver -U -DskipTests -Dmaven.javadoc.skip=true && cd  ${basedir}
cd  ./binance-infra/binance-infra-swagger          && mvn clean deploy -Pserver -U -DskipTests -Dmaven.javadoc.skip=true && cd  ${basedir}
cd  ./binance-infra/binance-infra-common           && mvn clean deploy -Pserver -U -DskipTests -Dmaven.javadoc.skip=true && cd  ${basedir}
cd  ./binance-infra/binance-infra-monitor          && mvn clean deploy -Pserver -U -DskipTests -Dmaven.javadoc.skip=true && cd  ${basedir}
cd  ./binance-infra/binance-infra-apollo           && mvn clean deploy -Pserver -U -DskipTests -Dmaven.javadoc.skip=true && cd  ${basedir}
cd  ./binance-infra/binance-infra-eureka           && mvn clean deploy -Pserver -U -DskipTests -Dmaven.javadoc.skip=true && cd  ${basedir}
cd  ./binance-infra/binance-infra-openfeign        && mvn clean deploy -Pserver -U -DskipTests -Dmaven.javadoc.skip=true && cd  ${basedir}
cd  ./binance-infra/binance-infra-redis            && mvn clean deploy -Pserver -U -DskipTests -Dmaven.javadoc.skip=true && cd  ${basedir}
cd  ./binance-infra/binance-infra-mybatis          && mvn clean deploy -Pserver -U -DskipTests -Dmaven.javadoc.skip=true && cd  ${basedir}
cd  ./binance-infra/binance-infra-rabbit           && mvn clean deploy -Pserver -U -DskipTests -Dmaven.javadoc.skip=true && cd  ${basedir}
cd  ./binance-infra/binance-infra-amazons3         && mvn clean deploy -Pserver -U -DskipTests -Dmaven.javadoc.skip=true && cd  ${basedir}
cd  ./binance-infra/binance-infra-web              && mvn clean deploy -Pserver -U -DskipTests -Dmaven.javadoc.skip=true && cd  ${basedir}
cd  ./binance-infra/binance-infra-resilience4j     && mvn clean deploy -Pserver -U -DskipTests -Dmaven.javadoc.skip=true && cd  ${basedir}
cd  ./binance-infra/binance-infra-mbx              && mvn clean deploy -Pserver -U -DskipTests -Dmaven.javadoc.skip=true && cd  ${basedir}
cd  ./binance-infra/binance-infra-websocket        && mvn clean deploy -Pserver -U -DskipTests -Dmaven.javadoc.skip=true && cd  ${basedir}
cd  ./binance-infra/binance-infra-mongo            && mvn clean deploy -Pserver -U -DskipTests -Dmaven.javadoc.skip=true && cd  ${basedir}
cd  ./binance-infra-release                        && mvn clean deploy -Pserver -U -DskipTests -Dmaven.javadoc.skip=true && cd  ${basedir}
cd  ./binance-infra/binance-infra-sapi             && mvn clean deploy -Pserver -U -DskipTests -Dmaven.javadoc.skip=true && cd  ${basedir}

