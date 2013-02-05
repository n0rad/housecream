!function($) {
	"use strict"; // jshint ;_;

	var BootstrapSelect = function(context, nestedSets) {
		var select = $("SELECT", context);

		for ( var i = 0; i < nestedSets.length; i++) {
			select.append('<option value="' + nestedSets[i].id + '" data-type="' + nestedSets[i].type + '">' + nestedSets[i].name + '</option>');
		}

		var ul = $("UL", context);

		var res = [];
		this.display(res, nestedSets);
		ul.append(res.join(""));
		$('A', ul).bind('click', function(e) {
			var childs = $('A.btn', context).children();
			$('A.btn', context).text($(this).text()).append(childs);

			$('LI', context).removeClass('active');
			$(this).parent().addClass('active');
			e.preventDefault();
			select.val($(this).data("id")).change();
		});

		select.bind('change', function() {
			$('LI', context).removeClass('active');
			var current = $('A:data(id=' + this.value + ')', context);
			current.parent().addClass('active');

			var childs = $('A.btn', context).children();
			$('A.btn', context).text(current.text()).append(childs);
		});
		$('.dropdown-toggle', context).dropdown();
	};

	BootstrapSelect.prototype = {
		constructor : BootstrapSelect,

		display : function(strings, data, currentParent) {
			var flag = false;
			for ( var i = 0; i < data.length; i++) {
				if ((!currentParent && !data[i].parentId) || (currentParent && data[i].parentId == currentParent.id)) {
					if (!flag && currentParent) {
						strings.push('<ul class="nav nav-list">');
						flag = true;
					}
					strings.push('<li><a href="#" data-id="' + data[i].id + '"><i class="icon-pencil"></i> '
							+ data[i].name + '</a></li>');
					this.display(strings, data, data[i]);
				}
			}
			if (flag) {
				strings.push('</ul>');
			}
		}
	};

	$.fn.bootstrapSelect = function(nestedSets) {
		return this.each(function() {
			var $this = $(this);
			var data = $this.data('bootstrapSelect');
			if (!data) {
				$this.data('bootstrapSelect', (data = new BootstrapSelect(this, nestedSets)))
			}
			if (typeof option == 'string') {
				data[option].call($this);
			}
		});
	};

	$.fn.bootstrapSelect.Constructor = BootstrapSelect;

}(window.jQuery);
