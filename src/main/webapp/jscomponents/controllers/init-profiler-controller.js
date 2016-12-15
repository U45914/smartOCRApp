(function (){
	
	'use strict';
	
	 angular.module('smartOCR')
	 .controller('InitProfilerController',InitProfilerController);
	 
	 InitProfilerController.$inject =['$scope', '$rootScope','$location', 'OCRServices', '$timeout', '$interval'];
	 
	 function InitProfilerController($scope, $rootScope, $location, OCRServices, $timeout, $interval){
		 
		 var ipcvm = this;
		 ipcvm.showSubmitButton = true;
		 ipcvm.imageArray = null;
		 ipcvm.parseText = parseText;
		 ipcvm.retry = retry;
		 ipcvm.startTimer = startTimer;			
		 ipcvm.isTimerVisible = false;
		 ipcvm.isButtonDisbled = true;
		 ipcvm.isRetryButtonVisible = false;
		 ipcvm.promise = null;
		 ipcvm.smartId = null;
		 
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
				ipcvm.showSubmitButton = false;
				ipcvm.isRetryButtonVisible = true;
				OCRServices.postImageData(data).then(function(response){
					if(response != undefined && response.data != undefined){
						if(response.data.smartId != undefined || response.data.smartId != null){
							ipcvm.smartId = response.data.smartId;
							OCRServices.setOcrId(response.data.smartId);														
							$rootScope.$broadcast('stop-spinner');
							OCRServices.getImageData(response.data.smartId).then(function(response){
								if(response != undefined && response.data != null){								
									OCRServices.setPreviewData(response.data);
									$location.path("/preview");
								}else{
									toastr.success("Request has submitted successfully");
									ipcvm.isButtonDisabled = true;
									ipcvm.isRetryButtonVisible = true;									
									ipcvm.startTimer();
								}
							});						
						}else{
							toastr.warning("The submitted request is under process. Please wait for the page get redirect automatically or click Next to continue");
						}						
					}else{
						toastr.error("Oops !!. Experiencing server error. Please try again later.");
					}
				});
			}
		 
		 function retry(){
			 OCRServices.getImageData(OCRServices.getOcrId()).then(function(response){
				 if (response != undefined) {
					 	ipcvm.isButtonDisabled = false;					 	
						OCRServices.setPreviewData(response.data);
						$location.path("/preview");
				 } else {
					 toastr.warning("Your request still in progress, please wait...");
					 ipcvm.isButtonDisabled = true;
					 ipcvm.startTimer();									 
				 }				 	
				});
		 }		 
		 
		
		 function startTimer(){
			 ipcvm.counter = 10;
			 ipcvm.isTimerVisible = true;
			 ipcvm.onTimeout = function(){
					ipcvm.counter--;
					if(!ipcvm.counter){
						$timeout.cancel(myTimeout);	
						ipcvm.isTimerVisible = false;
						ipcvm.isButtonDisabled = false
					}else{
						myTimeout = $timeout(ipcvm.onTimeout, 1000);
					}
				}
				 var myTimeout = $timeout(ipcvm.onTimeout, 1000);
		 }
		 
		 
	}
	
})();