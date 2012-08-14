define([ 'jquery', 'hcw/view/admin/AdminLayout', 'hcw/controller/InPointController', 'hcw/controller/ZoneController' ],
function($, AdminLayout, InPointController, ZoneController) {
	"use strict";
	

	function AdminCommand(rootUrl) {
		var self = this;
		this.adminLayout = new AdminLayout(rootUrl, $('#content'));
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
