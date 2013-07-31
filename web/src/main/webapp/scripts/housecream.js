'use strict';

var housecream = angular.module('housecream', [ 'ngResource', 'ui.bootstrap.dropdownToggle', 'ui.bootstrap.tabs' ]);

housecream.constant('hcWsUrl', hcWsUrl);
housecream.constant('hcVersion', hcVersion);

housecream.config(function($routeProvider, $locationProvider) {

	$locationProvider.html5Mode(true);

	$routeProvider.when('/', {
		controller : 'IndexController',
		templateUrl : 'views/index/Index.html'
	}).when('/admin', {
		controller : 'AdminController',
		templateUrl : 'views/admin/Admin.html'
	}).when('/admin/:type', {
		controller : 'AdminController',
		templateUrl : 'views/admin/Admin.html'
	}).when('/admin/:type/:id', {
		controller : 'AdminController',
		templateUrl : 'views/admin/Admin.html'
	}).when('/zone/:zoneId', {
		controller : 'ZoneController',
		templateUrl : 'views/index/Zone.html'
	}).otherwise({redirectTo:'/'});
});
