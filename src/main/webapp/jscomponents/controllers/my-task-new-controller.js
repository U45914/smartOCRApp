(function (){
	
	'use strict';
	
	 angular.module('smartOCR')
	 .controller('MyTaskNewController',MyTaskNewController);
	 
	 MyTaskNewController.$inject =['$scope','$location','$http', 'MyTaskServices', '$rootScope'];
	 
	 function MyTaskNewController($scope, $location, $http, MyTaskServices, $rootScope){
		 
		 var mtncvm=this;
		 
		 mtncvm.jsonString = null;
		 mtncvm.app = {};
		 mtncvm.app.name = "Smart OCR";	
		 mtncvm.convertToJson = convertToJson;
		 mtncvm.updateTexts = updateTexts;
		 mtncvm.updateDB = updateDB;
		 mtncvm.extractImagesFromMyTasks = extractImagesFromMyTasks;	
		 mtncvm.convertTextAreaFriendly = convertTextAreaFriendly;
		 mtncvm.isTablesVisible = false;
		 
		 $("#alertError").hide();
		 $("#alertSuccess").hide();
		 $("#mainPallete").hide();
		 
		 function convertToJson(data){
			 mtncvm.jsonString = JSON.stringify(data);
		 }
		 
		 /**
		  * This is for showing the images and dots under the image for carousel. The class uses "active" or  ""
		  */
		 function extractImagesFromMyTasks(data){
			 mtncvm.imagesArray = [];	
			
			 if(data.frontImage){
				 var details = {};
				 details.dotClass ="active";
				 details.imageData = data.image;
				 details.classAttr = "item active";
				 mtncvm.imagesArray.push(details);
			 }
			 if(data.backImage){
				 var details = {};
				 details.dotClass ="";
				 details.imageData = data.backImage;
				 details.classAttr = "item";
				 mtncvm.imagesArray.push(details);
			 }
			 if(data.rightImage){
				 var details = {};
				 details.dotClass ="";
				 details.imageData = data.rightImage;
				 details.classAttr = "item";
				 mtncvm.imagesArray.push(details);
			 }
			 if(data.leftImage){
				 var details = {};
				 details.dotClass ="";
				 details.imageData = data.leftImage;
				 details.classAttr = "item";
				 mtncvm.imagesArray.push(details);
			 }
			 if(data.topImage){	
				 var details = {};
				 details.dotClass ="";
				 details.imageData = data.topImage;
				 details.classAttr = "item";
				 mtncvm.imagesArray.push(details);
			 }
			 
		 }
		 
		 /**
		  * convertTextAreaFriendly is added to make the text area to parse html format.
		  */		 
		 function convertTextAreaFriendly(data){
			 if(data.message == undefined || data.message == null || data.message == ""){
				 angular.forEach(data.attributeBag, function(object, index){
					 angular.forEach(object, function(value, key){
						if(key == "Value"){
							data.attributeBag[index].Value = convertToInnerText(value.replace(new RegExp(" br ",'g'), "<br/>"));
						 }
					 });				 
				 });
			 }
			 return data;
		 }
		 
		 
		 function convertToInnerText(node){
			 return angular.element(appendPTag(node))[0].innerText;
		 }
		 
		 function appendPTag(text){
			 return '<p>' + text + '</p>';
		 }
		 
		 mtncvm.getMyTasks = function() {
			$rootScope.$broadcast('start-spinner');	
			MyTaskServices.getTasks().then(function(response) {	
				mtncvm.myTask = mtncvm.convertTextAreaFriendly(response.data);	
				mtncvm.responseText = mtncvm.myTask.request;
				if (mtncvm.myTask.message) {
					$("#mainPallete").hide();
					$("#alertError").fadeTo(2000,500).slideUp(500,function() {
						$("#alertError").slideUp(500);
					});
					$('#bn-item').addClass("attach_button").removeClass("btn-right");
					$rootScope.$broadcast('stop-spinner');	
					
				} else {
					$("#mainPallete").show();					
					mtncvm.convertToJson(mtncvm.myTask.request);
					mtncvm.extractImagesFromMyTasks(mtncvm.myTask);
					mtncvm.myTask.request.smartOcrId = mtncvm.myTask.smartId;
					mtncvm.isTablesVisible = true;
					$rootScope.$broadcast('stop-spinner');	
					$('#bn-item').removeClass("attach_button").addClass("btn-right");
				}
			});
		  }

		 function updateTexts() {
			$rootScope.$broadcast('start-spinner');
			MyTaskServices.updateTexts(mtncvm.jsonString).then(function(responseData) {
				$rootScope.$broadcast('stop-spinner');
				mtncvm.updateDB(responseData.data);
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