define([ 'jquery', 'underscore', 'ajsl/event', 'text!./ZoneTable.html' ], function($, _, event, ZoneTable) {

	function Zone(rootUrl, context) {
		this.context = $(context);
		this.rootUrl = rootUrl;
	}

	Zone.prototype = {
		displayTable : function(zones) {
			zones.rootUrl = this.rootUrl;
			this.context.html(_.template(ZoneTable, zones));
//			event.register(this.events, this.context);
			$('.dropdown-toggle', this.context).dropdown();
		}
	};

	return Zone;
});

