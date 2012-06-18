define(['main'], function(main) {

	define('hcsRootUrl', [], function() {
		return "${hcs.url}";
	});

	define('hcwVersion', [], function() {
		return "${project.version}";
	});

	return main;
});
