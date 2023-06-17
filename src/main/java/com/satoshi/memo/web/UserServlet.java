package com.satoshi.memo.web;

import cn.hutool.core.io.FileUtil;
import com.satoshi.memo.po.User;
import com.satoshi.memo.service.UserService;
import com.satoshi.memo.vo.ResultInfo;
import org.apache.commons.io.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * @Author:tibashiki
 * @DATE:2023/05/1711:31
 * @Description:
 */
@WebServlet("/user")
@MultipartConfig
public class UserServlet extends HttpServlet {
    private UserService userService = new UserService();
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置首页导航高亮
        req.setAttribute("menu_page","user");
        //这里专门用来接收用户行为
        String actionName = req.getParameter("actionName");//接收actionName，看看用户在干什么

        if("login".equals(actionName)){
            userLogin(req,resp);
        }
        if("register".equals(actionName)){
            userRegister(req,resp);
        }
        else if ("logout".equals(actionName)){
            userLogout(req,resp);
        }
        else if ("userCenter".equals(actionName)){
            userCenter(req,resp);
        }
        else if("userHead".equals(actionName)){
            userHead(req,resp);
        }
        else if("checkNick".equals(actionName)){
            checkNick(req,resp);
        }
        else if("updateUser".equals(actionName)) {
            updateUser(req,resp);
        }

    }

    private void userRegister(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String userName = req.getParameter("userName");
        String userPwd = req.getParameter("userPwd");
        String userPwdc = req.getParameter("userPwdc");
        ResultInfo<User> resultInfo = userService.userRegister(userName,userPwd,userPwdc);
        if(resultInfo.getCode()==1){//成功
            req.getSession().setAttribute("user",resultInfo.getResult());
            resp.sendRedirect("regSucc.jsp");
        }else{//失败
            req.setAttribute("resultInfo",resultInfo);
            req.getRequestDispatcher("register.jsp").forward(req,resp);
        }
    }


    /**
     * Web层登录
     * @param req
     * @param resp
     * Web层：接收参数，响应数据
     *         1,获取参数getPara(姓名，密码)
     *         2，调用Service层的方法，返回ResultInfo对象
     *         3，如果判断成功
     *             如果失败
     *                 将resultInfo设置到req中
     *                 请求转发跳转到登录页面
     *                 req.getRequestDispather("/login")dispatcher.forward(req.resp)
     *             如果成功
     *                  将用户信息放到session作用域中
     *                 判断用户是否选择记住登陆状态（rem=1）
     *                     如果是：将用户与密码存到Cookie中，设置失效时间，并响应给客户端
     *                     如果否：清空Cookie
     *                 重定向redirect到index
     */

    private void userLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = req.getParameter("userName");
        String userPwd = req.getParameter("userPwd");
        ResultInfo<User> resultInfo = userService.userLogin(userName,userPwd);
        if(resultInfo.getCode()==1){//成功
            req.getSession().setAttribute("user",resultInfo.getResult());
            String rem = req.getParameter("rem");
            if("1".equals(rem)){
                Cookie cookie = new Cookie("user",userName+"-"+userPwd);
                cookie.setMaxAge(3*24*60*60);
                resp.addCookie(cookie);
            }else{
                Cookie cookie = new Cookie("user",null);
                cookie.setMaxAge(0);
                resp.addCookie(cookie);
            }
            //req.getRequestDispatcher("index.jsp").forward(req,resp);//??
            resp.sendRedirect("index");
        }else{//失败
            req.setAttribute("resultInfo",resultInfo);
            req.getRequestDispatcher("login.jsp").forward(req,resp);
        }
    }
    private void userLogout(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException{
        req.getSession().invalidate();//关闭session
        Cookie cookie = new Cookie("user",null);
        cookie.setMaxAge(0);//设置0,表示删除cookie
        resp.addCookie(cookie);//重新发送空cookie
        resp.sendRedirect("login.jsp");//重定向至login
    }

    /**
     *  进入个人中心
     *      设置首页动态包含的页面值
     *      请求转发跳转index
     *
     * @param req
     * @param resp
     */
    private void userCenter(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置首页动态包含的页面值
        req.setAttribute("changePage","user/info.jsp");
        req.getRequestDispatcher("index.jsp").forward(req,resp);
    }

    /**
     * 加载头像
     *      1. 获取参数 （图片名称）
     *         2. 得到图片的存放路径 （request.getServletContext().getealPathR("/")）
     *         3. 通过图片的完整路径，得到file对象
     *         4. 通过截取，得到图片的后缀
     *         5. 通过不同的图片后缀，设置不同的响应的类型
     *         6. 利用FileUtils的copyFile()方法，将图片拷贝给浏览器
     * @param req
     * @param resp
     */
    private void userHead(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //1获取参数
        String head = req.getParameter("imageName");
        //2得到图片的存放路径(request.getServletContext().getRealPath("/"))
        String realPath = req.getServletContext().getRealPath("/WEB-INF/upload/");
        //3通过图片的完整路径，得到file对象
        File file = new File(realPath + "/" + head);
        // 4通过截取，得到图片的后缀
        String pic = head.substring(head.lastIndexOf(".")+1);
        // 5通过不同的图片后缀，设置不同的响应的类型
        if("PNG".equalsIgnoreCase(pic)){
            resp.setContentType("image/png");
        } else if ("JPG".equalsIgnoreCase(pic) || "JPEG".equalsIgnoreCase(pic)) {
            resp.setContentType("image/jpeg");
        } else if ("GIF".equalsIgnoreCase(pic)) {
            resp.setContentType("image/gif");
        }
        //利用FileUtils的copyFile()方法，将图片拷贝给浏览器
        FileUtils.copyFile(file,resp.getOutputStream());
    }

    private void checkNick(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String nick = req.getParameter("nick");
        User user = (User)req.getSession().getAttribute("user");
        Integer code = userService.checkNick(nick,user.getUserId());
        resp.getWriter().write(code + "");
        resp.getWriter().close();
    }

    /**
     * 1. 调用Service层的方法，传递request对象作为参数，返回resultInfo对象
     *             2. 将resultInfo对象存到request作用域中
     *             3. 请求转发跳转到个人中心页面 （user?actionName=userCenter）
     * @param req
     * @param resp
     */
    private void updateUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //调用Service层的方法，传递request对象作为参数，返回resultInfo对象
        ResultInfo<User> resultInfo = userService.updateUser(req);
        //将resultInfo对象存到request作用域中
        req.setAttribute("resultInfo",resultInfo);
        //请求转发跳转到个人中心页面 （user?actionName=userCenter）
        req.getRequestDispatcher("user?actionName=userCenter").forward(req, resp);
    }
}
