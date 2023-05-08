function synthesizeRequest() {
    var system = $('#systemSelector li.active').attr('id');

    $('.is-invalid').removeClass('is-invalid');

    //
    // if (typeof system == "undefined") {
    //     $('#systemSelector').addClass('is-invalid');
    //     return;
    // }

    system = "MaryTTS";
    var maryTTSParameters = {};
    // if (typeof $("#mary-tts-locale-select").find(':selected').val() == "undefined") {
    //     $("#mary-tts-locale-select").addClass('is-invalid');
    //     return;
    // }
    //
    // if (typeof $("#mary-tts-voice-select").find(':selected').val() == "undefined") {
    //     $("#mary-tts-voice-select").addClass('is-invalid');
    //     return;
    // }
    maryTTSParameters.locales = [$("#select-0ef8").find(':selected').val()]
    maryTTSParameters.voices = [$("#select-voice").find(':selected').val()]

    maryTTSParameters.useEffects = true;
    var volume = {};
    volume.name = "Volume";
    volume.effect = {};
    volume.effect.amount = $("#maryeffectVolumeamount").val();
    volume.isActive = $("#maryeffectVolumeamountcheck").is(":checked");


    var F0Scale = {};
    F0Scale.name = "F0Scale";
    F0Scale.effect = {};
    F0Scale.effect.f0Scale = $("#maryeffectF0Scalef0Scale").val();
    F0Scale.isActive = $("#maryeffectF0Scalef0Scalecheck").is(":checked");

    var F0Add = {};
    F0Add.name = "F0Add";
    F0Add.effect = {};
    F0Add.effect.f0Add = $("#maryeffectF0Addf0Add").val();
    F0Add.isActive = $("#maryeffectF0Addf0Addcheck").is(":checked");

    var Rate = {};
    Rate.name = "Rate";
    Rate.effect = {};
    Rate.effect.durScale = $("#maryeffectRatedurScale").val();
    Rate.isActive = $("#maryeffectRatedurScalecheck").is(":checked");

    var Robot = {};
    Robot.name = "Robot";
    Robot.effect = {};
    Robot.effect.amount = $("#maryeffectRobotamount").val();
    Robot.isActive = $("#maryeffectRobotamountcheck").is(":checked");

    var Whisper = {};
    Whisper.name = "Whisper";
    Whisper.effect = {};
    Whisper.effect.amount = $("#maryeffectWhisperamount").val();
    Whisper.isActive = $("#maryeffectWhisperamountcheck").is(":checked");

    var Stadium = {};
    Stadium.name = "Stadium";
    Stadium.effect = {};
    Stadium.effect.amount = $("#maryeffectStadiumamount").val();
    Stadium.isActive = $("#maryeffectStadiumamountcheck").is(":checked");

    var Chorus = {};
    Chorus.name = "Chorus";
    Chorus.effect = {};
    Chorus.effect.amp1 = $("#maryeffectChorusamp1").val();
    Chorus.effect.amp3 = $("#maryeffectChorusamp3").val();
    Chorus.effect.amp2 = $("#maryeffectChorusamp2").val();
    Chorus.effect.delay3 = $("#maryeffectChorusdelay3").val();
    Chorus.effect.delay1 = $("#maryeffectChorusdelay1").val();
    Chorus.effect.delay2 = $("#maryeffectChorusdelay2").val();
    Chorus.isActive = $("#checkbox-a569").is(":checked");


    maryTTSParameters.audioEffects = [
        volume, F0Scale, F0Add, Rate, Robot, Whisper, Stadium, Chorus
    ]

    var text = $("#textInput").val();
    // if (text == "") {
    //     $("#textInput").addClass('is-invalid');
    //     return;
    // }

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
            var src = location.pathname.replace("synthesis", "") + "synthesized/" + data;
            $("#audioOutput").attr("src", src);
        }
    });
}
