const resetPasswordForm = $('#resetPasswordForm');
const resetPasswordSubmitBtn = $('#resetPasswordSubmitBtn');
const formControls = $('.form-control');
const resetPasswordResponseModal = $('#resetPasswordResponseModal');
const resetPasswordResponseModalShowBtn = $('#resetPasswordResponseModalShowBtn');

resetPasswordForm.on('submit', function (e) {
    e.preventDefault();
    e.stopPropagation();

    const that = $(this);
    const token = that.attr('data-token');
    const ajaxUrl = serverContext + "reset-password";
    const formData = that.serialize() + `&token=${token}`;
    const invalidFormControls = $('.form-control:invalid');

    if (invalidFormControls.length <= 0) {
        resetPasswordSubmitBtn.find('.spinner-border').removeClass('d-none');

        $.ajax({
            type: 'POST',
            url: ajaxUrl,
            data: formData,
            cache: false
        }).done(function (response) {
            resetPasswordResponseModal.find('.modal-title').text($('#labelSuccess').text());
            resetPasswordResponseModal.find('.modal-body p').text(response);
            resetPasswordResponseModalShowBtn.trigger('click');
        }).fail(function (response) {
            that.removeClass('was-validated');
            formControls.removeClass('is-invalid');

            const fieldErrors = response.responseJSON;
            if (fieldErrors) {
                for (const field in fieldErrors) {
                    $('#' + field).addClass('is-invalid');
                }
            } else {
                resetPasswordResponseModal.find('.modal-title').text($('#labelError').text());
                resetPasswordResponseModal.find('.modal-body p').text(response.responseText);
                resetPasswordResponseModalShowBtn.trigger('click');
            }
        }).always(function () {
            resetPasswordSubmitBtn.find('.spinner-border').addClass('d-none');
        });
    }
});