(function () {
    const localeDropdown = $('#localeDropdown');
    const siteLangBtn = $('#siteLangBtn');

    localeDropdown.find('.dropdown-item').on('click', function (e) {
        e.preventDefault();
        e.stopPropagation();

        const that = $(this);
        const siteLang = that.attr('data-site-lang');
        const pageUrl = window.location.href;
        const urlLangParamIndex = pageUrl.indexOf('?lang=');

        if (urlLangParamIndex > -1) {
            const strToReplace = pageUrl.substr(urlLangParamIndex);
            const newPageUrl = pageUrl.replace(strToReplace, '');
            window.location.href = newPageUrl + `?lang=${siteLang}`;
        } else {
            window.location.href = pageUrl + `?lang=${siteLang}`;
        }
    })
})();