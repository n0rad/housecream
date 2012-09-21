define(['jquery', 'underscore', 'ajsl/event', 'hcw/service/OutPointService', 'text!./OutPointTable.html', 'text!./OutPointTableDeleteForm.html' ],
function($, _, event, OutPointService, OutPointTableTemplate, deleteFormTemplate) {
	
	var outPointService = new OutPointService("/hcs");
	
	function OutPoinTable(rootUrl, context) {
		this.context = $(context);
		this.rootUrl = rootUrl;
		
		this.events = {
//				'TABLE.inPointTable TBODY TD:not(.selector, .actions)|click' : function() {
//					History.pushState(null, this.href, this.href);
//				}
				'BUTTON.delete|click' : function(e) {
					e.preventDefault();
					var tr = $(this).closest('TR');
					var id = tr.find('TD.selector INPUT').val();
					var name = tr.find('TD.name').html();
					var form = $('#myModal').html(_.template(deleteFormTemplate, {name : name, id : id})).modal();
					event.register({
						'|submit' : function(e) {
							e.preventDefault();
							outPointService.deleteInPoint($("input[name=id]", this).val(), function() {
								tr.remove();
								form.modal('hide');								
							});
						}
					}, form);
				}
			};
	}

	OutPoinTable.prototype = {
		displayTable : function(outPoints) {
			outPoints.rootUrl = this.rootUrl;
			this.context.html(_.template(OutPointTableTemplate, outPoints));
			event.register(this.events, this.context);
	        $('.dropdown-toggle', this.context).dropdown();
		}
	};
	
	return OutPoinTable;
});
