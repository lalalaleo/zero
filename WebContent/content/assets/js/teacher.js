// 教师对象，用于数据提取
var teacher = {
    openid: null,
    name: null,
    info: null,
    classes: [],
    mobile: null,
    email: null
};
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
// 获取数据，目前是获取本地json中的测试数据。
// 修改url，type改为post
// data是发送的数据，格式和url后参数一样，例如："a=[]&b=[]"
// success中去掉if语句（本地json数据不止一组），之后修改为loadTeacherPage(data)即可;
// 预设获取的数据类型，不需要考虑数据类型，都为字符串
/*
    {
        "openid" : ,                                  // openid
        "name" : "",                                 // 教师姓名
        "info" : "",                                    // 教师介绍
        "classes" : ["","",""],                     // 课程数组
        "mobile" : ,                                 // 手机号码
        "email" : ""                                 // 邮箱
    }
*/
function getUrlData(){
    var openid = getUrlParam("openid");
    var teacherid = getUrlParam("teacherid");
    if(openid==null || openid=="") goErrorPage();
    else if(teacherid==null || teacherid=="") goErrorPage();
    else{
        $.ajax({
                url: "findteacher.do",
                type: "post",
                data: {
                	"useId": teacherid
                	},
                dataType: "JSON",
                success: function(data) {
                                loadTeacherPage(data);
                            },
                error: function() {alert("error");}
            });
    }
}
// 数据传递至全局变量teacher
function getTeacherInfo(JSONObject){
    teacher.openid = JSONObject.openid;
    teacher.name = JSONObject.name;
    teacher.info = JSONObject.info;
    teacher.classes = JSONObject.classes;
    teacher.mobile = JSONObject.mobile;
    teacher.email = JSONObject.email;
}
// 加载教师信息页面
function loadTeacherPage(JSONObject){
    getTeacherInfo(JSONObject);
    if(teacher.openid==null || teacher.name==null) goErrorPage();
    else {
        var html = document.getElementById("teacherPage").innerHTML;
        var source = html.replace(reg, function (node, key) { return {}[key]; });
        $("#loading").remove();
        $("title").text(teacher.name);
        $(document.body).append(source);
        createNewPage();
        initPage()
    };
}
// 教师信息页面初始化
function initPage(){
    var html = document.getElementById("classCell").innerHTML;
    // 教师姓名
    $("#teacher .page__hd .page__title").text(teacher.name);
    // 教师介绍
    if(teacher.info!=null&&teacher.info.length>0){
        if(teacher.info.length>50)
            $("#teacher .page__hd .page__desc").text(teacher.info.slice(0,51)+"...");
        else $("#teacher .page__hd .page__desc").text(teacher.info);
    }
    // 教授课程
    if(teacher.classes.length>0) $(".page .page__bd #noClass").css("display","none");
    for(var i in teacher.classes){
        var source = html.replace(reg, function (node, key) { return { 'className': teacher.classes[i]}[key]; });
		$("#teacher .page__bd #classesList").append(source);
    }
    // 手机号
    if(teacher.mobile!=null){
        $("#teacher .page__bd #contact div:first-child .weui-cell__bd").text(teacher.mobile)
    };
    // 邮件
    if(teacher.email!=null){
        $("#teacher .page__bd #contact div:nth-child(2) .weui-cell__bd").text(teacher.email);
    }
}
// 切换显示全部教师介绍
function switchAllTeacherInfo(){
    if(teacher.info.length>50){
        if($("#teacher .page__hd .page__desc").text()!=teacher.info){
            $("#teacher .page__hd .page__desc").text(teacher.info);
        }
        else{
            $("#teacher .page__hd .page__desc").text(teacher.info.slice(0,51)+"...");
        }
    }
}
// 跳转至error页
function goErrorPage(){
    window.location.href="./state.html?state=1&info=1";
}

