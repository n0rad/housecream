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