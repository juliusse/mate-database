'use strict';

require.config({
	paths : {
		jquery : '../lib/jquery/jquery.min',
		angular : '../lib/angularjs/angular.min',
		ngResource : '../lib/angularjs/angular-resource.min',
		ngRoute : '../lib/angularjs/angular-route.min',
		spin : '../lib/spin-js/spin',
		bootstrap : '../lib/bootstrap/js/bootstrap',
		image : 'ext/image',
		bootbox: '../lib/bootbox/bootbox.min'
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
		'ngRoute' : [ 'angular' ],
		'bootbox' : {
			deps: ['jquery', 'bootstrap'],
			exports : 'bootbox'
		},
	},
	priority : [ "jquery", "angular", "bootbox" ]

});

define([
// These are path alias that we configured in our bootstrap
'angular' //
, 'spin' //
, 'app/app' ], function(angular, spin, app) {	

	var $html = angular.element(document.getElementsByTagName('html')[0]);
	angular.element().ready(function() {
		angular.bootstrap(document, [ 'mateDatabase' ]);
	});
});
