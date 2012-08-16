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
			$('.dropdown-toggle', this.context).dropdown();

			
			var display = function(strings, data, currentParent) {
				var flag = false;
				for (var i = 0; i < data.length; i++) {
					if ((!currentParent && !data[i].parentId) || (currentParent && data[i].parentId == currentParent.id)) {
						if (!flag && currentParent) {
							strings.push('<ul class="nav nav-list">');
							flag = true;
						}
						strings.push('<li><a href="#" data-id="' + data[i].id + '"><i class="icon-pencil"></i> ' + data[i].name + '</a></li>');
						display(strings, data, data[i]);
					}
				}
				if (flag) {
					strings.push('</ul>');
				}
			};
			
			var selectGroup = $('.parentId', this.context);
			var select = $("SELECT", selectGroup);
			this.zoneService.getZones(function(zones) {
				for (var i = 0; i < zones.zones.length; i++) {
					select.append('<option value="' + zones.zones[i].id + '">' + zones.zones[i].name + '</option>');
				}

				var ul = $("UL", selectGroup);

				var res = [];
				display(res, zones.zones);
				ul.append(res.join(""));
				$('A', ul).bind('click', function(e) {
					var childs = $('A.btn', selectGroup).children();
					$('A.btn', selectGroup).text($(this).text()).append(childs);
					
					$('LI', selectGroup).removeClass('active');
					$(this).parent().addClass('active');
					e.preventDefault();
					select.val($(this).data("id"));
				});

				select.bind('change', function() {
					$('LI', selectGroup).removeClass('active');					
					var current = $('A:data(id=' + this.value + ')', selectGroup);
					current.parent().addClass('active');

					var childs = $('A.btn', selectGroup).children();
					$('A.btn', selectGroup).text(current.text()).append(childs);
				});

				
				view.rebuildFormRec(this.context, data);

				
			});
			
		}
	};

	return ZoneForm;
});