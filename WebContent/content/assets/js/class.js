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
    if((openid == null) || (openid == "")) goStatePage(2,2);
    else if((classid == null) || (classid == "")) goStatePage(3,2);
    else{
        $.ajax({
            url: "./content/assets/json/test_class.json",
                type: "get",
                data: {
                	"claId": classid
                	},
                dataType: "JSON",
                success: function(data) {
                                loadClassPage(data);
                            },
                error: function() {
                    goStatePage(3,1);
                }
        });
        // $.ajax({
        //     url: "findclazz.do",
        //         type: "post",
        //         data: {
        //         	"claId": classid
        //         	},
        //         dataType: "JSON",
        //         success: function(data) {
        //                         loadClassPage(data);
        //                     },
        //         error: function() {
        //             goStatePage(3,1);
        //         }
        // });
    }
}
// 加载ClassPage
function loadClassPage(data){
    var html = document.getElementById("classPage").innerHTML;
    var source = html.replace(reg, function (node, key) { return {"className":data.class_name,"classInfo":data.class_info,"teacher_id":data.teacher_id,"teacherName":data.teacher_name,"time":getTime(data.time)}[key]; });
    $("#loading").remove();
    $(document.body).append(source);
    createNewPage();
    initPage();
}
// 时间处理
function getTime(time){
    var week = ["周一","周二","周三","周四","周五","周六","周日"];
    var startList = ["8:00","8:50","9:50","10:40","11:30","13:30","14:20","15:20","16:10","18:30","19:20","20:10"];
    var endList = ["8:45","9:35","10:35","11:25","12:15","14:15","15:05","16:05","16:55","19:15","20:05","20:55"];
    var data_week = time.substr(0,time.indexOf(":"));
    var data_time = time.substr(time.indexOf(":")+1);
    var x = data_time.indexOf("-");
    var start = parseInt(data_time.substring(0,x)) - 1;
    var end = parseInt(data_time .substring(x+1)) - 1;
    return week[parseInt(data_week)-1]+" "+startList[start] + '-' + endList[end];
}
// 初始化Page
function initPage(){
    var openid=getUrlParam("openid");
    $("#teacher").attr("href","./teacher.html?openid="+openid+"&teacherid="+$("#teacherID").val());
}
// 跳转至state页
function goStatePage(state,content){
    window.location.href="./state.html?state="+state+"&info="+content;
}

// 历史消息页面
function messageHistory(){
    window.location.href="./messageHistory.html?openid="+getUrlParam("openid")+"&classid="+getUrlParam("classid");
}