$(document).ready(function() {

	$('.nav-collapse').scrollspy();
	$().smoothanchors();

	$('.navbar').affix({
		offset : $('.navbar').position()
	});
	
	
	$('.fancybox').fancybox();

	// $(".tweet").tweet({
	// username: "norad_fr",
	// join_text: "auto",
	// avatar_size: 32,
	// count: 2,
	// auto_join_text_default: "we said,",
	// auto_join_text_ed: "we",
	// auto_join_text_ing: "we were",
	// auto_join_text_reply: "we replied to",
	// auto_join_text_url: "we were checking out",
	// loading_text: "loading tweets..."
	// });

});