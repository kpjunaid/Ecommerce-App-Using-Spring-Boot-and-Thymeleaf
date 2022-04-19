const brandForm = $('#brandForm');
const brandFormSubmitBtn = $('#brandFormSubmitBtn');
const formControls = $('.form-control');
const brandFormResponseModal = $('#brandFormResponseModal');
const brandFormResponseModalShowBtn = $('#brandFormResponseModalShowBtn');
const brandUpdate = brandForm.attr("data-brandUpdate");
const brandId = brandForm.attr("data-brandId");

brandForm.on('submit', function (e) {
    e.preventDefault();
    e.stopPropagation();

    const that = $(this);
    let ajaxUrl = '';
    if (brandUpdate === "false") {
        ajaxUrl = serverContext + `admin/brands/create`;
    } else {
        ajaxUrl = serverContext + `admin/brands/update`;
    }
    const invalidFormControls = $('.form-control:invalid');
    const photo = $('#productPhoto1')[0].files[0];
    let formData = new FormData(that[0]);
    formData.append('brandPhoto', photo);
    formData.append('brandId', brandId);

    if (invalidFormControls.length <= 0) {
        brandFormSubmitBtn.find('.spinner-border').removeClass('d-none');

        $.ajax({
            type: 'POST',
            enctype: 'multipart/form-data',
            processData: false,
            contentType: false,
            url: ajaxUrl,
            data: formData,
            cache: false
        }).done(function (response) {
            brandFormResponseModal.find('.modal-title').text($('#labelSuccess').text());
            brandFormResponseModal.find('.modal-body').text(response);
            brandFormResponseModalShowBtn.trigger('click');
        }).fail(function (response) {
            that.removeClass('was-validated');
            formControls.removeClass('is-invalid');

            const fieldErrors = response.responseJSON;
            if (fieldErrors) {
                for (const field in fieldErrors) {
                    $('#' + field).addClass('is-invalid');
                }
            } else {
                brandFormResponseModal.find('.modal-title').text($('#labelError').text());
                brandFormResponseModal.find('.modal-body').text(response.responseText);
                brandFormResponseModalShowBtn.trigger('click');
            }
        }).always(function () {
            brandFormSubmitBtn.find('.spinner-border').addClass('d-none');
        });
    }
})