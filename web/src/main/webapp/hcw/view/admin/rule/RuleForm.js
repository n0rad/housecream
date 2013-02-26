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
				var formData = $(this).toObject({skipEmpty : true});
				restFormHandler.handleSubmit(this, rootUrl, "/hcs/ws/rule", validatorInfo, formData, function() {
					  var url = self.rootUrl + '/admin/rule';
					  History.pushState(null, url, url);
				});
			},
			'input[type=text], input[type=hidden], checkbox, select, textarea|keyup,blur,change' : function() {
				restFormHandler.handleFormElementChange(this, validatorInfo);
			},
			'.when-add|click' : function() {
				self.addFormConditions();
			},
			'.then-add|click' : function() {
				self.addFormConsequences();
			},
			'.when BUTTON.delete, .then BUTTON.delete |click|live' : function() {
				$(this).parent().remove();
			}
		};
	}

	Rule.prototype = {
			
		addFormConditions : function(i) {
			if (i == 0) return;
			var ul = $('.when', this.context);
			var li = ul.find("li")[0];
			var newLi = $.clone(li);
			$('input, select', newLi).each(function(index, value) {
				$(value).attr('name', $(value).attr('name').replace('0', i));
			});
			$('BUTTON.delete', newLi).show();
			ul.append(newLi);
		},
		addFormConsequences : function(i) {
			if (i == 0) return;
			var ul = $('.then', this.context);
			var li = ul.find("li")[0];
			var newLi = $.clone(li);
			$('input, select', newLi).each(function(index, value) {
				$(value).attr('name', $(value).attr('name').replace('0', i));
			});
			$('BUTTON.delete', newLi).show();
			ul.append(newLi);
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
				$('.when LI:first BUTTON.delete, .then LI:first BUTTON.delete', self.context).hide();
				
				
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