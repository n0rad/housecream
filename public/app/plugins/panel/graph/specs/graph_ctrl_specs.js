"use strict";
///<reference path="../../../../headers/common.d.ts" />
Object.defineProperty(exports, "__esModule", { value: true });
var common_1 = require("../../../../../test/lib/common");
var moment_1 = require("moment");
var module_1 = require("../module");
var helpers_1 = require("../../../../../test/specs/helpers");
common_1.describe('GraphCtrl', function () {
    var ctx = new helpers_1.default.ControllerTestContext();
    common_1.beforeEach(common_1.angularMocks.module('grafana.services'));
    common_1.beforeEach(common_1.angularMocks.module('grafana.controllers'));
    common_1.beforeEach(common_1.angularMocks.module(function ($compileProvider) {
        $compileProvider.preAssignBindingsEnabled(true);
    }));
    common_1.beforeEach(ctx.providePhase());
    common_1.beforeEach(ctx.createPanelController(module_1.GraphCtrl));
    common_1.beforeEach(function () {
        ctx.ctrl.annotationsPromise = Promise.resolve({});
        ctx.ctrl.updateTimeRange();
    });
    common_1.describe('when time series are outside range', function () {
        common_1.beforeEach(function () {
            var data = [
                { target: 'test.cpu1', datapoints: [[45, 1234567890], [60, 1234567899]] },
            ];
            ctx.ctrl.range = { from: moment_1.default().valueOf(), to: moment_1.default().valueOf() };
            ctx.ctrl.onDataReceived(data);
        });
        common_1.it('should set datapointsOutside', function () {
            common_1.expect(ctx.ctrl.dataWarning.title).to.be('Data points outside time range');
        });
    });
    common_1.describe('when time series are inside range', function () {
        common_1.beforeEach(function () {
            var range = {
                from: moment_1.default().subtract(1, 'days').valueOf(),
                to: moment_1.default().valueOf()
            };
            var data = [
                { target: 'test.cpu1', datapoints: [[45, range.from + 1000], [60, range.from + 10000]] },
            ];
            ctx.ctrl.range = range;
            ctx.ctrl.onDataReceived(data);
        });
        common_1.it('should set datapointsOutside', function () {
            common_1.expect(ctx.ctrl.dataWarning).to.be(null);
        });
    });
    common_1.describe('datapointsCount given 2 series', function () {
        common_1.beforeEach(function () {
            var data = [
                { target: 'test.cpu1', datapoints: [] },
                { target: 'test.cpu2', datapoints: [] },
            ];
            ctx.ctrl.onDataReceived(data);
        });
        common_1.it('should set datapointsCount warning', function () {
            common_1.expect(ctx.ctrl.dataWarning.title).to.be('No data points');
        });
    });
});
