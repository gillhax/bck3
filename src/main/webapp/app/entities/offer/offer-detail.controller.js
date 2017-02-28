(function () {
    'use strict';

    angular
        .module('quizApp')
        .controller('OfferDetailController', OfferDetailController);

    OfferDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Offer'];

    function OfferDetailController($scope, $rootScope, $stateParams, previousState, entity, Offer) {
        var vm = this;

        vm.offer = entity;
        vm.previousState = previousState.name;

    }
})();
