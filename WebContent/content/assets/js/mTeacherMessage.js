/*
    这次稍微改了一下写法
    主要分为三个模块，班级、学生、消息
    可以在下面，classTools、studentTools、messageTools中找到相关部分
    主要是classTools.getList()、studentTools.getList()、messageTools.sublime();
*/

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
// 获取url中的数据
function getUrlData(){
     var openid = getUrlParam("openid");
     if((openid == null) || (openid == "")) goStatePage(2,2);
    else{
        classTools.getList(openid);
    }
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
                error: function() {
                    goStatePage(3,1);
                }
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
        $(document.body).append(source);
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
                error: function() {
                    goStatePage(3,1);
                }
        });
    },
    "setList":function(data){
         for(var i in data){
            this.list.push(data[i]);
        }       
    },
    "loadPage":function(){
        var html = document.getElementById("selectStudentPage").innerHTML;
        var source = html.replace(reg, function (node, key) { return {}[key]; });
        $("#selectClass").remove();
        $(document.body).append(source);
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
        var name = $(argument).parent(".weui-cell__hd").parent("label").children(".weui-cell__bd").children("p").text();
        var studentID = $(argument).parent(".weui-cell__hd").parent("label").children(".weui-cell__bd").children("input").val();
        var student = {
            "name":name,
            "studentID":studentID
        }
        var checkState = this.judgeCheckState(studentID);
        if(checkState>=0){
            this.checkedList.splice(checkState,1);
        }
        else{
            this.checkedList.push(student);
        }
        this.sortCheckList();
    },
    "selectAll":function(){
        this.clearCheckList();
        $("#studentList label").click();
    },
    "clearCheckList":function(){
        $("#studentList label").children(".weui-cell__hd").children("input").prop("checked",false);
        this.checkedList = [];
    },
    "next":function(){
        messageTools.getList();
    }
}
// 消息工具模块
var messageTools = {
    "list":[],
    "getList":function(){
        this.setList(studentTools.checkedList);
        this.loadPage();
        this.initPage();
    },
    "setList":function(arr){
        for(var i in arr){
            this.list.push(arr[i]);
        }
    },
    "loadPage":function(){
        var html = document.getElementById("messagePage").innerHTML;
        var source = html.replace(reg, function (node, key) { return {}[key]; });
        $("#selectStudent").remove();
        $(document.body).append(source);
        createNewPage();
    },
    "initPage":function(){
        for(var i in this.list){
            $("#addresseeList").append("<button><p>"+this.list[i].name+"</p></button>");
        }
        $("#messageBox").children(".weui-cell__bd").children("textarea").bind('input', function(){  
            $("#messageBox").children(".weui-cell__bd").children("div").children("span").text($(this).val().length);
        });  
    },
    "submit":function(){
        var openid = getUrlParam("openid");
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
                    goStatePage(0,1);
                },
                error: function() {
                    goStatePage(3,1);
                }
        });
    }
}
// 跳转至state页
function goStatePage(state,content){
    window.location.href="./state.html?state="+state+"&info="+content;
}
