(function() {
    'use strict';

    angular
        .module('quizApp')
        .controller('PlayerDetailController', PlayerDetailController);

    PlayerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Player', 'Avatar'];

    function PlayerDetailController($scope, $rootScope, $stateParams, previousState, entity, Player, Avatar) {
        var vm = this;

        vm.player = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('quizApp:playerUpdate', function(event, result) {
            vm.player = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
