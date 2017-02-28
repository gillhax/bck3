(function () {
    'use strict';

    angular
        .module('quizApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('offer', {
                parent: 'entity',
                url: '/offer?page&sort&search',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'quizApp.offer.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/offer/offers.html',
                        controller: 'OfferController',
                        controllerAs: 'vm'
                    }
                },
                params: {
                    page: {
                        value: '1',
                        squash: true
                    },
                    sort: {
                        value: 'date,desc',
                        squash: true
                    },
                    search: null
                },
                resolve: {
                    pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                        return {
                            page: PaginationUtil.parsePage($stateParams.page),
                            sort: $stateParams.sort,
                            predicate: PaginationUtil.parsePredicate($stateParams.sort),
                            ascending: PaginationUtil.parseAscending($stateParams.sort),
                            search: $stateParams.search
                        };
                    }],
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('offer');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('offer-detail', {
                parent: 'entity',
                url: '/offer/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'quizApp.offer.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/offer/offer-detail.html',
                        controller: 'OfferDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('offer');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Offer', function ($stateParams, Offer) {
                        return Offer.get({id: $stateParams.id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'offer',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('offer.delete', {
                parent: 'offer',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/offer/offer-delete-dialog.html',
                        controller: 'OfferDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['Offer', function (Offer) {
                                return Offer.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('offer', null, {reload: 'offer'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('offer-detail.image', {
                parent: 'offer-detail',
                url: '/image/',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/offer/offer-image-dialog.html',
                        controller: 'OfferImageDialogController',
                        controllerAs: 'vm',
                        size: 'lg',
                        resolve: {
                            entity: ['Offer', function (Offer) {
                                return Offer.get({id: $stateParams.id}).$promise;
                            }],
                        }
                    }).result.then(function () {
                        $state.go('offer', null, {reload: 'offer'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            });
    }

})();
