var schoolList = {};
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
// 获取url中数据
function getUrlData(){
    var openid = getUrlParam("openid");
    if(openid==null || openid=="") goErrorPage();
    else{
        loadBindingPage(openid);
    }
}
function loadBindingPage(openid){
    var html = document.getElementById("bindingPage").innerHTML;
    var source = html.replace(reg, function (node, key) { return {}[key]; });
    $("#loading").remove();
    $(document.body).append(source);
    createNewPage();
    initPage();
}
function initPage(){
    getSchoolList();
}
// 获取学校列表
function getSchoolList(){
    $.ajax({
            url: "./content/assets/json/schoolList.json",
            type: "get",
            dataType: "JSON",
            success: function(data) {
                            screenSchoolList(data);
                        },
            error: function() {alert("error");}
        });
}
// 筛选有效省份、市县、学校
function screenSchoolList(data){
    for(var province in data){
        for(var city in data[province]){
            if( data[province][city] != null){
                if(schoolList[province]==undefined){
                    schoolList[province]={};
                }
                schoolList[province][city] = data[province][city];
            }
        }
    }
    initSchoolItem();
}
// 学校选择项填充
function initSchoolItem(){
    function loadProvince(){
        for(var province in schoolList){
            $("#select-province").append("<option>" +province+"</option>");
        }
    }
    function loadCity(province){
        $("#select-city").children().remove();
        $("#select-city").append("<option>" +''+"</option>");
        if(province!=""){
            for(var city in schoolList[province]){
                $("#select-city").append("<option>" +city+"</option>");
            }
        }
    }
    function loadSchool(province,city){
        $("#select-school").children().remove();
        $("#select-school").append("<option>" +''+"</option>");
        if(city != "" && province != ""){
            for(var i in schoolList[province][city]){
                var school = schoolList[province][city][i];
                $("#select-school").append("<option>" +school+"</option>");
            }
        }
    }
    $("#select-province").change(function(){
        loadCity($(this).find("option:selected").text());
        loadSchool($("#select-province").find("option:selected").text(),$(this).find("option:selected").text());
    });
    $("#select-city").change(function(){
        loadSchool($("#select-province").find("option:selected").text(),$(this).find("option:selected").text());
    });
    loadProvince();
}
// 同意协议
function agree(argument){
    if($(argument).prop("checked") == true){
        $("#submit").attr("class","weui-btn weui-btn_abled weui-btn_primary");
    }
    else{
        $("#submit").attr("class","weui-btn weui-btn_disabled weui-btn_primary");
    }
        btn_submit_click();
}
// submit点击事件
function btn_submit_click(){
    var btn_submit = $("#submit");
    if(btn_submit.attr("class")=="weui-btn weui-btn_abled weui-btn_primary"){
        btn_submit.click(function(){
            $("#name-warn").css("display","none");
            $("#number-warn").css("display","none");
            if($("#name").val()==""){
                showWarnInfo($("#name"),"请填写您的姓名");
            }
            else if($("#number").val()==""){
                showWarnInfo($("#number"),"请填写您的学号/工号");
            }
            else if(($("#select-school").find("option:selected").text()==" ")||($("#select-school").find("option:selected").text()=="")){
                showWarnInfo($("#select-school"),"请选择您的学校");
            }
            else{
                formSubmit();
            }
        });
    }
    else{
        btn_submit.unbind();
    }
}
// 提交表单
// 修改url
function formSubmit(){
    var openid = getUrlParam("openid");
    var name = $("#name").val();
    var number = $("#number").val();
    var school = $("#select-school").find("option:selected").text();
    $.ajax({
            url: "",
            type: "post",
            data:"openid="+openid+"&name="+name+"&number="+number+"&school="+school,
            dataType: "JSON",
            success: function(data) {
                            goErrorPage();  //此处状态为成功，并非异常状态，具体内容待完善。 
                        },
            error: function() {alert("error");}
        });
}
// 异常信息提示
function showWarnInfo(e,info){
    if($("#warn").css("display")=="none"){
        e.focus();
        e.parent().parent().children(".weui-cell__ft").children("i").css("display","block");
        $("#warn").text(info);
        $("#warn").css("display","block");
        setTimeout( function nonewarn(){ 
            $("#warn").css("display","none");
        } ,2000);
    }
}
// 跳转至error页
function goErrorPage(){
    window.location.href="./state.html?state=1&info=1";
}