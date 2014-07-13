require.config({
	paths : {
		jquery : 'ext/jquery-2.1.1.min',
		underscore : 'ext/underscore-1.6.0',
		backbone : 'ext/backbone-1.1.2',
		bootstrap : 'ext/bootstrap.min',
		text : 'ext/text',
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
