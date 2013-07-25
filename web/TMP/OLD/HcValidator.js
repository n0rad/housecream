define(['bootstrapValidator'], 
function(bootstrapValidator) {

	noValidation = function(obj, attributes) {
		return true;
	};
	bootstrapValidator.getValidator().registerConstraint('org.housecream.server.api.validator.ZoneParentType', noValidation);
//	bootstrapValidator.getValidator().registerConstraint('fr.norad.persistence.validator.ForeignId', noValidation);
	
	return bootstrapValidator;
});
