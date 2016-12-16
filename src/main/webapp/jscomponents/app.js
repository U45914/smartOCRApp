angular.module('smartOCR', ['ngRoute', 'ui.grid', 'ngSanitize', 'ui.bootstrap'])
.config(['$routeProvider', function ($routeProvider) {
		$routeProvider
	.when('/', {
		templateUrl : 'partials/first-screen.html',
		controller: 'LandingController',
		controllerAs:'lcvm'
	})
	.when('/initProfiler', {
		templateUrl : 'partials/init-profiler-screen.html',
		controller: 'InitProfilerController',
		controllerAs:'ipcvm'
	})
	.when('/preview', {
		templateUrl : 'partials/pre-profiler-view.html',
		controller: 'PreProfilerViewController',
		controllerAs:'ppvcvm'
	})
	.when('/smartVisionView', {
		templateUrl : 'partials/smart-vision-view.html',
		controller: 'GoogleVisionController',
		controllerAs:'gvcvm'
	})
	.when('/attributesView', {
		templateUrl : 'partials/attributes-view.html',
		controller: 'AttributesViewController',
		controllerAs:'avcvm'
	})
	/*.when('/multiOCR', {
		templateUrl : 'partials/multi-ocr-page.html',
		controller: 'MultiOcrController',
		controllerAs:'mocvm'
		
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
	.otherwise({redirectTo: '/'});
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
