(function(){
	'use strict';
	
	 angular.module('smartOCR')
	 .controller('PreProfilerViewController', PreProfilerViewController);
	 
	 PreProfilerViewController.$inject =['$scope','$rootScope', '$location','OCRServices'];
	 
	 function PreProfilerViewController($scope, $rootScope, $location, OCRServices){
		 
		 var ppvcvm = this;
		 ppvcvm.previewData = null;
		 ppvcvm.extractImageData = extractImageData;
		 ppvcvm.goToGoogleVisionView = goToGoogleVisionView;
		 
		 activate();
		 
		 function activate(){
			 ppvcvm.previewData = OCRServices.getPreviewData();	
			 if(ppvcvm.previewData.length){
				 var ocrId = ppvcvm.previewData[0].smartOcrId;
				 OCRServices.setOcrId(ocrId);
				 ppvcvm.extractImageData(ppvcvm.previewData);
			 }			 
		}	
		 
		 function extractImageData(data){
			 ppvcvm.sliderArray  = [];			 
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
				 ppvcvm.sliderArray.push(details);
			 });
		 }
		 
		 function goToGoogleVisionView(){
			 $location.path('/googleVisionView');
		 }
		 
		setTimeout(function(){
			$('.carousel').carousel({
			    interval: false,
			    
			});
			
			$('.multislider .item').each(function () {		
		        var next = $(this).next();
		        if (!next.length) {
		            next = $(this).siblings(':first');
		        }
		        next.children(':first-child').clone().appendTo($(this));

		        if (next.next().length > 0) {
		            next.next().children(':first-child').clone().appendTo($(this));
		        }
		        else {
		            $(this).siblings(':first').children(':first-child').clone().appendTo($(this));
		        }
		    });
			
			 $('.carousel-control').click(function(e){				
		         var nav = $(this);
		         var direction = nav.attr('class').indexOf('left') == 0 ? "prev" : "next";
		         nav.parents('.carousel').carousel(direction);
			 });
		
			},750);
		 
		 
	 }
})();