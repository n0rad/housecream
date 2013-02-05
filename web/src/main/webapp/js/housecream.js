'use strict';

var housecream = angular.module('housecream', [ 'ngResource' ]);

housecream.constant('hcWsUrl', hcWsUrl);
housecream.constant('hcVersion', hcVersion);

housecream.config(function($routeProvider, $locationProvider) {

	$locationProvider.html5Mode(true);

	$routeProvider.when('/', {
		controller : 'RestaurantsController',
		templateUrl : 'views/restaurants.html'
	}).when('/admin/inpoint', {
		controller : 'InpointTableController',
		templateUrl : 'js/view/admin/inpoint/InpointTable.html'
	}).when('/zone/:zoneId', {
		controller : 'ZoneController',
		templateUrl : 'js/view/index/Zone.html'
	}).when('/menu/:restaurantId', {
		controller : 'MenuController',
		templateUrl : 'views/menu.html'
	}).when('/checkout', {
		controller : 'CheckoutController',
		templateUrl : 'views/checkout.html'
	}).when('/thank-you', {
		controller : 'ThankYouController',
		templateUrl : 'views/thank-you.html'
	}).when('/customer', {
		controller : 'CustomerController',
		templateUrl : 'views/customer.html'
	}).when('/who-we-are', {
		templateUrl : 'views/who-we-are.html'
	}).when('/how-it-works', {
		templateUrl : 'views/how-it-works.html'
	}).when('/help', {
		templateUrl : 'views/help.html'
	});
});
