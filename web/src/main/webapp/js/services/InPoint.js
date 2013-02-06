'use strict';

housecream.factory('InPoint', function($resource, hcWsUrl) {
	var InPoint = $resource(hcWsUrl + '/inpoint/:id', {id : '@id'}, {update: { method: 'PUT' }});
	
	InPoint.prototype.update = function(cb) {
        return InPoint.update({id: this.id},
            angular.extend({}, this, {_id:undefined}), cb);
	};
	
	InPoint.prototype.destroy = function(cb) {
	   return InPoint.remove({id: this.id}, cb);
	};
	
	return InPoint;
});
