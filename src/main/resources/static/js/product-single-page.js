const addToCartBtn = $('#addToCartBtn');
const cartResponseModal = $('#cartResponseModal');
const cartResponseModalBtn = $('#cartResponseModalBtn');
const spinner = $('#cartSpinner');
const cartPreviewItems = $('#cartPreviewItems');
const cartPreviewItemCount = $('#cartPreviewItemCount');
const cartPreviewTotalPrice = $('#cartPreviewTotalPrice');
const cartPreviewBtn = $('#cartPreviewBtn');
const cartPreviewNoItem = $('#cartPreviewNoItem');

addToCartBtn.on('click', function () {
    const that = $(this);
    const userId = that.attr('data-userId');
    const productId = that.attr('data-productId');
    const quantity = 1;
    const productName = $('#productName');
    const productPrice = $('#productPrice span:last-child');
    const ajaxUrl = serverContext + 'cart/item/add';

    spinner.removeClass('d-none');

    $.ajax({
        type: 'POST',
        url: ajaxUrl,
        data: $.param({userId, productId, quantity, _csrf}),
        cache: false
    }).done(function (response) {
        cartResponseModal.find('.modal-title').text($('#labelSuccess').text());
        cartResponseModal.find('.modal-body').text(response);
        cartResponseModalBtn.trigger('click');

        const cartPreviewItem = `
                        <li>
                            <div class="p-2" style="width: 300px;">
                                <a href="${serverContext}products/${productId}">${productName.text()}</a>
                                <br>
                                <span>${$('#labelQty').text()}: ${quantity}</span>
                                <br>
                                <span>${$('#labelPrice').text()}: ${productPrice.text()}</span>
                            </div>
                        </li>
                        <hr class="dropdown-divider">
                    `;

        cartPreviewItems.append(cartPreviewItem);
        cartPreviewItemCount.text(parseInt(cartPreviewItemCount.text()) + 1);
        cartPreviewTotalPrice.text(parseFloat(cartPreviewTotalPrice.text()) + parseFloat(productPrice.text()));
        cartPreviewBtn.removeClass('d-none');
        cartPreviewNoItem.addClass('d-none');
    }).fail(function (response) {
        cartResponseModal.find('.modal-title').text($('#labelError').text());
        cartResponseModal.find('.modal-body').text(response.responseText);
        cartResponseModalBtn.trigger('click');
    }).always(function () {
        spinner.addClass('d-none');
    });
});