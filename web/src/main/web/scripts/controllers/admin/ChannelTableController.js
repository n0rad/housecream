'use strict';
housecream.controller('ChannelTableController', function InpointFormController($scope, hcWsUrl, Plugins) {
    $scope.hcWsUrl = hcWsUrl;
    $scope.plugins = Plugins.available();
});
