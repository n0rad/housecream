define(['bootstrapValidator'], 
function(bootstrapValidator) {

	zoneParentType = function(obj, attributes) {
		return true;
	};
	bootstrapValidator.getValidator().registerConstraint('net.awired.housecream.server.api.validator.ZoneParentType', zoneParentType);

	return bootstrapValidator;
});
