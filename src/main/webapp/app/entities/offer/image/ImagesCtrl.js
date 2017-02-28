angular.module('quizApp')
    .controller('ImagesCtrl', function ($scope, Lightbox) {

        $scope.openLightboxModal = function (images, index) {
            Lightbox.openModal(images, index);
        };
    });
