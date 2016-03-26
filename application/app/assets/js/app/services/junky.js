'use strict';

define(['angular', //
'ngResource', //
'image!/assets/dynamic/image1', //
'image!/assets/dynamic/image2', //
'image!/assets/dynamic/image3'],//
function (angular, ngResource, img1, img2, img3) {
    var service = {
        attach: function (module) {
            module.factory('Junky', ['$resource', function ($resource) {
                var Junky = $resource('rest/junkies/:junkyId', {
                    junkyId: '@id'
                }, {
                    fetchAll: {
                        method: 'GET',
                        isArray: true
                    },
                    drink: {
                    	method: 'GET',
                    	url: 'rest/junkies/:junkyId/drink'
                    }
                });

                Junky.prototype.btnText = "Durst!";
                Junky.prototype.btnClass = "btn-warning";

				Junky.prototype.getLevelCount = function(level) {
                    var bottles = this.count;
					switch(level) {
						case 1:
							return bottles % 20;
							break;
						case 2:
							return Math.floor((bottles % 160) / 20);
							break;
						case 3:
							return Math.floor(bottles / 160);
							break;
					}
				}

                Junky.prototype.getCreditColor = function () {
                    return this.credit > 0 ? "green" : "red";
                }

                return Junky;
            }]);
        }
    }

    return service;
});
