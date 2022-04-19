const cartResponseModal = $('#cartResponseModal');
const cartResponseModalBtn = $('#cartResponseModalBtn');
const cartUpdateActionBtn = $('.cart-update-action-btn');

cartUpdateActionBtn.on('click', function () {
    const that = $(this);
    const cartItemId = that.attr('data-btn-cartItemId');
    const productId = that.attr('data-btn-productId');
    const cartRow = $(`#cart_row_${cartItemId}`);
    const quantity = cartRow.find('.cart-item-quantity').find(':selected').text();
    const ajaxUrl = serverContext + `cart/update`;

    that.find('.fa').addClass('rotate');

    $.ajax({
        type: 'POST',
        url: ajaxUrl,
        data: $.param({productId, quantity, _csrf}),
        cache: false
    }).done(function (response) {
        window.location.href = serverContext + 'cart';
    }).fail(function (response) {
        cartResponseModal.find('.modal-title').text($('#labelError').text());
        cartResponseModal.find('.modal-body').text(response.responseText);
        cartResponseModalBtn.trigger('click');
    }).always(function () {
        that.find('.fa').removeClass('rotate');
    });
});



const cartDeleteActionBtn = $('.cart-delete-action-btn');
const deleteConfirmModal = $('#deleteConfirmModal');
const deleteConfirmBtn = $('#deleteConfirmBtn');
const spinner = deleteConfirmModal.find('.spinner-border');

cartDeleteActionBtn.on('click', function () {
    const that = $(this);
    deleteConfirmBtn.attr('data-modal-cartItemId', that.attr('data-btn-cartItemId'));
    deleteConfirmBtn.attr('data-modal-productId', that.attr('data-btn-productId'));
});

deleteConfirmBtn.on('click', function () {
    const that = $(this);
    const productId = that.attr('data-modal-productId');
    const ajaxUrl = serverContext + `cart/item/delete`;

    spinner.removeClass('d-none');

    $.ajax({
        type: 'POST',
        url: ajaxUrl,
        data: $.param({productId, _csrf}),
        cache: false
    }).done(function (response) {
        window.location.href = serverContext + 'cart';
    }).fail(function (response) {
        cartResponseModal.find('.modal-title').text($('#labelError').text());
        cartResponseModal.find('.modal-body').text(response.responseText);

        cartResponseModalBtn.trigger('click');
    }).always(function () {
        spinner.addClass('d-none');
    });
});