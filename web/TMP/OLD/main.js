define(['require', 'hcsRootUrl', //	
        'js!Underscore.js!order', //
//        'js!jquery-1.7.2.js!order', //
        'js!jquery.toObject.js!order', //
        'js!jquery.history.js!order', //
        
//        'css!jquery/jquery.marquee.min.css',
//        'css!jquery/jquery.selectBox.css',
//        
//      'js!jquery/jquery.cookie.js!order',
//        'js!jquery/jquery.marquee.js!order',
//        'js!jquery/jquery.timeago.js!order',
//        'js!jquery/jquery.selector.js!order',
//        'js!jquery/jquery.selectBox.js!order',
//        'js!jquery/jquery.countdown.js!order',
//        'js!jquery/jquery.textfill.0.1.js!order',
//        'js!jquery/jquery.qtip-1.0.0-rc3.js!order',
//        'js!jquery/jquery.selectbox-0.6.1.js!order',
//        'js!jquery/jquery.history.js!order',
//        'js!jquery/jquery.history.extended.js!order'
        ], function(require, hcsRootUrl) {

	define('jquery', [], function() {
		var jquery = $;
//		$.noConflict();
		return jquery;
	});

	define('underscore', function() {
//		var underscore = _;
//		underscore.noConflict();
		return _;
	});
	
	define('log', [ 'ajsl/Log' ], function(log) {
//		return window.console;
		var empty = function() {};
		return {log : empty, info : empty, error : empty, warn : empty, debug : empty};
	});
	
	require('js!' + hcsRootUrl + 'resources/Validator.js', function() {
		define('Validator', function() {
			return Validator;
		});		
	});
	
	
	return {
		start : function(data) {
			require([ 'Hcw' ], function(hcw) {
				new hcw().run(data);
			});
		}
	};
});
