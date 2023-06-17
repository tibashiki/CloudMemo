package com.satoshi.memo.dao;

import com.mysql.cj.protocol.Message;
import com.mysql.cj.protocol.Resultset;
import com.satoshi.memo.po.User;
import com.satoshi.memo.util.DBUtil;

import javax.xml.transform.Result;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author:tibashiki
 * @DATE:2023/05/1716:17
 * @Description:
 * 基础的jdbc操作类
 * 更新（添加修改删除）和查询
 * 查询：
 *  1，只查询一个字段
 *  2，查询集合
 *  3，查询某个对象
 */
public class BaseDao {
    /**
     * 更新操作
     *  得到数据库链接
     *  定义sql语句
     *  预编译
     *  设置参数（如果有，下标从1开始，如果多个，循环设置参数）
     *  执行更新，返回受影响的行数
     *  关闭资源
     *  注：需要两个参数:sql语句，所需参数的集合
     * @param sql
     * @return
     */
    public static int executeUpdate1(String sql, List<Object> params){//因为参数可能有多个，使用list集合获得参数们
        int row = 0;//受影响的行数
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try{
            connection = DBUtil.getConnection();//获得连接
            preparedStatement = connection.prepareStatement(sql);//预编译
            if(params != null && params.size()>0){
                for (int i = 0;i< params.size();i++){
                    preparedStatement.setObject(i+1,params.get(i));//预处理快的Object参数的下标，从1开始
                }
            }
            row = preparedStatement.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            DBUtil.close(null,preparedStatement,connection);
        }
        return row;
    }
    /**
     * 查询一个字段
     *  得到数据库链接
     *  定义sql语句
     *  预编译
     *  设置参数（如果有，下标从1开始，如果多个，循环设置参数）
     *  执行更新，返回受影响的行数
     *  关闭资源
     *  注：需要两个参数:sql语句，所需参数的集合
     * @param sql
     * @return
     */
    public static Object findSingleValue1(String sql,List<Object> params){
        int row = 0;
        Object object = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            if(params != null && params.size()>0){
                for (int i = 0;i< params.size();i++){
                    preparedStatement.setObject(i+1,params.get(i));//预处理快的Object参数的下标，从1开始
                }
            }
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                object = resultSet.getObject(1);

            }
        }catch(Exception e){
        e.printStackTrace();
        }finally {
        //关闭资源
        DBUtil.close(resultSet,preparedStatement,connection);
        }
        return object;
    }

    public static List queryMultiRows(String sql,List<Object> params,Class class1){
        /**
         * 获取数据库连接
         * 定义sql语句
         * 预编译
         * 设置参数
         * 执行查询，得到结果集
         * 得到结果集的元数据（查询到的字段数量，以及查询到了哪些字段）
         * 判断并分析结果集
         * 实例化对象
         * 遍历查询的字段数量，得到数据库中的每一个列名
         * 通过反射，使用列名，得到对应的field对象
         * 字符串拼接set方法
         * 通过反射，将set方法的字符串反射成类中指定set放法
         * 通过invoke调用set方法
         * 将对应的javaBean设置到集合中
         * 关闭资源
         */
        ArrayList list = new ArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            if(params != null && params.size()>0){
                for (int i = 0;i< params.size();i++){
                    preparedStatement.setObject(i+1,params.get(i));//预处理快的Object参数的下标，从1开始
                }
            }
            resultSet = preparedStatement.executeQuery();
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();//得到结果集的元数据对象（查询到的字段数量以及查询了哪些路径）
            int fieldColumn = resultSetMetaData.getColumnCount();//得到查字段列的数量
            while(resultSet.next()){
                Object object = class1.newInstance();
                for (int i = 1; i <= fieldColumn ; i++) {
                     String columnName = resultSetMetaData.getColumnLabel(i);//列名或别名(Label标签)
                     //String columnName = resultSetMetaData.getColumnName(i);//列名
                    Field field = class1.getDeclaredField(columnName);//JavaBean中的属性名，与数据库中表的字段，必须对应（实现反射的关键）
                    String setMethod = "set" + columnName.substring(0,1).toUpperCase() + columnName.substring(1);//拼接字符串，得到set
                    Method method = class1.getDeclaredMethod(setMethod,field.getType());//通过反射，将set方法字符串反射成类中对应的set方法
                    Object value = resultSet.getObject(columnName);//得到每一个字段对应的值
                    method.invoke(object,value);//通过invoke调用set方法
                }
                list.add(object);//将对应的javaBean设置到集合中
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DBUtil.close(resultSet,preparedStatement,connection);
        }


        return list;
    }

    public static Object querySelectedRow(String sql,List<Object> params,Class class1){
        List list = queryMultiRows(sql,params,class1);
        Object object = null;
        //如果集合不为空，则获取查询的第一条数据
        if(list !=null && list.size()>0){
            object=list.get(0);
        }
        return object;
    }
}
