(function () {
    'use strict';

    angular
        .module('quizApp')
        .controller('OfferImageDialogController', OfferImageDialogController);

    OfferImageDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Offer', 'imageId'];

    function OfferImageDialogController($timeout, $scope, $stateParams, $uibModalInstance, entity, Offer, imageId) {
        var vm = this;

        vm.offer = entity;
        vm.imageId = imageId;


    }
})();
