const signupForm = $('#signupForm');
const signupSubmitBtn = $('#signupSubmitBtn');
const formControls = $('.form-control');
const signupResponseModal = $('#signupResponseModal');
const signupResponseModalShowBtn = $('#signupResponseModalShowBtn');

signupForm.on('submit', function (e) {
    e.preventDefault();
    e.stopPropagation();

    const that = $(this);
    const ajaxUrl = serverContext + "signup";
    const formData = that.serialize();
    const invalidFormControls = $('.form-control:invalid');

    if (invalidFormControls.length <= 0) {
        signupSubmitBtn.find('.spinner-border').removeClass('d-none');

        $.ajax({
            type: 'POST',
            url: ajaxUrl,
            data: formData,
            cache: false
        }).done(function (response) {
            localStorage.setItem('messageType', 'success');
            localStorage.setItem('messageHeader', $('#signupSuccessHeader').text());
            localStorage.setItem('messageDetail', $('#signupSuccessDetail').text());
            window.location.href = serverContext + 'login';
        }).fail(function (response) {
            that.removeClass('was-validated');
            formControls.removeClass('is-invalid');

            const fieldErrors = response.responseJSON;
            if (fieldErrors) {
                for (const field in fieldErrors) {
                    $('#' + field).addClass('is-invalid');
                }
            } else {
                signupResponseModal.find('.modal-title').text($('#labelError').text());
                signupResponseModal.find('.modal-body').text(response.responseText);
                signupResponseModalShowBtn.trigger('click');
            }
        }).always(function () {
            signupSubmitBtn.find('.spinner-border').addClass('d-none');
        });
    }
});