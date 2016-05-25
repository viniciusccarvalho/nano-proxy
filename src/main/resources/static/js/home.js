var nanoproxy = angular.module('nanoproxy');

nanoproxy.controller('home',function($scope, $http){
    $scope.proxies = [];
    $scope.addProxy = function(){
        $http.post('/servers',$scope.proxy).then(function success(response){
            $scope.proxies.push(response.data);
            $scope.proxy = {};
            $scope.proxyForm.$setPristine();
            $scope.$apply();
        },
        function error(response){
            console.error(response);
        });
    }
    $http.get("/servers").then(function success(response){
        angular.forEach(response.data,function(p,key){
            $scope.proxies.push(p);
        }, function error(response){

        })
    });

});