//define([], function() {
//
//	function EventService(serverUrl) {
//		var self = this;
//		
//		if (!window.WebSocket) {
//			alert("WebSocket not supported by this browser");
//		}
//		
//		this.registerEventHandler = function(callback) {
//			self.eventHandler = callback;
//		};
//
//		this.join = function() {
//			var location = "ws://localhost:8888/"; //TODO wss
//			self._ws = new WebSocket(location);
//			self._ws.onopen = self._onopen;
//			self._ws.onmessage = self._onmessage;
//			self._ws.onclose = self._onclose;
//			self._ws.onerror = self._onerror;
//		};
//
//		this._onopen = function() {
//			console.info("open");
//		};
//
//		this._onmessage = function(m) {
//			var event = $.parseJSON(m.data);
//			if (self.eventHandler) {
//				self.eventHandler(event);
//			}
//		};
//
//		this._onclose = function(m) {
//			console.info("close");
//			setTimeout(self.join, 10000);
//		};
//
//		this._onerror = function(e) {
//			console.info("error");
//			alert(e);
//		};
//
//		
//		
//		this.join();
//	}
//
//	EventService.prototype = {
//
//	};
//
//	return EventService;
//});