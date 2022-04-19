const cartPreviewItemDropdown = $('#cartPreviewItemDropdown');
cartPreviewItemDropdown.on('click',  function (event) {
    event.stopPropagation();
});

const serverContext = $('#site-footer').attr('href');
const siteSearchInput = $('#siteSearchInput');
const siteSearchResult = $('#siteSearchResult');
const searchResultPreview = $('#searchResultPreview');
const siteSearchSpinner = $('#siteSearchSpinner');
const siteSearchAllBtn = $('#siteSearchAllBtn');
const searchUrl = serverContext + 'products/search'
const _csrf = $('input[name="_csrf"]').val();

siteSearchInput.on('keyup', function () {
    const that = $(this);
    const search = that.val();
    siteSearchAllBtn.attr('href', serverContext + `products/page/1?search=${search}`);

    if (search.length >= 3) {
        siteSearchSpinner.removeClass('d-none');

        $.ajax({
            type: 'POST',
            url: searchUrl,
            data: $.param({search, _csrf}),
            cache: false
        }).done(function (response) {
            siteSearchResult.removeClass('d-none');
            searchResultPreview.empty();
            if (response.length > 0) {
                for (const i in response) {
                    const product = response[i];
                    const photoUrl = product.photoUrl === false ? serverContext + 'images/thumbnail.jpg' : serverContext + 'uploads/product/' + product.photoUrl;
                    const elem = `
                                    <div>
                                        <img class="rounded-3 me-2" src="${photoUrl}" alt="" style="height: 30px">
                                        <a href="${product.id}">${product.name}</a>
                                        <div class="mt-2">
                                            <span>${$('#labelPrice').text()}</span>
                                            <span>${': ' + $('#labelCurrency').text()}</span>
                                            <span>${product.price}</span>
                                        </div>
                                    </div>
                                    <hr class="dropdown-divider">
                                `;
                    searchResultPreview.append(elem);
                }
                siteSearchAllBtn.removeClass('d-none');
            } else {
                searchResultPreview.empty();
                const elem = `<span>${$('#labelSearchEmpty').text()}</span>`;
                searchResultPreview.append(elem);
                siteSearchAllBtn.addClass('d-none');
            }
        }).fail(function (response) {

        }).always(function () {
            siteSearchSpinner.addClass('d-none');
        });
    }
});

$(window).on('click', function () {
    siteSearchResult.addClass('d-none');
});

$(siteSearchResult).on('click', function (event) {
    event.stopPropagation();
});