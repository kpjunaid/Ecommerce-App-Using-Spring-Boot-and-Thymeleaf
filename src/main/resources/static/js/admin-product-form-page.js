const productForm = $('#productForm');
const productFormSubmitBtn = $('#productFormSubmitBtn');
const formControls = $('.form-control');
const productFormResponseModal = $('#productFormResponseModal');
const productFormResponseModalShowBtn = $('#productFormResponseModalShowBtn');
const productUpdate = productForm.attr("data-productUpdate");
const productId = productForm.attr("data-productId");

productForm.on('submit', function (e) {
    e.preventDefault();
    e.stopPropagation();

    const that = $(this);
    let ajaxUrl = '';
    if (productUpdate === "false") {
        ajaxUrl = serverContext + `admin/products/create`;
    } else {
        ajaxUrl = serverContext + `admin/products/update`;
    }

    let formData = new FormData(that[0]);
    const invalidFormControls = $('.form-control:invalid');
    const productPhoto1 = $('#productPhoto1')[0].files[0];
    const productPhoto2 = $('#productPhoto2')[0].files[0];
    const productPhoto3 = $('#productPhoto3')[0].files[0];
    formData.append('productPhoto1', productPhoto1);
    formData.append('productPhoto2', productPhoto2);
    formData.append('productPhoto3', productPhoto3);
    formData.append('productId', productId);

    if (invalidFormControls.length <= 0) {
        productFormSubmitBtn.find('.spinner-border').removeClass('d-none');

        $.ajax({
            type: 'POST',
            enctype: 'multipart/form-data',
            processData: false,
            contentType: false,
            url: ajaxUrl,
            data: formData,
            cache: false
        }).done(function (response) {
            productFormResponseModal.find('.modal-title').text($('#labelSuccess').text());
            productFormResponseModal.find('.modal-body').text(response);
            productFormResponseModalShowBtn.trigger('click');
        }).fail(function (response) {
            that.removeClass('was-validated');
            formControls.removeClass('is-invalid');

            const fieldErrors = response.responseJSON;
            if (fieldErrors) {
                for (const field in fieldErrors) {
                    $('#' + field).addClass('is-invalid');
                }
            } else {
                productFormResponseModal.find('.modal-title').text($('#labelError').text());
                productFormResponseModal.find('.modal-body').text(response.responseText);
                productFormResponseModalShowBtn.trigger('click');
            }
        }).always(function () {
            productFormSubmitBtn.find('.spinner-border').addClass('d-none');
        });
    }
});


const productPhotoDeleteBtn = $('.product-photo-delete');
const deleteConfirmModal = $('#deleteConfirmModal');
const deleteConfirmBtn = $('#deleteConfirmBtn');
const deleteConfirmModalBtn = $('#deleteConfirmModalBtn');
const deleteResponseModal = $('#deleteResponseModal');
const deleteResponseModalBtn = $('#deleteResponseModalBtn');
const spinner = deleteConfirmModal.find('.spinner-border');

productPhotoDeleteBtn.on('click', function(e) {
    e.preventDefault();
    e.stopPropagation();
    deleteConfirmBtn.attr('data-productPhotoId', $(this).attr('data-productPhotoId'));
    deleteConfirmModalBtn.trigger('click');
});

deleteConfirmBtn.on('click', function(e) {
    e.preventDefault();
    e.stopPropagation();

    const that = $(this);
    const productPhotoId = that.attr('data-productPhotoId');
    const ajaxUrl = serverContext + 'admin/products/photos/delete';

    $.ajax({
        type: 'POST',
        url: ajaxUrl,
        data: $.param({productPhotoId, _csrf}),
        cache: false
    }).done(function (response) {
        deleteResponseModal.find('.modal-title').text($('#labelSuccess').text());
        deleteResponseModal.find('.modal-body').text(response);
        deleteResponseModalBtn.trigger('click');
    }).fail(function (response) {
        deleteResponseModal.find('.modal-title').text($('#labelError').text());
        deleteResponseModal.find('.modal-body').text(response.responseText);
        deleteResponseModalBtn.trigger('click');
    }).always(function () {
        spinner.addClass('d-none');
    });
});