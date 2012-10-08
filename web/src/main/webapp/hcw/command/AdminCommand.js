define([ 'jquery', 'hcw/view/admin/AdminLayout', 'hcw/controller/AdminInPointController', 'hcw/controller/AdminOutPointController', 'hcw/controller/AdminZoneController' ],
function($, AdminLayout, InPointController, OutPointController, ZoneController) {
	"use strict";
	

	function AdminCommand(rootUrl) {
		var self = this;
		this.adminLayout = new AdminLayout(rootUrl, $('#container'));
		this.rootUrl = rootUrl;

		this.inpoint = function(params, inPointId) {
			self.adminLayout.displayAdmin();
			if (inPointId == 'new') {
				new InPointController(self.rootUrl, $('#current')).displayNewInPointForm();
			} else if (inPointId) {
				new InPointController(self.rootUrl, $('#current')).displayInPointForm(inPointId);
			} else {
				new InPointController(self.rootUrl, $('#current')).displayInPointTable();
			}
		};

		this.outpoint = function(params, outPointId) {
			self.adminLayout.displayAdmin();
			if (outPointId == 'new') {
				new OutPointController(self.rootUrl, $('#current')).displayNewOutPointForm();
			} else if (outPointId) {
				new OutPointController(self.rootUrl, $('#current')).displayOutPointForm(outPointId);
			} else {
				new OutPointController(self.rootUrl, $('#current')).displayOutPointTable();
			}
		};

		
		this.zone = function(params, zoneId) {
			self.adminLayout.displayAdmin();
			if (zoneId == 'new') {
				new ZoneController(self.rootUrl, $('#current')).displayNewForm();
			} else if (zoneId) {
				new ZoneController(self.rootUrl, $('#current')).displayForm(zoneId);
			} else {
				new ZoneController(self.rootUrl, $('#current')).displayTable();				
			}
		};
		
		this.run = function(params) {
			self.adminLayout.displayAdmin();
		};

	}

	return AdminCommand;
});
