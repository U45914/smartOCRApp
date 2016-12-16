(function(){
	'use strict';
	
	 angular.module('smartOCR')
	 .controller('AttributesViewController', AttributesViewController);
	 
	 AttributesViewController.$inject =['$scope','$rootScope', '$location','OCRServices', '$timeout'];
	 
	 function AttributesViewController($scope, $rootScope, $location, OCRServices, $timeout){
		 
		 var avcvm = this;
		 avcvm.abzoobaResponse = null;
		 avcvm.extractImageData = extractImageData;
		 avcvm.retry = retry;		 
		 avcvm.startTimer = startTimer;
		 avcvm.isTimerVisible = false;
		 avcvm.isButtonDisbled = true;
		 avcvm.isRetryButtonVisible = false;
		 
		 $rootScope.$broadcast('start-spinner');
		 
		 activate();
		 
		 function activate(){			 		
			 
			 OCRServices.getAbzoobaParsedAttributes(OCRServices.getOcrId()).then(function(response){
				 if(response != undefined && response.data != undefined){				 
					 avcvm.isRetryButtonVisible = false;
					 avcvm.abzoobaResponse = response.data;
					 avcvm.extractImageData(OCRServices.getPreviewData());
					 $rootScope.$broadcast('stop-spinner');
				 }else{
					 $rootScope.$broadcast('stop-spinner');
					 toastr.warning("The submitted request is under process. Please wait for some time and hit Retry button");
					 avcvm.isRetryButtonVisible = true;
					 avcvm.isButtonDisabled = true;
					 avcvm.startTimer();
				 }
			 });
		 }	
		 
		 function extractImageData(data){
			 avcvm.sliderArray  = [];			 
			 var counter = 0;
			 angular.forEach(data, function(obj, index){
				 var details = {};
				 details.image = obj.image;
				 details.type = obj.view;
				 if(counter == 0){
					 details.classAttr = "item active";					
				 }else{
					 details.classAttr = "item"; 					
				 }
				 counter  = 1;
				 avcvm.sliderArray.push(details);
			 });
		 }
		 
		 function retry(){
			 $rootScope.$broadcast('start-spinner');
			 activate();			 
		 }
		 
		 function startTimer(){
			 avcvm.counter = 10;
			 avcvm.isTimerVisible = true;
			 avcvm.onTimeout = function(){
				avcvm.counter--;
				if(!avcvm.counter){
					$timeout.cancel(myTimeout);	
					avcvm.isTimerVisible = false;
					avcvm.isButtonDisabled = false;
				}else{
					myTimeout = $timeout(avcvm.onTimeout, 1000);
				}
			}
			var myTimeout = $timeout(avcvm.onTimeout, 1000);
		 }
	 }
	
})();