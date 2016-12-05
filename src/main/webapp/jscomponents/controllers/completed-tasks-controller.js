(function(){
	
	'use strict';
	
	 angular.module('smartOCR').controller('CompletedTasksController', CompletedTasksController);
	 
	 CompletedTasksController.$inject =['$scope','$location','$http', 'CompletedTasksServices', '$rootScope'];
	 
	 function CompletedTasksController($scope, $location, $http, CompletedTasksServices, $rootScope){
		 
		 var ctcvm = this;
		 ctcvm.viewAllTasks = viewAllTasks;
		 ctcvm.viewTask = viewSingleTask;
		 ctcvm.allTasks  = null;
		 
		 $rootScope.$broadcast('stop-spinner');
		 ctcvm.app = {};
		 ctcvm.app.name = "Smart OCR";
		 ctcvm.viewAllTasks();

		 function viewSingleTask(ele) {
			 $rootScope.$broadcast('start-spinner');
			$('#myModal').modal('show');
			ctcvm.selected = ctcvm.allTasks[ele];
			 $rootScope.$broadcast('stop-spinner');
		}
		 
		 function viewAllTasks(){
			 $rootScope.$broadcast('start-spinner');
			 CompletedTasksServices.viewAllTasks().then(function(response){
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