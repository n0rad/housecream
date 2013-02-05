define(['jquery'], 
function($) {
	
	function InPointService(hcwRootUrl) {
		this.rootUrl = hcwRootUrl;
	}
	
	InPointService.prototype = {
			getInPoint : function(inPointId, callback) {
				$.getJSON(this.rootUrl + '/ws/inpoint/' + inPointId, {}, function(data) {
					callback(data);
				});
			},

			deleteInPoint : function(id, successCallback) {
				$.ajax({
					url : this.rootUrl + '/ws/inpoint/' + id,
					type : 'DELETE',
					error : function(jqXHR, textStatus, errorThrown) {
						//TODO
					},
					success : function(data, textStatus, jqXHR) {
						successCallback();
					}
				});
			},
			
			getInPoints : function(callback) {
				$.getJSON(this.rootUrl + '/ws/inpoints', {}, function(data) {
					callback(data);
				});				
			}
						
	};
	
	
	return InPointService;
});