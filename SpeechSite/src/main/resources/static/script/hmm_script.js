function synthesizeRequest() {

    $('.is-invalid').removeClass('is-invalid');

    let system = 'HMM';
    var hmmParameters = {};
    if (typeof $("#select-0ef8").find(':selected').val() == "undefined") {
        showModal('Не выбран язык', 'Ошибка')
        return;
    }

    hmmParameters.model = $("#select-0ef8").find(':selected').val();

    var text = $("#textarea-4b2c").val();
    if (text === "") {
        showModal('Не введен текст для синтеза', 'Ошибка')
        return;
    }
    if (text.length > 100) {
        showModal('Слишком много символов', 'Ошибка');
        return;
    }

    $("#sec-result").attr("style", "display: none");
    $("#sec-result-wait").removeAttr("style");
    $('html, body').animate({
        scrollTop: $("#sec-result-wait").offset().top
    }, 1000);


    $.ajax({
        url: location.pathname + '/synthesize',
        method: 'post',
        headers: {
            "Content-Type": "application/json; odata=verbose"
        },
        data: JSON.stringify({
            "text": text,
            "system": system,
            "hmmParameters": hmmParameters
        }),
        processData: false,
        success: function (data) {
            $("#sec-result").removeAttr("style");
            $("#sec-result-wait").attr("style", "display: none");
            $('html, body').animate({
                scrollTop: $("#sec-result").offset().top
            }, 1000);
            $("#hmm-result").html(data);
        },
        error: function (jqXHR, exception) {
            showModal('Произошла ошибка во время обработки запроса. Проверьте правильность введенных данных и повторите попытку.', 'Ошибка')
        }
    });
}

function showModal(text, header) {
    alert(text);
}