(function() {
    'use strict';

    angular
        .module('quizApp')
        .controller('AvatarDialogController', AvatarDialogController);

    AvatarDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Avatar'];

    function AvatarDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Avatar) {
        var vm = this;

        vm.avatar = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.avatar.id !== null) {
                Avatar.update(vm.avatar, onSaveSuccess, onSaveError);
            } else {
                Avatar.save(vm.avatar, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('quizApp:avatarUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
