"use strict";
///<reference path="../../../../headers/common.d.ts" />
Object.defineProperty(exports, "__esModule", { value: true });
var datasource_1 = require("./datasource");
exports.TestDataDatasource = datasource_1.TestDataDatasource;
exports.Datasource = datasource_1.TestDataDatasource;
var query_ctrl_1 = require("./query_ctrl");
exports.QueryCtrl = query_ctrl_1.TestDataQueryCtrl;
var TestDataAnnotationsQueryCtrl = (function () {
    function TestDataAnnotationsQueryCtrl() {
    }
    TestDataAnnotationsQueryCtrl.template = '<h2>test data</h2>';
    return TestDataAnnotationsQueryCtrl;
}());
exports.AnnotationsQueryCtrl = TestDataAnnotationsQueryCtrl;
