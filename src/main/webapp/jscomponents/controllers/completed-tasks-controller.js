(function(){
	
	'use strict';
	
	 angular.module('smartOCR').controller('CompletedTasksController', CompletedTasksController);
	 
	 CompletedTasksController.$inject =['$scope','$location','$http', 'CompletedTasksServices', '$rootScope'];
	 
	 function CompletedTasksController($scope, $location, $http, CompletedTasksServices, $rootScope){
		 
		 var ctcvm = this;
		 ctcvm.viewAllTasks = viewAllTasks;
		 ctcvm.viewSingleTask = viewSingleTask;
		 ctcvm.extractImagesFromResponse = extractImagesFromResponse;
		 ctcvm.allTasks  = null;
		 
		 $rootScope.$broadcast('stop-spinner');
		 ctcvm.app = {};
		 ctcvm.app.name = "Smart OCR";
		 ctcvm.viewAllTasks();

		 function extractImagesFromResponse(data){
			 ctcvm.imagesArray = [];	
				
			 if(data.image){
				 var details = {};
				 details.dotClass ="active";
				 details.imageData = data.image;
				 details.classAttr = "item active";
				 ctcvm.imagesArray.push(details);
			 }
			 if(data.backImage){
				 var details = {};
				 details.dotClass ="";
				 details.imageData = data.backImage;
				 details.classAttr = "item";
				 ctcvm.imagesArray.push(details);
			 }
			 if(data.rightImage){
				 var details = {};
				 details.dotClass ="";
				 details.imageData = data.rightImage;
				 details.classAttr = "item";
				 ctcvm.imagesArray.push(details);
			 }
			 if(data.leftImage){
				 var details = {};
				 details.dotClass ="";
				 details.imageData = data.leftImage;
				 details.classAttr = "item";
				 ctcvm.imagesArray.push(details);
			 }
			 if(data.topImage){	
				 var details = {};
				 details.dotClass ="";
				 details.imageData = data.topImage;
				 details.classAttr = "item";
				 ctcvm.imagesArray.push(details);
			 }
			 
		 }
		 
		 function viewSingleTask(ele) {
			 $rootScope.$broadcast('start-spinner');
			$('#myModal').modal('show');
			ctcvm.selected = ctcvm.allTasks[ele];
			 $rootScope.$broadcast('stop-spinner');
		}
		 
		 function viewAllTasks(){
			 $rootScope.$broadcast('start-spinner');
			 CompletedTasksServices.viewAllTasks().then(function(response){
				 extractImagesFromResponse(response.data);
				 ctcvm.allTasks = response.data;
					if ( ctcvm.allTasks.message) {
						$("#mainPallete").hide();
						$("#alertError").fadeTo(2000, 500).slideUp(500, function(){
						    $("#alertError").slideUp(500);
						});
					} else {
						$("#mainPallete").show();
						 //ctcvm.allTasks.request.smartOcrId = ctcvm.allTasks.smartId;
					}
					$rootScope.$broadcast('stop-spinner');
				});			 
		 }
		 
		 $('.carousel').carousel({
			    interval: false
			});
		 
	 }
	
})();