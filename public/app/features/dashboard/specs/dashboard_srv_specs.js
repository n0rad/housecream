"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var common_1 = require("test/lib/common");
var dashboard_srv_1 = require("../dashboard_srv");
common_1.describe('dashboardSrv', function () {
    var _dashboardSrv;
    common_1.beforeEach(function () {
        _dashboardSrv = new dashboard_srv_1.DashboardSrv({}, {}, {});
    });
});
