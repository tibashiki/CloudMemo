package com.satoshi.memo.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.satoshi.memo.dao.UserDao;
import com.satoshi.memo.po.User;
import com.satoshi.memo.vo.ResultInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.xml.transform.Result;
import java.util.Objects;

/**
 * @Author:tibashiki
 * @DATE:2023/05/1711:30
 * @Description:
 * 用户登录
 */
public class UserService {
    private UserDao userDao = new UserDao();

    public ResultInfo<User> userLogin(String userName, String userPwd) {//将用户名密码作为参数传入，用来做判断
        ResultInfo<User> resultInfo = new ResultInfo<>();
        //数据回显，当登录实现时，将登录信息返回给页面显示
        User u = new User();
        u.setUname(userName);
        u.setUpwd(userPwd);
        resultInfo.setResult(u);
        //判断参数为空
        if (StrUtil.isBlank(userName) || StrUtil.isBlank(userPwd)) {//如果为空
            resultInfo.setCode(0);//状态码设为0
            resultInfo.setMsg("用户姓名或密码不能为空！");//设置msg
            return resultInfo;//返回resultInfo对象
        }
        User user = userDao.queryUserByName(userName);
        if (user == null) {
            resultInfo.setCode(0);//状态码设为0
            resultInfo.setMsg("用户不存在！");//设置msg
            return resultInfo;//返回resultInfo对象
        }
        userPwd = DigestUtil.md5Hex(userPwd);//将前台传递的密码按照md5算法的方式加密
        if (!userPwd.equals(user.getUpwd())) {
            resultInfo.setCode(0);//状态码设为0
            resultInfo.setMsg("密码错误！");//设置msg
            return resultInfo;//返回resultInfo对象
        }
        resultInfo.setCode(1);//成功的判断
        resultInfo.setResult(user);//返回含有user的resultInfo
        return resultInfo;
    }

    /**
     * 验证名称的唯一性
     * Service层：
     * 1. 判断昵称是否为空
     * 如果为空，返回"0"
     * 2. 调用Dao层，通过用户ID和昵称查询用户对象
     * 3. 判断用户对象存在
     * 存在，返回"0"
     * 不存在，返回"1"
     *
     * @param nick
     * @param userId
     * @return
     */
    public Integer checkNick(String nick, Integer userId) {
        //判断昵称
        if (StrUtil.isBlank(nick)) {
            return 0;
        }
        User user = userDao.queryUserByNickAndUserId(nick, userId);
        if (user != null) {
            return 0;
        }
        return 1;
    }

    /**
     * 修改用户信息
     * 1. 获取参数（昵称、心情）
     * 2. 参数的非空校验（判断必填参数非空）
     * 如果昵称为空，将状态码和错误信息设置resultInfo对象中，返回resultInfo对象
     * 3. 从session作用域中获取用户对象（获取用户对象中默认的头像）
     * 4. 实现上上传文件
     * 1. 获取Part对象 request.getPart("name"); name代表的是file文件域的name属性值
     * 2. 通过Part对象获取上传文件的文件名
     * 3. 判断文件名是否为空
     * 4. 获取文件存放的路径  WEB-INF/upload/目录中
     * 5. 上传文件到指定目录
     * 5. 更新用户头像 （将原本用户对象中的默认头像设置为上传的文件名）
     * 6. 调用Dao层的更新方法，返回受影响的行数
     * 7. 判断受影响的行数
     * 如果大于0，则修改成功；否则修改失败
     * 8. 返回resultInfo对象
     *
     * @param req
     * @return
     */
    public ResultInfo<User> updateUser(HttpServletRequest req) {
        ResultInfo<User> resultInfo = new ResultInfo<>();
        //1获取参数
        String nick = req.getParameter("nick");
        String mood = req.getParameter("mood");
        //必填参数的校验,昵称可以不用判断
        if (StrUtil.isBlank(nick)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("用户昵称不能为空");
            return resultInfo;
        }
        User user = (User)req.getSession().getAttribute("user");
        //3设置修改的昵称和头像
        user.setNick(nick);
        user.setMood(mood);
        //4文件上传
        try{
            //1获取part对象,request.getPart("name");name代表的是file文件域的names属性值
            Part part = req.getPart("img");
            //2通过part对象获取上传文件名
            String header = part.getHeader("Content-Disposition");
            //获取具体的请求头对应的值
            String str = header.substring(header.lastIndexOf("=") + 2);
            //获取上传的文件名
            String fileName = str.substring(0,str.length() - 1);
            if(!StrUtil.isBlank(fileName)){
                //如果用户上传了头像,则更新用户对象的头像
                user.setHead(fileName);
                //获得文件存放的路径 WEB-INF/upload目录中
                String filePath = req.getServletContext().getRealPath("/WEB-INF/upload/");
                //上传文件到指定目录
                part.write(filePath + "/" + fileName);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        //调用Dao层的更新方法,返回受影响的行数
        int row = userDao.updateUser(user);
        if(row>0){//判断受影响的行数
            resultInfo.setCode(1);
            //更新Session用户
            req.getSession().setAttribute("user",user);

        }else {
            resultInfo.setCode(0);
            resultInfo.setMsg("更新失败");
        }
        return resultInfo;
    }

    public ResultInfo<User> userRegister(String userName, String userPwd,String userPwdc) {
        ResultInfo<User> resultInfo = new ResultInfo<>();
        User user1= userDao.queryUserByName(userName);
        //必填参数的校验,昵称可以不用判断
        if (user1 != null) {
        //if (!StrUtil.isEmpty(user1.getUname())) {
            resultInfo.setCode(0);
            resultInfo.setMsg("用户名已存在");
            return resultInfo;
        }
        if (StrUtil.isBlank(userName)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("用户名不能为空");
            return resultInfo;
        }
        if (StrUtil.isBlank(userPwd)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("密码不能为空");
            return resultInfo;
        }
        if (!userPwd.equals(userPwdc)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("密码不正确");
            return resultInfo;
        }

        User user = new User();
        user.setUname(userName);
        user.setUpwd(userPwd);
        resultInfo.setResult(user);

        //调用Dao层的更新方法,返回受影响的行数
        int row = userDao.userRegister(user);
        if(row>0){//判断受影响的行数
            resultInfo.setCode(1);


        }else {
            resultInfo.setCode(0);
            resultInfo.setMsg("更新失败");
        }
        return resultInfo;
    }
}
