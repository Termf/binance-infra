package com.binance.platform.openfeign.idempotent.store;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.binance.platform.openfeign.idempotent.IdempotentRepository;

public class DbIdempotentRepository implements IdempotentRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(DbIdempotentRepository.class);

    private static final String CREATE =
    // @formatter:off
        "CREATE TABLE `idempotent` ("+
        "    `id` bigint(20) NOT NULL AUTO_INCREMENT,"+
        "    `app_name` varchar(500) DEFAULT NULL,"+
        "    `idempotency_key` varchar(1000) DEFAULT NULL,"+
        "    `request` varchar(2500) DEFAULT NULL,"+
        "    PRIMARY KEY (`id`)"+
        "  )";
    // @formatter:on
    private static final String EXIST = "SELECT COUNT(*) FROM `idempotent`";

    private static final String INSERT =
        "INSERT INTO `idempotent`(`app_name`,`idempotency_key`,`request`)VALUES(?,?,?);";

    private static final String QUERY = "SELECT * FROM `idempotent` WHERE `app_name`=? AND `idempotency_key` = ?";

    private static final String DELETE = "DELETE FROM `idempotent` WHERE `app_name`=? AND `idempotency_key` = ?";

    private final DataSource dataSource;

    public DbIdempotentRepository(DataSource dataSource) {
        this.dataSource = dataSource;
        try (Connection conn = dataSource.getConnection()) {
            Statement st = conn.createStatement();
            st.executeQuery(EXIST);
        } catch (SQLException e) {
            LOGGER.warn("There is not table idempotent,will execute DDL");
        }
        try (Connection conn = dataSource.getConnection()) {
            Statement st = conn.createStatement();
            st.executeQuery(CREATE);
        } catch (SQLException e) {
            LOGGER.error("There is not table idempotent");
        }

    }

    @Override
    public boolean register(String appName, String idempotencyKey, String request) {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement st = conn.prepareStatement(INSERT);
            st.setString(1, appName);
            st.setString(2, idempotencyKey);
            st.setString(3, request);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean unregister(String appName, String idempotencyKey) {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement st = conn.prepareStatement(DELETE);
            st.setString(1, appName);
            st.setString(2, idempotencyKey);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean exist(String appName, String idempotencyKey) {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement st = conn.prepareStatement(QUERY);
            st.setString(1, appName);
            st.setString(2, idempotencyKey);
            ResultSet rs = st.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            return false;
        }
    }

}
