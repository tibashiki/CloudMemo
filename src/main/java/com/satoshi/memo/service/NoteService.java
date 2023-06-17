package com.satoshi.memo.service;

import cn.hutool.core.util.StrUtil;
import com.satoshi.memo.dao.NoteDao;
import com.satoshi.memo.po.Note;
import com.satoshi.memo.util.Page;
import com.satoshi.memo.vo.NoteVo;
import com.satoshi.memo.vo.ResultInfo;

import java.util.List;

import static java.lang.Integer.parseInt;

/**
 * @Author:tibashiki
 * @DATE:2023/05/235:15
 * @Description:
 */
public class NoteService {
    private NoteDao noteDao = new NoteDao();

    /**
     * Service层
     *         设置回显对象Note
     *         非空校验
     *             空，code=0，msg=...,result=note(把原来的数据回显)返回resultInfo
     *         非空，调用Dao层，添加云记，返回受影响的行数
     *         判断受影响行数
     *             大于0，code=1
     *             0，code=0,msg=...,result=note(把原来的数据回显)
     * @param typeId
     * @param title
     * @param content
     * @return
     */
    public ResultInfo<Note> addOrUpdate(String typeId, String title, String content) {
        ResultInfo<Note> resultInfo = new ResultInfo<>();

        if(StrUtil.isBlank(typeId)){
            resultInfo.setCode(0);
            resultInfo.setMsg("请选择云记类型");
            return resultInfo;
        }
        if(StrUtil.isBlank(title)){
            resultInfo.setCode(0);
            resultInfo.setMsg("标题不能为空");
            return resultInfo;
        }
        if(StrUtil.isBlank(content)){
            resultInfo.setCode(0);
            resultInfo.setMsg("内容不能为空");
            return resultInfo;
        }
        Note note = new Note();
        note.setTypeId(parseInt(typeId));
        note.setTitle(title);;
        note.setContent(content);
        resultInfo.setResult(note);
        int row = noteDao.addOrUpdate(note);//因为要做增删，需要note实例去改数据库的note表
        //判断受影响行数】
        if(row>0){
            resultInfo.setCode(1);

        }else {
            resultInfo.setCode(0);
            resultInfo.setResult(note);
            resultInfo.setMsg("更新失败");
        }
        return resultInfo;

    }

    /**
     * Service层
     *         参数的非空校验
     *             如果分页为空，设置默认值
     *         查询当前登录用户的云集数量，返回总记录数（long类型）
     *         判断总记录数是否大于0
     *         大于0，调用Page类的代餐构造，得到page的其他参数，并返回Page
     *         查询该用户当前页面下的数据列表，返回note集合
     *         将note集合设置到page对象中
     *         返回Page
     * @param pageNumStr
     * @param pageSizeStr
     * @param userId
     * @return
     */
    public Page<Note> findNoteListByPage(String pageNumStr, String pageSizeStr, Integer userId, String title,String date,String typeId) {
        //设置分页参数的默认值
        Integer pageNum = 1;
        Integer pageSize= 5;
        //非空校验
        if(!StrUtil.isBlank(pageNumStr)){
            pageNum = Integer.parseInt(pageNumStr);
        }
        if(!StrUtil.isBlank(pageSizeStr)){
            pageSize = Integer.parseInt(pageSizeStr);
        }
        //查询当前登录用户的云集数量，返回总记录数（long类型）
        long count = noteDao.findNoteCount(userId,title,date,typeId);
        //判断是否大于0
        if(count<1){
            return null;
        }
        Integer index = (pageNum-1) *pageSize;
        Page<Note> page = new Page(pageNum,pageSize,count);
        List<Note> noteList = noteDao.findNoteListByPage(userId,index,pageSize,title,date,typeId);
        page.setDataList(noteList);

        return page;
    }

    /**
     * 通过日期分组查询当前登录用户下的云记数量
     * @param userId
     * @return
     */
    public List<NoteVo> findNoteCountByDate(Integer userId) {
        return noteDao.findNoteCountByDate(userId);
    }

    public List<NoteVo> findNoteCountByType(Integer userId) {
        return noteDao.findNoteCountByType(userId);
    }

    /**
     * 查询云记详情
     * @param noteId
     * @return
     */
    public Note findNoteById(String noteId) {
        if(StrUtil.isBlank(noteId)){
            return null;
        }
        Note note = noteDao.findNoteById(noteId);

        return note;
    }
}
