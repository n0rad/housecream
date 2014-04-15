'use strict';
housecream.factory('Plugins', function ($resource, hcWsUrl, webPath) {
    var plugins = $resource(hcWsUrl + '/channels/available/:id/:elemCtrl', {
        id: '@id',
        listCtrl: '@listCtrl',
        elemCtrl: '@elemCtrl'
    }, {
        activate: {
            method: 'POST',
            params: {
                id: 'gmail',
                elemCtrl: 'activate',
                webPath: webPath
            }
        },

        available: {
            method: 'GET',
            isArray: true
        }
    });
    return plugins;
});

