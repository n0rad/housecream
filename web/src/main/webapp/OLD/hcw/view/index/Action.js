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

			zoneService.getOutPoints(zoneId, function(outPoints) { //TODO use when
				zoneService.getInPoints(zoneId, function(inPoints) {
					var tplVars = {rootUrl : self.rootUrl, inPoints : inPoints, outPoints : outPoints};
					self.context.html(_.template(ActionTpl, tplVars));
				});
			});
		}
	};
	
	return Action;
});