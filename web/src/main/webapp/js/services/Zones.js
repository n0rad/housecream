'use strict';

housecream.factory('Zones', function($resource, hcWsUrl) {
	return $resource(hcWsUrl + '/zones/:id', {id: '@id'}, {
		list: {method: 'GET', isArray: true},
		get: {method: 'GET', params: {}},
		save: {method: 'POST', params: {}},
		update: {method: 'PUT', params: {}},
		'delete': {method: 'DELETE', params: {}}
	});	
});
