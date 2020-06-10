/**
 * 提交回复
 */
function post(){
    /*获取id值和内容*/
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    comment2target(questionId, 1, content);
}
function comment2target(targetId, type, content) {
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
            "parentId":targetId,
            "content":content,
            "type":type
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

function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-"+commentId).val();
    comment2target(commentId, 2, content)
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
        var subCommentContainer = $("#comment-" + id);
        //判断子元素是否大于1（因为评论框算一个元素），
        // 大于就不用再显示，只有不大于才要去显示，不然每次刷新都会再显示出来
        if(subCommentContainer.children().length != 1){
            //该方法会为备选属性添加一个或多个class属性，而不会移除其他。
            //为那个要展开的div添加in的class属性，使其展开，也就是二级评论展开
            comments.addClass("in");
            //标记二级评论状态
            e.setAttribute("data-collapse","in");
            e.classList.add("active");
        }else{

            $.getJSON( "/comment/"+id, function(data) {
                console.log(data);
                //开始绘制二级评论界面（不在html中绘制）

                $.each(data.data.reverse(), function(index,comment) {

                    var avatarElement =$("<img/>",{
                        "class":"media-object img-circle",
                        "src":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1591516733853&di=6f72ea28ae69a71ce037b3a43df412dc&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201812%2F25%2F20181225102434_fsilt.thumb.400_0.jpg"
                    });

                    var mediaLeftElement =$("<div/>",{
                        "class":"media-left"
                    });
                    mediaLeftElement.append(avatarElement);

                    var mediaBodyElement= $("<div/>",{
                        "class":"media-body"
                    }).append($("<h5/>",{
                        "class":"media-heading",
                        "html":comment.user.name
                    })).append($("<div/>",{
                        "html":comment.content
                    })).append($("<div/>",{
                        "class":"menu"
                    }).append($("<span/>",{
                        "class":"pull-right",
                        "html":moment(comment.getCreate).format('YYYY-MM-DD')
                    })));

                    var mediaElement = $("<div/>",{
                        "class":"media"
                    });
                    mediaElement.append(mediaLeftElement)
                        .append(mediaBodyElement);

                    //定义一个div
                    var commentElement = $("<div/>",{
                        "class":"col-lg-12 col-md-12 col-sm-12 col-xs-12 comments",
                    });
                    commentElement.append(mediaElement);
                    subCommentContainer.prepend(commentElement);
                });

                //该方法会为备选属性添加一个或多个class属性，而不会移除其他。
                //为那个要展开的div添加in的class属性，使其展开，也就是二级评论展开
                comments.addClass("in");
                //标记二级评论状态
                e.setAttribute("data-collapse","in");
                e.classList.add("active");
            });
        }
    }
}

//展示SelectTag
function showSelectTag() {
    $("#select-tag").show();
}

//点击标签，使之写到上面的input上去
function selectTag(e) {
    var value = e.getAttribute("data-tag")
    //获取未点击之前文本框中的内容
    var previous = $("#tag").val();
    //判断文本框中标签是否有了要点的标签，有就不用添加。
    if(previous.indexOf(value) == -1){
        if(previous){
            $("#tag").val(previous + ',' + value);
        }else{
            $("#tag").val(value);
        }
    }

}
