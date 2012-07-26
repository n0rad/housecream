define([ 'jquery', 'hcw/inpoint/InPointForm', 'hcw/inpoint/InPointTable'],
function($, InpointForm, InpointTable) {

	function Hcw() {

	}

	Hcw.prototype = {
		run : function() {

				var context = $("<div></div>");
				$('BODY').append(context);
				var t = new InpointTable(context);
//				t.displayTable();

			
				var context = $("<div></div>");
				$('BODY').append(context);
				var t = new InpointForm(context);
//				t.displayForm();
		
		}
	};

	return Hcw;
});
