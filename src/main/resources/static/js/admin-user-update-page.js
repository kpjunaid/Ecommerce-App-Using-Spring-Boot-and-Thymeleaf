const adminUserUpdateInfoForm = $('#adminUserUpdateInfoForm');
const adminUserUpdateInfoSubmitBtn = $('#adminUserUpdateInfoSubmitBtn');
const formControls = $('.form-control');
const adminUserUpdateInfoResponseModal = $('#adminUserUpdateInfoResponseModal');
const adminUserUpdateInfoResponseModalShowBtn = $('#adminUserUpdateInfoResponseModalShowBtn');
const userId = adminUserUpdateInfoForm.attr("data-userId");

adminUserUpdateInfoForm.on('submit', function (e) {
    e.preventDefault();
    e.stopPropagation();

    const that = $(this);
    const ajaxUrl = serverContext + 'admin/users/update';
    const invalidFormControls = $('.form-control:invalid');
    const photo = $('#productPhoto1')[0].files[0];
    let formData = new FormData(that[0]);
    formData.append('userPhoto', photo);
    formData.append('userId', userId);

    if (invalidFormControls.length <= 0) {
        adminUserUpdateInfoSubmitBtn.find('.spinner-border').removeClass('d-none');

        $.ajax({
            type: 'POST',
            enctype: 'multipart/form-data',
            processData: false,
            contentType: false,
            url: ajaxUrl,
            data: formData,
            cache: false
        }).done(function (response) {
            adminUserUpdateInfoResponseModal.find('.modal-title').text($('#labelSuccess').text());
            adminUserUpdateInfoResponseModal.find('.modal-body').text(response);
            adminUserUpdateInfoResponseModalShowBtn.trigger('click');
        }).fail(function (response) {
            that.removeClass('was-validated');
            formControls.removeClass('is-invalid');

            const fieldErrors = response.responseJSON;
            if (fieldErrors) {
                for (const field in fieldErrors) {
                    $('#' + field).addClass('is-invalid');
                }
            } else {
                adminUserUpdateInfoResponseModal.find('.modal-title').text($('#labelError').text());
                adminUserUpdateInfoResponseModal.find('.modal-body').text(response.responseText);
                adminUserUpdateInfoResponseModalShowBtn.trigger('click');
            }
        }).always(function () {
            adminUserUpdateInfoSubmitBtn.find('.spinner-border').addClass('d-none');
        });
    }
});