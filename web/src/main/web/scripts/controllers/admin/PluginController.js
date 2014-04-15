'use strict';
housecream.controller('PluginController', function InpointFormController($scope, $routeParams, hcWsUrl, Plugins) {
    $scope.hcWsUrl = hcWsUrl;
    $scope.plugin = Plugins.get({id: $routeParams.id});

    $scope.activate = function () {
        Plugins.activate();
    };
});
