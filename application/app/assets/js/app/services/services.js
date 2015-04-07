'use strict';

define([ 'angular', 'app/services/junky' ], function(angular, junky) {
	var services = angular.module('mateServices', ['ngResource']);
	junky.attach(services);
});
