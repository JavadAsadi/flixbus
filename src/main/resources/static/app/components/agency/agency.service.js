angular.module('flix.agency')
    .factory('AgencyService', ['$resource', agencyService]);

function agencyService($resource) {
    var agencyService = {
        search: {method: 'GET', url: 'api/agency'},
        remove: {method: 'DELETE', url: 'api/agency/:code'},
        load: {method: 'GET', url: 'api/agency/:code'},
        update: {method:'PUT', url:'api/agency/:code'},
        create: {method:'POST', url:'api/agency'}
    };
    return $resource('', {}, agencyService);
}