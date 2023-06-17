package com.satoshi.memo.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * Project CloudMemo
 *获取数据库连接
 * 关闭数据库连接有关的资源
 * @author tibashiki
 */
public class DBUtil {
    private static Properties properties = new Properties();//properties工具类 HashTable
    static{//静态代码块
        InputStream inputStream = DBUtil.class.getClassLoader().getResourceAsStream("db.properties");//类加载器反射获得配置文件db.properties
        try {
            properties.load(inputStream);//通过load方法将输入流的内容加载到配置文件对象中
            Class.forName(properties.getProperty("jdbcName"));//获取驱动名并加载驱动
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 数据库连接
     * @return
     */
    public static Connection getConnection(){//获取数据库连接
        Connection connection = null;

        try {
            String dbUrl = properties.getProperty("dbUrl");//得到数据库链接的相关信息
            String dbName = properties.getProperty("dbName");
            String dbPwd = properties.getProperty("dbPwd");
            connection = DriverManager.getConnection(dbUrl,dbName,dbPwd);//得到数据库链接
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 关闭资源的方法
     * resultSet
     * preparedStatement
     * connection
     */
    public static void close(ResultSet resultSet, PreparedStatement preparedStatement,Connection connection) {

        try {
            if (resultSet != null) {

                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
