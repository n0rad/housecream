'use strict';

housecream.controller('ThankYouController', function ThankYouController($scope, $routeParams) {
  $scope.orderId = $routeParams.orderId;
});
