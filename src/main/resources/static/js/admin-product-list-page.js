const productDeleteConfirmModal = $('#productDeleteConfirmModal');
const productDeleteActionBtn = $('.product-delete-action-btn');
const productDeleteConfirmBtn = $('#productDeleteConfirmBtn');
const productDeleteResponseModal = $('#productDeleteResponseModal');
const productDeleteResponseModalBtn = $('#productDeleteResponseModalBtn');
const spinner = productDeleteConfirmModal.find('.spinner-border');

productDeleteActionBtn.on('click', function () {
    productDeleteConfirmBtn.attr('data-modal-productId', $(this).attr('data-btn-productId'));
});

productDeleteConfirmBtn.on('click', function () {
    const that = $(this);
    const productId = that.attr('data-modal-productId');
    const productRow = $(`#product_row_${productId}`);
    const ajaxUrl = serverContext + 'admin/products/delete';

    spinner.removeClass('d-none');

    $.ajax({
        type: 'POST',
        url: ajaxUrl,
        data: $.param({productId, _csrf}),
        cache: false
    }).done(function (response) {
        productDeleteResponseModal.find('.modal-title').text($('#labelSuccess').text());
        productDeleteResponseModal.find('.modal-body').text(response);
        productDeleteResponseModalBtn.trigger('click');
        productRow.remove();
    }).fail(function (response) {
        productDeleteResponseModal.find('.modal-title').text($('#labelError').text());
        productDeleteResponseModal.find('.modal-body').text(response.responseText);
        productDeleteResponseModalBtn.trigger('click');
    }).always(function () {
        spinner.addClass('d-none');
    });
});