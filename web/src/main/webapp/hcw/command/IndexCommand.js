define([ 'jquery', 'hcw/view/index/Index' ],
function($, Index) {
	"use strict";

	function IndexCommand(rootUrl) {
		this.Index = new Index(rootUrl, $('#content'));
	}

	IndexCommand.prototype = {
		run : function() {
			this.Index.displayIndex();
		},
	};

	return IndexCommand;
});
