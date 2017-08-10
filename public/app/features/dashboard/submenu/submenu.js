"use strict";
///<reference path="../../../headers/common.d.ts" />
Object.defineProperty(exports, "__esModule", { value: true });
var angular_1 = require("angular");
var lodash_1 = require("lodash");
var SubmenuCtrl = (function () {
    /** @ngInject */
    function SubmenuCtrl($rootScope, variableSrv, templateSrv, $location) {
        this.$rootScope = $rootScope;
        this.variableSrv = variableSrv;
        this.templateSrv = templateSrv;
        this.$location = $location;
        this.annotations = this.dashboard.templating.list;
        this.variables = this.variableSrv.variables;
    }
    SubmenuCtrl.prototype.annotationStateChanged = function () {
        this.$rootScope.$broadcast('refresh');
    };
    SubmenuCtrl.prototype.variableUpdated = function (variable) {
        var _this = this;
        this.variableSrv.variableUpdated(variable).then(function () {
            _this.$rootScope.$emit('template-variable-value-updated');
            _this.$rootScope.$broadcast('refresh');
        });
    };
    SubmenuCtrl.prototype.openEditView = function (editview) {
        var search = lodash_1.default.extend(this.$location.search(), { editview: editview });
        this.$location.search(search);
    };
    SubmenuCtrl.prototype.exitBuildMode = function () {
        this.dashboard.toggleEditMode();
    };
    return SubmenuCtrl;
}());
exports.SubmenuCtrl = SubmenuCtrl;
function submenuDirective() {
    return {
        restrict: 'E',
        templateUrl: 'public/app/features/dashboard/submenu/submenu.html',
        controller: SubmenuCtrl,
        bindToController: true,
        controllerAs: 'ctrl',
        scope: {
            dashboard: "=",
        }
    };
}
exports.submenuDirective = submenuDirective;
angular_1.default.module('grafana.directives').directive('dashboardSubmenu', submenuDirective);
