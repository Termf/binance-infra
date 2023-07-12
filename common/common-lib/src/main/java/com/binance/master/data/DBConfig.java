package com.binance.master.data;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.util.Strings;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.commons.util.InetUtilsProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.util.StopWatch;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.binance.master.commons.NamedThreadFactory;
import com.binance.master.constant.Constant;
import com.binance.master.constant.DBConst;
import com.binance.master.data.sharding.TableRuleSharding;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import io.shardingsphere.api.algorithm.masterslave.RoundRobinMasterSlaveLoadBalanceAlgorithm;
import io.shardingsphere.api.config.rule.MasterSlaveRuleConfiguration;
import io.shardingsphere.api.config.rule.ShardingRuleConfiguration;
import io.shardingsphere.api.config.rule.TableRuleConfiguration;
import io.shardingsphere.core.constant.properties.ShardingPropertiesConstant;
import io.shardingsphere.core.keygen.DefaultKeyGenerator;
import io.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import lombok.extern.log4j.Log4j2;

/**
 * 数据连接基础配置类
 */
@Log4j2
public abstract class DBConfig {

    private static InetUtils inetUtils;
    private static volatile InetAddress LOCAL_ADDRESS = null;

    private String mapperLocation;

    @Resource
    protected ApplicationContext applicationContext;

    protected Integer maxConnectionsSizePerQuery;

    protected Boolean sqlShow;

    @Resource
    protected Environment env;

    static {
        System.setProperty("flyway.enabled", "false");
    }

    protected void setMapperLocation(String mapperLocation) {
        this.mapperLocation = mapperLocation;
    }

    protected String getMapperLocation() {
        if (mapperLocation == null) {
            mapperLocation = StringUtils.removeEnd(this.getClass().getResource("").toString(), "configs/") + "**/*.xml";
        }
        return mapperLocation;
    }

    protected abstract DataSource dataSource() throws SQLException;

    private void initWorkerId() {
        long workerId = genWorkerIdFromIp();
        DefaultKeyGenerator.setWorkerId(workerId);
    }

    private long genWorkerIdFromIp() {
        InetAddress address = getAddress();
        byte[] ipAddressByteArray = address.getAddress();
        long workerId;
        int ipAddressLength = ipAddressByteArray.length;
        if (ipAddressLength == 4 || ipAddressLength == 16) {
            log.info("use new snow flake");
            // 取最后16个bit位转为long
            workerId = ((ipAddressByteArray[ipAddressLength - 2] & 0B11111111) << Byte.SIZE) + (ipAddressByteArray[ipAddressLength - 1] & 0B11111111);
            workerId = 0B1111111111111111 & workerId;
        } else {
            throw new IllegalStateException("Bad LocalHost InetAddress, please check your network!");
        }
        log.info("DBConfig.genWorkerIdFromIp: {}, localIp:{}", workerId, getIp());
        return workerId;
    }

    /**
     * 调用这个实例化DataSource 走Sharding
     *
     * @param datas
     * @param tableRuleMap
     * @return
     * @throws SQLException
     * @author wangzhenhua
     */
    protected DataSource dataSource(JSONObject datas, Map<String, TableRuleSharding> tableRuleMap) throws SQLException {
        initWorkerId();// 设置全局ip分布式主键
        final ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        final Map<String, Future<DataSource>> dataSourceMapAll = new HashMap<>();
        final Map<String, Future<DataSource>> masterDataSourceMapAll = new HashMap<>();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ExecutorService executorService = Executors.newFixedThreadPool(20, new NamedThreadFactory("data-source-init"));
        for (String dbName : datas.keySet()) {
            final Map<String, Future<DataSource>> dataSourceMap = new HashMap<>();
            String masterDataSourceName = null;
            JSONArray dbs = datas.getJSONArray(dbName);
            for (int i = 0; i < dbs.size(); i++) {
                String prefix = dbs.getString(i);

                Future<DataSource> dataSource = executorService.submit(new DataSourceCallable(prefix));
                String poolName = dbName + "-" + this.env.getProperty(prefix + ".poolName", String.class);
                if (masterDataSourceName == null) {
                    masterDataSourceName = poolName;
                } else {
                    dataSourceMap.put(poolName, dataSource);
                }
                dataSourceMapAll.put(poolName, dataSource);
                if (!env.getProperty(prefix + ".readOnly", Boolean.class)) {// 获取写库
                    masterDataSourceMapAll.put(dbName, dataSource);
                }
            }

            List<String> slaveDataSourceNames = new ArrayList<>();
            if (dataSourceMap.size() > 0) {
                slaveDataSourceNames.addAll(dataSourceMap.keySet());
            } else {// 只有一个读写同一个库
                slaveDataSourceNames.add(masterDataSourceName);
            }
            MasterSlaveRuleConfiguration masterSlaveRuleConfig = new MasterSlaveRuleConfiguration(dbName, masterDataSourceName, slaveDataSourceNames,
                    new RoundRobinMasterSlaveLoadBalanceAlgorithm());

            shardingRuleConfig.getMasterSlaveRuleConfigs().add(masterSlaveRuleConfig);
        }


        if (tableRuleMap != null && !tableRuleMap.isEmpty()) {
            tableRuleMap.forEach((e, v) -> {
                final TableRuleConfiguration trc = new TableRuleConfiguration();
                trc.setLogicTable(v.getLogicTable());
                trc.setActualDataNodes(v.getActualDataNodes());
                trc.setDatabaseShardingStrategyConfig(v.getDatabaseShardingStrategyConfig());
                trc.setTableShardingStrategyConfig(v.getTableShardingStrategyConfig());
                trc.setKeyGeneratorColumnName(v.getKeyGeneratorColumnName());
                trc.setKeyGenerator(v.getKeyGenerator());
                trc.setLogicIndex(v.getLogicIndex());

                shardingRuleConfig.getTableRuleConfigs().add(trc);
                shardingRuleConfig.getBindingTableGroups().add(trc.getLogicTable());
            });
        }
        stopWatch.stop();
        log.info("setting up data source takes {} ms", stopWatch.getLastTaskTimeMillis());

        final Properties properties = new Properties();
        properties.put(ShardingPropertiesConstant.SQL_SHOW.getKey(), env.getProperty("sql.show", Boolean.class, false).toString());
        if (sqlShow != null) {
            properties.put(ShardingPropertiesConstant.SQL_SHOW.getKey(), sqlShow.toString());
        }
        properties.put(ShardingPropertiesConstant.EXECUTOR_SIZE.getKey(),
                env.getProperty("executor.size", Integer.class, Integer.parseInt(ShardingPropertiesConstant.EXECUTOR_SIZE.getDefaultValue()))
                        .toString());
        if (maxConnectionsSizePerQuery != null) {
            properties.put(ShardingPropertiesConstant.MAX_CONNECTIONS_SIZE_PER_QUERY.getKey(), maxConnectionsSizePerQuery);
        }

        if ("false".equals(env.getProperty(DBConst.DB_SHARDING_TABLE_INIT_SWITCH))) {
            log.warn("Skipped table init, you can open it by set {}={}", DBConst.DB_SHARDING_TABLE_INIT_SWITCH, true);
        } else if (this.isDev() || (this.isProd() && StringUtils.equalsIgnoreCase("gray", getEnvFlag()))) {
            // only init check tables in gray env and prod
            initTables(tableRuleMap, masterDataSourceMapAll, stopWatch);
        }

        stopWatch.start();
        DataSource dataSource = ShardingDataSourceFactory.createDataSource(waiting4DataSourceMap(dataSourceMapAll), shardingRuleConfig,
                new ConcurrentHashMap<>(), properties);
        stopWatch.stop();
        log.info("createDataSource end consuming: {} Seconds", stopWatch.getLastTaskTimeMillis() / 1000);

        executorService.shutdown();
        log.debug("executor service was shutdown!");
        return dataSource;
    }

    /**
     * initialize the table according the table rules.
     *
     * @param tableRuleMap
     * @param masterDataSourceMapAll
     * @param stopWatch
     */
    private void initTables(Map<String, TableRuleSharding> tableRuleMap, Map<String, Future<DataSource>> masterDataSourceMapAll,
            StopWatch stopWatch) {
        stopWatch.start();
        log.info("Checking tables start, you can close it by set {}={}", DBConst.DB_SHARDING_TABLE_INIT_SWITCH, false);
        Map<String, DataSource> masterDataSourceMap = waiting4DataSourceMap(masterDataSourceMapAll);
        if (tableRuleMap != null && !tableRuleMap.isEmpty()) {
            CountDownLatch latch = new CountDownLatch(tableRuleMap.size());
            tableRuleMap.forEach((e, v) -> {
                new Thread(() -> {
                    v.initTable(masterDataSourceMap);
                    latch.countDown();
                }, "init-table-" + v.getLogicTable()).start();
            });
            // waiting for all the threads above to be done
            try {
                latch.await();
            } catch (InterruptedException e) {
                log.warn("table initialization was interrupted!", e);
            }
        }
        stopWatch.stop();
        log.info("checking tables end consuming: {} Seconds", stopWatch.getLastTaskTimeMillis() / 1000);
    }

    /**
     * waiting for the data source to be returned via Future.get().
     *
     * @param dataSourceFutureMap
     * @return
     */
    private Map<String, DataSource> waiting4DataSourceMap(Map<String, Future<DataSource>> dataSourceFutureMap) {
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        log.info("start to fetch data source metadata ...");
        long start = System.currentTimeMillis();
        Collection<String> badDataSourceNames = new ConcurrentLinkedQueue<>();
        dataSourceFutureMap.entrySet().stream().forEach(e -> {
            try {
                log.info("fetching data source [{}]", e.getKey());
                DataSource dataSource = e.getValue().get();
                dataSourceMap.put(e.getKey(), dataSource);
            } catch (InterruptedException ex) {
                log.error("get data source metadata was interrupted!", ex);
                badDataSourceNames.add(e.getKey());
            } catch (ExecutionException ex) {
                log.error("execution error!", ex);
                badDataSourceNames.add(e.getKey());
            }
        });
        if (badDataSourceNames.size() > 0 && !StringUtils.equalsIgnoreCase("true", env.getProperty(DBConst.DB_ALLOW_BAD_DATASOURCE_STARTUP_SWITCH))) {
            log.error("Exit the application(exitCode: -1). Some data sources got exception when fetching data source. bad data sources: {}",
                    Strings.join(badDataSourceNames, ','));
            SpringApplication.exit(applicationContext, () -> -1);
        }
        log.info("end of fetching data source metadata. duration(ms):{}", System.currentTimeMillis() - start);
        return dataSourceMap;
    }

    /**
     * 获取当前实例的灰度标签。如果是正常实例，则返回null.
     *
     * @return
     */
    private String getEnvFlag() {
        String envFlag = null;
        if (env != null) {
            envFlag = env.getProperty("eureka.instance.metadataMap.envflag", String.class);
            if (envFlag == null) {
                envFlag = env.getProperty("spring.application.envflag", String.class);
            }
        }
        return Constant.ENV_FLAG_NORMAL.equals(envFlag) ? null : envFlag;
    }


    public boolean isDev() {
        if (env != null) {
            final String active = env.getProperty("spring.profiles.active", String.class);
            if (active != null)
                return StringUtils.equalsIgnoreCase(active, "dev") || StringUtils.endsWithIgnoreCase(active, "dev")
                        || StringUtils.equalsIgnoreCase(active, "local") || StringUtils.endsWithIgnoreCase(active, "local");
        }

        String env = System.getProperty("env", "dev");
        if (StringUtils.equalsIgnoreCase("dev", env) || StringUtils.endsWithIgnoreCase(env, "dev")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isProd() {
        if (env != null) {
            final String active = env.getProperty("spring.profiles.active", String.class);
            if (active != null)
                return StringUtils.equalsIgnoreCase(active, "prod") || StringUtils.endsWithIgnoreCase(active, "prod");
        }
        String env = System.getProperty("env", "prod");
        if (StringUtils.equalsIgnoreCase("prod", env) || StringUtils.endsWithIgnoreCase(env, "prod")) {
            return true;
        } else {
            return false;
        }
    }

    private static synchronized InetUtils getInetUtils() {
        if (inetUtils == null) {
            final Map<String, String> envs = System.getenv();
            final Properties properties = new Properties();
            for (Map.Entry<String, String> env : envs.entrySet()) {
                if (StringUtils.startsWith(env.getKey(), InetUtilsProperties.PREFIX)) {
                    properties.put(env.getKey(), env.getValue());
                }
            }
            final Properties temp = System.getProperties();
            for (Map.Entry<Object, Object> enu : temp.entrySet()) {
                if (StringUtils.startsWith(enu.getKey().toString(), InetUtilsProperties.PREFIX)) {
                    properties.put(enu.getKey().toString(), enu.getValue());
                }
            }
            // System.out.println("InetUtils Params:" + JSON.toJSONString(properties));
            final MutablePropertySources propertySources = new MutablePropertySources();
            PropertiesPropertySource propertySource = new PropertiesPropertySource("sys-init", properties);
            propertySources.addLast(propertySource);

            final InetUtilsProperties target = new InetUtilsProperties();
            // springboot1.0
            // final RelaxedDataBinder binder = new RelaxedDataBinder(target, InetUtilsProperties.PREFIX);
            // binder.bind(new PropertySourcesPropertyValues(propertySources));

            // springboot2.0
            Binder newBinder = new Binder(new MapConfigurationPropertySource(properties));
            newBinder.bind(InetUtilsProperties.PREFIX, Bindable.ofInstance(target));

            inetUtils = new InetUtils(target);
        }
        return inetUtils;
    }

    private static InetAddress getFirstValidAddress() {
        InetAddress result = null;
        InetUtils inetUtils = getInetUtils();
        if (inetUtils != null) {
            return inetUtils.findFirstNonLoopbackAddress();
        }
        try {
            int lowest = Integer.MAX_VALUE;
            for (Enumeration<NetworkInterface> nics = NetworkInterface.getNetworkInterfaces(); nics.hasMoreElements();) {
                NetworkInterface ifc = nics.nextElement();
                if (ifc.isUp()) {
                    log.info("Testing interface: " + ifc.getDisplayName());
                    if (ifc.getIndex() < lowest || result == null) {
                        lowest = ifc.getIndex();
                    } else if (result != null) {
                        continue;
                    }
                    // @formatter:off
                    for (Enumeration<InetAddress> addrs = ifc.getInetAddresses(); addrs.hasMoreElements();) {
                        InetAddress address = addrs.nextElement();
                        if (address instanceof Inet4Address && !address.isLoopbackAddress()) {
                            log.info("Found non-loopback interface: " + ifc.getDisplayName());
                            result = address;
                        }
                    }
                    // @formatter:on
                }
            }
        } catch (IOException ex) {
            log.error("Cannot get first non-loopback address", ex);
        }
        if (result != null) {
            return result;
        }
        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            log.warn("Unable to retrieve localhost");
        }
        return null;
    }

    private static InetAddress getAddress() {
        if (LOCAL_ADDRESS != null) {
            return LOCAL_ADDRESS;
        }
        InetAddress localAddress = getFirstValidAddress();
        LOCAL_ADDRESS = localAddress;
        return localAddress;
    }

    private static String getIp() {
        InetAddress address = getAddress();
        if (address == null) {
            return null;
        }
        return address.getHostAddress();
    }

    /**
     * W允许直接调用这个进行实例化 DataSource W直接走HikariDataSource W建议走单表查询的可以使用这个进行实例化可以避免Sharding支持sql不全已经bug卡死问题
     * W建议链接使用aurora模式
     *
     * @return
     */
    protected DataSource dataSource(String prefix) {
        HikariConfig config = new HikariConfig();
        config.setPoolName(env.getProperty(prefix + ".poolName", String.class));
        config.setDriverClassName(env.getProperty(prefix + ".driverClassName", String.class));
        config.setJdbcUrl(env.getProperty(prefix + ".jdbcUrl", String.class));
        config.setUsername(env.getProperty(prefix + ".username", String.class));
        config.setPassword(env.getProperty(prefix + ".password", String.class));
        config.setReadOnly(env.getProperty(prefix + ".readOnly", Boolean.class));
        config.setConnectionTimeout(env.getProperty(prefix + ".connectionTimeout", Long.class));
        config.setIdleTimeout(env.getProperty(prefix + ".idleTimeout", Long.class));
        config.setMaxLifetime(env.getProperty(prefix + ".maxLifetime", Long.class));
        config.setMaximumPoolSize(env.getProperty(prefix + ".maximumPoolSize", Integer.class));
        config.setMinimumIdle(env.getProperty(prefix + ".minimumIdle", Integer.class));
        return new HikariDataSource(config);
    }

    protected DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    protected SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(getMapperLocation()));
        sessionFactory.setConfiguration(this.getConfiguration());
        return sessionFactory.getObject();
    }

    protected Configuration getConfiguration() {
        Configuration config = new Configuration();
        // 使全局的映射器启用或禁用缓存
        config.setCacheEnabled(true);
        // 全局启用或禁用延迟加载。当禁用时，所有关联对象都会即时加载
        config.setLazyLoadingEnabled(false);
        // 配置默认的执行器。SIMPLE 就是普通的执行器；REUSE 执行器会重用预处理语句（prepared statements）； BATCH 执行器将重用语句并执行批量更新。
        config.setDefaultExecutorType(ExecutorType.SIMPLE);
        return config;
    }

    class DataSourceCallable implements Callable<DataSource> {
        private String prefix;

        public DataSourceCallable(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public DataSource call() throws Exception {
            return dataSource(prefix);
        }
    }
}
