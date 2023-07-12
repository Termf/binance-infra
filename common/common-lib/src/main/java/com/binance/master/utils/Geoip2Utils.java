package com.binance.master.utils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Postal;
import com.maxmind.geoip2.record.Subdivision;

/**
 * 
 * <pre>
 * // A File object pointing to your GeoIP2 or GeoLite2 database
 * File database = new File("/path/to/GeoIP2-City.mmdb");
 * 
 * // This creates the DatabaseReader object. To improve performance, reuse
 * // the object across lookups. The object is thread-safe.
 * DatabaseReader reader = new DatabaseReader.Builder(database).build();
 * 
 * InetAddress ipAddress = InetAddress.getByName("128.101.101.101");
 * 
 * // Replace "city" with the appropriate method for your database, e.g.,
 * // "country".
 * CityResponse response = reader.city(ipAddress);
 * 
 * Country country = response.getCountry();
 * System.out.println(country.getIsoCode()); // 'US'
 * System.out.println(country.getName()); // 'United States'
 * System.out.println(country.getNames().get("zh-CN")); // '美国'
 * 
 * Subdivision subdivision = response.getMostSpecificSubdivision();
 * System.out.println(subdivision.getName()); // 'Minnesota'
 * System.out.println(subdivision.getIsoCode()); // 'MN'
 * 
 * City city = response.getCity();
 * System.out.println(city.getName()); // 'Minneapolis'
 * 
 * Postal postal = response.getPostal();
 * System.out.println(postal.getCode()); // '55455'
 * 
 * Location location = response.getLocation();
 * System.out.println(location.getLatitude()); // 44.9733
 * System.out.println(location.getLongitude()); // -93.2323
 * </pre>
 * 
 * @author liushiming
 * @date 2021/01/20
 */
public final class Geoip2Utils {

    private static final transient Logger logger = LoggerFactory.getLogger(Geoip2Utils.class);

    private static DatabaseReader databaseReader;

    public static void init(String filePath) {
        if (FileUtils.exists(filePath)) {
            try {
                File database = new File(filePath);
                DatabaseReader reader = new DatabaseReader.Builder(database).build();
                Geoip2Utils.databaseReader = reader;
            } catch (IOException e) {
                logger.error("初始化 Geoip2 失败", e);
            }
        } else {
            logger.error("初始化 Geoip2 失败 配置路径不存在");
        }
    }

    /**
     * 获取Ip所在的城市，返回国家全称，例如：Minneapolis
     * 
     * <pre>
     * 如果传入Ip为空，则返回空字符串 
     * 如果传入IP不是标准的IP，则返回空字符串
     * </pre>
     */
    public static String getCountryCity(String ip2) {
        if (ip2 == null || databaseReader == null) {
            return StringUtils.EMPTY;
        } else {
            try {
                InetAddress ipAddress = InetAddress.getByName(ip2);
                CityResponse response = databaseReader.city(ipAddress);
                City city = response.getCity();
                return city.getName();
            } catch (Throwable e) {
                logger.error(e.getMessage(), e);
                return StringUtils.EMPTY;
            }
        }
    }

    /**
     * 获取Ip所在的国家，返回国家简写，例如：US
     * 
     * <pre>
     * 如果传入Ip为空，则返回空字符串 
     * 如果传入IP不是标准的IP，则返回空字符串
     * </pre>
     */
    public static String getCountryShort(String ip2) {
        if (ip2 == null || databaseReader == null) {
            return StringUtils.EMPTY;
        } else {
            try {
                InetAddress ipAddress = InetAddress.getByName(ip2);
                CityResponse response = databaseReader.city(ipAddress);
                Country country = response.getCountry();
                return country.getIsoCode();
            } catch (Throwable e) {
                logger.error(e.getMessage(), e);
                return StringUtils.EMPTY;
            }
        }
    }

    /**
     * 获取Ip所在详情
     * 
     * <pre>
     * 1: 国家
     * 2: 城市
     * 3: 住址
     * 4: 邮箱
     * 5: 经纬度
     * </pre>
     */
    public static Geoip2Detail getDetail(String ip2) {
        if (ip2 == null || databaseReader == null) {
            return null;
        } else {
            try {
                InetAddress ipAddress = InetAddress.getByName(ip2);
                CityResponse response = databaseReader.city(ipAddress);
                Country country = response.getCountry();
                Subdivision subdivision = response.getMostSpecificSubdivision();
                City city = response.getCity();
                Postal postal = response.getPostal();
                Location location = response.getLocation();
                return new Geoip2Detail(country, subdivision, city, postal, location);
            } catch (Throwable e) {
                logger.error(e.getMessage(), e);
                return null;
            }
        }
    }

    public static class Geoip2Detail {
        // 国家
        private final Country country;
        // 住房
        private final Subdivision subdivision;
        // 城市
        private final City city;
        // 邮箱
        private final Postal Postal;
        // 经纬度
        private final Location Location;

        public Geoip2Detail(Country country, Subdivision subdivision, City city, Postal postal, Location location) {
            this.country = country;
            this.subdivision = subdivision;
            this.city = city;
            Postal = postal;
            Location = location;
        }

        public Country getCountry() {
            return country;
        }

        public Subdivision getSubdivision() {
            return subdivision;
        }

        public City getCity() {
            return city;
        }

        public Postal getPostal() {
            return Postal;
        }

        public Location getLocation() {
            return Location;
        }

    }

}
