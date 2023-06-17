package com.satoshi.memo.web;

import com.satoshi.memo.po.Note;
import com.satoshi.memo.po.User;
import com.satoshi.memo.service.NoteService;
import com.satoshi.memo.util.Page;
import com.satoshi.memo.vo.NoteVo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @Author:tibashiki
 * @DATE:2023/05/189:26
 * @Description:
 */
@WebServlet("/index")
public class IndexServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置首页导航高亮
        req.setAttribute("menu_page","index");
        //得到用户行为，判断是什么条件查询，标题查询，日期查询，类型查询
        String actionName = req.getParameter("actionName");
        //将用户行为设置到req作用域(分页导航中需要获取)
        req.setAttribute("action",actionName);
        //判断用户行为
        if("searchTitle".equals(actionName)){//标题查询
            //得到查询条件：标题
            String title = req.getParameter("title");
            //查询条件设置到req请求域中（用于回显）
            req.setAttribute("title",title);
            //标题搜索
            noteList(req,resp,title,null,null);
        } else if ("searchDate".equals(actionName)) { // 日期查询

            // 得到查询条件：日期
            String date = req.getParameter("date");
            // 将查询条件设置到request请求域中（查询条件的回显）
            req.setAttribute("date", date);

            // 日期搜索
            noteList(req, resp, null, date, null);

        } else if ("searchType".equals(actionName)) { // 类型查询

            // 得到查询条件：类型ID
            String typeId = req.getParameter("typeId");
            // 将查询条件设置到request请求域中（查询条件的回显）
            req.setAttribute("typeId", typeId);

            // 日期搜索
            noteList(req, resp, null, null, typeId);

        } else {
            // 分页查询云记列表
            noteList(req, resp, null, null, null);
        }

        //设置首页动态包含的页面
        req.setAttribute("changePage","note/list.jsp");
        req.getRequestDispatcher("index.jsp").forward(req,resp);
    }

    /**
     * web层
     *         接收参数（当前页，每页显示的数量）
     *         获取Session作用域中的user
     *         调用Service层的查询方法，返回Page对象(通过userId查询对应的表里的属性)
     *         将page对象设置到req作用域
     *         设置首页动态包含的页面值
     *         请求转发跳转到index.jsp
     * @param req
     * @param resp
     * @param title
     */
    private void noteList(HttpServletRequest req, HttpServletResponse resp,String title,String date,String typeId) {
        //接收参数（当前页，每页显示的数量）
        String pageNum = req.getParameter("pageNum");
        String pageSize = req.getParameter("pageSize");
        //获取Session作用域中的user
        User user = (User)req.getSession().getAttribute("user");
        //调用Service层的查询方法，返回Page对象(通过userId查询对应的表里的属性)
        Page<Note> page = new NoteService().findNoteListByPage(pageNum,pageSize,user.getUserId(),title,date,typeId);
        //将配置对象设置到req作用域中
        req.setAttribute("page",page);
        //通过日期分组查询当前登录用户下的云记数量
        List<NoteVo> dateInfo = new NoteService().findNoteCountByDate(user.getUserId());
        //设置集合存放在req作用域中
        req.getSession().setAttribute("dateInfo",dateInfo);
        //通过类型分组查询当前登录用户下的云记数量
        List<NoteVo> typeInfo = new NoteService().findNoteCountByType(user.getUserId());
        //设置集合存放在req作用域在
        req.getSession().setAttribute("typeInfo",typeInfo);

    }
}
