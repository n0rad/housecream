"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var common_1 = require("test/lib/common");
var helpers_1 = require("test/specs/helpers");
var moment_1 = require("moment");
common_1.describe('timeSrv', function () {
    var ctx = new helpers_1.default.ServiceTestContext();
    var _dashboard = {
        time: { from: 'now-6h', to: 'now' },
    };
    common_1.beforeEach(common_1.angularMocks.module('grafana.core'));
    common_1.beforeEach(common_1.angularMocks.module('grafana.services'));
    common_1.beforeEach(ctx.createService('timeSrv'));
    common_1.beforeEach(function () {
        ctx.service.init(_dashboard);
    });
    common_1.describe('timeRange', function () {
        common_1.it('should return unparsed when parse is false', function () {
            ctx.service.setTime({ from: 'now', to: 'now-1h' });
            var time = ctx.service.timeRange();
            common_1.expect(time.raw.from).to.be('now');
            common_1.expect(time.raw.to).to.be('now-1h');
        });
        common_1.it('should return parsed when parse is true', function () {
            ctx.service.setTime({ from: 'now', to: 'now-1h' });
            var time = ctx.service.timeRange();
            common_1.expect(moment_1.default.isMoment(time.from)).to.be(true);
            common_1.expect(moment_1.default.isMoment(time.to)).to.be(true);
        });
    });
    common_1.describe('init time from url', function () {
        common_1.it('should handle relative times', function () {
            ctx.$location.search({ from: 'now-2d', to: 'now' });
            ctx.service.init(_dashboard);
            var time = ctx.service.timeRange();
            common_1.expect(time.raw.from).to.be('now-2d');
            common_1.expect(time.raw.to).to.be('now');
        });
        common_1.it('should handle formated dates', function () {
            ctx.$location.search({ from: '20140410T052010', to: '20140520T031022' });
            ctx.service.init(_dashboard);
            var time = ctx.service.timeRange(true);
            common_1.expect(time.from.valueOf()).to.equal(new Date("2014-04-10T05:20:10Z").getTime());
            common_1.expect(time.to.valueOf()).to.equal(new Date("2014-05-20T03:10:22Z").getTime());
        });
        common_1.it('should handle formated dates without time', function () {
            ctx.$location.search({ from: '20140410', to: '20140520' });
            ctx.service.init(_dashboard);
            var time = ctx.service.timeRange(true);
            common_1.expect(time.from.valueOf()).to.equal(new Date("2014-04-10T00:00:00Z").getTime());
            common_1.expect(time.to.valueOf()).to.equal(new Date("2014-05-20T00:00:00Z").getTime());
        });
        common_1.it('should handle epochs', function () {
            ctx.$location.search({ from: '1410337646373', to: '1410337665699' });
            ctx.service.init(_dashboard);
            var time = ctx.service.timeRange(true);
            common_1.expect(time.from.valueOf()).to.equal(1410337646373);
            common_1.expect(time.to.valueOf()).to.equal(1410337665699);
        });
        common_1.it('should handle bad dates', function () {
            ctx.$location.search({ from: '20151126T00010%3C%2Fp%3E%3Cspan%20class', to: 'now' });
            _dashboard.time.from = 'now-6h';
            ctx.service.init(_dashboard);
            common_1.expect(ctx.service.time.from).to.equal('now-6h');
            common_1.expect(ctx.service.time.to).to.equal('now');
        });
    });
    common_1.describe('setTime', function () {
        common_1.it('should return disable refresh if refresh is disabled for any range', function () {
            _dashboard.refresh = false;
            ctx.service.setTime({ from: '2011-01-01', to: '2015-01-01' });
            common_1.expect(_dashboard.refresh).to.be(false);
        });
        common_1.it('should restore refresh for absolute time range', function () {
            _dashboard.refresh = '30s';
            ctx.service.setTime({ from: '2011-01-01', to: '2015-01-01' });
            common_1.expect(_dashboard.refresh).to.be('30s');
        });
        common_1.it('should restore refresh after relative time range is set', function () {
            _dashboard.refresh = '10s';
            ctx.service.setTime({ from: moment_1.default([2011, 1, 1]), to: moment_1.default([2015, 1, 1]) });
            common_1.expect(_dashboard.refresh).to.be(false);
            ctx.service.setTime({ from: '2011-01-01', to: 'now' });
            common_1.expect(_dashboard.refresh).to.be('10s');
        });
        common_1.it('should keep refresh after relative time range is changed and now delay exists', function () {
            _dashboard.refresh = '10s';
            ctx.service.setTime({ from: 'now-1h', to: 'now-10s' });
            common_1.expect(_dashboard.refresh).to.be('10s');
        });
    });
});
