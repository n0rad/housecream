'use strict';

housecream.controller('InpointTableController',
    function InpointTableController($scope, InPoints) {
	
	$scope.inPoints = InPoints.get();
	
});


//define(['jquery', 'underscore', 'ajsl/event', 'hcw/service/InPointService', 'text!./InPointTable.html', 'text!./InPointTableDeleteForm.html' ],
//function($, _, event, InPointService, InPointTableTemplate, deleteFormTemplate) {
//	
//	var inPointService = new InPointService("/hcs");
//	
//	function InPoinTable(rootUrl, context) {
//		this.context = $(context);
//		this.rootUrl = rootUrl;
//		
//		this.events = {
////				'TABLE.inPointTable TBODY TD:not(.selector, .actions)|click' : function() {
////					History.pushState(null, this.href, this.href);
////				}
//				'BUTTON.delete|click' : function(e) {
//					e.preventDefault();
//					var tr = $(this).closest('TR');
//					var id = tr.find('TD.selector INPUT').val();
//					var name = tr.find('TD.name').html();
//					var form = $('#myModal').html(_.template(deleteFormTemplate, {name : name, id : id})).modal();
//					event.register({
//						'|submit' : function(e) {
//							e.preventDefault();
//							inPointService.deleteInPoint($("input[name=id]", this).val(), function() {
//								tr.remove();
//								form.modal('hide');								
//							});
//						}
//					}, form);
//				}
//			};
//	}
//
//	InPoinTable.prototype = {
//		displayTable : function(inPoints) {
//			inPoints.rootUrl = this.rootUrl;
//			this.context.html(_.template(InPointTableTemplate, inPoints));
//			event.register(this.events, this.context);
//	        $('.dropdown-toggle', this.context).dropdown();
//		}
//	};
//	
//	return InPoinTable;
//});
