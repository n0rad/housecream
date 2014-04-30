'use strict';
housecream.controller('ConfigController', function ($scope, $location, $routeParams, Configs) {
    $scope.config = Configs.query();
});