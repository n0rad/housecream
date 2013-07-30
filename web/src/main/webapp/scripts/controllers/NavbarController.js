'use strict';

housecream.controller('NavbarController', function NavbarController($scope, $location, $window) {

	$scope.routeIs = function(routeName) {
		return $location.path() === routeName;
	};

});
