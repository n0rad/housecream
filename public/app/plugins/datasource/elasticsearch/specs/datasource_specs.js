"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var lodash_1 = require("lodash");
var common_1 = require("test/lib/common");
var moment_1 = require("moment");
var angular_1 = require("angular");
var helpers_1 = require("test/specs/helpers");
var datasource_1 = require("../datasource");
common_1.describe('ElasticDatasource', function () {
    var ctx = new helpers_1.default.ServiceTestContext();
    var instanceSettings = { jsonData: {} };
    common_1.beforeEach(common_1.angularMocks.module('grafana.core'));
    common_1.beforeEach(common_1.angularMocks.module('grafana.services'));
    common_1.beforeEach(ctx.providePhase(['templateSrv', 'backendSrv', 'timeSrv']));
    common_1.beforeEach(common_1.angularMocks.inject(function ($q, $rootScope, $httpBackend, $injector) {
        ctx.$q = $q;
        ctx.$httpBackend = $httpBackend;
        ctx.$rootScope = $rootScope;
        ctx.$injector = $injector;
        $httpBackend.when('GET', /\.html$/).respond('');
    }));
    function createDatasource(instanceSettings) {
        instanceSettings.jsonData = instanceSettings.jsonData || {};
        ctx.ds = ctx.$injector.instantiate(datasource_1.ElasticDatasource, { instanceSettings: instanceSettings });
    }
    common_1.describe('When testing datasource with index pattern', function () {
        common_1.beforeEach(function () {
            createDatasource({ url: 'http://es.com', index: '[asd-]YYYY.MM.DD', jsonData: { interval: 'Daily', esVersion: '2' } });
        });
        common_1.it('should translate index pattern to current day', function () {
            var requestOptions;
            ctx.backendSrv.datasourceRequest = function (options) {
                requestOptions = options;
                return ctx.$q.when({ data: {} });
            };
            ctx.ds.testDatasource();
            ctx.$rootScope.$apply();
            var today = moment_1.default.utc().format("YYYY.MM.DD");
            common_1.expect(requestOptions.url).to.be("http://es.com/asd-" + today + '/_stats');
        });
    });
    common_1.describe('When issueing metric query with interval pattern', function () {
        var requestOptions, parts, header;
        common_1.beforeEach(function () {
            createDatasource({ url: 'http://es.com', index: '[asd-]YYYY.MM.DD', jsonData: { interval: 'Daily', esVersion: '2' } });
            ctx.backendSrv.datasourceRequest = function (options) {
                requestOptions = options;
                return ctx.$q.when({ data: { responses: [] } });
            };
            ctx.ds.query({
                range: {
                    from: moment_1.default.utc([2015, 4, 30, 10]),
                    to: moment_1.default.utc([2015, 5, 1, 10])
                },
                targets: [{ bucketAggs: [], metrics: [], query: 'escape\\:test' }]
            });
            ctx.$rootScope.$apply();
            parts = requestOptions.data.split('\n');
            header = angular_1.default.fromJson(parts[0]);
        });
        common_1.it('should translate index pattern to current day', function () {
            common_1.expect(header.index).to.eql(['asd-2015.05.30', 'asd-2015.05.31', 'asd-2015.06.01']);
        });
        common_1.it('should json escape lucene query', function () {
            var body = angular_1.default.fromJson(parts[1]);
            common_1.expect(body.query.bool.filter[1].query_string.query).to.be('escape\\:test');
        });
    });
    common_1.describe('When issueing document query', function () {
        var requestOptions, parts, header;
        common_1.beforeEach(function () {
            createDatasource({ url: 'http://es.com', index: 'test', jsonData: { esVersion: '2' } });
            ctx.backendSrv.datasourceRequest = function (options) {
                requestOptions = options;
                return ctx.$q.when({ data: { responses: [] } });
            };
            ctx.ds.query({
                range: { from: moment_1.default([2015, 4, 30, 10]), to: moment_1.default([2015, 5, 1, 10]) },
                targets: [{ bucketAggs: [], metrics: [{ type: 'raw_document' }], query: 'test' }]
            });
            ctx.$rootScope.$apply();
            parts = requestOptions.data.split('\n');
            header = angular_1.default.fromJson(parts[0]);
        });
        common_1.it('should set search type to query_then_fetch', function () {
            common_1.expect(header.search_type).to.eql('query_then_fetch');
        });
        common_1.it('should set size', function () {
            var body = angular_1.default.fromJson(parts[1]);
            common_1.expect(body.size).to.be(500);
        });
    });
    common_1.describe('When getting fields', function () {
        var requestOptions, parts, header;
        common_1.beforeEach(function () {
            createDatasource({ url: 'http://es.com', index: 'metricbeat' });
            ctx.backendSrv.datasourceRequest = function (options) {
                requestOptions = options;
                return ctx.$q.when({ data: {
                        metricbeat: {
                            mappings: {
                                metricsets: {
                                    _all: {},
                                    properties: {
                                        '@timestamp': { type: 'date' },
                                        beat: {
                                            properties: {
                                                name: { type: 'string' },
                                                hostname: { type: 'string' },
                                            }
                                        },
                                        system: {
                                            properties: {
                                                cpu: {
                                                    properties: {
                                                        system: { type: 'float' },
                                                        user: { type: 'float' },
                                                    }
                                                },
                                                process: {
                                                    properties: {
                                                        cpu: {
                                                            properties: {
                                                                total: { type: 'float' }
                                                            }
                                                        },
                                                        name: { type: 'string' },
                                                    }
                                                },
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } });
            };
        });
        common_1.it('should return nested fields', function () {
            ctx.ds.getFields({
                find: 'fields',
                query: '*'
            }).then(function (fieldObjects) {
                var fields = lodash_1.default.map(fieldObjects, 'text');
                common_1.expect(fields).to.eql([
                    '@timestamp',
                    'beat.name',
                    'beat.hostname',
                    'system.cpu.system',
                    'system.cpu.user',
                    'system.process.cpu.total',
                    'system.process.name'
                ]);
            });
            ctx.$rootScope.$apply();
        });
        common_1.it('should return fields related to query type', function () {
            ctx.ds.getFields({
                find: 'fields',
                query: '*',
                type: 'number'
            }).then(function (fieldObjects) {
                var fields = lodash_1.default.map(fieldObjects, 'text');
                common_1.expect(fields).to.eql([
                    'system.cpu.system',
                    'system.cpu.user',
                    'system.process.cpu.total'
                ]);
            });
            ctx.ds.getFields({
                find: 'fields',
                query: '*',
                type: 'date'
            }).then(function (fieldObjects) {
                var fields = lodash_1.default.map(fieldObjects, 'text');
                common_1.expect(fields).to.eql([
                    '@timestamp'
                ]);
            });
            ctx.$rootScope.$apply();
        });
    });
    common_1.describe('When issuing aggregation query on es5.x', function () {
        var requestOptions, parts, header;
        common_1.beforeEach(function () {
            createDatasource({ url: 'http://es.com', index: 'test', jsonData: { esVersion: '5' } });
            ctx.backendSrv.datasourceRequest = function (options) {
                requestOptions = options;
                return ctx.$q.when({ data: { responses: [] } });
            };
            ctx.ds.query({
                range: { from: moment_1.default([2015, 4, 30, 10]), to: moment_1.default([2015, 5, 1, 10]) },
                targets: [{
                        bucketAggs: [
                            { type: 'date_histogram', field: '@timestamp', id: '2' }
                        ],
                        metrics: [
                            { type: 'count' }
                        ], query: 'test'
                    }
                ]
            });
            ctx.$rootScope.$apply();
            parts = requestOptions.data.split('\n');
            header = angular_1.default.fromJson(parts[0]);
        });
        common_1.it('should not set search type to count', function () {
            common_1.expect(header.search_type).to.not.eql('count');
        });
        common_1.it('should set size to 0', function () {
            var body = angular_1.default.fromJson(parts[1]);
            common_1.expect(body.size).to.be(0);
        });
    });
    common_1.describe('When issuing metricFind query on es5.x', function () {
        var requestOptions, parts, header, body;
        common_1.beforeEach(function () {
            createDatasource({ url: 'http://es.com', index: 'test', jsonData: { esVersion: '5' } });
            ctx.backendSrv.datasourceRequest = function (options) {
                requestOptions = options;
                return ctx.$q.when({
                    data: {
                        responses: [{ aggregations: { "1": [{ buckets: { text: 'test', value: '1' } }] } }]
                    }
                });
            };
            ctx.ds.metricFindQuery('{"find": "terms", "field": "test"}');
            ctx.$rootScope.$apply();
            parts = requestOptions.data.split('\n');
            header = angular_1.default.fromJson(parts[0]);
            body = angular_1.default.fromJson(parts[1]);
        });
        common_1.it('should not set search type to count', function () {
            common_1.expect(header.search_type).to.not.eql('count');
        });
        common_1.it('should set size to 0', function () {
            common_1.expect(body.size).to.be(0);
        });
        common_1.it('should not set terms aggregation size to 0', function () {
            common_1.expect(body['aggs']['1']['terms'].size).to.not.be(0);
        });
    });
});
