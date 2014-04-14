'use strict';
housecream.factory('InPoints', function($resource, hcWsUrl) {
  // var res = $resource(hcWsUrl + '/inpoints/:id', {
  // id : '@id'
  // }, {list : {method : 'GET',isArray : true},
  // get : {method : 'GET',params : {}},
  // save : {method : 'POST',params : {}},
  // update : {method : 'PUT',params : {}},
  // 'delete' : {method : 'DELETE',params : {}}});
  // // res.validator = res.query.bind($resource(hcWsUrl +
  // // '/inpoints/validator'));
  // return res;

  var inpoints = $resource(hcWsUrl + '/inpoints/:listCtrl:id/:elemCtrl', {
    id : '@id',
    listCtrl : '@listCtrl',
    elemCtrl : '@elemCtrl'
  }, {
    validator : {
      method : 'GET',
      params : {
        listCtrl : 'validator'
      }
    },
    value : {
      method : 'GET',
      params : {
        elemCtrl : 'value'
      }
    }
  });
  return inpoints;
});

// GET http://.../api.cfm/messages
// POST http://.../api.cfm/messages/clear-all
// GET http://.../api.cfm/messages/4
// POST http://.../api.cfm/messages/8/archive

// messages.query();
// messages.clear();
// messages.get({id : 4});
// messages.archive({id : 8});
// "./api.cfm/messages/:listController:id/:docController"

// define(['jquery'],
// function($) {
//	
// function InPointService(hcwRootUrl) {
// this.rootUrl = hcwRootUrl;
// }
//	
// InPointService.prototype = {
// getInPoint : function(inPointId, callback) {
// $.getJSON(this.rootUrl + '/ws/inpoint/' + inPointId, {}, function(data) {
// callback(data);
// });
// },
//
// deleteInPoint : function(id, successCallback) {
// $.ajax({
// url : this.rootUrl + '/ws/inpoint/' + id,
// type : 'DELETE',
// error : function(jqXHR, textStatus, errorThrown) {
// //TODO
// },
// success : function(data, textStatus, jqXHR) {
// successCallback();
// }
// });
// },
//			
// getInPoints : function(callback) {
// $.getJSON(this.rootUrl + '/ws/inpoints', {}, function(data) {
// callback(data);
// });
// }
//						
// };
//	
//	
// return InPointService;
// });
