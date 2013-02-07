'use strict';

housecream.factory('Zone', function($resource, hcWsUrl) {
	var Zone = $resource(hcWsUrl + '/zone/:id', {id : '@id'});	
	return Zone;
});


//define(['jquery'], 
//function($) {
//	
//	function ZoneService(hcwRootUrl) {
//		this.rootUrl = hcwRootUrl;
//	}
//	
//	ZoneService.prototype = {
//			getZones : function(callback) {
//				$.getJSON(this.rootUrl + '/ws/zones', {}, function(data) {
//					callback(data);
//				});				
//			},
//	
//			getZone : function(zoneId, callback) {
//				$.getJSON(this.rootUrl + '/ws/zone/' + zoneId, {}, function(data) {
//					callback(data);
//				});
//			},
//
//			getOutPoints : function(zoneId, callback) {
//				$.getJSON(this.rootUrl + '/ws/zone/' + zoneId + '/outpoints', {zoneId : zoneId}, function(data) {
//					callback(data);
//				});				
//			},
//
//			getInPoints : function(zoneId, callback) {
//				$.getJSON(this.rootUrl + '/ws/zone/' + zoneId + '/inpoints', {zoneId : zoneId}, function(data) {
//					callback(data);
//				});				
//			}
//	};
//	
//	return ZoneService;
//});