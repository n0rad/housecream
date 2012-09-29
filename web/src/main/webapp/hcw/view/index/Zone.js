define(['jquery', 'underscore', 'text!./Zone.html', 'hcw/service/ZoneService', 'hcw/service/EventService'],
function($, _, ZoneTpl, ZoneService) {
	
	
	var zoneService = new ZoneService('/hcs');
	
	function Zone(rootUrl, context) {
		this.rootUrl = rootUrl;
		this.context = $(context);
	}
	
	function buildZoneRec(rootUrl, strings, zones, selectedId, currentParent) {
		var flag = false;
		for (var i = 0; i < zones.length; i++) {
			if ((!currentParent && !zones[i].parentId) || (currentParent && zones[i].parentId == currentParent.id)) {
				if (!flag) {
					strings.push('<li class="dropdown" data-type="' + zones[i].type.ucFirst() + '"><a class="dropdown-toggle" data-toggle="dropdown" href="#tab3">'
							+ zones[i].type.ucFirst() +'<b class="caret"></b></a><ul class="dropdown-menu">');
					flag = true;
				}
				strings.push('<li class="' + (selectedId == zones[i].id ? 'active' : '') + '"><a class="pushState" href="' + rootUrl + '/zone/' + zones[i].id + '">' + zones[i].name + '</a></li>');
			}
		}
		if (flag) {
			strings.push('</ul></li>');
		}

		for (var i = 0; i < zones.length; i++) {
			if ((!currentParent && !zones[i].parentId) || (currentParent && zones[i].parentId == currentParent.id)) {
				buildZoneRec(rootUrl, strings, zones, selectedId, zones[i]);
			}
		}

	}
	
	Zone.prototype = {
			displayZone : function(zoneId) {
				var self = this;
				this.context.html(_.template(ZoneTpl, {rootUrl : this.rootUrl}));
				zoneService.getZones(function(zones) {
					var res = [];
					buildZoneRec(self.rootUrl, res, zones.zones, zoneId);
					$('.zoneNav', self.context).html(res.join(''));
					$('.dropdown-toggle', self.context).dropdown();		
				});
			},
	
			updateZone : function() {
				
			}
	};
	
	return Zone;
	
});
