(function() {
    'use strict';

    angular
        .module('quizApp')
        .controller('AvatarDeleteController',AvatarDeleteController);

    AvatarDeleteController.$inject = ['$uibModalInstance', 'entity', 'Avatar'];

    function AvatarDeleteController($uibModalInstance, entity, Avatar) {
        var vm = this;

        vm.avatar = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Avatar.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
