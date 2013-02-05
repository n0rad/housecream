define(['jquery', 'underscore', 'text!./Index.html', 'hcw/service/EventService'],
function($, _, IndexTemplate, EventService) {
	
	
	var eventService = new EventService();	
	
	eventService.registerEventHandler(function(event) {
		noty({layout: 'bottomRight', text: "pointId: " + event.pointId + ", value: " + event.value, timeout: 10000});
	});
//		noty({layout: 'bottomRight', text: error_note, type: 'error'});
//		noty({layout: 'bottomRight', text: success_note, type: 'success'});
//		noty({layout: 'bottomRight', text: information_note, type: 'information'});

	
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
