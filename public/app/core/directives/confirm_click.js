/*! grafana - v4.2.0 - 2017-03-22
 * Copyright (c) 2017 Torkel Ödegaard; Licensed Apache-2.0 */

define(["../core_module"],function(a){"use strict";a.default.directive("confirmClick",function(){return{restrict:"A",link:function(a,b,c){b.bind("click",function(){var b=c.confirmation||"Are you sure you want to do that?";if(window.confirm(b)){var d=c.confirmClick;d&&a.$apply(a.$eval(d))}})}}})});