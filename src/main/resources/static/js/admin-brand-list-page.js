const brandDeleteConfirmModal = $('#brandDeleteConfirmModal');
const brandDeleteActionBtn = $('.brand-delete-action-btn');
const brandDeleteConfirmBtn = $('#brandDeleteConfirmBtn');
const brandDeleteResponseModal = $('#brandDeleteResponseModal');
const brandDeleteResponseModalBtn = $('#brandDeleteResponseModalBtn');
const spinner = brandDeleteConfirmModal.find('.spinner-border');

brandDeleteActionBtn.on('click', function () {
    brandDeleteConfirmBtn.attr('data-modal-brandId', $(this).attr('data-btn-brandId'));
});

brandDeleteConfirmBtn.on('click', function () {
    const that = $(this);
    const brandId = that.attr('data-modal-brandId');
    const brandRow = $(`#brand_row_${brandId}`);
    const ajaxUrl = serverContext + 'admin/brands/delete';

    spinner.removeClass('d-none');

    $.ajax({
        type: 'POST',
        url: ajaxUrl,
        data: $.param({brandId, _csrf}),
        cache: false
    }).done(function (response) {
        brandDeleteResponseModal.find('.modal-title').text($('#labelSuccess').text());
        brandDeleteResponseModal.find('.modal-body').text(response);
        brandDeleteResponseModalBtn.trigger('click');
        brandRow.remove();
    }).fail(function (response) {
        brandDeleteResponseModal.find('.modal-title').text($('#labelError').text());
        brandDeleteResponseModal.find('.modal-body').text(response.responseText);
        brandDeleteResponseModalBtn.trigger('click');
    }).always(function () {
        spinner.addClass('d-none');
    });
});