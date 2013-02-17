define(['jquery'], 
function($) {
	
	function RuleService(hcwRootUrl) {
		this.rootUrl = hcwRootUrl;
	}
	
	RuleService.prototype = {
			getRules : function(callback) {
				$.getJSON(this.rootUrl + '/ws/rules', {}, function(data) {
					callback(data);
				});				
			},
	
			deleteRule : function(id, successCallback) {
				$.ajax({
					url : this.rootUrl + '/ws/rule/' + id,
					type : 'DELETE',
					error : function(jqXHR, textStatus, errorThrown) {
						//TODO
					},
					success : function(data, textStatus, jqXHR) {
						successCallback();
					}
				});
			}
	};
	
	return RuleService;
});