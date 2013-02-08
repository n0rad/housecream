'use strict';

housecream.controller('ZoneTableController', function ZoneTableController($scope, Zones) {
	$scope.zones = Zones.get();
});
