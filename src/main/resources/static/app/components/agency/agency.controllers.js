angular.module('flix.agency')
    .controller('AgencyListController',
        ['AgencyService', '$scope', 'Flash', 'ngTableParams', '$location', 'dialogs', AgencyListController])
    .controller('AgencyDisplayController',
        ['AgencyService', '$scope', 'Flash', '$location', '$route', '$routeParams', AgencyDisplayController])
    .controller('AgencyEditController',
        ['AgencyService', '$scope', 'Flash', 'data', '$modalInstance', AgencyEditController]);

function AgencyListController(AgencyService, $scope, Flash, ngTableParams, $location, dialogs) {
    $scope.remove = remove;
    $scope.display = display;
    $scope.edit = edit;
    $scope.search = loadTable;
    $scope.create = createAgency;
    $scope.data = {};
    $scope.data.searchParams = {};
    initializePage();

    var initialCondition = createParamsForTableInitialization();

    $scope.tableParams = new ngTableParams(
        initialCondition
        ,
        {
            getData: function ($defer, params) {

                transferSearchParamsFromNgTableInternalToSearchParams();

                AgencyService.search($scope.data.searchParams).$promise.then(onFulfillment, onRejection);

                function onFulfillment(searchResponse) {
                    params.total(searchResponse.totalCount);
                    $defer.resolve(searchResponse.items);
                    $scope.data.searchParams.allPages = Math.ceil(searchResponse.totalCount / $scope.data.searchParams.pageSize);
                }

                function onRejection() {
                    Flash.create('danger', 'Problem occurred while searching agencies')
                }

                function transferSearchParamsFromNgTableInternalToSearchParams() {

                    $scope.data.searchParams.pageSize = params.$params.count;
                    $scope.data.searchParams.pageNumber = params.$params.page;
                    var key = Object.keys(params.$params.sorting)[0];
                    $scope.data.searchParams.orderBy = key;
                    $scope.data.searchParams.sortDirection = params.$params.sorting[key].toUpperCase();

                }
            }
        }
    );

    function createParamsForTableInitialization() {
        var params = {};
        params.sorting = getSorting();
        params.page = getPageNumber();
        params.count = getPageSize();
        return params;

        function getSorting() {

            var order = 'name';
            var direction = getSortOrderDirection();

            var sortingObject = {};
            sortingObject[order] = direction;
            return sortingObject;

            function getSortOrderDirection() {
                var sortOrder = "ASC";
                if($location.search().hasOwnProperty('sortDirection')) {
                    if($location.search().sortDirection.toString().toUpperCase() === 'DESC') {
                        sortOrder = 'DESC';
                    }
                }
                return sortOrder;

            }

        }

        function getPageSize() {
            var pageSize = 10;
            if($location.search().hasOwnProperty('pageSize')) {
                var size = $location.search().pageSize;
                if((size === '25') || (size === '50') || (size === '100')) {
                    pageSize = size;
                }
            }
            return pageSize;
        }

        function getPageNumber() {
            var pageNumber = 1;
            if($location.search().hasOwnProperty('pageNumber')) {
                var num = $location.search().pageNumber;
                if (num > 0) {
                    pageNumber = num;
                }
            }
            return pageNumber;
        }

    }

    function createAgency() {
        dialogs.create('app/components/agency/agency.edit.html', 'AgencyEditController', {}, {}, '')
            .result
            .then(resolve);

        function resolve() {
            loadTable();
        }
    }

    function loadTable() {
        $scope.tableParams.reload();
    }

    function display(code) {
        $location.url('/agency/display/' + code);
    }

    function edit(code) {

        dialogs.create('app/components/agency/agency.edit.html', 'AgencyEditController', {'code': code}, {}, '')
            .result
            .then(resolve);

        function resolve() {
            loadTable();
        }
    }

    function initializePage() {
        $scope.data.searchParams.name = $location.search().name;
        $scope.data.searchParams.contactPerson = $location.search().contactPerson;

        $scope.data.searchParams.country = $location.search().country;
        $scope.data.searchParams.countryCode = $location.search().countryCode;

        $scope.data.searchParams.city = $location.search().city;
        $scope.data.searchParams.street = $location.search().street;

        $scope.data.searchParams.settlementCurrency = $location.search().settlementCurrency;
        $scope.data.searchParams.code = $location.search().code;
    }

    function remove(code) {
        AgencyService.remove({code : code})
            .$promise.then(onFulfillment, onRejection);

        function onFulfillment() {
            Flash.create('success', 'agency with code: +' + code + " was removed successfully");
            loadTable();
        }

        function onRejection(response) {
            if(response.status === 404) {
                Flash.create('warning', 'agency with code: ' + code + ' was not found.');
                loadTable();
            } else {
                Flash.create('danger', "some thing wrong happened during removal of agency with code: " + code);
            }
        }
    }

}

function AgencyDisplayController(AgencyService, $scope, Flash, $location, $route, $routeParams) {
    $scope.back = back;
    $scope.data = {};
    $scope.data.agency = {};


    load($routeParams.code);

    function load(code) {
        AgencyService.load({code : code})
            .$promise.then(onFulfillment, onRejection);

        function onFulfillment(response) {
            $scope.data.agency = response;
        }

        function onRejection(response) {
            if(response.status === 404) {
                Flash.create('warning', 'Cannot find agency with code : ' + code);
            } else {
                Flash.create('danger', "Error while displaying agency with code: "+ code);
            }
        }
    }

    function back() {
        $location.path("/agency/list");
    }

}

function AgencyEditController(AgencyService, $scope, Flash, data, $modalInstance) {
    $scope.cancel = cancel;
    $scope.modal = {};
    $scope.data = {};
    $scope.data.agency = {};

    if (data.code) {
        updateAgency();
    } else {
        createAgency();
    }

    function updateAgency() {
        $scope.modal.title = 'Edit Agency';
        $scope.mode = 'update';
        $scope.do = update;

        load(data.code);

        function load(code) {

            AgencyService.load({code: code})
                .$promise.then(onFulfillment, onRejection);


            function onFulfillment(response) {
                $scope.data.agency = response;
            }

            function onRejection(response) {
                if (response.status === 404) {
                    Flash.create('warning', 'Cannot find agency with code : ' + code);
                } else {
                    Flash.create('danger', "Error while displaying agency with code: " + code);
                }
                cancel();
            }
        }

        function update() {
            if (($("#agency_form").validate().form())) {

                AgencyService.update({code: $scope.data.agency.code},
                    {name: $scope.data.agency.name,
                    contactPerson: $scope.data.agency.contactPerson,
                    country: $scope.data.agency.country,
                    countryCode: $scope.data.agency.countryCode,
                    city: $scope.data.agency.city,
                    street: $scope.data.agency.street,
                    settlementCurrency: $scope.data.agency.settlementCurrency})
                    .$promise.then(onFulfilment, onRejection);

                function onFulfilment() {
                    $modalInstance.close();
                }

                function onRejection(response) {
                    if (response.status === 404) {
                        Flash.create('warning', 'Cannot find agency with code : ' + $scope.data.agency.code);
                    } else {
                        Flash.create('danger', "Error while displaying agency with code: " + $scope.data.agency.code);
                    }
                }
            }

        }
    }

    function createAgency() {
        $scope.modal.title = 'Create Agency'
        $scope.mode = 'Save';
        $scope.do = create;

        function create() {
            if (($("#agency_form").validate().form())) {

                AgencyService.create({},
                    {name: $scope.data.agency.name,
                    contactPerson: $scope.data.agency.contactPerson,
                    country: $scope.data.agency.country,
                    countryCode: $scope.data.agency.countryCode,
                    city: $scope.data.agency.city,
                    street: $scope.data.agency.street,
                    settlementCurrency: $scope.data.agency.settlementCurrency
                    })
                    .$promise.then(onFulfilment, onRejection);

                function onFulfilment() {
                    $modalInstance.close();
                }

                function onRejection(response) {
                    if (response.status === 404) {
                        Flash.create('warning', 'Cannot find agency with code : ' + $scope.data.agency.code);
                    } else {
                        Flash.create('danger', "Error while displaying agency with code: " + $scope.data.agency.code);
                    }
                }
            }
        }
    }

    function cancel() {
        $modalInstance.dismiss();
    }

}