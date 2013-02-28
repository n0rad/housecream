define(['jquery'], 
function($) {
	
	function RuleService(hcwRootUrl) {
		this.rootUrl = hcwRootUrl;
	}
	
	RuleService.prototype = {
			getRule : function(ruleId, callback) {
				$.getJSON(this.rootUrl + '/ws/rule/' + ruleId, {}, function(data) {
					callback(data);
				});
			},

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