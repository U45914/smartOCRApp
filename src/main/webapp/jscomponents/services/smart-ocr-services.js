(function() {

	'use strict';

	angular.module('smartOCR').factory('OCRServices', OCRServices);

	OCRServices.$inject = ['$http', '$rootScope'];

	function OCRServices($http, $rootScope) {
		
		var imageArray = [];
		var files = null;
		var previewData = null;
		var ocrId = null;
		
		var api = {
			postImageData : postImageData,
			getImageData : getImageData,
			getGoogleVisionResponse : getGoogleVisionResponse,
			getAbzoobaParsedAttributes : getAbzoobaParsedAttributes,			
			pushToArray : pushToArray,
			getArray : getArray,
			initArray : initArray,
			setFiles : setFiles,
			getFiles : getFiles,
			setPreviewData : setPreviewData,
			getPreviewData : getPreviewData,
			setOcrId : setOcrId,
			getOcrId : getOcrId			
		}

		return api;
		

		function postImageData(files) {
			return $http.post("rest/services/uploadImages", files, {
	            transformRequest: angular.identity,
	            headers: {'Content-Type': undefined}
	        }).then(onSuccessResponse, onErrorResponse);
		}
		
		function getImageData(id){
			var request = {
					method : 'GET',
					url : 'rest/services/images/'+ id,					
					headers: {
					    'Accept':'application/json'					    
					}
			};
			return $http(request).then(onSuccessResponse, onErrorResponse);						
		}
		
		function getGoogleVisionResponse(ocrId){
			var request = {
					method : 'GET',
					url : 'rest/services/ocrText/google/'+ ocrId,					
					headers: {
					    'Accept':'application/json'					    
					}
			};
			return $http(request).then(onSuccessResponse, onErrorResponse);			
		}
		
		function getAbzoobaParsedAttributes(ocrId){
			var request = {
					method : 'GET',
					url : 'rest/services/attributes/'+ ocrId,					
					headers: {
					    'Accept':'application/json'					    
					}
			};
			return $http(request).then(onSuccessResponse, onErrorResponse);			
		}
		
		function pushToArray(imageData){
			imageArray.push(imageData);
		}
		
		function initArray(){
			imageArray = [];
		}
		
		function getArray(){
			return imageArray;
		}
		
		function setFiles(filesData){
			files = filesData;
		}
		
		function getFiles(){
			return files;
		}
		
		function setPreviewData(data){
			previewData = data;
		}
		
		function getPreviewData(){
			return previewData;
		}
		
		function setOcrId(id){
			ocrId = id;
		}
		
		function getOcrId (){
			return ocrId;
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