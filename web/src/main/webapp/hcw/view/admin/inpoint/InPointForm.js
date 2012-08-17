define(['jquery', 'bootstrapValidator', 'ajsl/view', 'ajsl/event', 'text!./InPointForm.html', 'hcw/service/ZoneService' ],
function($, bv, view, event, InPointTemplate, ZoneService) {
	
	var validatorInfo = {};
	$.getJSON('/hcs/ws/inpoint/validator.json', {}, function(data) {
		validatorInfo = data;
	});

	function Inpoint(rootUrl, context) {
		this.context = $(context);
		this.zoneService = new ZoneService("/hcs");
		this.rootUrl = rootUrl;

		this.events = {
			'|submit' : function(e) {
				e.preventDefault();
				var formData = $(this).toObject({skipEmpty : false});
				if (bv.isValidDisplayedForm(this, formData, validatorInfo)) {
					$.ajax({
						  url: "/hcs/ws/inpoint",
						  contentType: "application/json; charset=utf-8",
						  type: "PUT", 
//						  dataType: 'json',
						  data: JSON.stringify(formData),
						  success: function(res) {
							  var dd;
						  },
						  failure: function(res) {
							  var ss;
						  }
						});
				}
			},

			'input[type=text], input[type=hidden], checkbox, select, textarea|keyup,blur,change' : function() {
				bv.isValidDisplayedFormElement(this, $(this).toObject({skipEmpty : false}), validatorInfo);
			}
		};
	}

	Inpoint.prototype = {
		displayForm : function(data) {
			this.context.html(_.template(InPointTemplate, {rootUrl : this.rootUrl}));
			
			view.rebuildFormRec(this.context, data);
			event.register(this.events, this.context);
			this.zoneService.getZones(function(zones) {
				$('.zoneId', this.context).bootstrapSelect(zones.zones);
				view.rebuildFormRec(this.context, data);
			});
		}
	};

	return Inpoint;
});