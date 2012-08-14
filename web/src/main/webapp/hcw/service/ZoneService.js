define(['jquery'], 
function($) {
	
	function ZoneService(hcwRootUrl) {
		this.rootUrl = hcwRootUrl;
	}
	
	ZoneService.prototype = {
			getZones : function(callback) {
				$.getJSON(this.rootUrl + '/ws/zones', {}, function(data) {
					callback(data);
				});				
			},
	
			getZone : function(zoneId, callback) {
				$.getJSON(this.rootUrl + '/ws/zone/' + zoneId, {}, function(data) {
					callback(data);
				});
			}
	};
	
	return ZoneService;
});