define([ 'curl', 'log', 'jquery', 'underscore' ], function(require, log, $, _) {
	"use strict";

    var _queryBuilderObj = {
    	
    	_data : null,
    	
		_parseController : function(url) {
			var result = {};
			var varsSepar = url.split('?');
			if (varsSepar.length > 2) {
				throw 'malform url with more than one "?" in controller' + controllers[0];
			}
			result.ctrlVars = varsSepar[0].split('/');
			result.ctrl = result.ctrlVars.shift();

			// vars
			if (varsSepar.length > 1) {
				result.vars = this._parseQueryVars(varsSepar[1]);
			} else {
				result.vars = {};
			}
			
			if (result.ctrl[result.ctrl.length - 1] === '!') {
				result.ctrl = result.ctrl.substring(0, result.ctrl.length - 1);
				result.forced = true;
			} else {
				result.forced = false;
			}
			return result;
		},

	    _parseQueryVars : function(varStr) {
	       	var res = {};
	       	var pairs = varStr.split('&');
	       	for (var i = 0; i < pairs.length; i++) {
	       	    var keyval = pairs[i].split('=');
	       	    res[keyval[0]] = keyval[1];
	       	}
	       	return res;
	    },

		_buildRequest : function() {
			var res = '';
			for (var i = 0; i < this._data.length; i++) {
				if (res) {
					res += ';';
				}
				res += this._data[i].ctrl;
				if (this._data[i].forced) {
					res += '!';
				}
				var ctrlVars = this._data[i].ctrlVars.join('/');
				if (ctrlVars) {
					res += '/' + ctrlVars;
				}
				
				var vars = this._data[i].vars;
				if (_.size(vars)) {
					res += '?';
				}
				var flag = 0;
				for (var varName in vars) {
					if (flag) {
						res += '&';
					}
						res += varName + '=' + vars[varName];
					flag++;
				}
			}
			return res;
		},
		
		_toggleForce : function(force, currentCtrl, newCtrl) {
    		if (force) {
    			newCtrl.forced = currentCtrl.forced ? false : true;
	    	}
		},

		addController : function(ctrlName, param, force) {
			var ctrlsNames = ctrlName.split('/');
    		var ctrl = this._parseController(param ? ctrlName + '/' + param : ctrlName);
    		
    		var replaced = false;
    		var i = 0;
    		for (; i < this._data.length; i++) {
    			if (ctrlsNames.length > 1) {
    				if (this._data[i].ctrl == ctrlsNames[0] && this._data[i].ctrlVars[0] == ctrlsNames[1]) {
    					
	    				this._data.splice(i, 1, ctrl);	    					
	    				replaced = true;
	    				break;
    				}
    			} else if (this._data[i].ctrl == ctrlsNames[0]) {
    				this._toggleForce(force, this._data[i], ctrl);
    				this._data.splice(i, 1, ctrl);
    				replaced = true;
    				break;
    			}
    		}
    		
    		if (!replaced) {
    			this._data.push(ctrl);
    		}
    		return this;
    	},
    	
    	forceController : function(ctrlName, param) {
    		this.addController(ctrlName, param, true);
    		return this;
    	},
    	
    	contains : function(ctrlName, param) {
    		var ctrl = this._parseController(param ? ctrlName + '/' + param : ctrlName);
    		for (var i = 0; i < this._data.length; i++) {
    			if (_.isEqual(ctrl, this._data[i])) {
    				return true;
    			}
    		}
    		return false;
    	},

    	removeController : function(ctrlName) {
    		var ctrlNames = ctrlName.split('/');
    		for (var i = 0; i < this._data.length; i++) {
    			var current = this._data[i];
    			if (ctrlNames.length >= 2) {
    				if (current.ctrl == ctrlNames[0]
    						&& current.ctrlVars[0] == ctrlNames[1]) {
    					this._data.splice(i, 1);
	    				break;
    				}
    			} else if (current.ctrl == ctrlNames[0]) {	    				
					this._data.splice(i, 1);
    				break;
    			}
    		}
    		return this;
    	},

		load : function() {
			$.history.load(this.build());
		},
    	
    	build : function() {
    		return this._buildRequest(this._data);
    	}
    };
	
    function queryBuilder(currentLocation) {
    	var builder = $.extend({}, _queryBuilderObj);
    	builder._data = parseRequest(currentLocation === undefined ? this.location.get() : currentLocation);	    		
    	return builder;
    }
	
	
	var defaultOptions = {
			suffix: "Command",
			mainModuleMain: null,
			modules: "command/",
	};

	function isAlreadyDispatch(ctrl, lastDispatch) {
		for (var i = 0; i < lastDispatch.length; i++) {
			if (_.isEqual(ctrl, lastDispatch[i])) {
				return true;
			}
		}
		return false;
	}
	
	function parseRequest(request) {			
		var result = [];
		if (request) {
			var controllers = request.split(';');
	       	for (var i = 0; i < controllers.length; i++) {
	       		var ctrl = _queryBuilderObj._parseController(controllers[i]);
	       		result.push(ctrl);
	       	}
		}
       	return result;
	}
	
	function Dispatcher(userOptions) {
		var self = this;
		this.lastDispatch = [];

		this.options = $.extend({}, defaultOptions, userOptions);
		if (this.options.modules.charAt(this.options.modules.length - 1) != '/') {
			this.options.modules += '/';
		}
		
		this.dispatch = function(url) {
			log.debug("dispatch for url '", url, "'");
			if (!url) {
				if (self.options.mainModuleMain) {
					var moduleName = self.options.modules + self.options.mainModuleMain + self.options.suffix;
					curl([ moduleName ], function(
							MainController) {
						new MainController(self.options.commandParam).run();
					});
				}
				self.lastDispatch = [];
				return;
			}

			var parsedCtrls = parseRequest(url);			
			for(var i = 0; i < parsedCtrls.length; i++) {

					if (isAlreadyDispatch(parsedCtrls[i], self.lastDispatch)) {
						log.debug("skip history '", parsedCtrls[i], "' as already dispatched in last url");
						continue;
					}

					var ctrlModuleName = self.options.modules + parsedCtrls[i].ctrl.ucFirst() + self.options.suffix;

					var tmp = function(index) { // hack? for i closure
					require([ ctrlModuleName ]).then(function(Controller) {
								var ctrlVars = parsedCtrls[index].ctrlVars.slice();
								var vars = parsedCtrls[index].vars;
								
								var controller = new Controller(self.options.commandParam);
								var method = 'run';
								if (ctrlVars[0] && controller[ctrlVars[0]]) {
									method = ctrlVars[0];
									ctrlVars.shift();
								}
								ctrlVars.unshift(vars);
								controller[method].apply(this || window, ctrlVars);
							}, function(data) {
								log.error("Failed to find command : " + ctrlModuleName);
							});
					};
					
					tmp(i);
			}
			self.lastDispatch = parsedCtrls;
		};
	};

	Dispatcher.prototype = {
		register : function() {
			window.onpopstate = this.dispatch;
		}
	}

	return Dispatcher;
});