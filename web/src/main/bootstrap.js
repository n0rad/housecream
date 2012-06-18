define(['main'], function(main) {

	define('hcsRootUrl', [], function() {
		return "${hcs.url}";
	});

	define('hcsWebVersion', [], function() {
		return "${project.version}";
	});

	return main;
});
