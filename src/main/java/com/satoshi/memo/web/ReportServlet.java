package com.satoshi.memo.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author:tibashiki
 * @DATE:2023/05/258:33
 * @Description:
 */
@WebServlet("/report")
public class ReportServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置导航栏高亮值
        req.setAttribute("menu_page","report");
        //得到用户行为
        String actionName = req.getParameter("actionName");
        if("info".equals(actionName)){
            reportInfo(req,resp);
        }
    }

    private void reportInfo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置首页动态包含的页面值
        req.setAttribute("changePage","report/info.jsp");
        req.getRequestDispatcher("index.jsp").forward(req,resp);
    }
}
