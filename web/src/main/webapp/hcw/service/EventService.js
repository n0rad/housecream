define([], function() {

	function EventService(serverUrl) {
		var self = this;
		
		if (!window.WebSocket) {
			alert("WebSocket not supported by this browser");
		}

		this.join = function() {
			var location = "ws://localhost:8888/"; //TODO wss
			self._ws = new WebSocket(location);
			self._ws.onopen = self._onopen;
			self._ws.onmessage = self._onmessage;
			self._ws.onclose = self._onclose;
			self._ws.onerror = self._onerror;
		};

		this._onopen = function() {
			console.info("open");
		};

		this._onmessage = function(m) {
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
		};

		this._onclose = function(m) {
			console.info("close");
			setTimeout(self.join, 10000);
		};

		this._onerror = function(e) {
			console.info("error");
			alert(e);
		};

		
		
		this.join();
	}

	EventService.prototype = {

	};

	return EventService;
});