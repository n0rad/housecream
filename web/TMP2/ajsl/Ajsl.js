define(['jquery'], function($) {
	"use strict";

	function Ajsl() {
	}

	Ajsl.prototype = {

		get : function(id) {

			$.ajax({
				url : url,
				dataType : 'script',
				success : success
			});

			$.when($.ajax("/page1.php"), $.ajax("/page2.php")).done(
					function(a1, a2) {
						/*
						 * a1 and a2 are arguments resolved for the page1 and
						 * page2 ajax requests, respectively
						 */
						var jqXHR = a1[2]; /*
											 * arguments are [ "success",
											 * statusText, jqXHR ]
											 */
						if (/Whip It/.test(jqXHR.responseText)) {
							alert("First page has 'Whip It' somewhere.");
						}
					});

			$.getScript("res/visuwall/ctrl/controller/", function(data,
					textStatus) {

			});
		}
	};
	return Ajsl;
});
