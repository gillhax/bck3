(function() {
    'use strict';

    angular
        .module('quizApp')
        .controller('PlayerDialogController', PlayerDialogController);

    PlayerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Player', 'Avatar'];

    function PlayerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Player, Avatar) {
        var vm = this;

        vm.player = entity;
        vm.clear = clear;
        vm.save = save;
        vm.avatars = Avatar.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.player.id !== null) {
                Player.update(vm.player, onSaveSuccess, onSaveError);
            } else {
                Player.save(vm.player, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('quizApp:playerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
