define(['jquery', 'HcValidator'], 
function($, bv) {
	
	
	return {
		handleSubmit : function(form, rootUrl, url, validator, dataObject, success) {
			if (bv.isValidDisplayedForm(form, dataObject, validator)) {
				$('.form-actions', form).find('.help-inline')
									.html('<img src="'+ rootUrl + '/img/ajax-loader.gif" />');
				
				$.ajax({
					  url: url,
					  headers: { 
					        Accept : "application/json; charset=utf-8",
					        "Content-Type": "application/json; charset=utf-8"
					  },
					  type: "PUT", 
					  data: JSON.stringify(dataObject),
					  success: function(data, textStatus, jqXHR) {
						  $('.form-actions', form).find('.control-group').addClass('success')
						  	.find('.help-inline').html('Success');
						  setTimeout(function() {
							  success(data);
						  }, 1000);
					  },
					  error : function(jqXHR, textStatus, errorThrown) {
						  bv.displayGlobalViolation($.parseJSON(jqXHR.responseText).message, form);
					  }
					});
			}
		},
		
		// 'input[type=text], input[type=hidden], checkbox, select, textarea|keyup,blur,change'
		handleFormElementChange : function(element, validator) {
			bv.isValidDisplayedFormElement(element, $(element).toObject({skipEmpty : false}), validator);
		}
		
	};
	
});