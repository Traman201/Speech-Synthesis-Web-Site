function synthesizeRequest (){
    var system = $('#systemSelector li.active').attr('id');

    $('.is-invalid').removeClass('is-invalid');

    if(typeof system == "undefined"){
        $('#systemSelector').addClass('is-invalid');
        return;
    }
    
    switch(system){
        case 'mary-tts-select' :
            system = "MaryTTS";
            var maryTTSParameters = {};
            if(typeof $("#mary-tts-locale-select").find(':selected').val() == "undefined"){
                $("#mary-tts-locale-select").addClass('is-invalid');
                return;
            }

            if(typeof $("#mary-tts-voice-select").find(':selected').val() == "undefined"){
                $("#mary-tts-voice-select").addClass('is-invalid');
                return;
            }
            maryTTSParameters.locales = [$("#mary-tts-locale-select").find(':selected').val()]
            maryTTSParameters.voices = [$("#mary-tts-voice-select").find(':selected').val()]
            maryTTSParameters.style = [$('input[name=flexRadioDefaultMaryTTS]:checked').val()]
            if($('#flexSwitchCheckDefaultMaryTTS').is(":checked")){
            maryTTSParameters.useEffects = true;
            var volume = {};
            volume.name = "Volume";
            volume.effect = {};
            volume.effect.amount = $("#maryeffectVolumeamount").val();

            var TractScaler = {};
            TractScaler.name = "TractScaler";
            TractScaler.effect = {};
            TractScaler.effect.amount = $("#maryeffectTractScaleramount").val();

            var F0Scale = {};
            F0Scale.name = "F0Scale";
            F0Scale.effect = {};
            F0Scale.effect.f0Scale = $("#maryeffectF0Scalef0Scale").val();


            var F0Add = {};
            F0Add.name = "F0Add";
            F0Add.effect = {};
            F0Add.effect.f0Add = $("#maryeffectF0Addf0Add").val();

            var Rate = {};
            Rate.name = "Rate";
            Rate.effect = {};
            Rate.effect.durScale = $("#maryeffectRatedurScale").val();

            var Robot = {};
            Robot.name = "Robot";
            Robot.effect = {};
            Robot.effect.amount = $("#maryeffectRobotamount").val();

            var Whisper = {};
            Whisper.name = "Whisper";
            Whisper.effect = {};
            Whisper.effect.amount = $("#maryeffectWhisperamount").val();

            var Stadium = {};
            Stadium.name = "Stadium";
            Stadium.effect = {};
            Stadium.effect.amount = $("#maryeffectStadiumamount").val();

            var Chorus = {};
            Chorus.name = "Chorus";
            Chorus.effect = {};
            Chorus.effect.amp1 = $("#maryeffectChorusamp1").val();
            Chorus.effect.amp3 = $("#maryeffectChorusamp3").val();
            Chorus.effect.amp2 = $("#maryeffectChorusamp2").val();
            Chorus.effect.delay3 = $("#maryeffectChorusdelay3").val();
            Chorus.effect.delay1 = $("#maryeffectChorusdelay1").val();
            Chorus.effect.delay2 = $("#maryeffectChorusdelay2").val();

             var FIRFilter = {};
             FIRFilter.name = "FIRFilter";
             FIRFilter.effect = {};
             FIRFilter.effect.fc2 = $("#maryeffectFIRFilterfc2").val();
             FIRFilter.effect.fc1 = $("#maryeffectFIRFilterfc1").val();
             FIRFilter.effect.type = $("#maryeffectFIRFiltertype").val();

                maryTTSParameters.audioEffects = [
                    volume, TractScaler, F0Scale, F0Add, Rate, Robot, Whisper, Stadium, Chorus, FIRFilter
                ]
            }

        break;
        case "hmm-select" : {
            break;
        }
    }
    var text = $("#textInput").val();
    if (text == "") {
        $("#textInput").addClass('is-invalid');
        return;
    }

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
        success: function(data){
            var src = location.pathname.replace("synthesis","") + "synthesized/" + data;
    	     $("#audioOutput").attr("src", src);
        }
    });
}

function getParameters(system){

    var marySelect = $('#mary-tts-select');
    var hmmSelect = $('#hmm-select');

    marySelect.removeClass('active');
    hmmSelect.removeClass('active');


    var hostname = window.location.origin;
        $.ajax({
            url: location.pathname + '/system/parameters',
            method: 'get',
            data: jQuery.param({system: system}),
            processData: false,
            success: function(data){

                 $('#modelSettings').html(data);

                 switch(system) {
                         case "MaryTTS" :
                             marySelect.addClass('active');

                            $("#toggle-mary-tts-button").click(function(){
                                  $("#collapseExample").collapse('toggle'); // toggle collapse
                             });
                             $('#mary-tts-locale-select').on('change', function (e) {
                                 var locale = $('#mary-tts-locale-select').find(':selected').val();
                                 $.ajax({
                                         url: location.pathname + "/system/parameters/voices",
                                         method: 'get',
                                         data: jQuery.param({locale: locale,
                                                             system: 'MaryTTS'}),
                                         success: function(data){
                                             $('#voice-select').html(data);
                                         }
                                     });
                             });

                             break;

                         case "HMM" :
                             hmmSelect.addClass('active');
                             break;
                     }
            }
        });
}

$('#mary-tts-select').click(function (clickEvent) {
    getParameters('MaryTTS')
});

$('#hmm-select').click(function (clickEvent) {
    getParameters('HMM')
});
