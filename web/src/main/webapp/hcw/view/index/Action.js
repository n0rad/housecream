define(['jquery', 'underscore', 'text!./Action.html'],
function($, _, ActionTpl) {
	
	function Action(rootUrl, context) {
		this.rootUrl = rootUrl;
		this.context = $(context);
	}
	
	Action.prototype = {
			displayActions : function(zoneId) {
				this.context.html(_.template(ActionTpl, {rootUrl : this.rootUrl}));
			}
	};
	
	return Action;
});