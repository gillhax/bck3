(function () {
    'use strict';

    angular
        .module('quizApp')
        .controller('SubcategoryDialogController', SubcategoryDialogController);

    SubcategoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Subcategory', 'Category'];

    function SubcategoryDialogController($timeout, $scope, $stateParams, $uibModalInstance, entity, Subcategory, Category) {
        var vm = this;

        vm.subcategory = entity;
        vm.clear = clear;
        vm.save = save;
        vm.categories = Category.query();

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function save() {
            vm.isSaving = true;
            if (vm.subcategory.id !== null) {
                Subcategory.update(vm.subcategory, onSaveSuccess, onSaveError);
            } else {
                Subcategory.save(vm.subcategory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            $scope.$emit('quizApp:subcategoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }


    }
})();
