!function ($) {
  "use strict"; // jshint ;_;
  
  var BootstrapSelect = function (element) {
        var $el = $(element).on('click.dropdown.data-api', this.toggle)
        $('html').on('click.dropdown.data-api', function () {
          $el.parent().removeClass('open')
        });
      };

  BootstrapSelect.prototype = {
    constructor: BootstrapSelect,
  };
  
  $.fn.BootstrapSelect = function (option) {
    return this.each(function () {
//      var $this = $(this)
//        , data = $this.data('dropdown')
//      if (!data) $this.data('dropdown', (data = new BootstrapSelect(this)))
//      if (typeof option == 'string') data[option].call($this)
    });
  };

  $.fn.bootstrapSelect.Constructor = BootstrapSelect;

}(window.jQuery);