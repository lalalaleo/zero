function submit(){
    // $.ajax({
    //         url: "",
    //             type: "get",
    //             data: {
    //             	"username": "123",
    //                 "password": "123"
    //             	},
    //             dataType: "JSON",
    //             success: function(data) {
    //             },
    //             error: function() {alert("error");}
    //     });
        var username = $("input[name='username']").val();
        var password = $("input[name='password']").val();
        switch(username){
            case "root": window.location.href="./dAdmin.html";break;
            default : window.location.href="./dTeacherMessage.html";
        }
}