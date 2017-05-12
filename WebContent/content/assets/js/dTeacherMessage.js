var reg = new RegExp("\\[([^\\[\\]]*?)\\]", 'igm');
// 创建新页面，调整page高度
function createNewPage(){
    $(".page").css({
        "height" : $(window).height()+"px"
    });
}
function contentLayout(){
    $(".content").css({
        "height":$(window).height()+"px"
    });
    $(".content-left").css({
        "height":$(window).height()+"px"
    });
    $(".content-center").css({
        "height":$(window).height()+"px"
    });
    $(".content-right").css({
        "height":$(window).height()+"px"
    });
}
$(window).resize(function(){
    contentLayout();
    createNewPage();
});
$(document).ready(function(){
    contentLayout();
    createNewPage();
    getData();
});

// 获取url中参数
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}
// 获取url中的数据
function getUrlData(){
     var openid = getUrlParam("openid");
     if((openid == null) || (openid == "")) goErrorPage();
    else{
        classTools.getList(openid);
    }
}
// 获取数据
function getData(){
    var openid = "oHU1c09lJWPR63kWu0fEfX1Kf1PI ";
    classTools.getList(openid);
    messageTools.loadPage();
    messageTools.initPage();
}
//班级工具模块
var classTools = {
    "list":[],
    "checked":null,
    "getList":function(openid){
        /*
            希望得到的数据
            [
                {
                    "name":"人机交互",
                    "classid":"123456"
                },
                {
                    "name":"UI设计",
                    "classid":"123457"
                }
            ]
        */
        $.ajax({
            url: "sendfindclazz.do",
                type: "post",
                data: {
                	"openId": openid
                	},

                dataType: "JSON",
                success: function(data) {
                                classTools.setList(data);
                                classTools.loadPage();
                                classTools.initPage();
                            },
                error: function() {alert("error");}
        });
    },
    "setList":function(data){
        for(var i in data){
            this.list.push(data[i]);
        }
    },
    "loadPage":function(){
        var html = document.getElementById("selectClassPage").innerHTML;
        var source = html.replace(reg, function (node, key) { return {}[key]; });
        $("#loading").remove();
        $(".content").css("display","block");
        $(".content-left").append(source);
        createNewPage();
    },
    "initPage":function(){
        var html = document.getElementById("classItem").innerHTML;
        for(var i in this.list){
            var source = html.replace(reg, function (node, key) { return {"classID":classTools.list[i]["classid"],"className":classTools.list[i]["name"]}[key]; });
            $("#classList").append(source);
        }
    },
    "click":function(argument){
        this.checked = $(argument).children("input").val();
        studentTools.getList();
    }
}
// 学生工具模块
var studentTools = {
    "list":[],
    "checkedList":[],
    "getList":function(classid){
        /*
            希望得到的数据
            [
                {
                    "name":"张三",
                    "number":"3100001",
                    "studentid":"001"
                },
                {
                    "name":"李四",
                    "number":"3100002",
                    "studentid":"002"
                },
                {
                    "name":"王五",
                    "number":"3100003",
                    "studentid":"003"
                }
            ]
        */
        $.ajax({
            url: "sendfindstudent.do",
                type: "post",
                data: {
                	"claId": classTools.checked
                	},
                dataType: "JSON",
                success: function(data) {
                                studentTools.setList(data);
                                studentTools.loadPage();
                                studentTools.initPage();
                            },
                error: function() {alert("error");}
        });
    },
    "setList":function(data){
        this.list=[];
        this.checkedList=[];
         for(var i in data){
            this.list.push(data[i]);
        }
        messageTools.getList();
    },
    "loadPage":function(){
        var html = document.getElementById("selectStudentPage").innerHTML;
        var source = html.replace(reg, function (node, key) { return {}[key]; });
        $("#selectClass").remove();
        $(".content-left").append(source);
        createNewPage();
    },
    "initPage":function(){
        var html = document.getElementById("studentItem").innerHTML;
        for(var i in this.list){
            var source = html.replace(reg, function (node, key) { return {"studentID":studentTools.list[i]["studentid"],"studentName":studentTools.list[i]["name"]}[key]; });
            $("#studentList").append(source);
        }        
    },
    "judgeCheckState":function(target){
        for(var i in this.checkedList){
            if(this.checkedList[i]["studentID"]==target) return i;
        }
        return -1;
    },
    "sortCheckList":function(){
        function rule(a,b){
            var aid = parseInt(a.studentID);
            var bid = parseInt(b.studentID);
            return aid - bid;
        }
        return this.checkedList.sort(rule);
    },
    "click":function(argument){
        var name = $(argument).children(".weui-cell__bd").children("p").text();
        var studentID = $(argument).children(".weui-cell__bd").children("input").val();
        var student = {
            "name":name,
            "studentID":studentID
        }
        var checkState = this.judgeCheckState(studentID);
        if(checkState>=0) ;
        else{
            this.checkedList.push(student);
        }
        this.sortCheckList();
        messageTools.getList();
    },
    "selectAll":function(){
        $("#studentList").children(".weui-cell").click();
    },
    "clearCheckList":function(){
        this.checkedList = [];
        messageTools.getList();
    },
    "back":function(){
        $("#selectStudent").remove();
        classTools.loadPage();
        classTools.initPage();
    }
}
// 消息工具模块
var messageTools = {
    "list":[],
    "getList":function(){
        this.setList(studentTools.checkedList);
    },
    "setList":function(arr){
        this.list = [];
        for(var i in arr){
            this.list.push(arr[i]);
        }
        $("#addresseeList").children().remove();
        for(var i in this.list){
            $("#addresseeList").append("<button onclick='messageTools.click(this)'><input class='input-data__hide' value="+this.list[i].studentID+"><p>"+this.list[i].name+"</p></button>");
        }
    },
    "loadPage":function(){
        var html = document.getElementById("messagePage").innerHTML;
        var source = html.replace(reg, function (node, key) { return {}[key]; });
        $(".content-center").append(source);
        createNewPage();
    },
    "initPage":function(){
        
        $("#messageBox").children(".weui-cell__bd").children("textarea").bind('input', function(){  
            $("#messageBox").children(".weui-cell__bd").children("div").children("span").text($(this).val().length);
        });  
    },
    "click":function(argument){
        var studentID = $(argument).children("input").val();
        var name = $(argument).children("p").text();
        var student = {
            "name":name,
            "studentID":studentID
        }
        var checkState = studentTools.judgeCheckState(studentID);
        if(checkState>=0) {
            studentTools.checkedList.splice(checkState,1);
        }
        studentTools.sortCheckList();
        this.getList();
    },
    "submit":function(){
        var openid = "oHU1c09lJWPR63kWu0fEfX1Kf1PI";
        var addresseeList = "";
        for(var i in this.list){
            addresseeList = addresseeList+this.list[i].studentID+",";
        }
        addresseeList = addresseeList+"]";
        var message = $("#messageBox").children(".weui-cell__bd").children("textarea").val();
        var data = "openId="+openid+"&addresseeList="+addresseeList+"&message="+message+"&classid="+classTools.checked;
        //data数据大概是这样的：
        //openid=1111&addresseeList=[001,002,003,]&message=hello world
        $.ajax({
            url: "sendmessage.do",
                type: "post",
                data: data,
                dataType: "JSON",
                success: function(data) {
                    $("#toast").css({
                        "opacity": 1, 
                        "display":"block"
                    });
                    setTimeout( function nonewarn(){ 
                        $("#toast").css({
                            "opacity":0,
                            "display":"none"
                        });
                    } ,2000);
                },
                error: function() {
                    alert("error");
                }
        });
    }
}
// 跳转至error页
function goErrorPage(){
    window.location.href="./state.html?state=1&info=1";
}
function change(){
    $("#call").children(".weui-cell__bd").children(".weui-label-in").css("display","none");
    $("#call").children(".weui-cell__bd").children(".weui-input").val($("#call").children(".weui-cell__bd").children(".weui-label-in").text());
    $("#call").children(".weui-cell__bd").children(".weui-input").css("display","");
    $("#call").children(".weui-cell__bd").children(".weui-input").focus();
    $("#email").children(".weui-cell__bd").children(".weui-label-in").css("display","none");
    $("#email").children(".weui-cell__bd").children(".weui-input").val($("#email").children(".weui-cell__bd").children(".weui-label-in").text());
    $("#email").children(".weui-cell__bd").children(".weui-input").css("display","");
    $("#change").css("display","none");
    $("#ok").css("display","inline-block");
}
function ok(){
    $("#call").children(".weui-cell__bd").children(".weui-input").css({"display":"none"});
    $("#call").children(".weui-cell__bd").children(".weui-label-in").text($("#call").children(".weui-cell__bd").children(".weui-input").val());
    $("#call").children(".weui-cell__bd").children(".weui-label-in").css({"display":""});

    $("#email").children(".weui-cell__bd").children(".weui-input").css("display","none");
    $("#email").children(".weui-cell__bd").children(".weui-label-in").text($("#email").children(".weui-cell__bd").children(".weui-input").val());
    $("#email").children(".weui-cell__bd").children(".weui-label-in").css("display","");
    $("#ok").css("display","none");
    $("#change").css("display","inline-block");
}