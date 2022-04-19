const userUpdateInfoForm = $('#userUpdateInfoForm');
const userUpdateInfoSubmitBtn = $('#userUpdateInfoSubmitBtn');
const formControls = $('.form-control');
const userUpdateInfoResponseModal = $('#userUpdateInfoResponseModal');
const userUpdateInfoResponseModalShowBtn = $('#userUpdateInfoResponseModalShowBtn');

userUpdateInfoForm.on('submit', function (e) {
    e.preventDefault();
    e.stopPropagation();

    const that = $(this);
    const ajaxUrl = serverContext + `profile/update/info`;
    const invalidFormControls = $('.form-control:invalid');
    const photo = $('#productPhoto1')[0].files[0];
    let formData = new FormData(that[0]);
    formData.append('userPhoto', photo);

    if (invalidFormControls.length <= 0) {
        userUpdateInfoSubmitBtn.find('.spinner-border').removeClass('d-none');

        $.ajax({
            type: 'POST',
            enctype: 'multipart/form-data',
            processData: false,
            contentType: false,
            url: ajaxUrl,
            data: formData,
            cache: false
        }).done(function (response) {
            window.location.href = serverContext + `profile`;
        }).fail(function (response) {
            that.removeClass('was-validated');
            formControls.removeClass('is-invalid');

            const fieldErrors = response.responseJSON;
            if (fieldErrors) {
                for (const field in fieldErrors) {
                    $('#' + field).addClass('is-invalid');
                }
            } else {
                userUpdateInfoResponseModal.find('.modal-title').text($('#labelError').text());
                userUpdateInfoResponseModal.find('.modal-body').text(response.responseText);
                userUpdateInfoResponseModalShowBtn.trigger('click');
            }
        }).always(function () {
            userUpdateInfoSubmitBtn.find('.spinner-border').addClass('d-none');
        });
    }
});