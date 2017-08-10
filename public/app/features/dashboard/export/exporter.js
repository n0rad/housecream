"use strict";
///<reference path="../../../headers/common.d.ts" />
Object.defineProperty(exports, "__esModule", { value: true });
var config_1 = require("app/core/config");
var lodash_1 = require("lodash");
var dynamic_dashboard_srv_1 = require("../dynamic_dashboard_srv");
var DashboardExporter = (function () {
    function DashboardExporter(datasourceSrv) {
        this.datasourceSrv = datasourceSrv;
    }
    DashboardExporter.prototype.makeExportable = function (dashboard) {
        var _this = this;
        var dynSrv = new dynamic_dashboard_srv_1.DynamicDashboardSrv();
        // clean up repeated rows and panels,
        // this is done on the live real dashboard instance, not on a clone
        // so we need to undo this
        // this is pretty hacky and needs to be changed
        dynSrv.init(dashboard);
        dynSrv.process({ cleanUpOnly: true });
        var saveModel = dashboard.getSaveModelClone();
        saveModel.id = null;
        // undo repeat cleanup
        dynSrv.process();
        var inputs = [];
        var requires = {};
        var datasources = {};
        var promises = [];
        var variableLookup = {};
        for (var _i = 0, _a = saveModel.templating.list; _i < _a.length; _i++) {
            var variable = _a[_i];
            variableLookup[variable.name] = variable;
        }
        var templateizeDatasourceUsage = function (obj) {
            // ignore data source properties that contain a variable
            if (obj.datasource && obj.datasource.indexOf('$') === 0) {
                if (variableLookup[obj.datasource.substring(1)]) {
                    return;
                }
            }
            promises.push(_this.datasourceSrv.get(obj.datasource).then(function (ds) {
                if (ds.meta.builtIn) {
                    return;
                }
                var refName = 'DS_' + ds.name.replace(' ', '_').toUpperCase();
                datasources[refName] = {
                    name: refName,
                    label: ds.name,
                    description: '',
                    type: 'datasource',
                    pluginId: ds.meta.id,
                    pluginName: ds.meta.name,
                };
                obj.datasource = '${' + refName + '}';
                requires['datasource' + ds.meta.id] = {
                    type: 'datasource',
                    id: ds.meta.id,
                    name: ds.meta.name,
                    version: ds.meta.info.version || "1.0.0",
                };
            }));
        };
        // check up panel data sources
        for (var _b = 0, _c = saveModel.rows; _b < _c.length; _b++) {
            var row = _c[_b];
            for (var _d = 0, _e = row.panels; _d < _e.length; _d++) {
                var panel = _e[_d];
                if (panel.datasource !== undefined) {
                    templateizeDatasourceUsage(panel);
                }
                if (panel.targets) {
                    for (var _f = 0, _g = panel.targets; _f < _g.length; _f++) {
                        var target = _g[_f];
                        if (target.datasource !== undefined) {
                            templateizeDatasourceUsage(target);
                        }
                    }
                }
                var panelDef = config_1.default.panels[panel.type];
                if (panelDef) {
                    requires['panel' + panelDef.id] = {
                        type: 'panel',
                        id: panelDef.id,
                        name: panelDef.name,
                        version: panelDef.info.version,
                    };
                }
            }
        }
        // templatize template vars
        for (var _h = 0, _j = saveModel.templating.list; _h < _j.length; _h++) {
            var variable = _j[_h];
            if (variable.type === 'query') {
                templateizeDatasourceUsage(variable);
                variable.options = [];
                variable.current = {};
                variable.refresh = 1;
            }
        }
        // templatize annotations vars
        for (var _k = 0, _l = saveModel.annotations.list; _k < _l.length; _k++) {
            var annotationDef = _l[_k];
            templateizeDatasourceUsage(annotationDef);
        }
        // add grafana version
        requires['grafana'] = {
            type: 'grafana',
            id: 'grafana',
            name: 'Grafana',
            version: config_1.default.buildInfo.version
        };
        return Promise.all(promises).then(function () {
            lodash_1.default.each(datasources, function (value, key) {
                inputs.push(value);
            });
            // templatize constants
            for (var _i = 0, _a = saveModel.templating.list; _i < _a.length; _i++) {
                var variable = _a[_i];
                if (variable.type === 'constant') {
                    var refName = 'VAR_' + variable.name.replace(' ', '_').toUpperCase();
                    inputs.push({
                        name: refName,
                        type: 'constant',
                        label: variable.label || variable.name,
                        value: variable.current.value,
                        description: '',
                    });
                    // update current and option
                    variable.query = '${' + refName + '}';
                    variable.options[0] = variable.current = {
                        value: variable.query,
                        text: variable.query,
                    };
                }
            }
            // make inputs and requires a top thing
            var newObj = {};
            newObj["__inputs"] = inputs;
            newObj["__requires"] = lodash_1.default.sortBy(requires, ['id']);
            lodash_1.default.defaults(newObj, saveModel);
            return newObj;
        }).catch(function (err) {
            console.log('Export failed:', err);
            return {
                error: err
            };
        });
    };
    return DashboardExporter;
}());
exports.DashboardExporter = DashboardExporter;
