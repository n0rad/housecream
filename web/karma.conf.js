  module.exports = function(config) {
    config.set({
    	basePath : '',
    	files : [
    	         'src/main/webapp/bower_components/angular/angular.js',
    	         'src/main/webapp/bower_components/angular-mocks/angular-mocks.js',
    	         'src/main/webapp/scripts/*.js',
    	         'src/main/webapp/scripts/**/*.js',
    	         'src/test/webapp/mock/**/*.js',
    	         'src/test/webapp/spec/**/*.js'
    	       ],
    	 frameworks : ["jasmine"],
    	 exclude : [],
    	 reporters : ['progress'],

    	 port : 8080,
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
