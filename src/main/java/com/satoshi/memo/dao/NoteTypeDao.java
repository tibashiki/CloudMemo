package com.satoshi.memo.dao;

import com.satoshi.memo.po.NoteType;
import com.satoshi.memo.util.DBUtil;
import com.satoshi.memo.vo.ResultInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author:tibashiki
 * @DATE:2023/05/219:05
 * @Description:
 */
public class NoteTypeDao {
    /**
     * 通过userId查询类型集合
     * String sql = "select typeId,typeName,userId from tb_note_type where userId=?"
     * 设置参数列表
     * 调用BaseDao的查询方法，返回集合
     * @param userId
     * @return
     */
    public List<NoteType> findTypeListByUserId(Integer userId){
        String sql = "select typeId,typeName,userId from tb_note_type where userId=?";
        List<Object> params = new ArrayList<>();//参数可能为数字，可能为字符串，可能有多个，所以用一个list的Object(顶级斧类)来存参数
        params.add(userId);//不过这里好像就一个参数
        List<NoteType> list = BaseDao.queryMultiRows(sql,params, NoteType.class);//这里的参数根据BaseDao里的方法所定义的参数来填写
        return list;//返回一个list，一个多行的list
    }
    public long findNoteCountByTypeId(String typeId){
        String sql = "select count(1) from tb_note where typeId=?";
        List<Object> params = new ArrayList<>();
        params.add(typeId);
        long count = (long)BaseDao.findSingleValue1(sql,params);
        return count;

    }

    public int deleteTypeById(String typeId) {
        String sql = "delete from tb_note_type where typeId = ?";
        List<Object> params = new ArrayList<>();
        params.add(typeId);
        int row = (int)BaseDao.executeUpdate1(sql,params);
        return row;
    }

    public Integer checkTypeName(String typeName, Integer userId, String typeId) {
        //定义SQL语句
        String sql= "select * from tb_note_type where userId=? and typeName = ?";
        List<Object> params = new ArrayList<>();
        params.add(userId);
        params.add(typeName);
        NoteType noteType = (NoteType) BaseDao.querySelectedRow(sql,params,NoteType.class);
        //如果对象为空
        if(noteType==null){
            return 1;
        }else {
            //如果是修改操作，则需要判断的、是否是当前记录本身
            if(typeId.equals(noteType.getTypeId().toString())){
                return 1;
            }
        }
        return 0;
    }

    public Integer addType(String typeName, Integer userId) {
        Integer key = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            //得到数据库链接
            connection = DBUtil.getConnection();
            String sql = "insert into tb_note_type (typeName,userId) values (?,?)";
            //预编译，设置参数
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,typeName);
            preparedStatement.setInt(2,userId);
            int row = preparedStatement.executeUpdate();
            if (row>0){
                resultSet = preparedStatement.getGeneratedKeys();
                if(resultSet.next()){
                    key = resultSet.getInt(1);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            DBUtil.close(resultSet,preparedStatement,connection);
        }
        return key;
    }

    /**
     * 修改方法，返回受影响的行数
     * @param typeName
     * @param typeId
     * @return
     */
    public Integer updateType(String typeName, String typeId) {
        String sql="update tb_note_type set typeName=? where typeId = ?";
        List<Object> params = new ArrayList<>();
        params.add(typeName);
        params.add(typeId);
        int row = BaseDao.executeUpdate1(sql,params);
        return row;
    }
}
