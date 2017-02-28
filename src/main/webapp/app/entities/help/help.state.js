(function() {
    'use strict';

    angular
        .module('quizApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('help', {
            parent: 'entity',
            url: '/help',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'quizApp.help.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/help/helps.html',
                    controller: 'HelpController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('help');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('help-detail', {
            parent: 'entity',
            url: '/help/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'quizApp.help.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/help/help-detail.html',
                    controller: 'HelpDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('help');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Help', function($stateParams, Help) {
                    return Help.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'help',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('help-detail.edit', {
            parent: 'help-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/help/help-dialog.html',
                    controller: 'HelpDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Help', function(Help) {
                            return Help.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('help.new', {
            parent: 'help',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/help/help-dialog.html',
                    controller: 'HelpDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                title: null,
                                description: null,
                                image: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('help', null, { reload: 'help' });
                }, function() {
                    $state.go('help');
                });
            }]
        })
        .state('help.edit', {
            parent: 'help',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/help/help-dialog.html',
                    controller: 'HelpDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Help', function(Help) {
                            return Help.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('help', null, { reload: 'help' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('help.delete', {
            parent: 'help',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/help/help-delete-dialog.html',
                    controller: 'HelpDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Help', function(Help) {
                            return Help.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('help', null, { reload: 'help' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
