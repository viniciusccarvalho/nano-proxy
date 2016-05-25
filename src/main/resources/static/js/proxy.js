var nanoproxy = angular.module('nanoproxy');
nanoproxy.controller('proxy', function($scope, $routeParams, $http, $interval){

    $scope.promise = {}
    $scope.stats = {}
    $scope.last = {}
    $scope.console = false;
    $scope.received =[[]];
    $scope.sent = [[]];
    $scope.labels= [];
    $scope.series = ['Bytes Received'];
    $scope.sentColors = [{ // dark grey
        fillColor: 'rgba(208,12,9,0.2)',
        strokeColor: 'rgba(208,12,9,1)',
        pointColor: 'rgba(208,12,9,1)',
        pointStrokeColor: '#fff',
        pointHighlightFill: '#fff',
        pointHighlightStroke: 'rgba(208,12,9,1)'
    }]
    $scope.maxPoints = 100;
    $scope.options = {
        animation: false,
        showScale: true,
        showTooltips: false,
        pointDot: false,
        datasetStrokeWidth: 0.5
    };

    $http.get('/servers/'+$routeParams.id).then(function success(response){
        $scope.proxy = response.data;

    }, function error(response){
        console.error(response);
    });

    $scope.updateStats = function(){
        $http.get('/servers/'+$scope.proxy.id+'/stats').then(function success(response){



            if($scope.received.length && $scope.received[0].length >= $scope.maxPoints){
                $scope.labels = $scope.labels.slice(1);
                $scope.sent[0] = $scope.sent[0].slice(1);
                $scope.received[0] = $scope.received[0].slice(1);
            }

            if(typeof $scope.last.bytesSent != 'undefined'){
                stats = response.data;
                deltaReceived = stats.bytesReceived - $scope.last.bytesReceived;
                deltaSent = stats.bytesSent - $scope.last.bytesSent;
                $scope.labels.push('');
                $scope.received[0].push(deltaReceived/1024);
                $scope.sent[0].push(deltaSent/1024);
            }

            $scope.last = response.data;


        }, function error(response){
            console.error(response);
        });
    }

    $scope.getRandomValue = function(data){
        var l = data.length, previous = l ? data[l - 1] : 50;
        var y = previous + Math.random() * 10 - 5;
        return y < 0 ? 0 : y > 100 ? 100 : y;
    }

    $scope.toggleStatus = function($event){
        $http.post('/servers/'+$scope.proxy.id+'/status',{active: $event}).then(function success(response){

        }, function error(response){
            console.error(response);
        });
    }
    $scope.promise = $interval(function(){$scope.updateStats()},250);
    $scope.toggleConsole = function(){
        $scope.console = !$scope.console;
    }

    //$scope.transferChart = new transferChart('transfer_chart');
    //
    //function transferChart(element){
    //    var self = this;
    //
    //    self.chart = new google.visualization.AreaChart(document.getElementById(element));
    //    self.last = [];
    //    self.delta = [];
    //
    //    var update = function(connectionStats){
    //        if(self.last.length == 0){
    //            self.last.push(connectionStats.bytesReceived);
    //            self.last.push(connectionStats.bytesSent);
    //        }else{
    //            self.delta[0] = connectionStats.bytesReceived - self.last[0];
    //            self.delta[1] = connectionStats.bytesSent - self.last[1];
    //            self.updateChart();
    //        }
    //    }
    //
    //    var updateChart = function(){
    //        if(self.data.getNumberOfRows() > 600){
    //            self.data.removeRows(0,1);
    //        }
    //    }
    //
    //}




});