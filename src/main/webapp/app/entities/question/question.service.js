(function() {
    'use strict';
    angular
        .module('quizApp')
        .factory('Question', Question);

    Question.$inject = ['$resource', 'DateUtils'];

    function Question ($resource, DateUtils) {
        var resourceUrl =  'api/questions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.version = DateUtils.convertLocalDateFromServer(data.version);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.version = DateUtils.convertLocalDateToServer(copy.version);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.version = DateUtils.convertLocalDateToServer(copy.version);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
