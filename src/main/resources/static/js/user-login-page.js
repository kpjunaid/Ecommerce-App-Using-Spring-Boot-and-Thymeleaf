const loginForm = $('#loginForm');
const loginSubmitBtn = $('#loginSubmitBtn');
const loginResponseModalShowBtn = $('#loginResponseModalShowBtn');

loginForm.on('submit', function (e) {
    e.preventDefault();
    e.stopPropagation();

    const that = $(this);
    const ajaxUrl = serverContext + "login";
    const loginData = that.serialize();
    const invalidFormControls = $('.form-control:invalid');

    if (invalidFormControls.length <= 0) {
        loginSubmitBtn.find('.spinner-border').removeClass('d-none');

        $.ajax({
            type: 'POST',
            url: ajaxUrl,
            data: loginData,
            cache: false
        }).done(function (response) {
            window.location.href = serverContext + 'profile';
        }).fail(function (response) {
            loginResponseModalShowBtn.trigger('click');
        }).always(function () {
            loginSubmitBtn.find('.spinner-border').addClass('d-none');
        });
    }
});



const messageType = localStorage.getItem('messageType');
const messageHeader = localStorage.getItem('messageHeader');
const messageDetail = localStorage.getItem('messageDetail');

if (messageType && messageHeader && messageDetail) {
    const messageWrapper = $('#messageWrapper');

    if (messageType === 'success') {
        messageWrapper.addClass('bg-success');
    } else if (messageType === 'warning') {
        messageWrapper.addClass('bg-warning');
    }else if (messageType === 'danger') {
        messageWrapper.addClass('bg-danger');
    }

    messageWrapper.find('.card-header').text(messageHeader);
    messageWrapper.find('.card-text').text(messageDetail);
    messageWrapper.removeClass('d-none');

    localStorage.removeItem("messageType");
    localStorage.removeItem("messageHeader");
    localStorage.removeItem("messageDetail");
}