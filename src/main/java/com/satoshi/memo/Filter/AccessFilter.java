package com.satoshi.memo.Filter;

import com.satoshi.memo.po.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author:tibashiki
 * @DATE:2023/05/1810:02
 * @Description:
 */

/**
 * 非法访问拦截
 *  拦截的资源
 *      所有的资源
 *      需要被放行的资源
 *          指定页面放行(不需要登陆就能访问 login,register)
 *          静态资源(图片,样式,static目录,jss,css,img)
 *          执行行为放行(用户无需登录,登陆操作actionName = login )
 *          登陆状态,放行(判断是否登录session作用域中,是否存在User,存在则放行,不存在则拦截跳转)
 *
 *      免登录/自动登录
 *          通过Cookie对象实现
 *          未登录:需要访问登录权限资源,自动调用
 *          从Cookie中获取用户的姓名和密码,自动执行登陆操作
 *              Cookie数组 request.getCookies()
 *              判断Cookie数组
 *              遍历Cookie数组,获取指定的Cookie对象 name为User的Cookie对象
 *              得到对应的cookie对象的value(姓名密码)
 *              通过split()方法将value字符串分割成数组
 *              从数组中分别得到对印的姓名和密码值
 *              请求转发到登陆操作 user?actionName = login & userName=xxx & userPwd=xxx
 *              return
 *       以上都不满足,拦截跳转登录页面
 *
 */
@WebFilter("/*")
public class AccessFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //得到访问的路径
        String path = request.getRequestURI();//项目路径或者资源路径
        //指定页面放行
        if(path.contains("/login.jsp")){
            filterChain.doFilter(request,response);
            return;
        }
        if(path.contains("/register.jsp")){
            filterChain.doFilter(request,response);
            return;
        }
        //静态页面放行
        if(path.contains("/static")){
            filterChain.doFilter(request,response);
            return;
        }
        //登录相关放行
        if(path.contains("/upload")){
            filterChain.doFilter(request,response);
            return;
        }
        //指定行为放行
        if(path.contains("/user")){
            String actionName = request.getParameter("actionName");
            if("login".equals(actionName)){
                filterChain.doFilter(request,response);
                return;
            }
            if("register".equals(actionName)){
                filterChain.doFilter(request,response);
                return;
            }
        }
        //登陆状态放行
        //获取session作用域中的用户对象
        User user = (User)request.getSession().getAttribute("user");
        if(user != null){
            filterChain.doFilter(request,response);
            return;
        }
/**         免登录/自动登录
 *          通过Cookie对象实现
 *          未登录:需要访问登录权限资源,自动调用
 *          从Cookie中获取用户的姓名和密码,自动执行登陆操作
 *              Cookie数组 request.getCookies()
 *              判断Cookie数组
 *              遍历Cookie数组,获取指定的Cookie对象 name为User的Cookie对象
 *              得到对应的cookie对象的value(姓名密码)
 *              通过split()方法将value字符串分割成数组
 *              从数组中分别得到对印的姓名和密码值
 *              请求转发到登陆操作 user?actionName = login & userName=xxx & userPwd=xxx
 *              return
 *              以上都不满足,拦截跳转登录页面
 */
        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length>0){
            for(Cookie cookie:cookies){
                if("user".equals(cookie.getName())){//这里的getName是cookie自带的方法
                    String value = cookie.getValue();
                    String[] val = value.split("-");
                    String userName = val[0];
                    String userPwd = val[1];//字符串通过"-"分开,就两个,分别是用户名,密码,用户名下标为0,密码下标为1
                    String url = "user?actionName=login&rem=1&userName="+userName+"&userPwd="+userPwd;
                    request.getRequestDispatcher(url).forward(request,response);
                    return;
                }
            }
        }
        //request.getRequestDispatcher("login.jsp").forward(request,response);
        response.sendRedirect("login.jsp");
    }

    @Override
    public void destroy() {

    }
}
