define([ 'jquery', 'ajsl/Dispatcher' ],
//		'hcw/inpoint/InPointForm', 'hcw/inpoint/InPointTable'
//		InpointForm, InpointTable
function($, Dispatcher) {

	function Hcw() {
	}

	Hcw.prototype = {
		run : function(data) {
			
			$(function() {
				$("#topNav").slideDown(400);				
			});

			$("A.pushState").live("click", function(e) {
				e.preventDefault();
				History.pushState(null, this.href, this.href);
			});
			
			var dispatcher = new Dispatcher({modules : 'hcw/command', mainModuleMain: 'Index', commandParam : data.root});

			var _firstLoad = true;
			window.onpopstate = function(event) {
	            if (_firstLoad) {
	                _firstLoad = false;
	            } else {
	    			var url = location.href.substr(data.root.length + 1);
	            	dispatcher.dispatch(url);
	            }
			};
			setTimeout(function () { _firstLoad = false; dispatcher.dispatch(location.href.substr(data.root.length + 1)); }, 0);

//			$.history.init(dispatcher.dispatch, {unescape : true});

//				var context = $("<div></div>");
//				$('BODY').append(context);
//				var t = new InpointTable(context);
////				t.displayTable();
//
//			
//				var context = $("<div></div>");
//				$('BODY').append(context);
//				var t = new InpointForm(context);
////				t.displayForm();
		
		}
	};

	return Hcw;
});

