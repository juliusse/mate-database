'use strict';

define(['angular'], function (angular) {
    var controller = {
        attach: function (module) {
            module.controller('LogViewController', ['$scope', '$http',
					function ($scope, $http) {

					    $scope.logEntries = [];
					    $http.get("/rest/log").success(function (data) {
					        $scope.logEntries = data;
					    });

					}]);
        }
    }

    return controller;
});
