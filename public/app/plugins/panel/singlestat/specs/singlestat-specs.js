"use strict";
///<reference path="../../../../headers/common.d.ts" />
Object.defineProperty(exports, "__esModule", { value: true });
var common_1 = require("../../../../../test/lib/common");
var helpers_1 = require("../../../../../test/specs/helpers");
var module_1 = require("../module");
common_1.describe('SingleStatCtrl', function () {
    var ctx = new helpers_1.default.ControllerTestContext();
    function singleStatScenario(desc, func) {
        common_1.describe(desc, function () {
            ctx.setup = function (setupFunc) {
                common_1.beforeEach(common_1.angularMocks.module('grafana.services'));
                common_1.beforeEach(common_1.angularMocks.module('grafana.controllers'));
                common_1.beforeEach(common_1.angularMocks.module(function ($compileProvider) {
                    $compileProvider.preAssignBindingsEnabled(true);
                }));
                common_1.beforeEach(ctx.providePhase());
                common_1.beforeEach(ctx.createPanelController(module_1.SingleStatCtrl));
                common_1.beforeEach(function () {
                    setupFunc();
                    var data = [
                        { target: 'test.cpu1', datapoints: ctx.datapoints }
                    ];
                    ctx.ctrl.onDataReceived(data);
                    ctx.data = ctx.ctrl.data;
                });
            };
            func(ctx);
        });
    }
    singleStatScenario('with defaults', function (ctx) {
        ctx.setup(function () {
            ctx.datapoints = [[10, 1], [20, 2]];
        });
        common_1.it('Should use series avg as default main value', function () {
            common_1.expect(ctx.data.value).to.be(15);
            common_1.expect(ctx.data.valueRounded).to.be(15);
        });
        common_1.it('should set formated falue', function () {
            common_1.expect(ctx.data.valueFormated).to.be('15');
        });
    });
    singleStatScenario('showing serie name instead of value', function (ctx) {
        ctx.setup(function () {
            ctx.datapoints = [[10, 1], [20, 2]];
            ctx.ctrl.panel.valueName = 'name';
        });
        common_1.it('Should use series avg as default main value', function () {
            common_1.expect(ctx.data.value).to.be(0);
            common_1.expect(ctx.data.valueRounded).to.be(0);
        });
        common_1.it('should set formated falue', function () {
            common_1.expect(ctx.data.valueFormated).to.be('test.cpu1');
        });
    });
    singleStatScenario('MainValue should use same number for decimals as displayed when checking thresholds', function (ctx) {
        ctx.setup(function () {
            ctx.datapoints = [[99.999, 1], [99.99999, 2]];
        });
        common_1.it('Should be rounded', function () {
            common_1.expect(ctx.data.value).to.be(99.999495);
            common_1.expect(ctx.data.valueRounded).to.be(100);
        });
        common_1.it('should set formated falue', function () {
            common_1.expect(ctx.data.valueFormated).to.be('100');
        });
    });
    singleStatScenario('When value to text mapping is specified', function (ctx) {
        ctx.setup(function () {
            ctx.datapoints = [[9.9, 1]];
            ctx.ctrl.panel.valueMaps = [{ value: '10', text: 'OK' }];
        });
        common_1.it('value should remain', function () {
            common_1.expect(ctx.data.value).to.be(9.9);
        });
        common_1.it('round should be rounded up', function () {
            common_1.expect(ctx.data.valueRounded).to.be(10);
        });
        common_1.it('Should replace value with text', function () {
            common_1.expect(ctx.data.valueFormated).to.be('OK');
        });
    });
    singleStatScenario('When range to text mapping is specifiedfor first range', function (ctx) {
        ctx.setup(function () {
            ctx.datapoints = [[41, 50]];
            ctx.ctrl.panel.mappingType = 2;
            ctx.ctrl.panel.rangeMaps = [{ from: '10', to: '50', text: 'OK' }, { from: '51', to: '100', text: 'NOT OK' }];
        });
        common_1.it('Should replace value with text OK', function () {
            common_1.expect(ctx.data.valueFormated).to.be('OK');
        });
    });
    singleStatScenario('When range to text mapping is specified for other ranges', function (ctx) {
        ctx.setup(function () {
            ctx.datapoints = [[65, 75]];
            ctx.ctrl.panel.mappingType = 2;
            ctx.ctrl.panel.rangeMaps = [{ from: '10', to: '50', text: 'OK' }, { from: '51', to: '100', text: 'NOT OK' }];
        });
        common_1.it('Should replace value with text NOT OK', function () {
            common_1.expect(ctx.data.valueFormated).to.be('NOT OK');
        });
    });
});
