define(['jquery'], function($) {

	var view = new function() {
		var $this = this;
		
		this.formFields = "input, checkbox, select, textarea";
		this.valueFormFields = "input[type=text], input[type=hidden], checkbox, select, textarea";

		this.setValue = function(elem, value) {
			if (elem.is(':checkbox')) {
				//TODO change to prop with jquery 1.6
				elem.attr('checked', value == true ? 'checked' : '').change();
//				elem.prop("checked", );
			} else {
//				jQuery.data(elem, 'oldVal', elem.val());
//				jQuery.data(elem, 'newVal', value);

				elem.data('oldVal', elem.val());
				elem.data('newVal', value);
				elem.val(value).change();
			}
		};
		
		this.rebuildFormRec = function(form, data, formManager, root, rootMethod) {
			if (root == undefined) {
				root = '';
			}
			if (rootMethod == undefined) {
				rootMethod = 'addForm';
			}

			
			if (!(jQuery.isArray(data) && typeof(data[0]) != 'object')) {
				for (var formElem in data) {
					if (data[formElem] instanceof Array) {
						var assertMethodName = rootMethod + formElem.ucFirst();
						if (jQuery.isArray(data[formElem]) && typeof(data[formElem][0]) != 'object') {
							if (assertMethodName in formManager) {
								formManager[assertMethodName](i);
							}
							$this.rebuildFormRec(form, data[formElem], formManager, root + formElem, assertMethodName);
						} else {
							for ( var i = 0; i < data[formElem].length; i++) {
								if (assertMethodName in formManager) {
									formManager[assertMethodName](i);
								}
								$this.rebuildFormRec(form, data[formElem][i], formManager, root + formElem
										+ '[' + i + '].', assertMethodName);
							}
						}
					} else {
						var elem = $('[name="' + root + formElem + '"]', form);
						$this.setValue(elem, data[formElem]);
					}
				}			
			} else {
				var elem = $('[name="' + root + '"]', form);
				$this.setValue(elem, data);			
			}
			
		};

		this.resetFormValues = function(element) {
			element.find($this.valueFormFields).val('').change();
			element.find('input:checkbox').removeAttr('checked').change();
		};

		this.incrementFormIndexes = function(element) {
			// increment name
			element.find($this.formFields).each(function() {
				this.name = this.name.replace(/\[(\d+)\]/, function(str, p1) {
					return '[' + (parseInt(p1, 10) + 1) + ']';
				});

				this.id = this.id.replace(/(\d+)\./, function(str, p1) {
					return (parseInt(p1, 10) + 1) + '.';
				});
			});

			// increment labels
			element.find('label').each(function() {
				this.htmlFor = this.htmlFor.replace(/(\d+)\./, function(str, p1) {
					return (parseInt(p1, 10) + 1) + '.';
				});
				this.id = this.id.replace(/(\d+)\./, function(str, p1) {
					return (parseInt(p1, 10) + 1) + '.';
				});
			});
		};
		
		// $(':input','#myform')
		// .not(':button, :submit, :reset, :hidden')
		// .val('')
		// .removeAttr('checked')
		// .removeAttr('selected');

		// (function($)
		// {
		// $.fn.defaultValue = function( options )
		// {
		// var options = $.extend(
		// {
		// defVal: 'Search'
		// },
		// options
		// );
		// return this.each( function()
		// {
		// $( this ).blur( function()
		// {
		// if ( $( this ).val() === '' )
		// $( this ).val( options.defVal );
		// }
		// ).focus( function()
		// {
		// if ( $( this ).val() === options.defVal )
		// $( this ).val( '' );
		// }
		// );
		// }
		// );
		// }
		// }
		// )(jQuery);
		//		

		// $.fn.clearForm = function() {
		// return this.each(function() {
		// var type = this.type, tag = this.tagName.toLowerCase();
		// if (tag == 'form')
		// return $(':input',this).clearForm();
		// if (type == 'text' || type == 'password' || tag == 'textarea')
		// this.value = '';
		// else if (type == 'checkbox' || type == 'radio')
		// this.checked = false;
		// else if (tag == 'select')
		// this.selectedIndex = -1;
		// });
		// };

	};

	return view;
});
