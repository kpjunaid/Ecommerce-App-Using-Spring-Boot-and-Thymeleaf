const userEmailVerifyForm = $('#userEmailVerifyForm');
const userEmailVerifyFormSubmitBtn = $('#userEmailVerifyForm button');
const verifyResponseModal = $('#verifyResponseModal');
const verifyResponseModalShowBtn = $('#verifyResponseModalShowBtn');

userEmailVerifyForm.on('submit', function (e) {
    e.preventDefault();
    e.stopPropagation();

    const that = $(this);
    const token = that.attr('data-token');
    const ajaxUrl = serverContext + "email-token";
    const formData = that.serialize();

    that.find('.spinner-border').removeClass('d-none');

    $.ajax({
        type: 'POST',
        url: ajaxUrl,
        data: formData,
        cache: false
    }).done(function (response) {
        verifyResponseModal.find('.modal-title').text($('#labelSuccess').text());
        verifyResponseModal.find('.modal-body p').text(response);
        verifyResponseModalShowBtn.trigger('click');
    }).fail(function (response) {
        verifyResponseModal.find('.modal-title').text($('#labelError').text());
        verifyResponseModal.find('.modal-body p').text(response.responseText);
        verifyResponseModalShowBtn.trigger('click');
    }).always(function () {
        that.find('.spinner-border').addClass('d-none');
    });
});