<%--
  Created by IntelliJ IDEA.
  User: tibashiki
  Date: 2023/05/19
  Time: 9:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <title>上海红茶馆</title>
  <link href="static/css/css.css" rel="stylesheet" type="text/css">
  <link href="static/css/font-awesome.min.css" rel="stylesheet" integrity="sha384-wvfXpqpZZVQGK6TAh5PVlGOfQNHSoD2xbE+QkPxCAFlNEevoEH3Sl0sibVcOQVnN" crossorigin="anonymous">
  <link rel="stylesheet" href="static/css/style.css">
  <script src="static/js/jquery-1.11.3.js" type=text/javascript></script>
  <script src="static/js/config.js" type=text/javascript></script>
  <%--
      <script src="static/js/util.js" type=text/javascript></script>
  --%><%--这里把在前端js的非空校验移至后端所有可以不用--%>
</head>
<body>
<!-- partial:index.partial.html -->
<div class="box-form">
  <div class="left">
    <div class="overlay">
      <h1>上海红茶馆</h1>
      <p>坐下来喝杯咖啡，上传艺术作品和音乐，分享日记和美好时光。</p>
      <p>坐下来喝杯咖啡，上传艺术作品和音乐，分享日记和美好时光。</p>
      <p>坐下来喝杯咖啡，上传艺术作品和音乐，分享日记和美好时光。</p>
      <span>
			<p>使用其他途径登录</p>
			<a href="#"><i class="fa fa-facebook" aria-hidden="true"></i>Login with Gmail</a>
			<a href="#"><i class="fa fa-twitter" aria-hidden="true"></i> Login with Twitter</a>
		</span>
    </div>
  </div>


  <div class="right">
    <form action="user" method="post" id="registerForm">
      <h5>请注册</h5>
      <p>填写您的注册信息<a href="login.jsp">已有帐号？登录</a> </p>
      <div class="inputs">

        <input type="hidden" name="actionName" value="register"/><!--用hidden表示用户行为，通过这个参数判断Servlet中用户访问的操作-->
        <input type="text" placeholder="用户名" id="userName" name="userName" value="${resultInfo.result.uname}">
        <br>
        <input type="password" placeholder="密码" id="userPwd" name="userPwd" value="${resultInfo.result.upwd}">
        <br>
        <input type="password" placeholder="确认密码" id="userPwdc" name="userPwdc" value="">

      </div>

      <br><br>
      <div class="remember-me--forget-password">
        <!-- Angular -->
        <label>
          <input name="rem" type="checkbox" value="1"  class="inputcheckbox"/> <label>检查用户是否可用</label>
        </label>
        <p href="#">用户协议</p>
      </div>

      <br>
      <span id="msg" style="color: red;font-size: 18px">${resultInfo.msg}</span>
      <button onclick="checkRegister()">注册</button>
      <%--<input type="button" class="log jc yahei16" value="login" onclick="checkLogin()" />--%>
    </form>
  </div>

</div>
<!-- partial -->

</body>
</html>

