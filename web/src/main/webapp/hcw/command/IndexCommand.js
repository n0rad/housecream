define([ 'jquery',  'hcw/controller/ZoneController'],
function($, ZoneController) {
	"use strict";

	function IndexCommand(rootUrl) {
		this.zoneController = new ZoneController();
	}

	IndexCommand.prototype = {
		run : function() {
			this.zoneController.displayZone();
		},
	};

	return IndexCommand;
});
