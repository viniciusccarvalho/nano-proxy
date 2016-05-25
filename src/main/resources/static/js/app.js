var nanoproxy = angular.module('nanoproxy', ['ngRoute','ui.materialize','pageslide-directive','chart.js'])

nanoproxy.config(function ($routeProvider, $httpProvider) {
    $routeProvider
        .when('/', {
            templateUrl: 'views/home.html',
            controller: 'home'
        }).when('/proxy/:id',{
            templateUrl: 'views/proxy.html',
            controller: 'proxy'
    })
    $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
});