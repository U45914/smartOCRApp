(function(){
	'use strict';
	
	 angular.module('smartOCR')
	 .controller('GoogleVisionController', GoogleVisionController);
	 
	 GoogleVisionController.$inject =['$scope','$rootScope', '$location','OCRServices'];
	 
	 function GoogleVisionController($scope, $rootScope, $location, OCRServices){
		 var gvcvm = this;
		 gvcvm.extractImageData = extractImageData;
		 gvcvm.goToAttributesView = goToAttributesView;
		 
		 activate();
		 $rootScope.$broadcast('start-spinner');
		 
		 function activate(){			 		
			 OCRServices.getGoogleVisionResponse(OCRServices.getOcrId()).then(function(response){
				 var data = response.data;
				 gvcvm.extractImageData(data);
				 $rootScope.$broadcast('stop-spinner');
			 });
		 }
		 
		 function extractImageData(data){
			 gvcvm.sliderArray  = [];			 
			 var counter = 0;
			 angular.forEach(data, function(obj, index){
				 var details = {};
				 details.image = obj.image;
				 details.type = obj.view;
				 details.ocrText = obj.ocrText;
				 if(counter == 0){
					 details.classAttr = "item active";					
				 }else{
					 details.classAttr = "item"; 					
				 }
				 counter  = 1;
				 gvcvm.sliderArray.push(details);
			 });
		 }
		 
		 function goToAttributesView(){
			 $location.path('/attributesView');
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
			
				},2000);
		 
	 }
 })();