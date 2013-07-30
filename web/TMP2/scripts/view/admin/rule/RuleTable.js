define(['jquery', 'underscore', 'ajsl/event', 'text!./RuleTable.html', 'text!./RuleTableDeleteForm.html' ],
function($, _, event, RuleTableTemplate, deleteFormTemplate) {

	function RuleTable(rootUrl, context) {
		this.context = $(context);
		this.rootUrl = rootUrl;
		
		this.events = {
//				'TABLE.ruleTable TBODY TD:not(.selector, .actions)|click' : function() {
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
							require(['hcw/service/RuleService'], function(RuleService) {
								new RuleService("/hcs").deleteRule(id, function() {
									tr.remove();
									form.modal('hide');								
								});								
							});
						}
					}, form);
				}
			};
	}

	RuleTable.prototype = {
		displayTable : function(rules) {
			rules.rootUrl = this.rootUrl;
			this.context.html(_.template(RuleTableTemplate, rules));
			event.register(this.events, this.context);
	        $('.dropdown-toggle', this.context).dropdown();
		}
	};
	
	return RuleTable;
});
