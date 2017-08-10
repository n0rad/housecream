"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var common_1 = require("test/lib/common");
require("../all");
var lodash_1 = require("lodash");
var helpers_1 = require("test/specs/helpers");
var core_1 = require("app/core/core");
common_1.describe('VariableSrv init', function () {
    var ctx = new helpers_1.default.ControllerTestContext();
    common_1.beforeEach(common_1.angularMocks.module('grafana.core'));
    common_1.beforeEach(common_1.angularMocks.module('grafana.controllers'));
    common_1.beforeEach(common_1.angularMocks.module('grafana.services'));
    common_1.beforeEach(common_1.angularMocks.module(function ($compileProvider) {
        $compileProvider.preAssignBindingsEnabled(true);
    }));
    common_1.beforeEach(ctx.providePhase(['datasourceSrv', 'timeSrv', 'templateSrv', '$location']));
    common_1.beforeEach(common_1.angularMocks.inject(function ($rootScope, $q, $location, $injector) {
        ctx.$q = $q;
        ctx.$rootScope = $rootScope;
        ctx.$location = $location;
        ctx.variableSrv = $injector.get('variableSrv');
        ctx.$rootScope.$digest();
    }));
    function describeInitScenario(desc, fn) {
        common_1.describe(desc, function () {
            var scenario = {
                urlParams: {},
                setup: function (setupFn) {
                    scenario.setupFn = setupFn;
                }
            };
            common_1.beforeEach(function () {
                scenario.setupFn();
                ctx.datasource = {};
                ctx.datasource.metricFindQuery = common_1.sinon.stub().returns(ctx.$q.when(scenario.queryResult));
                ctx.datasourceSrv.get = common_1.sinon.stub().returns(ctx.$q.when(ctx.datasource));
                ctx.datasourceSrv.getMetricSources = common_1.sinon.stub().returns(scenario.metricSources);
                ctx.$location.search = common_1.sinon.stub().returns(scenario.urlParams);
                ctx.dashboard = { templating: { list: scenario.variables }, events: new core_1.Emitter() };
                ctx.variableSrv.init(ctx.dashboard);
                ctx.$rootScope.$digest();
                scenario.variables = ctx.variableSrv.variables;
            });
            fn(scenario);
        });
    }
    ['query', 'interval', 'custom', 'datasource'].forEach(function (type) {
        describeInitScenario('when setting ' + type + ' variable via url', function (scenario) {
            scenario.setup(function () {
                scenario.variables = [{
                        name: 'apps',
                        type: type,
                        current: { text: "test", value: "test" },
                        options: [{ text: "test", value: "test" }]
                    }];
                scenario.urlParams["var-apps"] = "new";
                scenario.metricSources = [];
            });
            common_1.it('should update current value', function () {
                common_1.expect(scenario.variables[0].current.value).to.be("new");
                common_1.expect(scenario.variables[0].current.text).to.be("new");
            });
        });
    });
    common_1.describe('given dependent variables', function () {
        var variableList = [
            {
                name: 'app',
                type: 'query',
                query: '',
                current: { text: "app1", value: "app1" },
                options: [{ text: "app1", value: "app1" }]
            },
            {
                name: 'server',
                type: 'query',
                refresh: 1,
                query: '$app.*',
                current: { text: "server1", value: "server1" },
                options: [{ text: "server1", value: "server1" }]
            },
        ];
        describeInitScenario('when setting parent var from url', function (scenario) {
            scenario.setup(function () {
                scenario.variables = lodash_1.default.cloneDeep(variableList);
                scenario.urlParams["var-app"] = "google";
                scenario.queryResult = [{ text: 'google-server1' }, { text: 'google-server2' }];
            });
            common_1.it('should update child variable', function () {
                common_1.expect(scenario.variables[1].options.length).to.be(2);
                common_1.expect(scenario.variables[1].current.text).to.be("google-server1");
            });
            common_1.it('should only update it once', function () {
                common_1.expect(ctx.datasource.metricFindQuery.callCount).to.be(1);
            });
        });
    });
    describeInitScenario('when datasource variable is initialized', function (scenario) {
        scenario.setup(function () {
            scenario.variables = [{
                    type: 'datasource',
                    query: 'graphite',
                    name: 'test',
                    current: { value: 'backend4_pee', text: 'backend4_pee' },
                    regex: '/pee$/'
                }
            ];
            scenario.metricSources = [
                { name: 'backend1', meta: { id: 'influx' } },
                { name: 'backend2_pee', meta: { id: 'graphite' } },
                { name: 'backend3', meta: { id: 'graphite' } },
                { name: 'backend4_pee', meta: { id: 'graphite' } },
            ];
        });
        common_1.it('should update current value', function () {
            var variable = ctx.variableSrv.variables[0];
            common_1.expect(variable.options.length).to.be(2);
        });
    });
    describeInitScenario('when template variable is present in url multiple times', function (scenario) {
        scenario.setup(function () {
            scenario.variables = [{
                    name: 'apps',
                    type: 'query',
                    multi: true,
                    current: { text: "val1", value: "val1" },
                    options: [{ text: "val1", value: "val1" }, { text: 'val2', value: 'val2' }, { text: 'val3', value: 'val3', selected: true }]
                }];
            scenario.urlParams["var-apps"] = ["val2", "val1"];
        });
        common_1.it('should update current value', function () {
            var variable = ctx.variableSrv.variables[0];
            common_1.expect(variable.current.value.length).to.be(2);
            common_1.expect(variable.current.value[0]).to.be("val2");
            common_1.expect(variable.current.value[1]).to.be("val1");
            common_1.expect(variable.current.text).to.be("val2 + val1");
            common_1.expect(variable.options[0].selected).to.be(true);
            common_1.expect(variable.options[1].selected).to.be(true);
        });
        common_1.it('should set options that are not in value to selected false', function () {
            var variable = ctx.variableSrv.variables[0];
            common_1.expect(variable.options[2].selected).to.be(false);
        });
    });
});
