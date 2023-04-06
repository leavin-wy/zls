package com.javaweb.common.utils;

import com.javaweb.common.config.DbConfig;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component
@Data
public class JDBCUtils {

    /**
     * 数据库驱动
     */
    private static String driver;
    /**
     * 数据库连接
     */
    private static String url;
    /**
     * 数据库登录名
     */
    private static String username;
    /**
     * 数据库密码
     */
    private static String password;

    /**
     * 数据库驱动
     *
     * @param driver2 驱动名
     */
    @Value("${spring.datasource.driver-class-name}")
    public void setDriver(String driver2) {
        driver = driver2;
    }

    /**
     * 数据库连接
     *
     * @param url2 连接
     */
    @Value("${spring.datasource.url}")
    public void setUrl(String url2) {
        url = url2;
    }

    /**
     * 数据库登录名
     *
     * @param userName2 登录名
     */
    @Value("${spring.datasource.username}")
    public void setUsername(String userName2) {
        username = userName2;
    }

    @Value("${spring.datasource.password}")
    public void setPassword(String password2) {
        password = password2;
    }

    /**
     * 构造函数
     */
    private JDBCUtils() {
    }

    /*static {
        *//**
         * 驱动注册
         *//*
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }

    }*/

    /**
     * 获取 Connetion
     *
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
        return DriverManager.getConnection(url, username, password);
    }

    /**
     * 释放资源
     *
     * @param conn
     * @param st
     * @param rs
     */
    public static void colseResource(Connection conn, Statement st, ResultSet rs) {
        closeResultSet(rs);
        closeStatement(st);
        closeConnection(conn);
    }

    /**
     * 释放连接 Connection
     *
     * @param conn
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        //等待垃圾回收
        conn = null;
    }

    /**
     * 释放语句执行者 Statement
     *
     * @param st
     */
    public static void closeStatement(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        //等待垃圾回收
        st = null;
    }

    /**
     * 释放结果集 ResultSet
     *
     * @param rs
     */
    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        //等待垃圾回收
        rs = null;
    }
}
