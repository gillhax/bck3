(function() {
    'use strict';
    angular
        .module('quizApp')
        .factory('Avatar', Avatar);

    Avatar.$inject = ['$resource'];

    function Avatar ($resource) {
        var resourceUrl =  'api/avatars/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
