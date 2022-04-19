const categoryForm = $('#categoryForm');
const categoryFormSubmitBtn = $('#categoryFormSubmitBtn');
const formControls = $('.form-control');
const categoryFormResponseModal = $('#categoryFormResponseModal');
const categoryFormResponseModalShowBtn = $('#categoryFormResponseModalShowBtn');
const categoryUpdate = categoryForm.attr("data-categoryUpdate");
const categoryId = categoryForm.attr("data-categoryId");

categoryForm.on('submit', function (e) {
    e.preventDefault();
    e.stopPropagation();

    const that = $(this);
    let ajaxUrl = '';
    if (categoryUpdate === "false") {
        ajaxUrl = serverContext + `admin/categories/create`;
    } else {
        ajaxUrl = serverContext + `admin/categories/update`;
    }
    const invalidFormControls = $('.form-control:invalid');
    const photo = $('#productPhoto1')[0].files[0];
    let formData = new FormData(that[0]);
    formData.append('categoryPhoto', photo);
    formData.append("categoryId", categoryId);

    if (invalidFormControls.length <= 0) {
        categoryFormSubmitBtn.find('.spinner-border').removeClass('d-none');

        $.ajax({
            type: 'POST',
            enctype: 'multipart/form-data',
            processData: false,
            contentType: false,
            url: ajaxUrl,
            data: formData,
            cache: false
        }).done(function (response) {
            categoryFormResponseModal.find('.modal-title').text($('#labelSuccess').text());
            categoryFormResponseModal.find('.modal-body').text(response);
            categoryFormResponseModalShowBtn.trigger('click');
        }).fail(function (response) {
            that.removeClass('was-validated');
            formControls.removeClass('is-invalid');

            const fieldErrors = response.responseJSON;
            if (fieldErrors) {
                for (const field in fieldErrors) {
                    $('#' + field).addClass('is-invalid');
                }
            } else {
                categoryFormResponseModal.find('.modal-title').text($('#labelError').text());
                categoryFormResponseModal.find('.modal-body').text(response.responseText);
                categoryFormResponseModalShowBtn.trigger('click');
            }
        }).always(function () {
            categoryFormSubmitBtn.find('.spinner-border').addClass('d-none');
        });
    }
});