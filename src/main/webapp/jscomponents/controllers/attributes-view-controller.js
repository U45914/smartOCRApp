(function(){
	'use strict';
	
	 angular.module('smartOCR')
	 .controller('AttributesViewController', AttributesViewController);
	 
	 AttributesViewController.$inject =['$scope','$rootScope', '$location','OCRServices'];
	 
	 function AttributesViewController($scope, $rootScope, $location, OCRServices){
		 
		 var avcvm = this;
		 avcvm.abzoobaResponse = null;
		 avcvm.extractImageData = extractImageData;
		 
		 $rootScope.$broadcast('start-spinner');
		 
		 activate();
		 
		 function activate(){			 		
			 
			 OCRServices.getAbzoobaParsedAttributes(OCRServices.getOcrId()).then(function(response){
				 avcvm.abzoobaResponse = response.data;
				 avcvm.extractImageData(OCRServices.getPreviewData());
				 $rootScope.$broadcast('stop-spinner');
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
		 
	 }
	
})();