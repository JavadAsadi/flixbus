angular.module('flix.agency', ["ngResource", "ngFlash", "ngTable", "ngRoute", "ui.bootstrap", "dialogs.main"])
    .config(function ($routeProvider) {
        $routeProvider
            .when('/agency/list', {
                templateUrl : 'app/components/agency/agency.list.html',
                controller : "AgencyListController",
                reloadOnSearch: false
            })
            .when('/agency/display/:code', {
                templateUrl : 'app/components/agency/agency.display.html',
                controller : 'AgencyDisplayController'
            })
    });

