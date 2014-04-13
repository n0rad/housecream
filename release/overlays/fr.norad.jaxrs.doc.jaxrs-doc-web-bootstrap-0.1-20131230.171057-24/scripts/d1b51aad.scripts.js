"use strict";function buildApiGroups(a){for(var b={},c=0;c<a.length;c++)for(var d=a[c],e=0;e<d.operations.length;e++){var f=d.operations[e],g=rootPath(f.path);b[g]=b[g]?b[g]:{},b[g][f.path]=b[g][f.path]?b[g][f.path]:{};var h=b[g][f.path];h.methods=h.methods?h.methods:[],h.operations=h.operations?h.operations:[],h.methods.push(f.httpMethod),h.operations.push(f)}return b}function rootPath(a){if(!a)return"";var b=a.substring(1).indexOf("/")+1;0>=b&&(b=a.length);var c=a.substring(0,b);return c}var jaxrsDoc=angular.module("jaxrs-doc",["ui.router","ui.bootstrap"]);jaxrsDoc.constant("docServiceUrl",docServiceUrl),jaxrsDoc.config(["$stateProvider","$urlRouterProvider","$locationProvider",function(a,b,c){c.html5Mode(!0),a.state("slash",{url:"/",templateUrl:"views/Main.html"}).state("project",{url:"/:currentProject",templateUrl:"views/Main.html"}).state("version",{url:"/:currentProject/:currentVersion",templateUrl:"views/Project.html"}).state("api",{url:"/:currentProject/:currentVersion/apiGroup/*currentApi",templateUrl:"views/Project.html"})}]),jaxrsDoc.controller("MainController",["$routeParams",function(){}]),jaxrsDoc.controller("GlobalController",["$scope","$location","Projects",function(a,b,c){a.$watch(function(){return b.path()+b.hash()},function(b){var d=b.split("/",3);c.currentProject=a.currentProject=d[1],c.currentVersion=a.currentVersion=d[2],c.currentApiUrl=a.currentApiUrl=b.substr(d[1].length+d[2].length+2+"/apiGroup".length)})}]),jaxrsDoc.controller("FilterController",["$scope","Projects","$location",function(a,b){a.projects=b.loadProjectList(),a.$on("currentProject",function(){a.currentProject=b.currentProject})}]),jaxrsDoc.controller("ApiController",["$scope","Projects","Apis",function(a,b){a.currentApiGroup=b.findApiGroup(a.currentApiUrl)}]),jaxrsDoc.controller("ApiGroupController",["$scope","Projects","Apis",function(a,b){a.projectDefinition=b.getProject(b.currentProject,b.currentVersion)}]),jaxrsDoc.factory("Projects",["$http","$rootScope",function(a){var b={},c={currentProject:void 0,loadProjectList:function(){var c=a.get("projects.csv").then(function(a){console.log(a);for(var c=a.data.split("\n"),d=0;d<c.length;d++){var e=c[d].split(",");e&&e[2]&&(b[e[0]]||(b[e[0]]={}),b[e[0]][e[1]]={},b[e[0]][e[1]].url=e[2])}return b});return c},findApiGroup:function(a){var c=b[this.currentProject][this.currentVersion].apiGroups;for(var d in c)if(c.hasOwnProperty(d))for(var e in c[d])if(c[d].hasOwnProperty(e)&&e==a)return c[d][e]},getProject:function(c,d){if(c&&d&&b[c]&&b[c][d]){if(b[c][d].data)return b[c][d];var e=a.get(b[c][d].url).then(function(a){return console.log(a),b[c][d].data=a.data,b[c][d].apiGroups=buildApiGroups(a.data.apis),b[c][d]});return e}}};return c}]),jaxrsDoc.factory("Apis",["$http",function(){var a={};return a}]),jaxrsDoc.filter("classShortName",function(){return function(a){return a.substring(a.lastIndexOf(".")+1)}}),jaxrsDoc.filter("default",function(){return function(a,b){return"undefined"!=typeof a&&a?a:b}}),jaxrsDoc.directive("myCurrentTime",["$timeout","dateFilter",function(a,b){return function(c,d,e){function f(){d.text(b(new Date,h))}function g(){i=a(function(){f(),g()},1e3)}var h,i;c.$watch(e.myCurrentTime,function(a){h=a,f()}),d.on("$destroy",function(){a.cancel(i)}),g()}}]);