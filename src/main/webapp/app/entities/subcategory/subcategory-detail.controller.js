(function () {
    'use strict';

    angular
        .module('quizApp')
        .controller('SubcategoryDetailController', SubcategoryDetailController);

    SubcategoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Subcategory', 'Category'];

    function SubcategoryDetailController($scope, $rootScope, $stateParams, previousState, entity, Subcategory, Category) {
        var vm = this;

        vm.subcategory = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('quizApp:subcategoryUpdate', function (event, result) {
            vm.subcategory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
