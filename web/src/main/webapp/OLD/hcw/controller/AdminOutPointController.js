define(['hcw/service/OutPointService', 'hcw/view/admin/outpoint/OutPointForm', 'hcw/view/admin/outpoint/OutPointTable'],
function(OutPointService, OutPointForm, OutPointTable) {
	
	function OutPointController(rootUrl, context) {
		this.outPointService = new OutPointService("/hcs");
		this.outPointForm = new OutPointForm(rootUrl, context);
		this.outPointTable = new OutPointTable(rootUrl, context);
	}
	
	OutPointController.prototype = {
			displayOutPointForm : function(outPointId) {
				var self = this;
				this.outPointService.getOutPoint(outPointId, function(data) {
					self.outPointForm.displayForm(data);
				});
			},
			
			displayNewOutPointForm : function() {
				this.outPointForm.displayForm();				
			},
	
			displayOutPointTable : function() {
				var self = this;
				this.outPointService.getOutPoints(function(data) {
					self.outPointTable.displayTable(data);
				});
			}
	};
	
	return OutPointController;
});