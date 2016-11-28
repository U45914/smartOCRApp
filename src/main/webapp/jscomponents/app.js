angular.module('smartOCR', ['ngRoute', 'ui.grid', 'ngSanitize'])
.config(['$routeProvider', function ($routeProvider) {
		$routeProvider
	.when('/', {
		templateUrl : 'partials/landing-page.html',
		controller: 'LandingController',
		controllerAs:'lcvm'
		
	})	
	.when('/multiOCR', {
		templateUrl : 'partials/multi-ocr-page.html',
		controller: 'MultiOcrController',
		controllerAs:'mocvm'
		
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
