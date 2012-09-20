define(['jquery', 'restFormHandler', 'ajsl/view', 'ajsl/event', 'hcw/service/ZoneService', 'text!./ZoneForm.html' ],
function($, restFormHandler, view, event, ZoneService, ZoneTemplate) {
	
	var types = ['Land', 'Building', 'Floor', 'Room', 'Area'];
	var validatorsInfoByParent = {};
	$.getJSON('/hcs/ws/zone/validator.json', {}, function(data) {
		for (var type in data) {
			   var obj = data[type];
			   var parent = types[types.indexOf(type) - 1];
			   validatorsInfoByParent[parent] = obj;
			}
	});
	function findParentType(form) {
		return $(form).find("Select[name=parentId]").find("option:selected").data('type');			
	}
	
	function findValidator(form) {
		var parentType = findParentType(form);
		if (!parentType){
			return validatorsInfoByParent['Land'];
		}
		return validatorsInfoByParent[parentType.ucFirst()];
	}

	function ZoneForm(rootUrl, context) {
		var self = this;
		this.context = $(context);
		this.zoneService = new ZoneService("/hcs");
		this.rootUrl = rootUrl;
		
		this.events = {
				'|submit' : function(e) {
					e.preventDefault();
					var formData = $(this).toObject({skipEmpty : false});
					var parentType = findParentType(this);
					var type;
					if (parentType) {
						type = types[types.indexOf(parentType.ucFirst()) + 1];
					} else {
						type = 'land';
					}
					formData.type = type.lcFirst();

					restFormHandler.handleSubmit(this, rootUrl, "/hcs/ws/zone", findValidator(this), formData, function() {
						  var url = self.rootUrl + '/admin/zone';
						  History.pushState(null, url, url);
					});
				},
		
				'input[type=text], input[type=hidden], checkbox, select, textarea|keyup,blur,change' : function() {
					restFormHandler.handleFormElementChange(this, findValidator($(this).closest("form")));
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