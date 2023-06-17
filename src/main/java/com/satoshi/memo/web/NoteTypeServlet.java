package com.satoshi.memo.web;

import com.alibaba.fastjson.JSON;
import com.satoshi.memo.po.NoteType;
import com.satoshi.memo.po.User;
import com.satoshi.memo.service.NoteTypeService;
import com.satoshi.memo.util.JsonUtil;
import com.satoshi.memo.vo.ResultInfo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * @Author:tibashiki
 * @DATE:2023/05/219:06
 * @Description:
 */
@WebServlet("/type")
public class NoteTypeServlet extends HttpServlet {
    private NoteTypeService noteTypeService = new NoteTypeService();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("menu_page","type");
        //得到用户行为
        String actionName = req.getParameter("actionName");
        //判断用户行为
        if("list".equals(actionName)){


            typeList(req,resp);
        } else if ("delete".equals(actionName)) {
            deleteType(req,resp);
        }else if("addOrUpdate".equals(actionName)){
            addOrUpdate(req,resp);
        }
    }

    /**
     * 删除类型
     * 接收参数
     * 调用Service的更新操作，返回ResultInfo对象
     * 将ResultInfo对象转换成JSON格式的字符串，相应给ajax的回调函数
     * @param req
     * @param resp
     */

    private void addOrUpdate(HttpServletRequest req, HttpServletResponse resp) {
        String typeName = req.getParameter("typeName");
        String typeId = req.getParameter("typeId");
        User user = (User)req.getSession().getAttribute("user");
        ResultInfo<Integer> resultInfo =  noteTypeService.addOrUpdate(typeName,user.getUserId(),typeId);
        JsonUtil.toJson(resp,resultInfo);

    }

    /**
     * 删除类型
     * 1接收参数typeId
     * 调用service的更新操作，返回resultInfo对象
     * 将info对象变成Json字符串，resp给ajax的回调函数
     * @param req
     * @param resp
     */
    private void deleteType(HttpServletRequest req, HttpServletResponse resp) {
        String typeId = req.getParameter("typeId");
        ResultInfo<NoteType> resultInfo = noteTypeService.deleteType(typeId);
        JsonUtil.toJson(resp,resultInfo);
    }


    /**
     * 查询类型列表
     * 1获取Session作用域设置的user对象
     * 2调用Service层的查询方法，查询当前登录用户的类型集合，返回集合
     * 3将类型列表设置到request请求域中
     * 4设置首页动态包含的页面值
     * 5请求转发跳转到index.jsp
     * @param req
     * @param resp
     */
    private void typeList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User)req.getSession().getAttribute("user");//键值对"user",user
        List<NoteType> typeList = noteTypeService.findTypeList(user.getUserId());//去找Service层定义的方法，参数是什么，返回值是什么
        req.setAttribute("typeList",typeList);//键值对，名字"typeList"，值为类型为NoteType的List
        req.setAttribute("changePage","type/list.jsp");//键值对，名字changePage,值为页面地址
        req.getRequestDispatcher("index.jsp").forward(req,resp);
    }
}
