/*! grafana - v4.2.0 - 2017-03-22
 * Copyright (c) 2017 Torkel Ödegaard; Licensed Apache-2.0 */

System.register(["lodash","../core_module"],function(a,b){"use strict";function c(){return{restrict:"A",require:"ngModel",link:function(a,b,c,e){function f(a){return(a||"").split(",")}function g(a){return d.default.isArray(a)?(a||"").join(","):a}e.$parsers.push(f),e.$formatters.push(g)}}}b&&b.id;a("arrayJoin",c);var d,e;return{setters:[function(a){d=a},function(a){e=a}],execute:function(){e.default.directive("arrayJoin",c)}}});