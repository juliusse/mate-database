'use strict';

define([ 'angular',//
'image!/assets/dynamic/image1', //
'image!/assets/dynamic/image2', //
'image!/assets/dynamic/image3' ],//
function(angular, img1, img2, img3) {
	var controller = {
		attach : function(module) {
			module.controller('SetttingsViewController', [ '$scope',
					function($scope) {
						$scope.img1 = '/assets/dynamic/image1';
						$scope.img2 = '/assets/dynamic/image2';
						$scope.img3 = '/assets/dynamic/image3';

					} ]);
		}
	}

	return controller;
});
