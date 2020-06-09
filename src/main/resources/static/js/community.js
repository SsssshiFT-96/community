function post(){
    /*获取id值和内容*/
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    /*前端做评论回复是否合规的验证*/
    if(!content){
        alert("不能回复空内容");
        return;
    }
    /*将内容通过POST请求发送给后端服务器*/
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json",
        data: JSON.stringify({
            "parentId":questionId,
            "content":content,
            "type":1
        }),
        success: function (response) {
            /*如果评论成功就关闭评论编辑画面，改为刷新页面*/
            if(response.code == 200){
                // $("#comment_section").hide();
                window.location.reload();

            }else{
                //如果是未登录，就弹出一个窗，点击接受就跳转登录
                if(response.code == 2003){
                    var isAccepted = confirm(response.message);
                    if(isAccepted){
                        window.open("https://github.com/login/oauth/authorize?client_id=486218ff86ab2b6a8825&redirect_uri=http://localhost:8081/callback&scope=user&state=1");
                        //点击成功跳转页面后，存了一个变量为closable
                        window.localStorage.setItem("closable",true);
                    }
                }else{
                    /*其他的就报错*/
                    alert(response.message);
                }
            }
            console.log(response);
        },
        dataType: "json"
    });
}
