package com.satoshi.memo.dao;

import cn.hutool.crypto.digest.DigestUtil;
import com.satoshi.memo.po.User;
import com.satoshi.memo.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author:tibashiki
 * @DATE:2023/05/1711:30
 * @Description:
 * DAO层：数据访问层 数据的增删改查
 *         通过用户名查询用户对象，返回用户对象
 *             1获取数据库链接
 *             2定义sql语句
 *             3预编译
 *             4设置参数
 *             5执行查询
 *             6判断并分析结果
 *             7关闭资源
 */
public class UserDao {
    public User queryUserByName1(String userName){
        /**
         * 定义sql语句
         * 设置参数集合
         * 调用BaseDao的查询方法
         */

        User user = null;
        String sql = "select * from tb_user where uname = ?";

        List<Object> params = new ArrayList<>();
        params.add(userName);

        user = (User)BaseDao.querySelectedRow(sql,params,User.class);

        return user;

    }
    public User queryUserByName(String userName){
        User user=null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            connection = DBUtil.getConnection();//获取数据库链接
            String sql = "select * from tb_user where uname = ?";//定义sql语句
            preparedStatement = connection.prepareStatement(sql);//预编译
            preparedStatement.setString(1,userName);//设置参数？？
            resultSet = preparedStatement.executeQuery();//执行查询?
            if (resultSet.next()){
                user = new User();
                user.setUserId(resultSet.getInt("userId"));
                user.setUname(userName);
                user.setHead(resultSet.getString("head"));
                user.setMood(resultSet.getString("mood"));
                user.setNick(resultSet.getString("Nick"));
                user.setUpwd(resultSet.getString("upwd"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            //关闭资源
            DBUtil.close(resultSet,preparedStatement,connection);
        }
        return user;
    }

    /**
     * Dao层：
     *             1. 定义SQL语句
     *                 通过用户ID查询除了当前登录用户之外是否有其他用户使用了该昵称
     *                     指定昵称  nick （前台传递的参数）
     *                     当前用户  userId （session作用域中的user对象）
     *                     String sql = "select * from tb_user where nick = ? and userId != ?";
     *             2. 设置参数集合
     *             3. 调用BaseDao的查询方法
     * @param nick
     * @param userId
     * @return
     */
    public User queryUserByNickAndUserId(String nick, Integer userId) {
        //定义sql语句
        String sql = "select * from tb_user where nick = ? and userId !=?";
        //设置集合参数
        List<Object> params = new ArrayList<>();
        params.add(nick);
        params.add(userId);

        User user = (User)BaseDao.querySelectedRow(sql,params,User.class);
        return user;
    }

    /**
     * 通过用户ID修改用户信息
     *             1. 定义SQL语句
     *              String sql = "update tb_user set nick = ?, mood = ?, head = ? where userId = ? ";
     *             2. 设置参数集合
     *             3. 调用BaseDao的更新方法，返回受影响的行数
     *             4. 返回受影响的行数
     * @param user
     * @return
     */
    public int updateUser(User user) {
        //定义SQL语句
        String sql = "update tb_user set nick = ?,mood = ?,head = ? where userId = ?";
        //设置参数集合
        List<Object> params = new ArrayList<>();
        params.add(user.getNick());
        params.add(user.getMood());
        params.add(user.getHead());
        params.add(user.getUserId());
        //调用BaseDao的更新方法
        int row = BaseDao.executeUpdate1(sql,params);
        return row;
    }

    public int userRegister(User user) {
        String sql = "insert into tb_user (uname,upwd) values (?,?)";
        //设置参数集合
        List<Object> params = new ArrayList<>();
        params.add(user.getUname());
        params.add(DigestUtil.md5Hex(user.getUpwd()));
        //调用BaseDao的更新方法
        int row = BaseDao.executeUpdate1(sql,params);
        return row;
    }
}
