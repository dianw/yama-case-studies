'use strict';

angular.module('websocketApp').config(function ($stateProvider) {
	$stateProvider.state('backend.application', {
		url: '/admin/application',
		templateUrl: 'backend/admin/application/application.html',
		controller: 'ApplicationCtrl'
	});
});
