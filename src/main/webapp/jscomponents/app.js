angular.module('smartOCR', ['ngRoute', 'ui.grid', 'ngSanitize'])
.config(['$routeProvider', function ($routeProvider) {
		$routeProvider
	/*.when('/', {
		templateUrl : 'partials/landing-page.html',
		controller: 'LandingController',
		controllerAs:'lcvm'
	})*/
	.when('/multiOCR', {
		templateUrl : 'partials/multi-ocr-page.html',
		controller: 'MultiOcrController',
		controllerAs:'mocvm'
		
	})
	/*.when('/myTasks',{
		templateUrl : 'partials/my-tasks.html',
		controller: 'MyTaskController',
		controllerAs:'mtcvm'
	})*/
	.when('/myTasksNew',{
		templateUrl : 'partials/my-tasks-new.html',
		controller: 'MyTaskNewController',
		controllerAs:'mtncvm'
	})
	.when('/completedTasks', {
		templateUrl : 'partials/completed-tasks.html',
		controller: 'CompletedTasksController',
		controllerAs:'ctcvm'
	})
	.otherwise({redirectTo: '/multiOCR'});
}]).directive("ocrSpinner",function(){
	 return{
         link: linkFunction
     };

     function linkFunction(scope, element, attrs) {
         scope.$on('start-spinner', function() {  
        	 var template = angular.element('<div class="ocr-spinner-outer"><img src="images/spinner.gif" class="ocr-spinner-gif"></img></div>');
             element.append(template);        	
         });
         scope.$on('stop-spinner', function() {
             $(".ocr-spinner-outer").remove();
         });
     }
	
});
