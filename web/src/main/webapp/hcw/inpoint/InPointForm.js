define(['jquery', 'bootstrapValidator', 'Ajsl/event', 'text!./InPointForm.html' ],
function($, bv, event, InPointTemplate) {

	var validatorInfo = {};
	$.getJSON('/hcs/ws/inpoint/validator.json', {}, function(data) {
		validatorInfo = data;
	});

	function Inpoint(context) {
		this.context = $(context);

		this.events = {
			'|submit' : function() {
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
				return false;					
			},

			'input[type=text], input[type=hidden], checkbox, select, textarea|keyup,blur,change' : function() {
				bv.isValidDisplayedFormElement(this, $(this).toObject({skipEmpty : false}), validatorInfo);
			}
		};
	}

	Inpoint.prototype = {
		displayForm : function() {
			this.context.html(InPointTemplate);
			event.register(this.events, this.context);
		}
	};

	return Inpoint;
});