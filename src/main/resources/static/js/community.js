function post(){
    /*获取id值和内容*/
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
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
            /*如果评论成功就关闭评论编辑画面*/
            if(response.code == 200){
                $("#comment_section").hide();
            }else{
                /*失败就报错*/
                alert(response.message);
            }
            console.log(response);
        },
        dataType: "json"
    });
}
