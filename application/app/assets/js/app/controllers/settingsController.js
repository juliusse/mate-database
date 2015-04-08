'use strict';

define(['angular'],
function (angular) {
    var controller = {
        attach: function (module) {
            module.controller('SetttingsViewController', ['$scope',
					function ($scope) {
					    $scope.img1 = '/assets/dynamic/image1';
					    $scope.img2 = '/assets/dynamic/image2';
					    $scope.img3 = '/assets/dynamic/image3';

					}]);
        }
    }

    return controller;
});
