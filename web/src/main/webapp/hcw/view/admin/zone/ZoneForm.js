define(['jquery', 'HcValidator', 'ajsl/view', 'ajsl/event', 'hcw/service/ZoneService', 'text!./ZoneForm.html' ],
function($, bv, view, event, ZoneService, ZoneTemplate) {

	
	var types = ['Land', 'Building', 'Floor', 'Room', 'Area'];
	var validatorsInfoByParent = {};
	$.getJSON('/hcs/ws/zone/validator.json', {}, function(data) {
		for (var type in data) {
			   var obj = data[type];
			   var parent = types[types.indexOf(type) - 1];
			   validatorsInfoByParent[parent] = obj;
			}
	});
	function findValidator(form) {
		var parentType = form.find("Select[name=parentId]").find("option:selected").data('type');	
		if (!parentType){
			return validatorsInfoByParent['Land'];
		}
		return validatorsInfoByParent[parentType.ucFirst()];
	}
	
	function ZoneForm(rootUrl, context) {
		this.context = $(context);
		this.zoneService = new ZoneService("/hcs");
		this.rootUrl = rootUrl;
		
		this.events = {
				'|submit' : function(e) {
					e.preventDefault();
					var formData = $(this).toObject({skipEmpty : false});
					if (bv.isValidDisplayedForm(this, formData, findValidator($(this)))) {
						$.ajax({
							  url: "/hcs/ws/zone",
							  headers: { 
							        Accept : "application/json; charset=utf-8",
							        "Content-Type": "application/json; charset=utf-8"
							  },
							  type: "PUT", 
//							  dataType: 'json',
							  data: JSON.stringify(formData),
							  success: function(res) {
								  var dd;
							  },
							  error : function(jqXHR, textStatus, errorThrown) {
								  var ss;
							  }
							});
					}
				},
		
				'input[type=text], input[type=hidden], checkbox, select, textarea|keyup,blur,change' : function() {
					bv.isValidDisplayedFormElement(this, $(this).toObject({skipEmpty : false}), findValidator($(this).closest("form")));
				}

			};
	}
	
	ZoneForm.prototype = {
		displayForm : function(data) {
			this.context.html(_.template(ZoneTemplate, {rootUrl: this.rootUrl}));
//			view.rebuildFormRec(this.context, data);
			event.register(this.events, this.context);

			this.zoneService.getZones(function(zones) {
				$('.parentId', this.context).bootstrapSelect(zones.zones);
				view.rebuildFormRec(this.context, data);
			});
			
		}
	};

	return ZoneForm;
});