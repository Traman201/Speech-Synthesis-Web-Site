$('#mary-tts-locale-select').on('change', function (e) {
    var locale = $('#mary-tts-locale-select').find(':selected').value();
    $.ajax({
            url: location.pathname.replace("synthesis","") + "mary-tts/parameters/voices",
            method: 'get',
            data: locale,
            success: function(data){
                $('#voice-select').html(data);
            }
        });
});