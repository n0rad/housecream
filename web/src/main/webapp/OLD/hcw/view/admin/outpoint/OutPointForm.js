define(['jquery', 'restFormHandler', 'ajsl/view', 'ajsl/event', 'text!./OutPointForm.html', 'hcw/service/ZoneService' ],
function($, restFormHandler, view, event, OutPointTemplate, ZoneService) {
	
	var validatorInfo = {};
	$.getJSON('/hcs/ws/outpoint/validator.json', {}, function(data) {
		validatorInfo = data;
	});

	function Outpoint(rootUrl, context) {
		var self = this;
		this.context = $(context);
		this.zoneService = new ZoneService("/hcs");
		this.rootUrl = rootUrl;

		this.events = {
			'|submit' : function(e) {
				e.preventDefault();
				var formData = $(this).toObject({skipEmpty : false});
				restFormHandler.handleSubmit(this, rootUrl, "/hcs/ws/outpoint", validatorInfo, formData, function() {
					  var url = self.rootUrl + '/admin/outpoint';
					  History.pushState(null, url, url);
				});
			},

			'input[type=text], input[type=hidden], checkbox, select, textarea|keyup,blur,change' : function() {
				restFormHandler.handleFormElementChange(this, validatorInfo);
			}
		};
	}

	Outpoint.prototype = {
		displayForm : function(data) {
			var self = this;
			$.getJSON('/hcs/ws/outpoints/types.json', {}, function(outPointTypes) { //TODO use when.js
				var tplData = {rootUrl : self.rootUrl, types : outPointTypes};				
				self.context.html(_.template(OutPointTemplate, tplData));
				
				view.rebuildFormRec(self.context, data);
				event.register(self.events, self.context);
				self.zoneService.getZones(function(zones) {
					$('.zoneId', self.context).bootstrapSelect(zones.zones);
					view.rebuildFormRec(self.context, data);
				});
			});
		}
	};

	return Outpoint;
});