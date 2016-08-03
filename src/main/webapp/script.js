$(function()
{
	// Variable to store your files
	var files;
     

     var $jqValue = $('.jqValue');
     var $jqValue1 = $('.jqValue1');
	// Add events
	$('input[type=file]').on('change', prepareUpload);
	$('form').on('submit', uploadFiles);
    $("#imagefile").change(function ()
              {
                     $("#img").show();
                     $("#img").attr("src",'');
                     if (typeof(FileReader)!="undefined"){

                         var regex = /^([a-zA-Z0-9\s_\\.\-:])+(.jpg|.jpeg|.gif|.png)$/;
                         $($(this)[0].files).each(function () {
                             var getfile = $(this);
                             if (regex.test(getfile[0].name.toLowerCase())) {
                                 var reader = new FileReader();
                                 reader.onload = function (e) {
                                     $("#img").attr("src",e.target.result);
                                 }
                                 reader.readAsDataURL(getfile[0]);
                             } else {
                                 alert(getfile[0].name + " is not image file.");
                                 return false;
                             }
                         });
                     }
                     else {
                         alert("Browser does not supportFileReader.");
                     }
            });

	// Grab the files and set them to our variable
	function prepareUpload(event)
	{
		files = event.target.files;
	}

	// Catch the form submit and upload the files
	function uploadFiles(event)
	{
		event.stopPropagation(); // Stop stuff happening
        event.preventDefault(); // Totally stop stuff happening

        // START A LOADING SPINNER HERE

        // Create a formdata object and add the files
		var data = new FormData();
		$.each(files, function(key, value)
		{
			data.append("file", value);
		});
        
        $.ajax({
            url: 'rest/ocr/convertImageToText',
            type: 'POST',
            data: data,
            cache: false,
            //dataType: 'json',
            processData: false, // Don't process the files
            contentType: false, // Set content type to false as jQuery will tell the server its a query string request
            success: function(data, textStatus, jqXHR)
            {
            	if(typeof data.error === 'undefined')
            	{
				    $jqValue.html(data);
            		// Success so call function to process the form
            		submitForm(event, data);
            	}
            	else
            	{
				 $jqValue.html("Unable to retrieve text");
            		// Handle errors here
            		console.log('ERRORS: ' + data.error);
            	}
            },
            error: function(jqXHR, textStatus, errorThrown)
            {
			   $jqValue.html("Unable to retrieve text");
            	// Handle errors here
            	console.log('ERRORS: ' + textStatus);
            	// STOP LOADING SPINNER
            }
        });
    }

    function submitForm(event, data)
	{
    	event.stopPropagation(); // Stop stuff happening
        event.preventDefault(); // Totally stop stuff happening

		// Create a jQuery object from the form
	//	$form = $(event.target);
		
		// Serialize the form data
	//	var formData = $form.serialize();
		
		// You should sterilise the file names
	//	$.each(data.files, function(key, value)
	//	{
	//		formData = formData + '&filenames[]=' + value;
	//	});

		$.ajax({
			url: 'rest/abzoobaParse/parseText',
            type: 'POST',
            contentType:"application/json; charset=utf-8",
            data: JSON.stringify({"id":"hello" ,"text":data}),
            cache: false,
            dataType: 'json',
            processData: false, // Don't process the files
            //contentType: false, // Set content type to false as jQuery will tell the server its a query string request
            success: function(data, textStatus, jqXHR)
            {
            	if(typeof data.error === 'undefined')
            	{
            		// Success so call function to process the form
            		console.log('SUCCESS: ' + data.success);
            		$jqValue1.html(data);
            	}
            	else
            	{
            		// Handle errors here
            		console.log('ERRORS: ' + data.error);
            	}
            },
            error: function(jqXHR, textStatus, errorThrown)
            {
            	// Handle errors here
            	console.log('ERRORS: ' + textStatus);
            },
            complete: function()
            {
            	// STOP LOADING SPINNER
            }
		});
	}
});