(function() {
    'use strict';

    angular
        .module('quizApp')
        .controller('AvatarController', AvatarController);

    AvatarController.$inject = ['$scope', '$state', 'Avatar'];

    function AvatarController ($scope, $state, Avatar) {
        var vm = this;

        vm.avatars = [];

        loadAll();

        function loadAll() {
            Avatar.query(function(result) {
                vm.avatars = result;
                vm.searchQuery = null;
            });
        }
    }
})();
