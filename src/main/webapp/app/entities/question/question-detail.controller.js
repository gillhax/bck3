(function() {
    'use strict';

    angular
        .module('quizApp')
        .controller('QuestionDetailController', QuestionDetailController);

    QuestionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Question', 'Category'];

    function QuestionDetailController($scope, $rootScope, $stateParams, previousState, entity, Question, Category) {
        var vm = this;

        vm.question = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('quizApp:questionUpdate', function(event, result) {
            vm.question = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
