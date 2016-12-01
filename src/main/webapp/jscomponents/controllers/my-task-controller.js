(function (){
	
	'use strict';
	
	 angular.module('smartOCR')
	 .controller('MyTaskController',MyTaskController);
	 
	 MyTaskController.$inject =['$scope','$location','$http', 'MyTaskServices', '$rootScope'];
	 
	 function MyTaskController($scope, $location, $http, MyTaskServices, $rootScope){
		 
		 var mtcvm=this;
		 
		 mtcvm.jsonString = null;
		 mtcvm.app = {};
		 mtcvm.app.name = "Smart OCR";	
		 mtcvm.convertToJson = convertToJson;
		 mtcvm.updateTexts = updateTexts;
		 mtcvm.updateDB = updateDB;
		 mtcvm.extractImagesFromMyTasks = extractImagesFromMyTasks;	
		 mtcvm.convertTextAreaFriendly = convertTextAreaFriendly;
		 
		 $("#alertError").hide();
		 $("#alertSuccess").hide();
		 $("#mainPallete").hide();
		 
		 function convertToJson(data){
			 mtcvm.jsonString = JSON.stringify(data);
		 }
		 
		 /**
		  * This is for showing the dots under the image for carousel. The class uses "active" or  " "
		  */
		 function extractImagesFromMyTasks(data){
			 mtcvm.imagesArray = [];			 
			 if(data.image){				
				 mtcvm.imagesArray.push("active");
			 }
			 if(data.backImage){				 
				 mtcvm.imagesArray.push(" ");
			 }
			 if(data.rightImage){					 
				 mtcvm.imagesArray.push(" ");
			 }
			 if(data.leftImage){				 
				 mtcvm.imagesArray.push(" ");
			 }
			 if(data.topImage){				
				 mtcvm.imagesArray.push(" ");
			 }
			 
		 }
		 
		 /**
		  * convertTextAreaFriendly is added to make the text area to parse html format.
		  */
		 function convertTextAreaFriendly(data){
			 if(data.message == undefined || data.message == null || data.message == ""){
				 data.request.frontText = convertToInnerText(data.request.frontText);
				 data.request.backText = convertToInnerText(data.request.backText);
				 data.request.leftSideText = convertToInnerText(data.request.leftSideText);
				 data.request.rightSideText = convertToInnerText(data.request.rightSideText);
				 data.request.topSideText = convertToInnerText(data.request.topSideText);
			 }
			 return data;
		 }
		 
		 function convertToInnerText(node){
			 return angular.element(appendPTag(node))[0].innerText;
		 }
		 
		 function appendPTag(text){
			 return '<p>' + text + '</p>';
		 }
		 
		 mtcvm.getMyTasks = function() {
			$rootScope.$broadcast('start-spinner');	
			MyTaskServices.getTasks().then(function(response) {	
				mtcvm.myTask = mtcvm.convertTextAreaFriendly(response.data);				
				if (mtcvm.myTask.message) {
					$("#mainPallete").hide();
					$("#alertError").fadeTo(2000,500).slideUp(500,function() {
						$("#alertError").slideUp(500);
					});
					$('#bn-item').addClass("attach_button").removeClass("btn-right");
					$rootScope.$broadcast('stop-spinner');	
					
				} else {
					$("#mainPallete").show();					
					mtcvm.convertToJson(mtcvm.myTask.request);
					mtcvm.extractImagesFromMyTasks(mtcvm.myTask);
					mtcvm.myTask.request.smartOcrId = mtcvm.myTask.smartId;
					$rootScope.$broadcast('stop-spinner');	
					$('#bn-item').removeClass("attach_button").addClass("btn-right");
				}
			});
		  }

		 function updateTexts() {
			$rootScope.$broadcast('start-spinner');
			MyTaskServices.updateTexts(mtcvm.jsonString).then(function(responseData) {
				$rootScope.$broadcast('stop-spinner');
				mtcvm.updateDB(responseData.data);
				$("#alertSuccess").fadeTo(2000, 500).slideUp(500,function() {
					$("#alertSuccess").slideUp(500);
				});
			});
		}
				
		function updateDB(data){
			$rootScope.$broadcast('start-spinner');
			MyTaskServices.updateTexts(data).then(function(responseData) {
				$rootScope.$broadcast('stop-spinner');
				$("#alertSuccess").fadeTo(2000, 500).slideUp(500,function() {
					$("#alertSuccess").slideUp(500);
				});
			});
		}

		$('.carousel').carousel({
		    interval: false
		});
		 
	}
	
})();