(function (){
	
	'use strict';
	
	 angular.module('smartOCR')
	 .controller('InitProfilerController',InitProfilerController);
	 
	 InitProfilerController.$inject =['$scope', '$rootScope','$location', 'OCRServices'];
	 
	 function InitProfilerController($scope, $rootScope, $location, OCRServices){
		 
		 var ipcvm = this;
		 ipcvm.imageArray = null;
		 ipcvm.parseText = parseText;
		 
		 activate();
		 
		 function activate(){
			 ipcvm.imageArray =  OCRServices.getArray();			 
		 }
		
		 function parseText(){			
				$rootScope.$broadcast('start-spinner');		
				ipcvm.parseTextResponseArray = [];
				var data = new FormData();
				$.each(OCRServices.getFiles(), function(key, value) {
					data.append("file", value);
				});
				OCRServices.postImageData(data).then(function(response){
					if(response.data !== undefined){
						console.log("started");
						setTimeout(function(){
							console.log("Done")
							$rootScope.$broadcast('stop-spinner');
							OCRServices.getImageData(response.data.smartId).then(function(response){
								OCRServices.setPreviewData(response.data);
								$location.path("/preview");
							});
						},10000);
						
					}
				});
			}
		 
		 
	}
	
})();