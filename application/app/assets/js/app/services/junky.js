'use strict';

define([ 'angular', //
'ngResource', //
'image!/assets/dynamic/image1', //
'image!/assets/dynamic/image2', //
'image!/assets/dynamic/image3' ],//
function(angular, ngResource, img1, img2, img3) {
	var service = {
		attach : function(module) {
			module.factory('Junky', [ '$resource', function($resource) {
				var Junky = $resource('rest/junkies/:junkyId', {
					junkyId : '@id'
				}, {
					fetchAll : {
						method : 'GET',
						isArray : true
					},

				});

				Junky.prototype.setImages = function() {
					var list = [];

					var bottles = this.count;
					var level3Images = Math.floor(bottles / 160);
					var level2Images = Math.floor((bottles % 160) / 20);
					var level1Images = bottles % 20;

					for (var i = 0; i < level3Images; i++) {
						list.push(img3.cloneNode());
						list.push(" ");
					}
					for (var i = 0; i < level2Images; i++) {
						list.push(img2.cloneNode());
						list.push(" ");
					}
					for (var i = 0; i < level1Images; i++) {
						list.push(img1.cloneNode());
						list.push(" ");
					}
					angular.element("#pictures-junky" + this.id).empty();
					angular.element("#pictures-junky" + this.id).append(list);
				}

				Junky.prototype.getCreditColor = function() {
					return this.credit > 0 ? "green" : "red";
				}

				return Junky;
			} ]);
		}
	}

	return service;
});
