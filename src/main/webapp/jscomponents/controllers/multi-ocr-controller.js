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
		mocvm.callEmiAppForCreation = callEmiAppForCreation;
		var files;
		
		function parseText(){			
			$rootScope.$broadcast('start-spinner');				
			mocvm.isExtactContentVisible =true;
			mocvm.parseTextResponseArray = [];
			var data = new FormData();
			$.each(files, function(key, value) {
				data.append("file", value);
			});
			OCRServices.postImageData(data).then(function(response){
				if(response.data !== undefined){
					mocvm.responseText = response.data;
					mocvm.parseTextResponseArray.push(response.data);
					//mocvm.gridOptions.data = mocvm.parseTextResponseArray;
					mocvm.isParseImageContentVisible = true;				
					$('html, body').animate({scrollTop: $('#extract_wrap').offset().top}, 'slow');
					//Triggering the call for loading the next grid
					mocvm.convertToJson(response.data);	
					mocvm.parseAbzoobaResponse(mocvm.jsonString);
				}
			});
		}		
		
		function parseAbzoobaResponse(jsonString){
			OCRServices.extractAbzoobaResponse(jsonString).then(function(response){	
				if(response.data !== undefined)	{
					mocvm.abzoobaResponse = response.data;
					$("#parse-text-div.margin_left").animate({'margin-left': 0});
					mocvm.isAbzoobaContentVisible = true;
					$rootScope.$broadcast('stop-spinner');
					mocvm.convertToJson(response.data);	
					//mocvm.callEmiAppForCreation(mocvm.jsonString); // will be un-commented at the time local machine is up and running
				}
			});
		}
		
		function callEmiAppForCreation(jsonString){
			OCRServices.postDataToEmiForCreation(jsonString).then(function(response){
				console.log(response.data);
			})
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

		/*function sendAttributeMapToEmi(data) {
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
		}*/
		
	}
	
})();