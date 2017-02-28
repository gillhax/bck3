(function () {
    'use strict';
    angular
        .module('quizApp')
        .factory('Subcategory', Subcategory);

    Subcategory.$inject = ['$resource'];

    function Subcategory($resource) {
        var resourceUrl = 'api/subcategories/:id';

        return $resource(resourceUrl, {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': {method: 'PUT'}
        });
    }
})();
