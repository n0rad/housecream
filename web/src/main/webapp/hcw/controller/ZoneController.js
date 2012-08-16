define(['hcw/service/ZoneService', 'hcw/view/index/Zone', 'hcw/view/index/Index', 'hcw/view/index/Action'], 
function(ZoneService, Zone, Index, Action) {
	
	function ZoneController(rootUrl, context) {
		this.rootUrl = rootUrl;
		this.context = $(context);
		this.zoneService = new ZoneService("/hcs");
//		this.zoneTable = new ZoneTable(rootUrl, context);
	}

	ZoneController.prototype = {
			displayZone : function(zoneId) {
				var index = new Index(this.rootUrl, this.context);
				index.displayIndex();

				var zone = new Zone(this.rootUrl, $("#content", this.context));
				zone.displayZone(zoneId);
				
				var action = new Action(this.rootUrl, $("#widget", this.context));
				action.displayActions(zoneId);
			}
	};
	
	return ZoneController;
});
