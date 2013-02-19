define(['jquery', 'restFormHandler', 'ajsl/view', 'ajsl/event', 'text!./RuleForm.html', 'hcw/service/RuleService' ],
function($, restFormHandler, view, event, RuleTemplate, RuleService) {
	
	var validatorInfo = {};
	$.getJSON('/hcs/ws/rule/validator.json', {}, function(data) {
		validatorInfo = data;
	});
	
	function Rule(rootUrl, context) {
		var self = this;
		this.context = $(context);
		this.ruleService = new RuleService("/hcs");
		this.rootUrl = rootUrl;

		this.events = {
			'|submit' : function(e) {
				e.preventDefault();
				var formData = $(this).toObject({skipEmpty : false});
				restFormHandler.handleSubmit(this, rootUrl, "/hcs/ws/rule", validatorInfo, formData, function() {
					  var url = self.rootUrl + '/admin/rule';
					  History.pushState(null, url, url);
				});
			},

			'input[type=text], input[type=hidden], checkbox, select, textarea|keyup,blur,change' : function() {
				restFormHandler.handleFormElementChange(this, validatorInfo);
			}
		};
	}

	Rule.prototype = {
			
		addFormConditions : function(i) {
			var ul = $('.when', this.context);
			var li = ul.find("li")[0];
			var newLi = $.clone(li);
			ul.append(newLi);
		},
		addFormConsequences : function(i) {
			
		},
		displayForm : function(data) {
			var self = this;
			$.getJSON('/hcs/ws/inpoints/.json', {}, function(inpoints) { //TODO use when.js
				$.getJSON('/hcs/ws/outpoints/.json', {}, function(outpoints) { //TODO use when.js
				var tplData = {rootUrl : self.rootUrl,
						       inpoints : inpoints.inPoints,
						       outpoints : outpoints.outPoints};
				self.context.html(_.template(RuleTemplate, tplData));
				
				view.rebuildFormRec(self.context, data, self);
				event.register(self.events, self.context);
//				self.ruleService.getRules(function(rules) {
//					$('.ruleId', self.context).bootstrapSelect(rules.rules);
//					view.rebuildFormRec(self.context, data);
//				});
				});
			});
		}
	};

	return Rule;
});