/*(function(){
	angular
	.module("smartOCR",[])
	.directive("fileread", [function () {
	    return {
	        restrict: 'A',
	        link: function (scope, element, attributes) {
	            element.bind("change", function (changeEvent) {	            	
	                var reader = new FileReader();
	                reader.onload = function (loadEvent) {
	                    scope.$apply(function () {
	                        scope.fileread = loadEvent.target.result;
	                    });
	                };
	                reader.readAsDataURL(changeEvent.target.files[0]);
	            });
	        }
	    };
	}]);
});*/


/*(function(){
	angular.module("smartOCR",[]).directive("ngFileSelect", function () {
		 return {
			    link: function($scope,el){
			      
			      el.bind("change", function(e){
			      
			        $scope.file = (e.srcElement || e.target).files[0];
			        //$scope.getFile();
			      });
			      
			    }
			    
			  };
	});
})*/



/*angular.module('smartOCR').directive('appFilereader', function($q) {
    var slice = Array.prototype.slice;

    return {
        restrict: 'A',
        require: '?ngModel',
        link: function(scope, element, attrs, ngModel) {
                if (!ngModel) return;

                ngModel.$render = function() {};

                element.bind('change', function(e) {
                    var element = e.target;

                    $q.all(slice.call(element.files, 0).map(readFile))
                        .then(function(values) {
                            if (element.multiple) ngModel.$setViewValue(values);
                            else ngModel.$setViewValue(values.length ? values[0] : null);
                        });

                    function readFile(file) {
                        var deferred = $q.defer();

                        var reader = new FileReader();
                        reader.onload = function(e) {
                            deferred.resolve(e.target.result);
                        };
                        reader.onerror = function(e) {
                            deferred.reject(e);
                        };
                        reader.readAsDataURL(file);

                        return deferred.promise;
                    }

                }); //change

            } //link
    }; //return
});*/