define([], function() {

	function EventService(serverUrl) {
		if (!window.WebSocket) {
			alert("WebSocket not supported by this browser");
		}

		var location = "ws://localhost:8888/";
		this._ws = new WebSocket(location);
		this._ws.onopen = this._onopen;
		this._ws.onmessage = this._onmessage;
		this._ws.onclose = this._onclose;
		this._ws.onerror = this._onerror;
	}

	EventService.prototype = {
//		join : function(name) {
//			this._username = name;
//			// var location = document.location.toString().replace('http://',
//			// 'ws://').replace('https://', 'wss://');
//			var location = "ws://localhost:8081/";
//			this._ws = new WebSocket(location);
//			this._ws.onopen = this._onopen;
//			this._ws.onmessage = this._onmessage;
//			this._ws.onclose = this._onclose;
//			this._ws.onerror = this._onerror;
//		},

		_onopen : function() {
			console.info("open");
		},

		_onmessage : function(m) {
			var event = $.parseJSON(m.data);
			
			noty({layout: 'bottomRight', text: m.data, timeout: 10000});
			
//			$('.ex8.error').click(function() {
//				noty({layout: 'bottomRight', text: error_note, type: 'error'});
//				return false;
//			});
//			
//			$('.ex8.success').click(function() {
//				noty({layout: 'bottomRight', text: success_note, type: 'success'});
//				return false;
//			});
//			
//			$('.ex8.information').click(function() {
//				noty({layout: 'bottomRight', text: information_note, type: 'information'});
//				return false;
//			});
			
			
			console.info("message" + m);
		},

		_onclose : function(m) {
			console.info("close");
		},

		_onerror : function(e) {
			console.info("error");
			alert(e);
		},

	};

	return EventService;
});