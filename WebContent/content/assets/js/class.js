// 创建新页面，调整page高度
function createNewPage(){
    $(".page").css({
        "height" : $(window).height()-39+"px"
    });
}

$(document).ready(function(){
    getUrlData();
});

// 获取url中参数
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}
// 获取URL中的数据，获取课程数据
// 修改url
// 修改type为post
// 预期的数据为：
/*
    {
        "class_id" : "课程id",
        "class_name" : "课程名字",
        "class_info" : "课程介绍",
        "teacher_id" : "教师openid",
        "teacher_name" : "教师姓名",
        "count":"16周",
        "time":"周一 9:00-11:25"
    }
*/
// 这页有个错误，不知道是本地json的错误还是获取代码不合法
// 数据获取正常，但错误代码大致为 Cannot read property 'class_name' of undefined
// 帮忙测试获取服务器数据后，该报错是否还存在
function getUrlData(){
    var openid = getUrlParam("openid");
    var classid = getUrlParam("classid");
    if((openid == null) || (openid == "")) goErrorPage();
    else if((classid == null) || (classid == "")) goErrorPage();
    else{
        $.ajax({
            url: "./content/assets/json/test_class.json",
                type: "get",
                data: "openid="+openid+"classid="+classid,
                dataType: "JSON",
                success: function(data) {
                                loadClassPage(data);
                            },
                error: function() {alert("error");}
        });
        loadClassPage();
    }
}
// 加载ClassPage
function loadClassPage(data){
    var html = document.getElementById("classPage").innerHTML;
    var source = html.replace(reg, function (node, key) { return {"className":data.class_name,"classInfo":data.class_info,"teacher_id":data.teacher_id,"teacherName":data.teacher_name,"count":data.count,"time":data.time}[key]; });
    $("#loading").remove();
    $(document.body).append(source);
    createNewPage();
    initPage();
}
// 初始化Page
function initPage(){
    var openid=getUrlParam("openid");
    $("#teacher").attr("href","./teacher.html?openid="+openid+"&teacherid="+$("#teacherID").val());
}
// 跳转至error页
function goErrorPage(){
    window.location.href="./state.html?state=1&info=1";
}