'use strict';

housecream.factory('Restaurant', function($resource) {
  return $resource('/api/restaurant/:id', {id: '@id'});
});
