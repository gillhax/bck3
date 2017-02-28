(function () {
    'use strict';

    angular
        .module('quizApp')
        .controller('SubcategoryDeleteController', SubcategoryDeleteController);

    SubcategoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'Subcategory'];

    function SubcategoryDeleteController($uibModalInstance, entity, Subcategory) {
        var vm = this;

        vm.subcategory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete(id) {
            Subcategory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
