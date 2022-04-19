function loadScript(url) {
    const footer = document.getElementById('site-footer');
    const script = document.createElement('script');
    const contextPath = footer.getAttribute('href');
    script.type = 'text/javascript';
    script.src = contextPath + 'js/' + url;
    footer.appendChild(script);
}

loadScript('bootstrap-components.js');
loadScript('photo-upload.js');
loadScript('locale-change.js');