define(['jquery', 'underscore', 'text!./Index.html' ],
function($, _, IndexTemplate) {
	
	function Index(rootUrl, context) {
		this.context = $(context);
		this.rootUrl = rootUrl;
	}
	
	Index.prototype = {
			displayIndex : function() {
				this.context.html(IndexTemplate);
			}
	};
	
	return Index;
	
});
