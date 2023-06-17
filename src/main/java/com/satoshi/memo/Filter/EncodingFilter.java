package com.satoshi.memo.Filter;


import cn.hutool.core.util.StrUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author:tibashiki
 * @DATE:2023/05/189:33
 * @Description:
 *
 * 请求乱码
 *  乱码的原因:服务器默认的解析编码ISO-8859-1,不支持中文
 *  乱码情况:
 *      Post请求:
 *          Tomcat7及以下版本 乱码
 *          Tomcat8 乱码
 *      Get请求:
 *          Tomcat7及以下版本 乱码
 *          Tomcat8 不乱码(如果处理,反而会乱码)
 *   解决方案:
 *      Post请求:
 *          无论如何都会乱码:
 *              通过req.setCharacterEncoding("UTF-8")设置编码格式(只针对Post)
 *      Get请求:
 *          Tomcat7及以下版本
 *              new String(req.getParameter("xxx").getBytes("ISO-8859-1"),"UTF-8");
 *
 *
 */
@WebFilter("/*")//过滤所有资源
public class EncodingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;//基于HTTP,格式强转
        //处理post请求
        request.setCharacterEncoding("UTF-8");
        //得到请求类型(GetPost)
        String method = request.getMethod();
        if("GET".equalsIgnoreCase(method)){//会忽略大小写比较
            String serverInfo = request.getServletContext().getServerInfo();//Apache Tomcat 7+
            //通过截取字符串,得到具体版
            String version = serverInfo.substring(serverInfo.lastIndexOf("/")+1,serverInfo.indexOf("."));
            if(version !=null && Integer.parseInt(version)<8){
                //Tomcat8以下版本的服务器GET请i去
                MyWapper myRequest = new MyWapper(request);
                //方形资源
                filterChain.doFilter(myRequest,response);
                return;
            }
        }
        filterChain.doFilter(request,response);
    }

    /**
     * 定义内部类
     * 类的本质是,request对象
     * 去继承包装类,重写getParameter
     * 通过重写Para处理乱码
     */
    class MyWapper extends HttpServletRequestWrapper{
        private HttpServletRequest request;

        /**
         * 带参构造
         * 可以得到需要处理的request对象
         * @param request
         */
        public MyWapper(HttpServletRequest request){
            super(request);
            this.request = request;

        }

        /**
         * 重写para,处理乱码
         * @param name
         * @return
         */
        @Override
        public String getParameter(String name){
            String value = request.getParameter(name);
            if(StrUtil.isBlank(value)){
                return value;
            }
            try{
                value = new String(value.getBytes("ISO-8859-1"),"UTF-8");

            }catch (Exception e){
                e.printStackTrace();
            }
            return value;
        }
    }

    @Override
    public void destroy() {

    }
}
