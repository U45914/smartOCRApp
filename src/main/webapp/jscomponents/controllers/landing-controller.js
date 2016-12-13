(function (){
	
	'use strict';
	
	 angular.module('smartOCR')
	 .controller('LandingController',LandingController);
	 
	 LandingController.$inject =['$scope','$location', 'OCRServices'];
	 
	 function LandingController($scope, $location, OCRServices){
		 
		 var lcvm = this;
		 lcvm.picFiles = [];
		 var files = null;
		 
		 $('input[type=file]').on('change', prepareUpload);				
		 $("#imagefile").change(
				function() {				
					if (window.File && window.FileList && window.FileReader) {							
						var files = event.target.files; 
						OCRServices.initArray();
						for (var i = 0; i < files.length; i++) {
							var file = files[i];
							if (!file.type.match('image')){
								continue;
							}
							var picReader = new FileReader();
							picReader.addEventListener("load", function(event) {								
								var picFile = {};
								picFile.image = event.target.result;								
								//lcvm.picFiles.push(picFile);
								OCRServices.pushToArray(picFile);
								$scope.$apply();
								console.log(OCRServices.getArray());																
							});							
							picReader.readAsDataURL(file);
						}
						$location.path("/initProfiler");
					} else {
						console.log("Your browser does not support File API");
					}
				});
			
		// Grab the files and set them to our variable
		function prepareUpload(event) {
			files = event.target.files;
			OCRServices.setFiles(files);			
		}
	}
	
})();