'use strict';

define(['angular', 'spin', 'bootbox'], function (angular, Spinner, bootbox) {
    var controller = {
        attach: function (module) {
            module.controller('MainViewController', [
					'$scope',
					'$http',
					'$timeout',
					'Junky',
					function ($scope, $http, $timeout, Junky) {
					    $scope.junkies = [];
					    $scope.mateAvailable = 0;
					    $scope.totalMate = 0;
					    $scope.totalMoney = 0;

					    $scope.blockInput = true;

					    $scope.notificationVisible = false;
					    $scope.notificationClass = "bg-success";
					    $scope.notificationText = "Test"

					    $scope.init = function () {
					        $scope.createSpinner();
					        $scope.reloadJunkies();
					    }

						// Converts 'n' to array with 'n' items.
						$scope.getNumber = function(num) {
						    return new Array(num);   
						}

					    $scope.createSpinner = function () {
					        var opts = {
					            lines: 15, // The number of lines to draw
					            length: 24, // The length of each line
					            width: 10, // The line thickness
					            radius: 45, // The radius of the inner circle
					            corners: 1, // Corner roundness (0..1)
					            rotate: 0, // The rotation offset
					            direction: 1, // 1: clockwise, -1: counterclockwise
					            color: '#000', // #rgb or #rrggbb or array of colors
					            speed: 1, // Rounds per second
					            trail: 60, // Afterglow percentage
					            shadow: false, // Whether to render a shadow
					            hwaccel: false, // Whether to use hardware acceleration
					            className: 'spinner', // The CSS class to assign to the spinner
					            zIndex: 2e9, // The z-index (defaults to 2000000000)
					            top: '50%', // Top position relative to parent
					            left: '50%' // Left position relative to parent
					        };
					        var target = document.getElementById('block-spinner');
					        new Spinner(opts).spin(target);
					    }

					    // payment form
					    $scope.payment_userId = -1;
					    $scope.payment_amount = "10.00";

					    $scope.payment_submit = function () {
					        $scope.blockInput = true;

					        $scope.getJunky($scope.payment_userId, function (junky) {
					            var curAmount = junky.credit;
					            var payment = $scope.payment_amount.replace(
										",", ".");

					            if (!isNaN(payment)) {
					                var amount = payment * 100;

					                junky.credit = curAmount + amount;
					                $scope.saveJunky(junky, function () {
					                    $scope.showNotification(true, "Es wurden erfolgreich " + $scope.payment_amount + " Euro für " + junky.name + " eingezahlt!");
					                    $scope.payment_userId = -1;
					                    $scope.payment_amount = "10.00";
					                    $scope.reloadJunkies();
					                });
					            } else {
					                $scope.blockInput = false;
					                $scope.showNotification(false, "Der angegebene Betrag ist keine gültige Zahl!");
					            }
					            ;
					        })
					    }

					    // new user form
					    $scope.newUser_name = "";

					    $scope.newUser_submit = function () {
					        $scope.blockInput = true;

					        var junky = new Junky({
					            name: $scope.newUser_name
					        });

					        $scope.saveJunky(junky, function () {
					            $scope.newUser_name = "";
					            $scope.blockInput = false;
					            $scope.reloadJunkies();
					            $scope.showNotification(true, "Herzlich willkommen " + junky.name + "!");
					        })
					    }

					    // add mate form
					    $scope.addMate_amount = "";

					    $scope.addMate_submit = function () {
					        if (!isNaN($scope.addMate_amount)) {
					            $http.post("/rest/mate/add", { count: $scope.addMate_amount }).success(function () {
					                $scope.showNotification(true, "Es wurden " + $scope.addMate_amount + " Mate dem Bestand hinzugefügt!");
					                $scope.addMate_amount = "";
					                $scope.reloadAvailableMateCount();

					            });
					        }

					    }

					    $scope.reloadJunkies = function (callback) {
					        $scope.blockInput = true;
					        Junky.fetchAll().$promise.then(
									function (junkies) { //success
									    $scope.junkies = junkies;
									    $scope.blockInput = false;
									    $scope.updateTotalValues();

									    $scope.reloadAvailableMateCount();

									    if (callback) {
									        callback(true);
									    }
									}, function () { //failuret
									    $scope.blockInput = false;
									    $scope.junkies = {};

									    if (callback) {
									        callback(false);
									    } else {
									        $scope.showNotification(false, "Junkies konnten nicht geladen werden...");
									    }
									});
					    };

					    $scope.reloadAvailableMateCount = function () {
					        $http.get("/rest/meta").success(function (data) {
					            $scope.mateAvailable = data.bottlesAvailable;
					        });
					    };

					    $scope.updateTotalValues = function () {
					        var mate = 0;
					        var money = 0;
					        $scope.junkies.forEach(function (item) {
					            mate += item.count;
					            money += item.credit;
					        });

					        $scope.totalMate = mate;
					        $scope.totalMoney = (money / 100.0).toFixed(2);
					    };
					    
					    $scope.countMate = function (junkyId) {
					        $scope.blockInput = true;
					        
					        var successCallback = function () {
					            $scope.reloadJunkies(function (success) {
					                $scope.blockInput = false;
					                if (success) {
					                    $scope.showNotification(true, "Guten Durst!", 2000);
					                } else {
					                    $scope.showNotification(false, "Die Mate wurde abgezogen! Trotzdem ist ein unbekannter Fehler aufgetreten.", 3000);
					                }
					            });
					        }
					        
					        var errorCallback = function () {
					            $scope.blockInput = false;
					            $scope.showNotification(false, "Es ist ein unbekannter Fehler aufgetreten. Besteht eine Verbindung zum Server?", 3000);
					        };
					        
					        Junky.drink({},
					        	{'id': junkyId},
					        	successCallback,
					        	errorCallback
					        );
					    };

					    $scope.showNotification = function (isSuccess, text, duration) {
					        duration = typeof duration !== 'undefined' ? duration : 2000;

					        $scope.notificationClass = isSuccess ? "bg-success" : "bg-danger";
					        $scope.notificationText = text;
					        $scope.notificationVisible = true;

					        $timeout(function () {
					            $scope.notificationVisible = false;
					        }, duration);
					    };
					    
					    $scope.getJunky = function (id, success, unblockUiOnError, notificationOnError) {
					        unblockUiOnError = typeof unblockUiOnError !== 'undefined' ? unblockUiOnError : true;
					        notificationOnError = typeof notificationOnError !== 'undefined' ? notificationOnError : "Es ist ein unbekannter Fehler aufgetreten. Besteht eine Verbindung zum Server?";

					        var errorCallback = function () {
					            $scope.blockInput = !unblockUiOnError;
					            $scope.showNotification(false, notificationOnError, 3000);
					        };

					        return Junky.get({}, {
					            'id': id
					        }, success, errorCallback);
					    };

					    $scope.saveJunky = function (junky, success, unblockUiOnError, notificationOnError) {
					        unblockUiOnError = typeof unblockUiOnError !== 'undefined' ? unblockUiOnError : true;
					        notificationOnError = typeof notificationOnError !== 'undefined' ? notificationOnError : "Es ist ein unbekannter Fehler aufgetreten. Besteht eine Verbindung zum Server?";

					        var errorCallback = function () {
					            $scope.blockInput = !unblockUiOnError;
					            $scope.showNotification(false, notificationOnError, 3000);
					        };

					        junky.$save({}, success, errorCallback);
					    };
					    
					    
					    $scope.deleteJunky = function (id, name) {
					    	
					        var successCallback = function () {
					            $scope.reloadJunkies(function (success) {
					                $scope.blockInput = false;
					                if (success) {
					                    $scope.showNotification(true, "Der Junkie " + name + " wurde gelöscht.", 2000);
					                } else {
					                    $scope.showNotification(false, "Der Junkie " + name + " konnte nicht gelöscht werden.", 3000);
					                }
					            });
					        }
					        
					        var errorCallback = function () {
					            $scope.blockInput = false;
					            $scope.showNotification(false, "Es ist ein unbekannter Fehler aufgetreten. Besteht eine Verbindung zum Server?", 3000);
					        };
					    
					        bootbox.confirm("Soll der Junkie <strong>" + name + "</strong> wirklich gelöscht werden?", function(result) {
					            if (result) {
					                $scope.blockInput = true;
					                return Junky.delete({}, {
					                    'id': id
					                    }, successCallback, errorCallback);
					            }
					        });
					    };

					}]);
        }
    }

    return controller;
});
