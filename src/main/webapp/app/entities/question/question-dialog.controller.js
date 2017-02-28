(function() {
    'use strict';

    angular
        .module('quizApp')
        .controller('QuestionDialogController', QuestionDialogController);

    QuestionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Question', 'Category'];

    function QuestionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Question, Category) {
        var vm = this;

        vm.question = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.categories = Category.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.question.id !== null) {
                Question.update(vm.question, onSaveSuccess, onSaveError);
            } else {
                Question.save(vm.question, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('quizApp:questionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.version = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
