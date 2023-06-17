package com.satoshi.memo.service;

import cn.hutool.core.util.StrUtil;
import com.satoshi.memo.dao.NoteTypeDao;
import com.satoshi.memo.po.NoteType;
import com.satoshi.memo.vo.ResultInfo;

import java.util.List;

/**
 * @Author:tibashiki
 * @DATE:2023/05/219:06
 * @Description:
 */
public class NoteTypeService {

    NoteTypeDao noteTypeDao = new NoteTypeDao();
    /**
     *查询类型列表
     * 1调用Dao层的查询方法，通过userId查询类型集合
     * 2返回集合
     */
    public List<NoteType> findTypeList(Integer userId){
        List<NoteType> typeList = noteTypeDao.findTypeListByUserId(userId);
        return typeList;
    }

    /**
     * 删除类型
     * 判断参数是否为空
     * 调用Dao层查询功能，查typeId内的云记录数量
     *
     * 如果数量大于0，存在子记录，不删
     * 如果不存在子记录，调用Dao层更新方法，通过typeId删除指定记录
     * 判断受影响行数是否大于0
     * 大于0，code=1，否则code=2 ，msg="删除失败"
     * 返回resultInfo
     * @param typeId
     * @return
     */
    public ResultInfo<NoteType> deleteType(String typeId) {
        ResultInfo<NoteType> resultInfo = new ResultInfo<>();
        if(StrUtil.isBlank(typeId)){
            resultInfo.setCode(0);
            resultInfo.setMsg("系统异常请重试");
            return resultInfo;
        }
        //调用Dao层，通过typeId查询云记录数量
        long noteCount = noteTypeDao.findNoteCountByTypeId(typeId);
        //如果云集数量大于0.说明存在子记录，不可删除
         if(noteCount>0){
             resultInfo.setCode(0);;
             resultInfo.setMsg("存在子记录，不可删除");
             return resultInfo;
         }
        int row = noteTypeDao.deleteTypeById(typeId);
         if(row >0){
             resultInfo.setCode(1);

         }else{
             resultInfo.setCode(0);
             resultInfo.setMsg("删除失败");
         }

        return  resultInfo;
    }

    public ResultInfo<Integer> addOrUpdate(String typeName, Integer userId, String typeId) {
        ResultInfo<Integer> resultInfo = new ResultInfo<>();
        if(StrUtil.isBlank(typeName)){
            resultInfo.setCode(0);
            resultInfo.setMsg("类型名称不能为空");
            return resultInfo;
        }
        Integer code = noteTypeDao.checkTypeName(typeName,userId,typeId);
        if(code==0){
            resultInfo.setCode(0);
            resultInfo.setMsg("类型名称已存在，请重新输入！");
            return resultInfo;
        }
        Integer key = null;
        if(StrUtil.isBlank(typeId)){//id为空，所以是添加操作
            key = noteTypeDao.addType(typeName,userId);
        }else {
            key = noteTypeDao.updateType(typeName,typeId);
        }
        if(key>0){
            resultInfo.setCode(1);
            resultInfo.setResult(key);
        }else {
            resultInfo.setCode(0);
            resultInfo.setMsg("更新失败");
        }
        return resultInfo;
    }
}
