define(['hcw/service/ZoneService', 'hcw/view/admin/zone/ZoneForm', 'hcw/view/admin/zone/ZoneTable'], 
function(ZoneService, ZoneForm, ZoneTable) {
	
	function ZoneController(rootUrl, context) {
		this.zoneService = new ZoneService("/hcs");
		this.zoneForm = new ZoneForm(context);
		this.zoneTable = new ZoneTable(rootUrl, context);
	}
	
	ZoneController.prototype = {
			displayForm : function(zoneId) {
				var self = this;
				this.zoneService.getZone(zoneId, function(data) {
					self.zoneForm.displayForm(data);
				});
			},
			
			displayNewForm : function() {
				this.zoneForm.displayForm();
			},
	
			displayTable : function() {
				var self = this;
				this.zoneService.getZones(function(data) {
					self.zoneTable.displayTable(data);
				});
			}
	};
	
	return ZoneController;
});
