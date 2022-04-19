const updatePasswordForm = $('#updatePasswordForm');
const updatePasswordSubmitBtn = $('#updatePasswordSubmitBtn');
const formControls = $('.form-control');
const userUpdatePasswordResponseModal = $('#userUpdatePasswordResponseModal');
const userUpdatePasswordResponseModalShowBtn = $('#userUpdatePasswordResponseModalShowBtn');

updatePasswordForm.on('submit', function (e) {
    e.preventDefault();
    e.stopPropagation();

    const that = $(this);
    const ajaxUrl = serverContext + `profile/update/password`;
    const invalidFormControls = $('.form-control:invalid');
    const formData = that.serialize();

    if (invalidFormControls.length <= 0) {
        updatePasswordSubmitBtn.find('.spinner-border').removeClass('d-none');

        $.ajax({
            type: 'POST',
            url: ajaxUrl,
            data: formData,
            cache: false
        }).done(function (response) {
            localStorage.setItem('messageType', 'success');
            localStorage.setItem('messageHeader', $('#passwordChangeSuccessHeader').text());
            localStorage.setItem('messageDetail', $('#passwordChangeSuccessDetail').text());
            window.location.href = serverContext + 'logout?logout=true';
        }).fail(function (response) {
            that.removeClass('was-validated');
            formControls.removeClass('is-invalid');

            const fieldErrors = response.responseJSON;
            if (fieldErrors) {
                for (const field in fieldErrors) {
                    $('#' + field).addClass('is-invalid');
                }
            } else {
                userUpdatePasswordResponseModal.find('.modal-title').text($('#labelError').text());
                userUpdatePasswordResponseModal.find('.modal-body').text(response.responseText);
                userUpdatePasswordResponseModalShowBtn.trigger('click');
            }
        }).always(function () {
            updatePasswordSubmitBtn.find('.spinner-border').addClass('d-none');
        });
    }
});