'use strict';
housecream.controller('InpointFormController', function InpointFormController($scope, $location, $routeParams, InPoints, Zones) {
	var self = this;
	
	Zones.get(function(data) {
		$scope.zones = data.zones;
	});
	
//	InPoints.getTypes(function(types) {
//		$scope.inPointTypes = types;
//	});

	if ($routeParams.id != 'new') {
		InPoints.get({id: $routeParams.id}, function(inPoint) {
		    self.original = inPoint;
		    $scope.inPoint = new InPoints(self.original);
		});
		$scope.actionLabel = 'update';
	} else {
		$scope.actionLabel = 'create';
	}

	$scope.isClean = function() {
		return angular.equals(self.original, $scope.inPoint);
	};
	
	$scope.create = function() {
		InPoints.save($scope.inPoint, function(inPoint) {
		      $location.path('admin/inpoint');
		    });
	};

	$scope.update = function() {
		InPoints.update($scope.inPoint, function(inPoint) {
		      $location.path('admin/inpoint');
		    });
	};
	
	$scope.destroy = function() {
	    self.original.$delete(function() {
	      $location.path('admin/inpoint');
	    });
	};
});


//housecream.controller('InpointFormNewController', function InpointFormNewController($scope, $location, InPoint, InPoints) {
//
//	InPoints.getTypes(function(types) {
//		$scope.inPointTypes = types;
//	});
//
//	
//	$scope.save = function() {
//		InPoint.save($scope.inPoint, function(inPoint) {
//			$location.path('admin/inpoint');
//		});
//	};
//});





// define(['jquery', 'restFormHandler', 'ajsl/view', 'ajsl/event',
// 'text!./InPointForm.html', 'hcw/service/ZoneService' ],
//function($, restFormHandler, view, event, InPointTemplate, ZoneService) {
//	
//	var validatorInfo = {};
//	$.getJSON('/hcs/ws/inpoint/validator.json', {}, function(data) {
//		validatorInfo = data;
//	});
//	
//	function Inpoint(rootUrl, context) {
//		var self = this;
//		this.context = $(context);
//		this.zoneService = new ZoneService("/hcs");
//		this.rootUrl = rootUrl;
//
//		this.events = {
//			'|submit' : function(e) {
//				e.preventDefault();
//				var formData = $(this).toObject({skipEmpty : false});
//				restFormHandler.handleSubmit(this, rootUrl, "/hcs/ws/inpoint", validatorInfo, formData, function() {
//					  var url = self.rootUrl + '/admin/inpoint';
//					  History.pushState(null, url, url);
//				});
//			},
//
//			'input[type=text], input[type=hidden], checkbox, select, textarea|keyup,blur,change' : function() {
//				restFormHandler.handleFormElementChange(this, validatorInfo);
//			}
//		};
//	}
//
//	Inpoint.prototype = {
//		displayForm : function(data) {
//			var self = this;
//			$.getJSON('/hcs/ws/inpoints/types.json', {}, function(inPointTypes) { //TODO use when.js
//				var tplData = {rootUrl : self.rootUrl, types : inPointTypes};
//				self.context.html(_.template(InPointTemplate, tplData));
//				
//				view.rebuildFormRec(self.context, data);
//				event.register(self.events, self.context);
//				self.zoneService.getZones(function(zones) {
//					$('.zoneId', self.context).bootstrapSelect(zones.zones);
//					view.rebuildFormRec(self.context, data);
//				});
//			});
//		}
//	};
//
//	return Inpoint;
//});