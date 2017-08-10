"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
require("../query_ctrl");
require("app/core/services/segment_srv");
var common_1 = require("test/lib/common");
var gfunc_1 = require("../gfunc");
var helpers_1 = require("test/specs/helpers");
var query_ctrl_1 = require("../query_ctrl");
common_1.describe('GraphiteQueryCtrl', function () {
    var ctx = new helpers_1.default.ControllerTestContext();
    common_1.beforeEach(common_1.angularMocks.module('grafana.core'));
    common_1.beforeEach(common_1.angularMocks.module('grafana.controllers'));
    common_1.beforeEach(common_1.angularMocks.module('grafana.services'));
    common_1.beforeEach(common_1.angularMocks.module(function ($compileProvider) {
        $compileProvider.preAssignBindingsEnabled(true);
    }));
    common_1.beforeEach(ctx.providePhase());
    common_1.beforeEach(common_1.angularMocks.inject(function ($rootScope, $controller, $q) {
        ctx.$q = $q;
        ctx.scope = $rootScope.$new();
        ctx.target = { target: 'aliasByNode(scaleToSeconds(test.prod.*,1),2)' };
        ctx.datasource.metricFindQuery = common_1.sinon.stub().returns(ctx.$q.when([]));
        ctx.panelCtrl = { panel: {} };
        ctx.panelCtrl.refresh = common_1.sinon.spy();
        ctx.ctrl = $controller(query_ctrl_1.GraphiteQueryCtrl, { $scope: ctx.scope }, {
            panelCtrl: ctx.panelCtrl,
            datasource: ctx.datasource,
            target: ctx.target
        });
        ctx.scope.$digest();
    }));
    common_1.describe('init', function () {
        common_1.it('should validate metric key exists', function () {
            common_1.expect(ctx.datasource.metricFindQuery.getCall(0).args[0]).to.be('test.prod.*');
        });
        common_1.it('should delete last segment if no metrics are found', function () {
            common_1.expect(ctx.ctrl.segments[2].value).to.be('select metric');
        });
        common_1.it('should parse expression and build function model', function () {
            common_1.expect(ctx.ctrl.functions.length).to.be(2);
        });
    });
    common_1.describe('when adding function', function () {
        common_1.beforeEach(function () {
            ctx.ctrl.target.target = 'test.prod.*.count';
            ctx.ctrl.datasource.metricFindQuery = common_1.sinon.stub().returns(ctx.$q.when([{ expandable: false }]));
            ctx.ctrl.parseTarget();
            ctx.ctrl.addFunction(gfunc_1.default.getFuncDef('aliasByNode'));
        });
        common_1.it('should add function with correct node number', function () {
            common_1.expect(ctx.ctrl.functions[0].params[0]).to.be(2);
        });
        common_1.it('should update target', function () {
            common_1.expect(ctx.ctrl.target.target).to.be('aliasByNode(test.prod.*.count, 2)');
        });
        common_1.it('should call refresh', function () {
            common_1.expect(ctx.panelCtrl.refresh.called).to.be(true);
        });
    });
    common_1.describe('when adding function before any metric segment', function () {
        common_1.beforeEach(function () {
            ctx.ctrl.target.target = '';
            ctx.ctrl.datasource.metricFindQuery.returns(ctx.$q.when([{ expandable: true }]));
            ctx.ctrl.parseTarget();
            ctx.ctrl.addFunction(gfunc_1.default.getFuncDef('asPercent'));
        });
        common_1.it('should add function and remove select metric link', function () {
            common_1.expect(ctx.ctrl.segments.length).to.be(0);
        });
    });
    common_1.describe('when initalizing target without metric expression and only function', function () {
        common_1.beforeEach(function () {
            ctx.ctrl.target.target = 'asPercent(#A, #B)';
            ctx.ctrl.datasource.metricFindQuery.returns(ctx.$q.when([]));
            ctx.ctrl.parseTarget();
            ctx.scope.$digest();
        });
        common_1.it('should not add select metric segment', function () {
            common_1.expect(ctx.ctrl.segments.length).to.be(0);
        });
        common_1.it('should add both series refs as params', function () {
            common_1.expect(ctx.ctrl.functions[0].params.length).to.be(2);
        });
    });
    common_1.describe('when initializing a target with single param func using variable', function () {
        common_1.beforeEach(function () {
            ctx.ctrl.target.target = 'movingAverage(prod.count, $var)';
            ctx.ctrl.datasource.metricFindQuery.returns(ctx.$q.when([]));
            ctx.ctrl.parseTarget();
        });
        common_1.it('should add 2 segments', function () {
            common_1.expect(ctx.ctrl.segments.length).to.be(2);
        });
        common_1.it('should add function param', function () {
            common_1.expect(ctx.ctrl.functions[0].params.length).to.be(1);
        });
    });
    common_1.describe('when initalizing target without metric expression and function with series-ref', function () {
        common_1.beforeEach(function () {
            ctx.ctrl.target.target = 'asPercent(metric.node.count, #A)';
            ctx.ctrl.datasource.metricFindQuery.returns(ctx.$q.when([]));
            ctx.ctrl.parseTarget();
        });
        common_1.it('should add segments', function () {
            common_1.expect(ctx.ctrl.segments.length).to.be(3);
        });
        common_1.it('should have correct func params', function () {
            common_1.expect(ctx.ctrl.functions[0].params.length).to.be(1);
        });
    });
    common_1.describe('when getting altSegments and metricFindQuery retuns empty array', function () {
        common_1.beforeEach(function () {
            ctx.ctrl.target.target = 'test.count';
            ctx.ctrl.datasource.metricFindQuery.returns(ctx.$q.when([]));
            ctx.ctrl.parseTarget();
            ctx.ctrl.getAltSegments(1).then(function (results) {
                ctx.altSegments = results;
            });
            ctx.scope.$digest();
        });
        common_1.it('should have no segments', function () {
            common_1.expect(ctx.altSegments.length).to.be(0);
        });
    });
    common_1.describe('targetChanged', function () {
        common_1.beforeEach(function () {
            ctx.ctrl.datasource.metricFindQuery = common_1.sinon.stub().returns(ctx.$q.when([{ expandable: false }]));
            ctx.ctrl.parseTarget();
            ctx.ctrl.target.target = '';
            ctx.ctrl.targetChanged();
        });
        common_1.it('should rebuld target after expression model', function () {
            common_1.expect(ctx.ctrl.target.target).to.be('aliasByNode(scaleToSeconds(test.prod.*, 1), 2)');
        });
        common_1.it('should call panelCtrl.refresh', function () {
            common_1.expect(ctx.panelCtrl.refresh.called).to.be(true);
        });
    });
    common_1.describe('when updating targets with nested query', function () {
        common_1.beforeEach(function () {
            ctx.ctrl.target.target = 'scaleToSeconds(#A)';
            ctx.ctrl.datasource.metricFindQuery = common_1.sinon.stub().returns(ctx.$q.when([{ expandable: false }]));
            ctx.ctrl.parseTarget();
            ctx.ctrl.panelCtrl.panel.targets = [{
                    target: 'nested.query.count',
                    refId: 'A'
                }];
            ctx.ctrl.updateModelTarget();
        });
        common_1.it('target should remain the same', function () {
            common_1.expect(ctx.ctrl.target.target).to.be('scaleToSeconds(#A)');
        });
        common_1.it('targetFull should include nexted queries', function () {
            common_1.expect(ctx.ctrl.target.targetFull).to.be('scaleToSeconds(nested.query.count)');
        });
    });
});
