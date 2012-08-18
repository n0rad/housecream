define(['jquery', 'underscore', 'text!./Action.html', 'hcw/service/ZoneService'],
function($, _, ActionTpl, ZoneService) {
	
	var zoneService = new ZoneService('/hcs');
	
	function Action(rootUrl, context) {
		this.rootUrl = rootUrl;
		this.context = $(context);
	}
	
	Action.prototype = {
		displayActions : function(zoneId) {
			var self = this;
			zoneService.getInPoints(zoneId, function(inPoints) {
				self.context.html(_.template(ActionTpl, {rootUrl : self.rootUrl, inPoints : inPoints}));
			});
		}
	};
	
	return Action;
});