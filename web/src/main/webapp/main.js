define(['require', //	
        'js!Underscore.js!order', //
        'js!jquery-1.7.2.js!order', //
        'js!jquery.toObject.js!order', //
        'js!/hcs/ws/resources/Validator.js'
        
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
        ], function(require) {

	define('jquery', [], function() {
		var jquery = $;
		$.noConflict();
		return jquery;
	});

	define('underscore', function() {
//		var underscore = _;
//		underscore.noConflict();
		return _;
	});
	
//	define('jqueryui', [
//
//'css!jquery/themes/base/jquery.ui.accordion.css',
//'css!jquery/themes/base/jquery.ui.all.css',
//'css!jquery/themes/base/jquery.ui.autocomplete.css',
//'css!jquery/themes/base/jquery.ui.base.css',
//'css!jquery/themes/base/jquery.ui.button.css',
//'css!jquery/themes/base/jquery.ui.core.css',
//'css!jquery/themes/base/jquery.ui.datepicker.css',
//'css!jquery/themes/base/jquery.ui.dialog.css',
//'css!jquery/themes/base/jquery.ui.progressbar.css',
//'css!jquery/themes/base/jquery.ui.resizable.css',
//'css!jquery/themes/base/jquery.ui.selectable.css',
//'css!jquery/themes/base/jquery.ui.slider.css',
//'css!jquery/themes/base/jquery.ui.tabs.css',
//'css!jquery/themes/base/jquery.ui.theme.css',
//
//	                    
//	                    'js!jquery/ui/jquery.ui.core.js!order', //
//	                    'js!jquery/ui/jquery.ui.widget.js!order', //
//	                    'js!jquery/ui/jquery.effects.core.js!order', //
//
//	                    'js!jquery/ui/jquery.ui.mouse.js!order', //
//	                    'js!jquery/ui/jquery.ui.dialog.js!order', //
//	                    'js!jquery/ui/jquery.ui.slider.js!order', //
//	                    'js!jquery/ui/jquery.ui.button.js!order', //
//	                    'js!jquery/ui/jquery.ui.position.js!order', //
////	                    'js!jquery/ui/jquery.multiselect.js!order', //
//	                    'js!jquery/ui/jquery.ui.sortable.js!order', //
//	                    'js!jquery/ui/jquery.ui.draggable.js!order', //
//	                    'js!jquery/ui/jquery.ui.resizable.js!order', //
//	                    'js!jquery/ui/jquery.ui.droppable.js!order', //
//	                    'js!jquery/ui/jquery.ui.accordion.js!order', //
//	                    'js!jquery/ui/jquery.ui.datepicker.js!order', //
//	                    'js!jquery/ui/jquery.ui.selectable.js!order', //
//	                    'js!jquery/ui/jquery.ui.progressbar.js!order', //
//	                    'js!jquery/ui/jquery.ui.autocomplete.js!order', //
//	                    
//	                    
//	                    'js!jquery/ui/jquery.effects.clip.js!order', //
//	                    'js!jquery/ui/jquery.effects.fold.js!order', //
//	                    'js!jquery/ui/jquery.effects.drop.js!order', //
//	                    'js!jquery/ui/jquery.effects.fade.js!order', //
//	                    'js!jquery/ui/jquery.effects.slide.js!order', //
//	                    'js!jquery/ui/jquery.effects.scale.js!order', //
//	                    'js!jquery/ui/jquery.effects.blind.js!order', //
//	                    'js!jquery/ui/jquery.effects.shake.js!order', //
//	                    'js!jquery/ui/jquery.effects.bounce.js!order', //
//	                    'js!jquery/ui/jquery.effects.explode.js!order', //
//	                    'js!jquery/ui/jquery.effects.pulsate.js!order', //
//	                    'js!jquery/ui/jquery.effects.transfer.js!order', //
//	                    'js!jquery/ui/jquery.effects.highlight.js!order' //
//
//	                    ], function() {
//		
//	});

	define('log', [ 'Ajsl/Log' ], function(log) {
		return window.console;
	});	
	
	define('hcsUrl', function() {
		return "hcs/ws/";
	});
	
	define('Validator', function() {
		return Validator;
	});
	
	return {
		start : function(data) {
			require([ 'Hcw' ], function(hcw) {
				new hcw().run();
			});
		}
	};
});
