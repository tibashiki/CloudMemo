function deleteType(typeId){
    swal({
        title:"",
        text:"<h3>确认要删除这条记录吗</h3>",
        type:"warning",
        showCancelButton:true,
        confirmButtonColor:"orange",
        confirmButtonText:"确定",
        cancelButtonText:"取消"
    }).then(function(){
        //如果用户确认删除，则发送ajax请求，给后端
        $.ajax({
            type:"post",
            url:"type",
            data:{
                actionName:"delete",
                typeId:typeId
            },
            success:function(result){
                //判断是否删除成功
                if(result.code==1){
                    swal("","<h3>删除成功</h3>","success");
                    deleteDom(typeId);
                }else{
                    swal("","<h3>"+result.msg+"</h3>","error");
                }
            }
        });
    });
}

/**
 * 删除类型的Dom操作
 * @param typeId
 */
function deleteDom(typeId){
    var myTable = $("#myTable");
    var trLength = $("#myTable tr").length;
    if(trLength==2){
        $("#myTable").remove();//删掉整个表格
        $("#myDiv").html("<h2>暂未查询到类型数据!</h2>");
    }else{
        $("#tr_"+typeId).remove();//如果tr的长度大于2，删除（remove）指定tr对象
    }
    $("#li_"+typeId).remove();
}

/**
 * 打开添加模态框
 * 绑定“添加类型”的点击事件
 */
$("#addBtn").click(function(){
    //修改添加模态框的标题
    $("#myModalLabel").html("新增类型");
    //清空一下
    $("#typeId").val("");
    $("#typeName").val("");
    //清空信息
    $("#msg").html("");
    $("#myModal").modal("show");
});

/**
 * 打开修改模态框
 * 绑定“修改”按钮的点击事件
 * @param typeId
 */
function openUpdateDialog(typeId){
    $("#myModalLabel").html("修改类型");
    //得到当前修改按钮的类型记录
    //通过id选择器，获取当前的tr
    var tr = $("#tr_"+typeId);
    //得到tr具体的单元格的值（第二个td,下标是1）
    var typeName = tr.children().eq(1).text();
    //将类型名称设置给模态框中的文本框
    $("#typeName").val(typeName);
    var typeId = tr.children().eq(0).text();
    $("#typeId").val(typeId);
    $("#msg").html("");
    $("#myModal").modal("show");
}

/**
 * 判断参数是否为空
 * 如果为空，提示信息，return
 * 发送ajax请求后台，执行添加或修改功能，返回ResultInfo对象（通过类型ID是否为空来判断）
 * 如果code=0,失败，提示用户
 * 如果code=1，成功，执行Dom操作
 *      如果ID为空，增加；如果ID不为空，修改；
 */
$("#btn_submit").click(function(){
    //获取参数（文本框：类型名称，隐藏域，类型ID）
    var typeName = $("#typeName").val();
    var typeId = $("#typeId").val();
    if(isEmpty(typeName)){
        $("#msg").html("类型名称不能为空！");
        return;
    }
    //发送ajax对象请求后台，执行添加或者修改功能，返回ResultInfo对象
    $.ajax({
        type:"post",
        url:"type",
        data:{
            actionName: "addOrUpdate",
            typeName:typeName,
            typeId:typeId
        },
        success:function (result){
            //判断是否成功
            if(result.code==1){
                $("#myModal").modal("hide");
                if(isEmpty(typeId)){//为空
                    addDom(typeName,result.result);
                }else {
                    updateDom(typeName,typeId);
                }
            }else {
                $("#msg").html(result.msg);
            }
        }
    });
});

/**
 * 修改的DOM操作
 * 修改左侧类型分组导航栏的列表项
 * 给左侧类型名设置Span标签，并指定id属性，修改span元素的文本值
 * @param typeName
 * @param typeId
 */
function updateDom(typeName,typeId){
    var tr = $("#tr_"+typeId);
    tr.children().eq(1).text(typeName);
    $("#sp_"+typeId).html(typeName);

}

/**
 * 添加tr记录
 * 1拼接tr标签
 * 2获取表格对象
 * 判断表格对象是否存在(length>0),不存在先新建表格
 * 3将tr标签追加到表格当中
 * 左侧分组导航栏的列表项
 * 1拼接li元素
 *
 * @param typeName
 * @param typeId
 */
function addDom(typeName,typeId) {
    var tr = '<tr id="tr_' + typeId + '">' +
        '<td>' + typeId + '</td>' +
        '<td>' + typeName + '</td>';
    tr += '<td>' +
        '<button className="btn btn-primary" type="button" onClick="openUpdateDialog(' + typeId + ')">修改</button>&nbsp;';
    tr += '<button className="btn btn-danger del" type="button" onClick="deleteType(' + typeId + ')">删除</button>\n' +
        '</td>\n' +
        '</tr>';
    var myTable = $("#myTable");
    if (myTable.length>0){
        myTable.append(tr);
    }else{
        //表示表格不存在
        myTable='<table id="myTable" class="table table-hover table-striped ">';
        myTable+='<tbody><tr><th>编号</th><th>类型</th><th>操作</th></tr>';
        myTable+=tr+'</tbody></table>';
        $("#myDiv").html(myTable);
    }
    var li = '<li id="li_'+typeId+'"><a href=""><span id="sp_'+typeId+'">'+typeName+'</span> <span class="badge">0</span></a></li>';
    $("#typeUl").append(li);
}






