define(['curl/tdd/runner', 'require'], function (runner, require) {
	runner(require).run(function(require) {

		
require(['js!Underscore.js!order',
        'js!jquery-1.7.2.js!order&'], function() {
	
	describe('Dummy test', function() {
		
		it('should be true', function() {
			expect(true).toBe(true);
		});
		
			
	});
});


	}).then(loaded);
});
