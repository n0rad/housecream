define([ 'jquery', 'hcw/controller/ZoneController' ], function($, ZoneController) {
	"use strict";

	function ZoneCommand(rootUrl) {

		this.run = function(args, zoneId) {
			new ZoneController(rootUrl, $('#content')).displayZone(zoneId);
		};

	}

	return ZoneCommand;
});
