'use strict';

require.config({
	paths : {
		jquery : '../lib/jquery/jquery.min',
		angular : '../lib/angularjs/angular.min',
		ngResource : '../lib/angularjs/angular-resource.min',
		ngRoute : '../lib/angularjs/angular-route.min',
		bootstrap : 'lib/bootstrap/js/bootstrap',
		image : 'ext/image',
	},
	shim : {
		'jquery' : {
			exports : 'jquery'
		},
		'angular' : {
			deps : [ 'jquery' ],
			exports : 'angular'
		},
		'ngResource' : [ 'angular' ],
		'ngRoute' : [ 'angular' ]
	},
	priority : [ "jquery", "angular" ]

});

define([
// These are path alias that we configured in our bootstrap
'angular' //
, 'app/app' ], function(angular, app) {	

	var $html = angular.element(document.getElementsByTagName('html')[0]);
	angular.element().ready(function() {
		angular.bootstrap(document, [ 'mateDatabase' ]);
	});
});
