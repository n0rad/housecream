  module.exports = function(config) {
    config.set({
    	basePath : '',
    	files : [
    	         'src/main/web/bower_components/angular/angular.js',
    	         'src/main/web/bower_components/angular-mocks/angular-mocks.js',
    	         'src/main/web/scripts/*.js',
    	         'src/main/web/scripts/**/*.js',
    	         'src/test/web/mock/**/*.js',
    	         'src/test/web/spec/**/*.js'
    	       ],
    	 frameworks : ["jasmine"],
    	 exclude : [],
    	 reporters : ['progress'],

    	 port : 8180,
    	 runnerPort : 9100,

    	 colors : true,
	    logLevel : config.LOG_INFO,
	    autoWatch : false,
	    browsers : ['PhantomJS'],
	    captureTimeout : 5000,
	    singleRun : false

    });
  };

// possible values: LOG_DISABLE || LOG_ERROR || LOG_WARN || LOG_INFO || LOG_DEBUG

// Start these browsers, currently available:
// - Chrome
// - ChromeCanary
// - Firefox
// - Opera
// - Safari (only Mac)
// - PhantomJS
// - IE (only Windows)
