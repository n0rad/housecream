"use strict";
///<reference path="../../../../headers/common.d.ts" />
Object.defineProperty(exports, "__esModule", { value: true });
var lodash_1 = require("lodash");
var angular_1 = require("angular");
var TestDataDatasource = (function () {
    /** @ngInject */
    function TestDataDatasource(backendSrv, $q) {
        this.backendSrv = backendSrv;
        this.$q = $q;
    }
    TestDataDatasource.prototype.query = function (options) {
        var queries = lodash_1.default.filter(options.targets, function (item) {
            return item.hide !== true;
        }).map(function (item) {
            return {
                refId: item.refId,
                scenarioId: item.scenarioId,
                intervalMs: options.intervalMs,
                maxDataPoints: options.maxDataPoints,
                stringInput: item.stringInput,
                jsonInput: angular_1.default.fromJson(item.jsonInput),
            };
        });
        if (queries.length === 0) {
            return this.$q.when({ data: [] });
        }
        return this.backendSrv.post('/api/tsdb/query', {
            from: options.range.from.valueOf().toString(),
            to: options.range.to.valueOf().toString(),
            queries: queries,
        }).then(function (res) {
            var data = [];
            if (res.results) {
                lodash_1.default.forEach(res.results, function (queryRes) {
                    for (var _i = 0, _a = queryRes.series; _i < _a.length; _i++) {
                        var series = _a[_i];
                        data.push({
                            target: series.name,
                            datapoints: series.points
                        });
                    }
                });
            }
            return { data: data };
        });
    };
    TestDataDatasource.prototype.annotationQuery = function (options) {
        return this.backendSrv.get('/api/annotations', {
            from: options.range.from.valueOf(),
            to: options.range.to.valueOf(),
            limit: options.limit,
            type: options.type,
        });
    };
    return TestDataDatasource;
}());
exports.TestDataDatasource = TestDataDatasource;
