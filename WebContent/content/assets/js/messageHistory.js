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
// 获取url中的数据
// 修改url，data
// url带的参数是openid和classid，如果classid=all，显示所有历史消息，classid为具体课程id，显示该门课程历史消息
// 想要的数据
/*
[
    {
        "msgid":"121",
        "class":"人机交互",
        "sender":"侯宏仑",
        "content":"明天早上教室换到教三201，谢谢配合",
        "time":"2017.5.10 10:27"
    },
    {
        "msgid":"122",
        "class":"Oracle",
        "sender":"朱勇",
        "content":"下周三，考试，请同学们好好准备",
        "time":"2017.5.10 13:33"
    },
    {
        "msgid":"123",
        "class":"Android开发",
        "sender":"彭斌",
        "content":"近期将召开Android开发竞赛，希望同学们积极参加！",
        "time":"2017.5.10 16:56"

type: "post",

    }
]
*/
// 顺便帮忙测试一下，课程页面的历史消息
function getUrlData(){
    var openid = getUrlParam("openid");
    var classid = getUrlParam("classid");
    if((openid == null) || (openid == "")) goStatePage(2,2);
    else if((classid == null) || (classid == "")) goStatePage(3,2);
    else{
        $.ajax({
        		url: "history.do",
        		type: "post",
                data: {
                	"openId": openid,
                	"classid": classid
                	},
                dataType: "JSON",
                success: function(data) {
                                loadMessageHistoryPage(data);
                            },
                error: function() {
                    goStatePage(3,1);
                }
        });
    }
}
// 加载ClassPage
function loadMessageHistoryPage(data){
    var html = document.getElementById("messageHistoryPage").innerHTML;
    var source = html.replace(reg, function (node, key) { return {}[key]; });
    $("#loading").remove();
    $(document.body).append(source);
    createNewPage();
    initPage(data);
}
// 初始化Page
function initPage(data){
    if(getUrlParam("classid")!="all") $("#messageHistory").children(".page__hd").children(".page__desc").text(data[0]["class"]);
    for(var i in data){
        var msgid = data[i]["msgid"];
        var className = data[i]["class"];
        var sender = data[i]["sender"];
        var content = data[i]["content"];
        var time = data[i]["time"]
        var html = document.getElementById("messagePreview").innerHTML;
        var source = html.replace(reg, function (node, key) { return {"msgid":msgid,"class":className,"sender":sender,"content":content,"time":time}[key]; });
        $("#messageHistory").children(".page__bd").append(source);
    }
}
// 跳转至state页
function goStatePage(state,content){
    window.location.href="./state.html?state="+state+"&info="+content;
}
