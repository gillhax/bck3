(function() {
    'use strict';

    angular
        .module('quizApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('avatar', {
            parent: 'entity',
            url: '/avatar',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'quizApp.avatar.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/avatar/avatars.html',
                    controller: 'AvatarController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('avatar');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('avatar-detail', {
            parent: 'entity',
            url: '/avatar/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'quizApp.avatar.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/avatar/avatar-detail.html',
                    controller: 'AvatarDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('avatar');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Avatar', function($stateParams, Avatar) {
                    return Avatar.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'avatar',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('avatar-detail.edit', {
            parent: 'avatar-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/avatar/avatar-dialog.html',
                    controller: 'AvatarDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Avatar', function(Avatar) {
                            return Avatar.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('avatar.new', {
            parent: 'avatar',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/avatar/avatar-dialog.html',
                    controller: 'AvatarDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                path: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('avatar', null, { reload: 'avatar' });
                }, function() {
                    $state.go('avatar');
                });
            }]
        })
        .state('avatar.edit', {
            parent: 'avatar',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/avatar/avatar-dialog.html',
                    controller: 'AvatarDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Avatar', function(Avatar) {
                            return Avatar.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('avatar', null, { reload: 'avatar' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('avatar.delete', {
            parent: 'avatar',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/avatar/avatar-delete-dialog.html',
                    controller: 'AvatarDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Avatar', function(Avatar) {
                            return Avatar.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('avatar', null, { reload: 'avatar' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
