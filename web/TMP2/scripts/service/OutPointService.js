define(['jquery'], 
function($) {
	
	function OutPointService(hcwRootUrl) {
		this.rootUrl = hcwRootUrl;
	}
	
	OutPointService.prototype = {
			getOutPoint : function(outPointId, callback) {
				$.getJSON(this.rootUrl + '/ws/outpoint/' + outPointId, {}, function(data) {
					callback(data);
				});
			},

			deleteOutPoint : function(id, successCallback) {
				$.ajax({
					url : this.rootUrl + '/ws/outpoint/' + id,
					type : 'DELETE',
					error : function(jqXHR, textStatus, errorThrown) {
						//TODO
					},
					success : function(data, textStatus, jqXHR) {
						successCallback();
					}
				});
			},
			
			getOutPoints : function(callback) {
				$.getJSON(this.rootUrl + '/ws/outpoints', {}, function(data) {
					callback(data);
				});				
			},
			
			setValue : function(id, value, callback) {
				$.ajax({
					url : this.rootUrl + '/ws/outpoint/' + id + '/value',
					type : 'PUT',
					data : value,
					processData: false,
					dataType : 'application/json',
					error : function(jqXHR, textStatus, errorThrown) {
						//TODO
					},
					success : function(data, textStatus, jqXHR) {
						callback();
					}
				});				
			}
						
	};
	
	
	return OutPointService;
});