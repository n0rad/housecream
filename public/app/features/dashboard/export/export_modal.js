"use strict";
///<reference path="../../../headers/common.d.ts" />
Object.defineProperty(exports, "__esModule", { value: true });
var angular_1 = require("angular");
var core_module_1 = require("app/core/core_module");
var exporter_1 = require("./exporter");
var DashExportCtrl = (function () {
    /** @ngInject */
    function DashExportCtrl(backendSrv, dashboardSrv, datasourceSrv, $scope) {
        var _this = this;
        this.backendSrv = backendSrv;
        this.exporter = new exporter_1.DashboardExporter(datasourceSrv);
        this.exporter.makeExportable(dashboardSrv.getCurrent()).then(function (dash) {
            $scope.$apply(function () {
                _this.dash = dash;
            });
        });
    }
    DashExportCtrl.prototype.save = function () {
        var blob = new Blob([angular_1.default.toJson(this.dash, true)], { type: "application/json;charset=utf-8" });
        var wnd = window;
        wnd.saveAs(blob, this.dash.title + '-' + new Date().getTime() + '.json');
    };
    DashExportCtrl.prototype.saveJson = function () {
        var html = angular_1.default.toJson(this.dash, true);
        var uri = "data:application/json," + encodeURIComponent(html);
        var newWindow = window.open(uri);
    };
    return DashExportCtrl;
}());
exports.DashExportCtrl = DashExportCtrl;
function dashExportDirective() {
    return {
        restrict: 'E',
        templateUrl: 'public/app/features/dashboard/export/export_modal.html',
        controller: DashExportCtrl,
        bindToController: true,
        controllerAs: 'ctrl',
    };
}
exports.dashExportDirective = dashExportDirective;
core_module_1.default.directive('dashExportModal', dashExportDirective);
