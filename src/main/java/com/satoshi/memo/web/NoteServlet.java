package com.satoshi.memo.web;

import com.satoshi.memo.po.Note;
import com.satoshi.memo.po.NoteType;
import com.satoshi.memo.po.User;
import com.satoshi.memo.service.NoteService;
import com.satoshi.memo.service.NoteTypeService;
import com.satoshi.memo.vo.ResultInfo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @Author:tibashiki
 * @DATE:2023/05/235:15
 * @Description:
 */
@WebServlet("/note")
public class NoteServlet extends HttpServlet {
    private NoteService noteService = new NoteService();
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置首页导航高亮
        req.setAttribute("menu_page","note");
        //看看用户在干什么
        String actionName = req.getParameter("actionName");
        //哦你在view
        if("view".equals(actionName)){
            noteView(req,resp);
        }else if("addOrUpdate".equals(actionName)){
            addOrUpdate(req,resp);
        } else if ("detail".equals(actionName)) {
            noteDetail(req,resp);
        }
    }

    /**
     * 查询云记详情
     * @param req
     * @param resp
     */
    private void noteDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. 接收参数 （noteId）
        String noteId = req.getParameter("noteId");
        // 2. 调用Service层的查询方法，返回Note对象
        Note note = noteService.findNoteById(noteId);
        // 3. 将Note对象设置到request请求域中
        req.setAttribute("note", note);
        // 4. 设置首页动态包含的页面值
        req.setAttribute("changePage","note/detail.jsp");
        // 5. 请求转发跳转到index.jsp
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }

    /**
     *Web:
     *         接收参数（typeId,title,content）
     *         调用Service层方法，返回ResultInfo对象
     *         判断resultInfo的code值
     *             如果code=1，成功
     *                 重定向跳转到页面index
     *             code=0,失败
     *                 将resultInfo对象设置到req作用域
     *                 请求转发跳转到note?actionName=view
     * @param req
     * @param resp
     */
    private void addOrUpdate(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        //接收参数（typeId,title,content）
        String typeId = req.getParameter("typeId");
        String title = req.getParameter("title");
        String content = req.getParameter("content");
        //调用Service层方法，返回ResultInfo对象
        ResultInfo<Note> resultInfo = noteService.addOrUpdate(typeId,title,content);
        //判断resultInfo的code值
        if(resultInfo.getCode() == 1){
            resp.sendRedirect("index");
        }else {
            req.setAttribute("resultInfo",resultInfo);
            req.getRequestDispatcher("note?actionName=view").forward(req,resp);
        }
    }

    /**
     * 进入发布云记页面
     *         从session对象中获取User
     *         通过userId查询对应的tb_note_type表里的属性
     *         将NoteType设置到request请求域中（只需要在一次请求中生效）
     *         设置首页动态包含的页面值
     *         请求转发跳转到index.jsp
     * @param req
     * @param resp
     */
    private void noteView(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //从session对象中获取User
        User user = (User)req.getSession().getAttribute("user");//强转？为了保持getAttr的通用性，也没有重写getAttr，所以直接强转，反正Object也能什么都存
        //通过userId查询对应的tb_note_type表里的属性
        List<NoteType> typeList = new NoteTypeService().findTypeList(user.getUserId());
        //将NoteType设置到request请求域中（只需要在一次请求中生效）
        req.setAttribute("typeList",typeList);
        //执行noteView那我就要访问动态view页面了,设置首页动态包含的页面值
        req.setAttribute("changePage","note/view.jsp");
        //请求转发跳转到index.jsp
        req.getRequestDispatcher("index.jsp").forward(req,resp);
    }
}
