function submit(){
    $.ajax({
            url: "./content/assets/json/test_message_students.json",
                type: "get",
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
    window.location.href="./dTeacherMessage.html";
}