const categoryDeleteConfirmModal = $('#categoryDeleteConfirmModal');
const categoryDeleteActionBtn = $('.category-delete-action-btn');
const categoryDeleteConfirmBtn = $('#categoryDeleteConfirmBtn');
const categoryDeleteResponseModal = $('#categoryDeleteResponseModal');
const categoryDeleteResponseModalBtn = $('#categoryDeleteResponseModalBtn');
const spinner = categoryDeleteConfirmModal.find('.spinner-border');

categoryDeleteActionBtn.on('click', function () {
    categoryDeleteConfirmBtn.attr('data-modal-categoryId', $(this).attr('data-btn-categoryId'));
});

categoryDeleteConfirmBtn.on('click', function () {
    const that = $(this);
    const categoryId = that.attr('data-modal-categoryId');
    const categoryRow = $(`#category_row_${categoryId}`);
    const ajaxUrl = serverContext + `admin/categories/delete`;

    spinner.removeClass('d-none');

    $.ajax({
        type: 'POST',
        url: ajaxUrl,
        data: $.param({categoryId, _csrf}),
        cache: false
    }).done(function (response) {
        categoryDeleteResponseModal.find('.modal-title').text($('#labelSuccess').text());
        categoryDeleteResponseModal.find('.modal-body').text(response);
        categoryDeleteResponseModalBtn.trigger('click');
        categoryRow.remove();
    }).fail(function (response) {
        categoryDeleteResponseModal.find('.modal-title').text($('#labelError').text());
        categoryDeleteResponseModal.find('.modal-body').text(response.responseText);
        categoryDeleteResponseModalBtn.trigger('click');
    }).always(function () {
        spinner.addClass('d-none');
    });
});