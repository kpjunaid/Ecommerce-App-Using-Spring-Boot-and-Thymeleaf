(function () {
    $('.upload-photo-input').on('change', function(event) {
        const photoInput = $(this);
        const photo = this.files[0];
        if (photo) {
            let reader = new FileReader();
            reader.onload = function(event) {
                switch (photoInput.attr('id')) {
                    case 'productPhoto1':
                        $('#productPhoto1Preview img').attr('src', event.target.result).removeClass('d-none');
                        break;
                    case 'productPhoto2':
                        $('#productPhoto2Preview img').attr('src', event.target.result).removeClass('d-none');
                        break;
                    case 'productPhoto3':
                        $('#productPhoto3Preview img').attr('src', event.target.result).removeClass('d-none');
                        break;
                    default:
                        break;
                }
            };
            reader.readAsDataURL(photo);
        }
    });
})();