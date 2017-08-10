"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var common_1 = require("test/lib/common");
var app_1 = require("app/app");
common_1.describe('GrafanaApp', function () {
    var app = new app_1.GrafanaApp();
    common_1.it('can call inits', function () {
        common_1.expect(app).to.not.be(null);
    });
});
