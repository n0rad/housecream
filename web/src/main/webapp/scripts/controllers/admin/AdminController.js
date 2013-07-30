'use strict';
housecream.controller('AdminController', function AdminController($scope, $routeParams) {
	var type = $routeParams.type;
	var tpl = '';
	if (type && (type == 'inpoint' || type == 'outpoint' || type == 'zone' || type == 'rule')) {
		tpl = 'views/admin/' + $routeParams.type.ucFirst();
		tpl += $routeParams.id ? 'Form.html' :  'Table.html';
	}
	$scope.subTemplate = tpl;
});