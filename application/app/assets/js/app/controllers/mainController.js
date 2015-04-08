'use strict';

define([ 'angular', 'spin' ], function(angular,Spinner) {
	var controller = {
		attach : function(module) {
			module.controller('MainViewController', [
					'$scope',
					'$http',
					'Junky',
					function($scope, $http, Junky) {
						$scope.junkies = [];
						$scope.mateAvailable = 0;
						$scope.totalMate = 0;
						$scope.totalMoney = 0;
						$scope.blockInput = true;
						
						$scope.buttonIdForGoodThirst = undefined;
						
						$scope.init = function() {
							$scope.createSpinner();
							$scope.reloadJunkies();
						}
						
						$scope.createSpinner = function() {
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

						$scope.payment_submit = function() {
							var junky = Junky.get({}, {
								id : $scope.payment_userId
							}, function() {
								var curAmount = junky.credit;
								var payment = $scope.payment_amount.replace(
										",", ".");

								if (!isNaN(payment)) {
									var amount = payment * 100;

									junky.credit = curAmount + amount;
									junky.$save({}, function() {
										$scope.payment_userId = -1;
										$scope.payment_amount = "10.00";
										$scope.reloadJunkies();
									});
								}
								;
							})
						}

						// new user form
						$scope.newUser_name = "";

						$scope.newUser_submit = function() {
							var junky = new Junky({
								name : $scope.newUser_name
							});

							$scope.newUser_name = "";

							junky.$save({}, function() {
								$scope.reloadJunkies();
							});
						}
						
						// add mate form
						$scope.addMate_amount = "";

						$scope.addMate_submit = function() {
							if(!isNaN($scope.addMate_amount)) {
								$http.post("/rest/mate/add", {count: $scope.addMate_amount}).success(function() {
									$scope.addMate_amount = "";
									$scope.reloadAvailableMateCount();
								});
							}

						}

						$scope.reloadJunkies = function() {
							$scope.blockInput = true;
							Junky.fetchAll().$promise.then(function(junkies) {
								$scope.junkies = junkies;
								$scope.blockInput = false;
								$scope.updateTotalValues();
							
								
								if($scope.buttonIdForGoodThirst != undefined) {
									setTimeout(function() {
										$scope.wishGoodThirst($scope.buttonIdForGoodThirst);
										$scope.buttonIdForGoodThirst = undefined;
									},100);

								}
								
								$scope.reloadAvailableMateCount();
							});
						};
						
						$scope.reloadAvailableMateCount = function() {
							$http.get("/rest/meta").success(function(data) {
								$scope.mateAvailable = data.bottlesAvailable;
							});
						};

						$scope.updateTotalValues = function() {
							var mate = 0;
							var money = 0;
							$scope.junkies.forEach(function(item) {
								mate += item.count;
								money += item.credit;
							});
							
							$scope.totalMate = mate;
							$scope.totalMoney = (money /100.0).toFixed(2);
						};

						$scope.countMate = function(junkyId) {
							$scope.buttonIdForGoodThirst = "#btn_"+junkyId;
							$scope.blockInput = true;
							var junky = Junky.get({}, {
								'id' : junkyId
							}, function() {

								junky.count++;
								junky.credit -= 75;
								junky.$save({}, function() {
									$scope.reloadJunkies();
								});
							});

						};
						
						$scope.wishGoodThirst = function(btn) {
							var mateButton = btn;
							var origText = $(mateButton).html();

							angular.element(mateButton).html(":-)");
							angular.element(mateButton).addClass("btn-success");
							angular.element(mateButton).removeClass("btn-warning");

							setTimeout(function() {
								angular.element(mateButton).html(origText);
								angular.element(mateButton).removeClass("btn-success");
								angular.element(mateButton).addClass("btn-warning");
							}, 3000);
						}
					} ]);
		}
	}

	return controller;
});
