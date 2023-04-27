function synthesizeRequest(){
var hostname = window.location.origin
    $.ajax({
        url: location.pathname + '/synthesize',
        method: 'post',
        headers: {
            "Content-Type": "application/json; odata=verbose"
          },
        data: JSON.stringify({"text" : $("#textInput").val()}),
        processData: false,
        success: function(data){
            var src = location.pathname.replace("synthesis","") + "synthesized/" + data;
    	     $("#audioOutput").attr("src", src);
        }
    });
}