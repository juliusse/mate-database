'use strict';

define(['angular'],
function (angular) {
    var controller = {
        attach: function (module) {
            module.controller('SettingsViewController', [
                    '$scope',
                    '$http',
					function ($scope, $http) {
					    $scope.img1 = '/assets/dynamic/image1';
					    $scope.img2 = '/assets/dynamic/image2';
					    $scope.img3 = '/assets/dynamic/image3';

				        $http.get("/rest/meta")
				        .success(function (data) {
				            $scope.pricePerBottle = data.currentBottlePrice.toFixed(2);
				        });
				        
				        $scope.savePricePerBottle = function() {
				        	$http.post("/rest/setCurrentBottlePrice", { newBottlePrice: $scope.pricePerBottle }).
				        		success(function (data) {
				        			$scope.pricePerBottleMsg = "Bottle price saved.";
				        		}).
				        		error(function myError(response) {
				        			$scope.pricePerBottleMsg = "Could not save bottle price.";
				        		});
				        }
					}]);
        }
    }

    return controller;
});
