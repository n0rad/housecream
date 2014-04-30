'use strict';
housecream.factory('Configs', function ($resource, hcWsUrl) {

    var inpoints = $resource(hcWsUrl + '/configs/:propertyName/:propertyValue', {
        propertyName: '@propertyName',
        propertyValue: '@propertyValue'
    }, {
        setValue: {
            method: 'PUT',
            params: {
                elemCtrl: 'value'
            }
        }
    });
    return inpoints;
});