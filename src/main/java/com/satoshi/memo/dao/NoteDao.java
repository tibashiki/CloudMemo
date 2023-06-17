package com.satoshi.memo.dao;

import cn.hutool.core.util.StrUtil;
import com.satoshi.memo.po.Note;
import com.satoshi.memo.vo.NoteVo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:tibashiki
 * @DATE:2023/05/235:14
 * @Description:
 */
public class NoteDao {
    /**
     * 添加云记，返回受影响的行数
     * @param note
     * @return
     */
    public int addOrUpdate(Note note) {
        String sql = "insert into tb_note (typeId,title,content,pubTime) values(?,?,?,now())";
        List<Object> params = new ArrayList<>();
        params.add(note.getTypeId());
        params.add(note.getTitle());
        params.add(note.getContent());
        //调用BaseDao的update方法
        int row = BaseDao.executeUpdate1(sql,params);
        return row;
    }

    public long findNoteCount(Integer userId,String title,String date,String typeId) {
        String sql="select count(1) from tb_note n inner join" +
                " tb_note_type t on n.typeId = t.typeId" +
                " where userId = ? ";
        List<Object> params = new ArrayList<>();
        params.add(userId);
        //判断条件查询的条件是否为空
        if(!StrUtil.isBlank(title)){
            //不为空，拼接sql，设置所需的参数
            sql += "and title like concat('%',?,'%')";
            params.add(title);
            //设置sql语句所需要的参数
        } else if (!StrUtil.isBlank(date)) {
            sql += " and date_format(pubTime,'%Y年%m月') = ? ";
            params.add(date);
        }else if (!StrUtil.isBlank(typeId)) {
            sql += " and n.typeId = ? ";
            params.add(typeId);
        }
        long count = (long)BaseDao.findSingleValue1(sql,params);
        return count;
    }

    public List<Note> findNoteListByPage(Integer userId, Integer index, Integer pageSize,String title,String date,String typeId) {
        String sql="select noteId,title,pubTime from tb_note n inner join " +
                "tb_note_type t on n.typeId = t.typeId where userId = ? ";
        List<Object> params = new ArrayList<>();
        params.add(userId);

        if(!StrUtil.isBlank(title)){
            //不为空，拼接sql，设置所需的参数
            sql += "and title like concat('%',?,'%')";
            params.add(title);
            //设置sql语句所需要的参数
        }else if (!StrUtil.isBlank(date)) {
            sql += "and date_format(pubTime, '%Y年%m月') = ?";
            params.add(date);
        }else if (!StrUtil.isBlank(typeId)) {
            sql += " and n.typeId = ? ";
            params.add(typeId);
        }
        //拼接分页的sql语句（limit语句需要写在sql最后）
        sql +="limit ?,?";

        params.add(index);
        params.add(pageSize);
        List<Note> noteList = BaseDao.queryMultiRows(sql,params,Note.class);
        return noteList;
    }

    public List<NoteVo> findNoteCountByDate(Integer userId) {
        String sql = "SELECT count(1) noteCount,DATE_FORMAT(pubTime,'%Y年%m月') groupName FROM tb_note n " +
                " INNER JOIN tb_note_type t ON n.typeId = t.typeId WHERE userId = ? " +
                " GROUP BY DATE_FORMAT(pubTime,'%Y年%m月')" +
                " ORDER BY DATE_FORMAT(pubTime,'%Y年%m月') DESC ";
        List<Object> params = new ArrayList<>();
        params.add(userId);

        List<NoteVo> list = BaseDao.queryMultiRows(sql,params,NoteVo.class);
        return list;
    }

    public List<NoteVo> findNoteCountByType(Integer userId) {
        String sql = "SELECT count(noteId) noteCount, t.typeId, typeName groupName FROM tb_note n " +
                " RIGHT JOIN tb_note_type t ON n.typeId = t.typeId WHERE userId = ? " +
                " GROUP BY t.typeId " +
                " ORDER BY COUNT(noteId) DESC";
        List<Object> params = new ArrayList<>();
        params.add(userId);
        List<NoteVo> list = BaseDao.queryMultiRows(sql,params,NoteVo.class);
        return list;
    }

    /**
     * 通过id查询云记对象
     * @param noteId
     * @return
     */
    public Note findNoteById(String noteId) {
        String sql = "select noteId,title,content,pubTime,typeName,n.typeId from tb_note n " +
                " inner join tb_note_type t on n.typeId=t.typeId where noteId = ?";
        List<Object> params = new ArrayList<>();
        params.add(noteId);
        Note note = (Note)BaseDao.querySelectedRow(sql,params,Note.class);
        return note;
    }
}
