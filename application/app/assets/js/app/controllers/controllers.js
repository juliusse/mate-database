'use strict';

define(
		['angular', 'app/controllers/mainController',
				'app/controllers/settingsController',
				'app/controllers/logController', ], function (angular, main,
				settings, log) {
				    var controllers = angular.module('mateControllers',
                            ['mateServices']);
				    main.attach(controllers);
				    settings.attach(controllers);
				    log.attach(controllers);
				});
