define(['hcw/service/InPointService', 'hcw/view/admin/inpoint/InPointForm', 'hcw/view/admin/inpoint/InPointTable'],
function(InPointService, InPointForm, InPointTable) {
	
	function InPointController(rootUrl, context) {
		this.inPointService = new InPointService("/hcs");
		this.inPointForm = new InPointForm(rootUrl, context);
		this.inPointTable = new InPointTable(rootUrl, context);
	}
	
	InPointController.prototype = {
			displayInPointForm : function(inPointId) {
				var self = this;
				this.inPointService.getInPoint(inPointId, function(data) {
					self.inPointForm.displayForm(data);
				});
			},
			
			displayNewInPointForm : function() {
				this.inPointForm.displayForm();				
			},
	
			displayInPointTable : function() {
				var self = this;
				this.inPointService.getInPoints(function(data) {
					self.inPointTable.displayTable(data);
				});
			}
	};
	
	return InPointController;
});