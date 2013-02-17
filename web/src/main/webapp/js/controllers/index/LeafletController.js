'use strict';

housecream.controller('LeafletController', function LeafletController($scope, $location, $window) {
    $scope.map = new LeafMap({id: 'map-container', src: 'img/sample_house_plan.jpg'});
});
