function synthesizeRequest() {

    $('.is-invalid').removeClass('is-invalid');


    let system = "MaryTTS";
    var maryTTSParameters = {};
    if (typeof $("#select-0ef8").find(':selected').val() == "undefined") {
        showModal('Не выбран язык', 'Ошибка')
        return;
    }

    if (typeof $("#select-voice").find(':selected').val() == "undefined") {
        showModal('Не выбран голос', 'Ошибка')
        return;
    }
    maryTTSParameters.locales = [$("#select-0ef8").find(':selected').val()]
    maryTTSParameters.voices = [$("#select-voice").find(':selected').val()]

    maryTTSParameters.useEffects = true;
    var volume = {};
    volume.name = "Volume";
    volume.effect = {};
    volume.effect.amount = $("#maryeffectVolumeamount").val();
    volume.isActive = $("#maryeffectVolumeamountcheck").prop('checked');


    var F0Scale = {};
    F0Scale.name = "F0Scale";
    F0Scale.effect = {};
    F0Scale.effect.f0Scale = $("#maryeffectF0Scalef0Scale").val();
    F0Scale.isActive = $("#maryeffectF0Scalef0Scalecheck").prop('checked');

    var F0Add = {};
    F0Add.name = "F0Add";
    F0Add.effect = {};
    F0Add.effect.f0Add = $("#maryeffectF0Addf0Add").val();
    F0Add.isActive = $("#maryeffectF0Addf0Addcheck").prop('checked');

    var Rate = {};
    Rate.name = "Rate";
    Rate.effect = {};
    Rate.effect.durScale = $("#maryeffectRatedurScale").val();
    Rate.isActive = $("#maryeffectRatedurScalecheck").prop('checked');

    var Robot = {};
    Robot.name = "Robot";
    Robot.effect = {};
    Robot.effect.amount = $("#maryeffectRobotamount").val();
    Robot.isActive = $("#maryeffectRobotamountcheck").prop('checked');

    var Whisper = {};
    Whisper.name = "Whisper";
    Whisper.effect = {};
    Whisper.effect.amount = $("#maryeffectWhisperamount").val();
    Whisper.isActive = $("#maryeffectWhisperamountcheck").prop('checked');

    var Stadium = {};
    Stadium.name = "Stadium";
    Stadium.effect = {};
    Stadium.effect.amount = $("#maryeffectStadiumamount").val();
    Stadium.isActive = $("#maryeffectStadiumamountcheck").prop('checked');

    var Chorus = {};
    Chorus.name = "Chorus";
    Chorus.effect = {};
    Chorus.effect.amp1 = $("#maryeffectChorusamp1").val();
    Chorus.effect.amp3 = $("#maryeffectChorusamp3").val();
    Chorus.effect.amp2 = $("#maryeffectChorusamp2").val();
    Chorus.effect.delay3 = $("#maryeffectChorusdelay3").val();
    Chorus.effect.delay1 = $("#maryeffectChorusdelay1").val();
    Chorus.effect.delay2 = $("#maryeffectChorusdelay2").val();
    Chorus.isActive = $("#checkbox-a569").prop('checked');


    maryTTSParameters.audioEffects = [
        volume, F0Scale, F0Add, Rate, Robot, Whisper, Stadium, Chorus
    ]

    var text = $("#textInput").val();
    if (text === "") {
        showModal('Не введен текст для синтеза', 'Ошибка')
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
            "maryTTSParameters": maryTTSParameters
        }),
        processData: false,
        success: function (data) {
            $("#sec-result").removeAttr("style");
            $("#sec-result-wait").attr("style", "display: none");
            $('html, body').animate({
                scrollTop: $("#sec-result").offset().top
            }, 1000);
            let src = location.pathname.replace("synthesis", "") + "synthesized/" + data;
            $("#audioOutput").attr("src", src);
        },
        error: function (jqXHR, exception) {
            showModal('Произошла ошибка во время обработки запроса. Проверьте правильность введенных данных и повторите попытку.', 'Ошибка')
        }
    });
}

function showModal(text, header) {
    alert(text);
}