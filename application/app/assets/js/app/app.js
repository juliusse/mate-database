'use strict';

define([ 'angular', //
'ngRoute', //
'app/controllers/controllers', //
'app/services/services', //
'image!/assets/dynamic/image1', //
'image!/assets/dynamic/image2', //
'image!/assets/dynamic/image3' ],//
function(angular, ngRoute, controllers, services) {
	var mateApp = angular.module('mateDatabase', [ 'ngResource', 'ngRoute',
			'mateServices', 'mateControllers' ]);

	mateApp.config([ '$routeProvider', function($routeProvider) {
		$routeProvider.when('/main', {
			templateUrl : 'assets/js/app/views/index.html',
			controller : 'MainViewController'
		}).when('/settings', {
			templateUrl : 'assets/js/app/views/settings.html',
			controller : 'SetttingsViewController'
		}).when('/log', {
			templateUrl : 'assets/js/app/views/log.html',
			controller : 'LogViewController'
		}).otherwise({
			redirectTo : '/main'
		});
	} ]);

	return mateApp;
});
