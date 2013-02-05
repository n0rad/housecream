define(['bootstrapValidator'], 
function(bootstrapValidator) {

	noValidation = function(obj, attributes) {
		return true;
	};
	bootstrapValidator.getValidator().registerConstraint('net.awired.housecream.server.api.validator.ZoneParentType', noValidation);
	bootstrapValidator.getValidator().registerConstraint('net.awired.ajsl.persistence.validator.ForeignId', noValidation);
	
	return bootstrapValidator;
});
