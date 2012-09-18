define(['Validator'], 
function(Validator) {
	
	var validator = new Validator();
	
	notEmpty = function(obj, attributes) {
		return obj.length > 0;
	};
	validator.registerConstraint('net.awired.client.bean.validation.js.constraint.EnumNotEmpty', notEmpty);


	return {
		
		getValidator : function() {
			return validator;
		},
		
		isValidDisplayedForm : function(form, formData, validatorInfo) {
			if (!validatorInfo) {
				return true;
			}
			this.clearViolations(form);
			var violations = validator.validate(formData, validatorInfo.properties);
			this.displayViolations(validator, validatorInfo, violations, form);
			return violations.length == 0;
		},
		
		isValidDisplayedFormElement : function(formElem, elemData, validatorInfo) {
			this.clearViolation(formElem);
			if (!validatorInfo) {
				return true;
			}
			var violations = validator.validateValue(elemData, validatorInfo.properties, formElem.name);
			this.displayViolations(validator, validatorInfo, violations, formElem.form);
			return violations.length == 0;
		},
		
		displayViolations : function(validator, validatorInfo, violations, form) {
			for ( var i = 0; i < violations.length; i++) {
				var e2 = $('[name="' + violations[i].propertyPath + '"]', form);
				var e = e2.closest('.control-group');
				e.addClass('error');
				$('.help-inline', e).html(validator.interpolate(validatorInfo, violations[i]));
			}
		},
		
		clearViolations : function(form) {
			$('.control-group.error', form).removeClass('error').find('.help-inline').html('');
		},
		
		clearViolation : function(element) {
			$(element).closest('.control-group').removeClass('error').find('.help-inline').html('');
		}
	};
});