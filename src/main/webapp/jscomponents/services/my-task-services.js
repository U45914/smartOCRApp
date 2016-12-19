(function() {

	'use strict';

	angular.module('smartOCR').factory('MyTaskServices', MyTaskServices);

	MyTaskServices.$inject = ['$http', '$rootScope'];

	function MyTaskServices($http, $rootScope) {

		var api = {
			getTasks : getTasks,
			updateTexts : updateTexts,
			updateDB : updateDB
		}

		return api;
		
		function getTasks(){
			var request = {
				method : 'GET',
				url : 'http://product-profiling.herokuapp.com/rest/mytask/get'
			}
			return $http(request).then(onSuccessResponse, onErrorResponse);
			//return $http.get("json/my-task-response.json").then(onSuccessResponse, onErrorResponse);	
		}
		
		function updateTexts(jsonData, id){
			var request = {
				method : 'PUT',
				url : 'http://product-profiling.herokuapp.com/rest/abzoobaParse/updateAbzoobaResponse/'+ id,
				data : jsonData,
				headers : {					
				    'Content-Type': 'application/json',
				    'Accept':'application/json',
				    'isCrowd' : 'True'
				}
			}
			return $http(request).then(onSuccessResponse, onErrorResponse);			
		}	
		
		function updateDB(data){
			var request ={
					method : 'POST',
					url : 'http://product-profiling.herokuapp.com/OCRImageToText/rest/Product/create',
					data : data,
					headers : {
						'Content-Type' : 'application/json'
					}			
			}
			return $http(request).then(onSuccessResponse, onErrorResponse);	
		}
		
		
		function onSuccessResponse(response) {
			return response;
		}

		function onErrorResponse(response) {
			console.log('Failed to load content'+ ' ' + response.statusText);
			toastr.error("Ooops !!! Something went wrong");
			$rootScope.$broadcast('stop-spinner');
		}

	}

})();
