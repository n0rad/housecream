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
					var form = $(this);
					var formData = $(this).toObject({skipEmpty : false});
					var parentType = findParentType(this);
					var type;
					if (parentType) {
						type = types[types.indexOf(parentType.ucFirst()) + 1];
					} else {
						type = 'land';
					}
					formData.type = type.lcFirst();

					restFormHandler.handleSubmit(this, rootUrl, "/hcs/ws/zone", findValidator(this), formData, function(res) {
						
						
						
						var data = new FormData();
						jQuery.each($('#zoneimg', form)[0].files, function(i, file) {
						    data.append('file-'+i, file);
						});
						
						var self = this; 
						
						progressHandlerFunction = function(e) {
							var loaded = Math.round((e.loaded / e.total) * 100); // on calcul le pourcentage de progression
							var $bar = $('.progress .bar', self);
							
//							if ($bar.width()==400) {
//						        clearInterval(progress);
//						        $('.progress').removeClass('active');
//						    } else {
//						        $bar.width($bar.width()+40);
//						    }
							$bar.width(loaded + '%');
						    $bar.text(loaded + "%");
						};
						
						$.ajax({
						    url: "/hcs/ws/zone/"+ res + "/image",
						    data: data,
						    cache: false,
						    contentType: false,
						    processData: false,
						    type: 'POST',
						    xhr: function() {
						        myXhr = $.ajaxSettings.xhr();
						        if(myXhr.upload){
						            myXhr.upload.addEventListener('progress',progressHandlerFunction, false);
						        }
						        return myXhr;
						    },
						    success: function(data){
								  var url = self.rootUrl + '/admin/zone';
								  History.pushState(null, url, url);
						    }
						});

						
						
						
						
					});
				},
		
				'input[type=text], input[type=hidden], checkbox, select, textarea|keyup,blur,change' : function() {
					restFormHandler.handleFormElementChange(this, findValidator($(this).closest("form")));
				}

			};
	}
	
	ZoneForm.prototype = {
		displayForm : function(data) {
			if (!data) {
				data = {};
			}
			this.context.html(_.template(ZoneTemplate, {rootUrl: this.rootUrl, zone: data}));
			event.register(this.events, this.context);

			this.zoneService.getZones(function(zones) {
				$('.parentId', this.context).bootstrapSelect(zones.zones);
				view.rebuildFormRec(this.context, data);
			});
			
		}
	};

	return ZoneForm;
});