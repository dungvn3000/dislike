function post_comment(e, url, input, dislikeId, avatarUrl, userName, userUrl) {
    if (typeof e == 'undefined' && window.event) {
        e = window.event;
    }
    if (e.keyCode == 13) {
        $.ajax({
            url: url,
            type: "POST",
            data: {
                content: $(input).val(),
                dislikeId: dislikeId
            },
            success: function(commentId) {
                //Add comment on client here
                $('#home-item-comment-list-' + dislikeId).append('<li id="home-item-comment-li-' + commentId + '">' +
                    '<div class="well-small row-fluid" onmouseover="show_delete(this)" onmouseout="hide_delete(this)">' +
                        '<div class="span1">' +
                            '<img src="' + avatarUrl + '" alt="" width="30">' +
                        '</div>' +
                        '<div class="span10">' +
                            '<a href="' + userUrl + '">' + userName + ' </a>' + $(input).val() +
                            '<div>' +
                                '<a>Like</a>' +
                            '</div>' +
                        '</div>' +
                        '<div class="home-item-comment-remover">' +
                            '<i class="icon-remove" onclick="delete_comment(\'' + commentId + '\',\'/comment/delete/?id=' + commentId + '\')"></i>' +
                        '</div>' +
                    '</li>');
                $(input).val('');
            }
        })
    }
}

function show_delete(div) {
    $(div).find('.home-item-comment-remover').show();
}

function hide_delete(div) {
    $(div).find('.home-item-comment-remover').hide();
}

function delete_comment(id, url) {
    $.ajax({
        url: url,
        type: "POST",
        success: function() {
            $('#home-item-comment-li-' + id).remove();
        }
    })
}

function show_comment(id) {
    $('#' + id).show();
}