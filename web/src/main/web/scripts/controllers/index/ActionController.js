define(['jquery', 'underscore', 'ajsl/event', 'text!./Action.html', 'hcw/service/ZoneService', 'hcw/service/OutPointService'],
function($, _, event, ActionTpl, ZoneService, OutPointService) {
	
	var zoneService = new ZoneService('/hcs');
	var outPointService = new OutPointService('/hcs');
	
	function Action(rootUrl, context) {
		this.rootUrl = rootUrl;
		this.context = $(context);
		
		this.events = {
				'#outPoints BUTTON|click' : function(e) {
					var val = $(this).data('val');
					var newVal = val;
					if (val == undefined || val == 0) {
						newVal = "1";
					} else {
						newVal = "0";
					}
					outPointService.setValue($(this).data('id'), newVal, function() {});
				}
		};
	}
	
	Action.prototype = {
		displayActions : function(zoneId) {
			var self = this;

			zoneService.getOutPoints(zoneId, function(outPoints) { //TODO use when
				zoneService.getInPoints(zoneId, function(inPoints) {
					var tplVars = {rootUrl : self.rootUrl, inPoints : inPoints, outPoints : outPoints};
					self.context.html(_.template(ActionTpl, tplVars));
					$('#outPoints BUTTON', self.context).each(function(index, elem) {
						var val = $(elem).data('val');
						if (val == 1) {
							$('IMG.green', elem).show();
						} else if (val == 0) {
							$('IMG.grey', elem).show();
						}
					});
					event.register(self.events, self.context);
				});
			});
		}
	};
	
	return Action;
});
