(function(){
	'use strict';

	angular.module('smartOCR').factory('CompletedTasksServices', CompletedTasksServices);

	CompletedTasksServices.$inject = ['$http', '$rootScope'];

	function CompletedTasksServices($http, $rootScope) {

		var api = {
			viewAllTasks : viewAllTasks			
		};

		return api;
		
		function viewAllTasks(){
			var request = {
				method : 'GET',
				url : 'rest/mytask/get/all',
				headers: {
				    'Accept':'application/json'				    
				}
			};
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