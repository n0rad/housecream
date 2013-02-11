'use strict';

housecream.factory('OutPoints', function($resource, hcWsUrl) {
	return $resource(hcWsUrl + '/zones/:id', {id: '@id'}, {
		list: {method: 'GET', isArray: true},
		get: {method: 'GET', params: {}},
		save: {method: 'POST', params: {}},
		update: {method: 'PUT', params: {}},
		'delete': {method: 'DELETE', params: {}}
	});	
});


//define(['jquery'], 
//function($) {
//	
//	function OutPointService(hcwRootUrl) {
//		this.rootUrl = hcwRootUrl;
//	}
//	
//	OutPointService.prototype = {
//			getOutPoint : function(outPointId, callback) {
//				$.getJSON(this.rootUrl + '/ws/outpoint/' + outPointId, {}, function(data) {
//					callback(data);
//				});
//			},
//
//			deleteOutPoint : function(id, successCallback) {
//				$.ajax({
//					url : this.rootUrl + '/ws/outpoint/' + id,
//					type : 'DELETE',
//					error : function(jqXHR, textStatus, errorThrown) {
//						//TODO
//					},
//					success : function(data, textStatus, jqXHR) {
//						successCallback();
//					}
//				});
//			},
//			
//			getOutPoints : function(callback) {
//				$.getJSON(this.rootUrl + '/ws/outpoints', {}, function(data) {
//					callback(data);
//				});				
//			}
//						
//	};
//	
//	
//	return OutPointService;
//});