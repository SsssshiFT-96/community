/**
 * 提交回复
 */
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


/**
 * 展开二级评论
 */
function collapseComments(e) {
    var id = e.getAttribute("data-id");
    //获取要展开的div的id
    var comments = $("#comment-" + id);
    //获取一下二级评论按钮的展开状态
    var collapse = e.getAttribute("data-collapse");
    //如果存在就关闭二级评论
    if(collapse){
        //该方法会为备选属性移除一个或多个class属性。
        comments.removeClass("in");
        //移除二级评论状态
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    }else{
        //该方法会为备选属性添加一个或多个class属性，而不会移除其他。
        //为那个要展开的div添加in的class属性，使其展开，也就是二级评论展开
        comments.addClass("in");
        //标记二级评论状态
        e.setAttribute("data-collapse","in");
        e.classList.add("active");
    }
}
