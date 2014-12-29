require.config({
	paths : {
		jquery : '../lib/jquery/jquery.min',
		underscore : '../lib/underscorejs/underscore',
		backbone : '../lib/backbonejs/backbone',
		bootstrap : 'lib/bootstrap/js/bootstrap',
		text : '../lib/requirejs-text/text',
		image : 'ext/image',
		mate : "app/mate",
		jquerymeta: "ext/jquery.metadata",
		jquerytable: "ext/jquery.tablesorter.min",
	}

});

define([
	// These are path alias that we configured in our bootstrap
	'jquery', // lib/jquery/jquery
	'underscore', // lib/underscore/underscore
	'backbone', // lib/backbone/backbone
	'mate'
], function($, _, Backbone, Mate) {
	
	// Above we have passed in jQuery, Underscore and Backbone
	// They will not be accessible in the global scope
	return {};
	// What we return here will be used by other modules
});
