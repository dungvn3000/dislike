/**
 * This module will run when the website is starting.
 */
$(function () {

    var $scrolltotop = $("#scrolltotop");
    $scrolltotop.css('display', 'none');

    $(window).scroll(function () {
        if ($(this).scrollTop() > 100) {
            $scrolltotop.slideDown('fast');
        } else {
            $scrolltotop.slideUp('fast');
        }
    });

    $scrolltotop.click(function () {
        $('body,html').animate({
            scrollTop: 0
        }, 'fast');
        return false;
    });


});