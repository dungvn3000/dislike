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
            success: function(data) {
                //Add comment on client here
                $('#home-item-comment-list-' + dislikeId).append('<li>' +
                    '<div class="well-small row-fluid">' +
                        '<div class="span1">' +
                            '<img src="' + avatarUrl + '" alt="" width="30">' +
                        '</div>' +
                        '<div class="span10">' +
                            '<a href="' + userUrl + '">' + userName + ' </a>' + $(input).val() +
                            '<div>' +
                                '<a>Like</a>' +
                            '</div>' +
                        '</div>' +
                    '</li>');
                $(input).val('');
            }
        })
    }
}