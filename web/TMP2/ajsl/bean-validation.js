'use strict';

angular.module('cbv', []).directive('awdCbv', function($document, $location, $window) {
	var openElement = null, close;
	return {
		restrict : 'CA',
		link : function(scope, element, attrs) {
			scope.$watch(function dropdownTogglePathWatch() {
				return $location.path();
			}, function dropdownTogglePathWatchAction() {
				if (close) {
					close();
				}
			});

			element.parent().bind('click', function(event) {
				if (close) {
					close();
				}
			});

			element.bind('click', function(event) {
				event.preventDefault();
				event.stopPropagation();

				var iWasOpen = false;

				if (openElement) {
					iWasOpen = openElement === element;
					close();
				}

				if (!iWasOpen) {
					element.parent().addClass('open');
					openElement = element;

					close = function(event) {
						if (event) {
							event.preventDefault();
							event.stopPropagation();
						}
						$document.unbind('click', close);
						element.parent().removeClass('open');
						close = null;
						openElement = null;
					};

					$document.bind('click', close);
				}
			});
		}
	};
});

//foodMeApp.directive('fmDeliverTo', function() {
//	return {
//		restrict : 'E',
//		templateUrl : 'js/directives/fmDeliverTo.html',
//		scope : {},
//		controller : function FmDeliverToController($scope, customer) {
//			$scope.customer = customer;
//		}
//	};
//});
