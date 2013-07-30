define([ 'jquery', 'ajsl/Log' ], function($, log) {

	var event = {

		// registerAll : function(eventObjs) {
		// for (var evObj in eventObjs) {
		// this.register(eventObjs[evObj]);
		// }
		// },

		register : function(eventObj, context) {
			for ( var ev in eventObj) {

				// var context = null;
				// if (typeof eventObj.context == 'function') {
				// context = eventObj.context();
				// } else if (eventObj.context) {
				// context = $(eventObj.context);
				// }

				if ($.isFunction(eventObj[ev])) {

					var eventSplit = ev.split('|');

					var selector = eventSplit[0].trim();
					var event = eventSplit[1];
					var run = eventSplit[2];

					var elements = context;

					if (selector == 'init' && !event && !run) {
						event = selector;
						selector = null;
					}

					if (!selector) {
						var elements = context;
					} else {
						var elements = $(selector, context);
					}
					if (elements.length == 0 && event != 'init') {
						log.error('nothing found to register event : ', ev);
						continue;
					}

					var events = event.split(',');
					for ( var current in events) {
						if (event == 'init') {
							elements.each(eventObj[ev]);
							// LOG.debug('init "', ev, '" event to', elements);
							// eventObj[ev](elements);
						} else {
							if (run) {
								log.debug('bind live "', ev, '" event to', elements);
								elements.live(events[current], {
									eventObj : eventObj
								}, eventObj[ev]);
							} else {
								log.debug('bind "', ev, '" event to', elements);
								elements.bind(events[current], {
									eventObj : eventObj
								}, eventObj[ev]);
							}
						}
					}

				}
			}
		}

	};

	return event;
});
