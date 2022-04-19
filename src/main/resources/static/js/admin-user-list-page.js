const userDeleteConfirmModal = $('#userDeleteConfirmModal');
const userDeleteActionBtn = $('.user-delete-action-btn');
const userDeleteConfirmBtn = $('#userDeleteConfirmBtn');
const userDeleteResponseModal = $('#userDeleteResponseModal');
const userDeleteResponseModalBtn = $('#userDeleteResponseModalBtn');
const spinner = userDeleteConfirmModal.find('.spinner-border');

userDeleteActionBtn.on('click', function () {
    userDeleteConfirmBtn.attr('data-modal-userId', $(this).attr('data-btn-userId'));
});

userDeleteConfirmBtn.on('click', function () {
    const that = $(this);
    const userId = that.attr('data-modal-userId');
    const userRow = $(`#user_row_${userId}`);
    const ajaxUrl = serverContext + 'admin/users/delete';

    spinner.removeClass('d-none');

    $.ajax({
        type: 'POST',
        url: ajaxUrl,
        data: $.param({userId, _csrf}),
        cache: false
    }).done(function (response) {
        userDeleteResponseModal.find('.modal-title').text($('#labelSuccess').text());
        userDeleteResponseModal.find('.modal-body').text(response);
        userDeleteResponseModalBtn.trigger('click');
        userRow.remove();
    }).fail(function (response) {
        userDeleteResponseModal.find('.modal-title').text($('#labelError').text());
        userDeleteResponseModal.find('.modal-body').text(response.responseText);
        userDeleteResponseModalBtn.trigger('click');
    }).always(function () {
        spinner.addClass('d-none');
    });
});



const userFilterSubmitBtn = $('#userFilterSubmitBtn');
const userFilterInput = $('.user-filter-input');

let destinationUrl = serverContext + 'admin/users/page/1?';
let filterInputObj = {};

userFilterInput.on('change', function () {
    const that = $(this);
    filterInputObj[that.attr('data-filter-field')] = that.attr('value');
});

userFilterSubmitBtn.on('click', function () {
    let count = 0;
    for (let field in filterInputObj) {
        destinationUrl += `${field}=${filterInputObj[field]}`;
        if (++count < Object.keys(filterInputObj).length) destinationUrl += '&';
    }
    window.location.href = destinationUrl;
});



const userSearchForm = $('#userSearchForm');
const userSearchField = $('.user-search-field');
const userSearchInput = $('#userSearchInput');

let searchField = $('.user-search-field:checked').val();
let searchKeyword = '';

userSearchField.on('change', function () {
    searchField = $(this).val();
});

userSearchForm.on('submit', function (e) {
    e.preventDefault();
    e.stopPropagation();

    const that = $(this);
    searchKeyword = userSearchInput.val();

    if (1 > searchKeyword.length || searchKeyword.length > 256) {
        that.addClass('was-validated');
        that.find('.invalid-feedback').removeClass('d-none');
    } else {
        let destinationUrl = serverContext + 'admin/users/page/1?';
        destinationUrl += ('search=' + searchField + '&key=' + searchKeyword);
        location.href = destinationUrl;
    }
});