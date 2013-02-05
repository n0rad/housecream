'use strict';

var foodMeApp = angular.module('foodMeApp', ['ngResource']);

foodMeApp.config(function($routeProvider, $locationProvider) {

  $locationProvider.html5Mode(true);
	
  $routeProvider.
      when(window.contextPath + '/', {
        controller: 'RestaurantsController',
        templateUrl: 'views/restaurants.html'
      }).
      when(window.contextPath + '/menu/:restaurantId', {
        controller: 'MenuController',
        templateUrl: 'views/menu.html'
      }).
      when(window.contextPath + '/checkout', {
        controller: 'CheckoutController',
        templateUrl: 'views/checkout.html'
      }).
      when(window.contextPath + '/thank-you', {
        controller: 'ThankYouController',
        templateUrl: 'views/thank-you.html'
      }).
      when(window.contextPath + '/customer', {
        controller: 'CustomerController',
        templateUrl: 'views/customer.html'
      }).
      when(window.contextPath + '/who-we-are', {
        templateUrl: 'views/who-we-are.html'
      }).
      when(window.contextPath + '/how-it-works', {
        templateUrl: 'views/how-it-works.html'
      }).
      when(window.contextPath + '/help', {
        templateUrl: 'views/help.html'
      });
});
