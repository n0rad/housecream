define(['jquery', 'text!./InPointTable.html' ],
function($, InPointTableTemplate) {
	
	function InPoinTable(context) {
		this.context = $(context);
	}

	InPoinTable.prototype = {
		displayTable : function() {
			this.context.html(InPointTableTemplate);
		}
	};
	
	return InPoinTable;
});
