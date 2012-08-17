define(['jquery', 'bootstrapValidator', 'ajsl/view', 'ajsl/event', 'hcw/service/ZoneService', 'text!./ZoneForm.html' ],
function($, bv, view, event, ZoneService, ZoneTemplate) {
	
	function ZoneForm(rootUrl, context) {
		this.context = $(context);
		this.zoneService = new ZoneService("/hcs");
		this.rootUrl = rootUrl;
		
		this.events = {
				'|submit' : function(e) {
					e.preventDefault();
				
					var data = new FormData();
					jQuery.each($('#fileInput', this)[0].files, function(i, file) {
					    data.append('file-'+i, file);
					});
					
					var self = this; 
					
					progressHandlerFunction = function(e) {
						var loaded = Math.round((e.loaded / e.total) * 100); // on calcul le pourcentage de progression
						var $bar = $('.progress .bar', self);
						
//						if ($bar.width()==400) {
//					        clearInterval(progress);
//					        $('.progress').removeClass('active');
//					    } else {
//					        $bar.width($bar.width()+40);
//					    }
						$bar.width(loaded + '%');
					    $bar.text(loaded + "%");
					};
					
					$.ajax({
					    url: "/hcs/ws/zone/test",
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
//					        alert(data);
					    }
					});
					
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