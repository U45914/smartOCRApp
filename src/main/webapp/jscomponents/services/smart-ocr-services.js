(function() {

	'use strict';

	angular.module('smartOCR').factory('OCRServices', OCRServices);

	OCRServices.$inject = ['$http', '$rootScope'];

	function OCRServices($http, $rootScope) {

		var api = {
			postImageData : postImageData,
			extractAbzoobaResponse : extractAbzoobaResponse,
			postDataToEmiForCreation : postDataToEmiForCreation
		}

		return api;
		
		

		function postImageData(files) {
			return $http.post("rest/smartOCR/convertImagesToText", files, {
	            transformRequest: angular.identity,
	            headers: {'Content-Type': undefined}
	        }).then(onSuccessResponse, onErrorResponse);
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
		}
		
		function postDataToEmiForCreation(jsonString){
			var request ={
				method : 'POST',
				url : 'http://WVWEA004C2483.homeoffice.Wal-Mart.com:8888/OCRImageToText/rest/Product/create',
				data : jsonString,
				headers :{
					'Content-Type': 'application/json'
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