(function (){
	
	'use strict';
	
	 angular.module('smartOCR')
	 .controller('MultiOcrController',MultiOcrController);
	 
	 MultiOcrController.$inject =['$scope','$rootScope', '$location','OCRServices'];
	 
	 function MultiOcrController($scope, $rootScope, $location, OCRServices){
		 
		var mocvm=this;			
		mocvm.isExtactButtonVisible = false;
		mocvm.isAbzoobaContentVisible = false;
		mocvm.isParseImageContentVisible = false;
		mocvm.gridOptions = null
		mocvm.parseText = parseText;
		mocvm.isGridVisible = true;
		mocvm.parseTextResponseArray = [];
		mocvm.jsonString = null;
		mocvm.convertToJson = convertToJson;
		mocvm.parseAbzoobaResponse = parseAbzoobaResponse;
		mocvm.responseText = null;	
		
		
		// Variable to store your files
		var files;
		//var $jqValue = $('.jqValue');
		//var $tableValue = $('.tableValue');
		//var imageFileName;
		
		
		function parseText(){			
			$rootScope.$broadcast('start-spinner');				
			mocvm.isExtactContentVisible =true;
			mocvm.parseTextResponseArray = [];
			var data = new FormData();
			$.each(files, function(key, value) {
				data.append("file", value);
			});
			OCRServices.postImageData(data).then(function(response){
				mocvm.convertToJson(response);	
				mocvm.responseText = response;
				mocvm.parseTextResponseArray.push(response);
				//mocvm.gridOptions.data = mocvm.parseTextResponseArray;
				mocvm.isParseImageContentVisible = true;				
				$('html, body').animate({scrollTop: $('#extract_wrap').offset().top -100 }, 'slow');
				//Triggering the call for loading the next grid
				mocvm.parseAbzoobaResponse(mocvm.jsonString);
			});
		}		
		
		function parseAbzoobaResponse(jsonString){
			OCRServices.extractAbzoobaResponse(jsonString).then(function(response){
				mocvm.abzoobaResponse = response;
				$("#parse-text-div.margin_left").animate({'margin-left': 0});
				mocvm.isAbzoobaContentVisible = true;
				$rootScope.$broadcast('stop-spinner');
			});
		}
		
		function convertToJson(data){
			mocvm.jsonString = JSON.stringify(data);
		}		
		
		/*mocvm.gridOptions = {		        
		        columnDefs: [
		          { name:'ID', field: 'id' },
		          { name:'Smart Ocr Id', field: 'smartOcrId' },
		          { name:'Front Text', field: 'frontTextFormatted'},
		          { name:'Back Text', field: 'backTextFormatted'}
		        ]
		     };
		*/
		
		// Add events
		$('input[type=file]').on('change', prepareUpload);		
		//$('form').on('submit', uploadFiles);
		$("#imagefile").change(
				function() {
					mocvm.isSpinnerVisible = true;
					mocvm.isParseImageContentVisible = false;
					mocvm.isAbzoobaContentVisible = false;
					$scope.$apply();
					var counter = 0;
					if (window.File && window.FileList && window.FileReader) {							
						var files = event.target.files; 
						if(files.length != 0){
							mocvm.picFiles = [];
						}
						$('#bn-item').removeClass("attach_button").addClass("btn-right");
						for (var i = 0; i < files.length; i++) {
							var file = files[i];
							if (!file.type.match('image')){
								continue;
							}
							var picReader = new FileReader();
							picReader.addEventListener("load", function(event) {								
								var picFile = {}
								picFile.result = event.target.result;
								if(counter == 0){
									picFile.classAttr = "item active";
									picFile.dot = "active";
									counter = 1;
								}else{
									picFile.classAttr = "item";
									picFile.dot = "";
								}
								mocvm.picFiles.push(picFile);
								mocvm.isExtactButtonVisible = true;	
								mocvm.isSpinnerVisible = false;	
								$scope.$apply();								
							});							
							picReader.readAsDataURL(file);
						}
					} else {
						console.log("Your browser does not support File API");
					}
				});
		
		// Grab the files and set them to our variable
		function prepareUpload(event) {
			files = event.target.files;

		}	
		
		// Catch the form submit and upload the files
		/*function uploadFiles(event) {
			event.stopPropagation(); // Stop stuff happening
			event.preventDefault(); // Totally stop stuff happening

			// START A LOADING SPINNER HERE

			// Create a formdata object and add the files
			var data = new FormData();			
			$.each(files, function(key, value) {
				data.append("file", value);
			});
			showLoadingScreen();
			$jqValue.html("");
			$tableValue.html("");
			$.ajax({
				url : 'rest/smartOCR/convertImagesToText',
				type : 'POST',
				data : data,
				cache : false,
				// dataType: 'json',
				processData : false, // Don't process the files
				//contentType : 'application/json',
				contentType : false, // Set content type to false as jQuery will
										// tell the server its a query string
										// request

				success : function(data, textStatus, jqXHR) {

					if (typeof data.error === 'undefined') {
						gettableData1(data);
						//hideLoadingScreen();
						var requestBody = {
							"id" : "newmmmmaaaa2",
							"text" : data,
							"imageFileName" : imageFileName
						};
						GetTableData(data);
						// Success so call function to process the form
						submitForm(event, data);
						
					} else {
						$jqValue.html("Unable to retrieve text");
						//var tabledata = gettableData(jsondata);
						// Handle errors here
						hideLoadingScreen();
						console.log('ERRORS: ' + data.error);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					$jqValue.html("Unable to retrieve text");
				//var tabledata = gettableData(jsondata);
					// Handle errors here
					console.log('ERRORS: ' + textStatus);
					// STOP LOADING SPINNER
					hideLoadingScreen();
				}
			});
		}
		function gettableData1(jsonObj) {
			var html = '<table border="0" class="ocrTable">';
			$.each(jsonObj, function(key, value) {
				html += '<tr>';
				html += '<td>' + key + '</td>';
				html += '<td>' + value + '</td>';
				html += '</tr>';
			});
			html += '</table>';
			$jqValue	.html(html);
		}*/

		/*function GetTableData(requestBody) {
			$.ajax({
				url : 'rest/abzoobaParse/parseText',
				// url: 'http://localhost:8080/abzooba/parseText',
				type : 'POST',
				data : JSON.stringify(requestBody),
				cache : false,
				crossDomain : true,
				// dataType: 'json',
				processData : false, // Don't process the files
				contentType : 'application/json', // Set content type to false as
													// jQuery will tell the server
													// its a query string request
				success : function(data, textStatus, jqXHR) {
					//hideLoadingScreen();
					if (typeof data.error === 'undefined') {
						hideLoadingScreen();

						var tabledata = gettableData(data);
						
						sendAttributeMapToEmi(data);
					} else {
						// Handle errors here
						console.log('ERRORS: ' + data.error);
						hideLoadingScreen();
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					// Handle errors here
					console.log('ERRORS: ' + textStatus);
					// STOP LOADING SPINNER
					hideLoadingScreen();
				}
			});
		}*/

		function sendAttributeMapToEmi(data) {
			$.ajax({
				url : 'http://WVWEA004C2483.homeoffice.Wal-Mart.com:8888/OCRImageToText/rest/Product/create',
				// url: 'http://localhost:8080/abzooba/parseText',
				type : 'POST',
				data : JSON.stringify(data),
				cache : false,
				crossDomain : true,
				// dataType: 'json',
				processData : false, // Don't process the files
				contentType : 'application/json', // Set content type to false as
													// jQuery will tell the server
													// its a query string request
				success : function(data, textStatus, jqXHR) {
					//hideLoadingScreen();
				},
				error : function(jqXHR, textStatus, errorThrown) {
					// Handle errors here
					console.log('ERRORS: ' + textStatus);
				}
			});
		}
		
		/*function gettableData(jsonObj) {
			var html = '<table border="0" class="ocrTable">';
			html += "<tr>";
			html += "<th> Attribute </th>";
			html += "<th> Value </th>";
			html += "<th> Confidence Level </th>";
			
			html += "</tr>";
				
			$.each(jsonObj, function(i) {
				html += '<tr>';
				html += '<td>' + jsonObj[i].Attribute + '</td>';
				html += '<td>' + jsonObj[i].Value + '</td>';
				html += '<td>' + jsonObj[i].CLevel + '</td>';
				html += '</tr>';
			});
			html += '</table>';
			$tableValue.html(html);
		}*/

		/*function submitForm(event, data) {
			// Create a jQuery object from the form
			$form = $(event.target);

			// Serialize the form data
			var formData = $form.serialize();

			// You should sterilise the file names
			$.each(data.files, function(key, value) {
				formData = formData + '&filenames[]=' + value;
			});

			$.ajax({
				url : 'submit.php',
				type : 'POST',
				data : formData,
				cache : false,
				dataType : 'json',
				success : function(data, textStatus, jqXHR) {
					if (typeof data.error === 'undefined') {
						// Success so call function to process the form
						console.log('SUCCESS: ' + data.success);
					} else {
						// Handle errors here
						console.log('ERRORS: ' + data.error);
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					// Handle errors here
					console.log('ERRORS: ' + textStatus);
				},
				complete : function() {
					// STOP LOADING SPINNER
				}
			});
		}*/
		 
		 
		 
	}
	
})();