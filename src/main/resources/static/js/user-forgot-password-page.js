const forgotPasswordForm = $('#forgotPasswordForm');
const forgotPasswordSubmitBtn = $('#forgotPasswordSubmitBtn');
const formControls = $('.form-control');
const forgotPasswordResponseModal = $('#forgotPasswordResponseModal');
const forgotPasswordResponseModalShowBtn = $('#forgotPasswordResponseModalShowBtn');

forgotPasswordForm.on('submit', function (e) {
    e.preventDefault();
    e.stopPropagation();

    const that = $(this);
    const ajaxUrl = serverContext + "forgot-password";
    const formData = that.serialize();
    const invalidFormControls = $('.form-control:invalid');

    if (invalidFormControls.length <= 0) {
        forgotPasswordSubmitBtn.find('.spinner-border').removeClass('d-none');

        $.ajax({
            type: 'POST',
            url: ajaxUrl,
            data: formData,
            cache: false
        }).done(function (response) {
            forgotPasswordResponseModal.find('.modal-title').text($('#labelSuccess').text());
            forgotPasswordResponseModal.find('.modal-body').text(response);
            forgotPasswordResponseModalShowBtn.trigger('click');
        }).fail(function (response) {
            that.removeClass('was-validated');
            formControls.removeClass('is-invalid');

            const fieldErrors = response.responseJSON;
            if (fieldErrors) {
                for (const field in fieldErrors) {
                    $('#' + field).addClass('is-invalid');
                }
            } else {
                forgotPasswordResponseModal.find('.modal-title').text($('#labelError').text());
                forgotPasswordResponseModal.find('.modal-body').text(response.responseText);
                forgotPasswordResponseModalShowBtn.trigger('click');
            }
        }).always(function () {
            forgotPasswordSubmitBtn.find('.spinner-border').addClass('d-none');
        });
    }
});