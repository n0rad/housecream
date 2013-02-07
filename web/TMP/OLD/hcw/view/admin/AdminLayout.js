define(['jquery', 'underscore', 'ajsl/event', 'text!./AdminLayoutTemplate.html'], 
function($, _, event, AdminLayoutTemplate) {
	
	function AdminLayout(rootUrl, context) {
		this.context = $(context);
		this.rootUrl = rootUrl;
		
		this.events = {
			'li a|click' : function(e) {
				e.preventDefault();
				var $this = $(this);
				$this.closest('UL').find('LI').removeClass('active');
				$this.closest('LI').addClass('active');
			},
			'li a|init' : function() {
				if (this.href == window.location.href) {
					$(this).closest('LI').addClass('active');
				}
			}
		};

	}
	
	AdminLayout.prototype = {
			displayAdmin : function() {
				this.context.html(_.template(AdminLayoutTemplate, {rootUrl : this.rootUrl}));
				event.register(this.events, this.context);
			}
	};
	
	return AdminLayout;
});
