(function() {

	'use strict';

	angular.module('smartOCR').factory('OCRServices', OCRServices);

	OCRServices.$inject = ['$http'];

	function OCRServices($http) {

		var api = {
			postImageData : postImageData,
			extractAbzoobaResponse : extractAbzoobaResponse
		}

		return api;
		
		

		function postImageData(files) {
			
			return $http.post("rest/smartOCR/convertImagesToText", files, {
	            transformRequest: angular.identity,
	            headers: {'Content-Type': undefined}
	        }).then(onSuccessResponse, onErrorResponse);			
			//return $http.get('json/google_vision_response.json').then(onSuccessResponse, onErrorResponse);
		}
		
		function extractAbzoobaResponse(jsonString){
			var request = {
				method : 'POST',
				url : 'rest/abzoobaParse/parseText',
				data: jsonString,
				headers: {
				    'Accept':'application/json',
				    'Content-Type': 'application/json'
				}
			}
			return $http(request).then(onSuccessResponse, onErrorResponse);
			//return $http.get("json/abzooba_response.json").then(onSuccessResponse, onErrorResponse);
		}


		function onSuccessResponse(response) {
			return response.data;
		}

		function onErrorResponse(response) {
			console.log('Failed to load content'+ ' ' + response.statusText);
		}

	}

})();