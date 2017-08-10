"use strict";
///<reference path="../../../../headers/common.d.ts" />
var __extends = (this && this.__extends) || (function () {
    var extendStatics = Object.setPrototypeOf ||
        ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
        function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
Object.defineProperty(exports, "__esModule", { value: true });
var lodash_1 = require("lodash");
var sdk_1 = require("app/plugins/sdk");
var TestDataQueryCtrl = (function (_super) {
    __extends(TestDataQueryCtrl, _super);
    /** @ngInject **/
    function TestDataQueryCtrl($scope, $injector, backendSrv) {
        var _this = _super.call(this, $scope, $injector) || this;
        _this.backendSrv = backendSrv;
        _this.target.scenarioId = _this.target.scenarioId || 'random_walk';
        _this.scenarioList = [];
        return _this;
    }
    TestDataQueryCtrl.prototype.$onInit = function () {
        var _this = this;
        return this.backendSrv.get('/api/tsdb/testdata/scenarios').then(function (res) {
            _this.scenarioList = res;
            _this.scenario = lodash_1.default.find(_this.scenarioList, { id: _this.target.scenarioId });
        });
    };
    TestDataQueryCtrl.prototype.scenarioChanged = function () {
        this.scenario = lodash_1.default.find(this.scenarioList, { id: this.target.scenarioId });
        this.target.stringInput = this.scenario.stringInput;
        this.refresh();
    };
    TestDataQueryCtrl.templateUrl = 'partials/query.editor.html';
    return TestDataQueryCtrl;
}(sdk_1.QueryCtrl));
exports.TestDataQueryCtrl = TestDataQueryCtrl;
