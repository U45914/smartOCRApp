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
				url : 'rest/mytask/get'
			}
			return $http(request).then(onSuccessResponse, onErrorResponse);			
		}
		
		function updateTexts(jsonData){
			var request = {
				method : 'POST',
				url : 'rest/abzoobaParse/parseText',
				data : jsonData,
				headers : {					
				    'Content-Type': 'application/json',
				    'isCrowd' : 'True'
				}
			}
			return $http(request).then(onSuccessResponse, onErrorResponse);			
		}	
		
		function updateDB(data){
			var request ={
					method : 'POST',
					url : 'http://WVWEA004C2483.homeoffice.Wal-Mart.com:8888/OCRImageToText/rest/Product/create',
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