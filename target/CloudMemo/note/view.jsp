<%--
  Created by IntelliJ IDEA.
  User: tibashiki
  Date: 2023/05/23
  Time: 5:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="col-md-9">

    <div class="data_list">
        <div class="data_list_title">
            <span class="glyphicon glyphicon-cloud-upload"></span>&nbsp;添加云记
        </div>
        <div class="container-fluid">
            <div class="container-fluid">
                <div class="row" style="padding-top: 20px;">
                    <div class="col-md-12">
                        <%--判断typeList是否为空，为空先添加用户类型--%>
                        <c:if test="${empty typeList}">
                            <h2>暂未查询到NoteType云记类型！</h2>
                            <h4><a href="type?actionName=list">添加类型</a></h4>
                        </c:if>
                            <c:if test="${!empty typeList}">
                        <form class="form-horizontal" method="post" action="note">
                                <%-- 设置隐藏域：用来存放用户行为actionName --%>
                            <input type="hidden" name="actionName" value="addOrUpdate">
                                <%-- 设置隐藏域：用来存放noteId --%>
                            <input type="hidden" name="noteId" value="${noteInfo.noteId}">
                            <div class="form-group">
                                <label for="typeId" class="col-sm-2 control-label">类别:</label>
                                <div class="col-sm-7">
                                    <select id="typeId" class="form-control" name="typeId">
                                        <option value="">请选择云记类别...</option>
                                        <c:forEach var="item" items="${typeList}">
                                            <c:choose>
                                                <c:when test="${!empty resultInfo}">
                                                    <option  <c:if test="${resultInfo.result.typeId == item.typeId}">selected</c:if> value="${item.typeId}">${item.typeName}</option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option  <c:if test="${noteInfo.typeId == item.typeId}">selected</c:if> value="${item.typeId}">${item.typeName}</option>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="title" class="col-sm-2 control-label">标题:</label>
                                <div class="col-sm-8">
                                    <c:choose>
                                        <c:when test="${!empty resultInfo}">
                                            <input class="form-control" name="title" id="title" placeholder="云记标题" value="${resultInfo.result.title}">
                                        </c:when>
                                        <c:otherwise>
                                            <input class="form-control" name="title" id="title" placeholder="云记标题" value="${noteInfo.title}">
                                        </c:otherwise>
                                    </c:choose>

                                </div>
                            </div>

                            <div class="form-group">
                                <%--富文本--%>
                                    <label for="title" class="col-sm-2 control-label">内容:</label>
                                <div class="col-sm-10">
                                    <c:choose>
                                        <c:when test="${!empty resultInfo}">
                                            <%-- 准备容器，加载富文本编辑器 --%>
                                            <textarea id="content" name="content">${resultInfo.result.content}</textarea>
                                        </c:when>
                                        <c:otherwise>
                                            <%-- 准备容器，加载富文本编辑器 --%>
                                            <textarea id="content" name="content">${noteInfo.content}</textarea>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                    <%--富文本--%>
                            </div>
                            <div class="form-group">
                                    <span id="msg" class="col-sm-offset-2 col-sm-4" style="font-size: 12px;color: red">${resultInfo.msg}</span>
                                <div class="col-sm-offset-10 col-sm-4">
                                    <input type="submit" class="btn btn-primary" onclick="return checkForm()" value="保存">
                                </div>
                            </div>
                        </form>
                            </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
        var ue;
        $(function (){
            // 加载富文本编辑器 UE.getEditor('容器Id');
            ue = UE.getEditor('content');
        });
        function checkForm() {
            /*  1. 获取表单元素的值 */
            // 获取下拉框选中的选项  .val()
            var typeId = $("#typeId").val();
            // 获取文本框的值       .val()
            var title = $("#title").val();
            //  获取富文本编辑器的内容 ue.getContent()
            var content = ue.getContent();

            /* 2. 参数的非空判断 */
            if (isEmpty(typeId)) {
                $("#msg").html("请选择云记类型！");
                return false;
            }
            if (isEmpty(title)) {
                $("#msg").html("云记标题不能为空！");
                return false;
            }
            if (isEmpty(content)) {
                $("#msg").html("云记内容不能为空！");
                return false;
            }
            return true;
        }
    </script>

</div>
