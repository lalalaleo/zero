// 创建新页面，调整page高度
function createNewPage(){
    $(".page").css({
        "height" : $(window).height()-39+"px"
    });
}
$(document).ready(function(){
    createNewPage();
    getUrlData();
});
// 获取url中参数
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}
// 获取url中数据
// 修改url
// 修改type为post
// 预期的数据为：
/*
    [
        {
            "name":"计算机网络",
            "time":"1:1-2;6:10-12;"
        },
        {
            "name":"日语",
            "time":"1:3-4;"
        },
        {
            "name":"Android开发",
            "time":"2:1-2;"
        },
        {
            "name":"人机交互",
            "time":"4:7-8;"
        },
        {
            "name":"UI设计",
            "time":"5:1-4;6:1-2;"
        },
        {
            "name":"创新思维",
            "time":"7:1-4;"
        }
    ]
*/
function getUrlData(){
    var openid = getUrlParam("openid");
    if((openid == null) || (openid == "")) goStatePage(2,2);
    else{
        $.ajax({
            url: "clazztable.do",
                type: "post",
                data: {
                	"openId": openid
                	},
                dataType: "JSON",
                success: function(data) {
                                loadSchoolTimeTablePage(data);
                            },
                error: function() {
                    goStatePage(3,1);
                }
        });
    }
}

// 加载课表页面
function loadSchoolTimeTablePage(data){
    var html = document.getElementById("schoolTimetablePage").innerHTML;
    var source = html.replace(reg, function (node, key) { return {}[key]; });
    $("#loading").remove();
    $(document.body).append(source);
    createNewPage();
    initPage(data);
}
// 初始化课表页面
function initPage(data){
    loadTodayClass(data);
    loadWeekClass(data);
}
// 加载今天课程
function loadTodayClass(data){
    var today = new Date();
    var html = document.getElementById("item_today_class").innerHTML;
    var source = html.replace(reg, function (node, key) { return {}[key]; });
    $("#tabPanelToday").append(source);
    var todayClassList=screenData(today.getDay(),data);
    for(var i in todayClassList){
        html = document.getElementById("item_class").innerHTML;
        source = html.replace(reg, function (node, key) { return {"className":todayClassList[i].name,"classTime":todayClassList[i].time}[key]; });
        $("#tabPanelToday").children(".weui-panel:nth-child(2)").children(".weui-panel__bd").append(source);
        switch(judgeClassState(todayClassList[i].time)){
            case "ago" : $("#tabPanelToday").children(".weui-panel:nth-child(2)").children(".weui-panel__bd").children("div:last-child").addClass("ago-class");break;
            case "now" : $("#tabPanelToday").children(".weui-panel:nth-child(2)").children(".weui-panel__bd").children("div:last-child").addClass("now-class");break;
            default : ;
        }
    }
}
// 加载本周课程
function loadWeekClass(data){
    var today = new Date();
    $("#tabPanelWeek").append(source);
    var week = ["周一","周二","周三","周四","周五","周六","周日"];
    var weekList = [screenData(1,data),screenData(2,data),screenData(3,data),screenData(4,data),screenData(5,data),screenData(6,data),screenData(7,data)];
    for(var i in weekList){
        if(weekList[i].length>0){
            var html = document.getElementById("item_week_class").innerHTML;
            var source = html.replace(reg, function (node, key) { return {"weekid":"week_"+i,"week":week[i]}[key]; });
            $("#tabPanelWeek").append(source);
            for(var j in weekList[i]){
                html = document.getElementById("item_class").innerHTML;
                source = html.replace(reg, function (node, key) { return {"className":weekList[i][j].name,"classTime":weekList[i][j].time}[key]; });
                $("#week_"+i).children(".weui-panel__bd").append(source);
                if(i < today.getDay()-1){
                    $("#week_"+i).children(".weui-panel__bd").children("div").addClass("ago-class");
                }
                else if(i == today.getDay()-1){
                    switch(judgeClassState(weekList[i][j].time)){
                        case "ago" : $("#week_"+i).children(".weui-panel__bd").children("div:last-child").addClass("ago-class");break;
                        case "now" : $("#week_"+i).children(".weui-panel__bd").children("div:last-child").addClass("now-class");break;
                        default : ;
                    }
                }
                else{;}
            }
            
        }
    }
}
// 筛选数据
function screenData(t,data){
    var screenList = new Array();
    for(var i in data){
        var time = data[i].time;
        var x = 0;
        while(time.indexOf(":",x) != -1){
            var k = time.indexOf(":",x);
            x = k+1;
            if(time[k-1]==t){
                screenList.push({
                    "name":data[i].name,
                    "time":getTimeInData(data[i],k-1)
                });
            }
        }
    }

    return sortList(screenList);
}
// 获取数据中的时间
function getTimeInData(obj,start){
    var end = obj.time.indexOf(";",start);
    return translateTime(obj.time.substring(start+2,end));
};
// 转化时间
function translateTime(time){
    var startList = ["8:00","8:50","9:50","10:40","11:30","13:30","14:20","15:20","16:10","18:30","19:20","20:10"];
    var endList = ["8:45","9:35","10:35","11:25","12:15","14:15","15:05","16:05","16:55","19:15","20:05","20:55"];
    var x = time.indexOf("-");
    var start = parseInt(time.substring(0,x)) - 1;
    var end = parseInt(time .substring(x+1)) - 1;
    return startList[start] + '-' + endList[end];
}
// 判断课程状态
function judgeClassState(time){
    var startHour = parseInt(time.substring(0,time.indexOf(":")));
    var startMin =parseInt(time.substring(time.indexOf(":")+1,time.indexOf("-")));
    var endHour = parseInt(time.substring(time.indexOf("-")+1,time.indexOf(":",time.indexOf("-"))));
    var endMin =parseInt(time.substring(time.indexOf(":",time.indexOf("-"))+1));
    var now = new Date();
    if(now.getHours()<startHour){
        return "future";
    }
    else if(now.getHours()>endHour){
        return "ago"
    }
    else if(now.getHours()==startHour&&now.getMinutes()<startMin){
        return "future";
    }
    else if(now.getHours()==endHour&&now.getMinutes()>endMin){
        return "ago";
    }
    else{
        return "now";
    }
}
// 数组排序
function  sortList(array){
    function rule(a){
        var time = a.time;
        var x = time.indexOf(":");
        var string = time.indexOf(0,x);
        return parseInt(string);
    }
    return array.sort(rule);
}
// Tab 今天点击时间
function tabTodayCilck(){

}
// Tab 本周点击时间
function tabCilck(argument){
    var width = $(window).width();
    $(".weui-navbar").children("div").attr("class","weui-navbar__item");
    $(argument).addClass("weui-bar__item_on");
    if($(".weui-bar__item_on").text()=="本周"){
        $("#tabPanelToday").css("display","none");
        $("#tabPanelWeek").css( "display","block");
    }
    else{
        $("#tabPanelToday").css("display","block");
        $("#tabPanelWeek").css( "display","none");
    }
}
// 跳转至state页
function goStatePage(state,content){
    window.location.href="./state.html?state="+state+"&info="+content;
}
