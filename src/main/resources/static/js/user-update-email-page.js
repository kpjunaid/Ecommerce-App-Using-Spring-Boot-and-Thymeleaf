const updateEmailForm = $('#updateEmailForm');
const updateEmailSubmitBtn = $('#updateEmailSubmitBtn');
const formControls = $('.form-control');
const userUpdateEmailResponseModal = $('#userUpdateEmailResponseModal');
const userUpdateEmailResponseShowBtn = $('#userUpdateEmailResponseShowBtn');
const userId = updateEmailForm.attr("data-userId");

updateEmailForm.on('submit', function (e) {
    e.preventDefault();
    e.stopPropagation();

    const that = $(this);
    const ajaxUrl = serverContext + `profile/update/email`;
    const invalidFormControls = $('.form-control:invalid');
    const formData = that.serialize();

    if (invalidFormControls.length <= 0) {
        updateEmailSubmitBtn.find('.spinner-border').removeClass('d-none');

        $.ajax({
            type: 'POST',
            url: ajaxUrl,
            data: formData,
            cache: false
        }).done(function (response) {
            localStorage.setItem('messageType', 'success');
            localStorage.setItem('messageHeader', $('#emailChangeSuccessHeader').text());
            localStorage.setItem('messageDetail', $('#emailChangeSuccessDetail').text());
            window.location.href = serverContext + `logout`;
        }).fail(function (response) {
            that.removeClass('was-validated');
            formControls.removeClass('is-invalid');

            const fieldErrors = response.responseJSON;
            if (fieldErrors) {
                for (const field in fieldErrors) {
                    $('#' + field).addClass('is-invalid');
                }
            } else {
                userUpdateEmailResponseModal.find('.modal-title').text($('#labelError').text());
                userUpdateEmailResponseModal.find('.modal-body').text(response.responseText);
                userUpdateEmailResponseShowBtn.trigger('click');
            }
        }).always(function () {
            updateEmailSubmitBtn.find('.spinner-border').addClass('d-none');
        });
    }
});