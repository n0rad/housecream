define(['jquery', 'underscore', 'text!./Index.html', 'text!./Action.html', 'text!./Zone.html' ],
function($, _, IndexTemplate, ActionTpl, ZoneTpl) {
	
	function Index(rootUrl, context) {
		this.context = $(context);
		this.rootUrl = rootUrl;
	}
	
	Index.prototype = {
			displayIndex : function() {
				this.context.html(IndexTemplate);
				$("#widget", this.context).html(_.template(ActionTpl, {rootUrl : this.rootUrl}));
				$("#content", this.context).html(_.template(ZoneTpl, {rootUrl : this.rootUrl}));
		        $('.dropdown-toggle', this.context).dropdown();
			}
	};
	
	return Index;
	
});
