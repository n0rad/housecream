'use strict';
housecream.factory('Plugins', function ($resource, hcWsUrl) {
    var plugins = $resource(hcWsUrl + '/channels/available');
    return plugins;
});

