"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var common_1 = require("test/lib/common");
var lodash_1 = require("lodash");
var model_1 = require("../model");
common_1.describe('DashboardModel', function () {
    common_1.describe('when creating new dashboard model defaults only', function () {
        var model;
        common_1.beforeEach(function () {
            model = new model_1.DashboardModel({}, {});
        });
        common_1.it('should have title', function () {
            common_1.expect(model.title).to.be('No Title');
        });
        common_1.it('should have meta', function () {
            common_1.expect(model.meta.canSave).to.be(true);
            common_1.expect(model.meta.canShare).to.be(true);
        });
        common_1.it('should have default properties', function () {
            common_1.expect(model.rows.length).to.be(0);
        });
    });
    common_1.describe('when getting next panel id', function () {
        var model;
        common_1.beforeEach(function () {
            model = new model_1.DashboardModel({
                rows: [{ panels: [{ id: 5 }] }]
            });
        });
        common_1.it('should return max id + 1', function () {
            common_1.expect(model.getNextPanelId()).to.be(6);
        });
    });
    common_1.describe('getSaveModelClone', function () {
        common_1.it('should sort keys', function () {
            var model = new model_1.DashboardModel({});
            var saveModel = model.getSaveModelClone();
            var keys = lodash_1.default.keys(saveModel);
            common_1.expect(keys[0]).to.be('addEmptyRow');
            common_1.expect(keys[1]).to.be('addPanel');
        });
    });
    common_1.describe('row and panel manipulation', function () {
        var dashboard;
        common_1.beforeEach(function () {
            dashboard = new model_1.DashboardModel({});
        });
        common_1.it('adding default should split span in half', function () {
            dashboard.addEmptyRow();
            dashboard.rows[0].addPanel({ span: 12 });
            dashboard.rows[0].addPanel({ span: 12 });
            common_1.expect(dashboard.rows[0].panels[0].span).to.be(6);
            common_1.expect(dashboard.rows[0].panels[1].span).to.be(6);
        });
        common_1.it('duplicate panel should try to add it to same row', function () {
            var panel = { span: 4, attr: '123', id: 10 };
            dashboard.addEmptyRow();
            dashboard.rows[0].addPanel(panel);
            dashboard.duplicatePanel(panel, dashboard.rows[0]);
            common_1.expect(dashboard.rows[0].panels[0].span).to.be(4);
            common_1.expect(dashboard.rows[0].panels[1].span).to.be(4);
            common_1.expect(dashboard.rows[0].panels[1].attr).to.be('123');
            common_1.expect(dashboard.rows[0].panels[1].id).to.be(11);
        });
        common_1.it('duplicate panel should remove repeat data', function () {
            var panel = { span: 4, attr: '123', id: 10, repeat: 'asd', scopedVars: { test: 'asd' } };
            dashboard.addEmptyRow();
            dashboard.rows[0].addPanel(panel);
            dashboard.duplicatePanel(panel, dashboard.rows[0]);
            common_1.expect(dashboard.rows[0].panels[1].repeat).to.be(undefined);
            common_1.expect(dashboard.rows[0].panels[1].scopedVars).to.be(undefined);
        });
    });
    common_1.describe('when creating dashboard with old schema', function () {
        var model;
        var graph;
        var singlestat;
        var table;
        common_1.beforeEach(function () {
            model = new model_1.DashboardModel({
                services: { filter: { time: { from: 'now-1d', to: 'now' }, list: [{}] } },
                pulldowns: [
                    { type: 'filtering', enable: true },
                    { type: 'annotations', enable: true, annotations: [{ name: 'old' }] }
                ],
                rows: [
                    {
                        panels: [
                            {
                                type: 'graph', legend: true, aliasYAxis: { test: 2 },
                                y_formats: ['kbyte', 'ms'],
                                grid: {
                                    min: 1,
                                    max: 10,
                                    rightMin: 5,
                                    rightMax: 15,
                                    leftLogBase: 1,
                                    rightLogBase: 2,
                                    threshold1: 200,
                                    threshold2: 400,
                                    threshold1Color: 'yellow',
                                    threshold2Color: 'red',
                                },
                                leftYAxisLabel: 'left label',
                                targets: [{ refId: 'A' }, {}],
                            },
                            {
                                type: 'singlestat', legend: true, thresholds: '10,20,30', aliasYAxis: { test: 2 }, grid: { min: 1, max: 10 },
                                targets: [{ refId: 'A' }, {}],
                            },
                            {
                                type: 'table', legend: true, styles: [{ thresholds: ["10", "20", "30"] }, { thresholds: ["100", "200", "300"] }],
                                targets: [{ refId: 'A' }, {}],
                            }
                        ]
                    }
                ]
            });
            graph = model.rows[0].panels[0];
            singlestat = model.rows[0].panels[1];
            table = model.rows[0].panels[2];
        });
        common_1.it('should have title', function () {
            common_1.expect(model.title).to.be('No Title');
        });
        common_1.it('should have panel id', function () {
            common_1.expect(graph.id).to.be(1);
        });
        common_1.it('should move time and filtering list', function () {
            common_1.expect(model.time.from).to.be('now-1d');
            common_1.expect(model.templating.list[0].allFormat).to.be('glob');
        });
        common_1.it('graphite panel should change name too graph', function () {
            common_1.expect(graph.type).to.be('graph');
        });
        common_1.it('single stat panel should have two thresholds', function () {
            common_1.expect(singlestat.thresholds).to.be('20,30');
        });
        common_1.it('queries without refId should get it', function () {
            common_1.expect(graph.targets[1].refId).to.be('B');
        });
        common_1.it('update legend setting', function () {
            common_1.expect(graph.legend.show).to.be(true);
        });
        common_1.it('move aliasYAxis to series override', function () {
            common_1.expect(graph.seriesOverrides[0].alias).to.be("test");
            common_1.expect(graph.seriesOverrides[0].yaxis).to.be(2);
        });
        common_1.it('should move pulldowns to new schema', function () {
            common_1.expect(model.annotations.list[0].name).to.be('old');
        });
        common_1.it('table panel should only have two thresholds values', function () {
            common_1.expect(table.styles[0].thresholds[0]).to.be("20");
            common_1.expect(table.styles[0].thresholds[1]).to.be("30");
            common_1.expect(table.styles[1].thresholds[0]).to.be("200");
            common_1.expect(table.styles[1].thresholds[1]).to.be("300");
        });
        common_1.it('graph grid to yaxes options', function () {
            common_1.expect(graph.yaxes[0].min).to.be(1);
            common_1.expect(graph.yaxes[0].max).to.be(10);
            common_1.expect(graph.yaxes[0].format).to.be('kbyte');
            common_1.expect(graph.yaxes[0].label).to.be('left label');
            common_1.expect(graph.yaxes[0].logBase).to.be(1);
            common_1.expect(graph.yaxes[1].min).to.be(5);
            common_1.expect(graph.yaxes[1].max).to.be(15);
            common_1.expect(graph.yaxes[1].format).to.be('ms');
            common_1.expect(graph.yaxes[1].logBase).to.be(2);
            common_1.expect(graph.grid.rightMax).to.be(undefined);
            common_1.expect(graph.grid.rightLogBase).to.be(undefined);
            common_1.expect(graph.y_formats).to.be(undefined);
        });
        common_1.it('dashboard schema version should be set to latest', function () {
            common_1.expect(model.schemaVersion).to.be(14);
        });
        common_1.it('graph thresholds should be migrated', function () {
            common_1.expect(graph.thresholds.length).to.be(2);
            common_1.expect(graph.thresholds[0].op).to.be('gt');
            common_1.expect(graph.thresholds[0].value).to.be(200);
            common_1.expect(graph.thresholds[0].fillColor).to.be('yellow');
            common_1.expect(graph.thresholds[1].value).to.be(400);
            common_1.expect(graph.thresholds[1].fillColor).to.be('red');
        });
    });
    common_1.describe('when creating dashboard model with missing list for annoations or templating', function () {
        var model;
        common_1.beforeEach(function () {
            model = new model_1.DashboardModel({
                annotations: {
                    enable: true,
                },
                templating: {
                    enable: true
                }
            });
        });
        common_1.it('should add empty list', function () {
            common_1.expect(model.annotations.list.length).to.be(0);
            common_1.expect(model.templating.list.length).to.be(0);
        });
    });
    common_1.describe('Given editable false dashboard', function () {
        var model;
        common_1.beforeEach(function () {
            model = new model_1.DashboardModel({ editable: false });
        });
        common_1.it('Should set meta canEdit and canSave to false', function () {
            common_1.expect(model.meta.canSave).to.be(false);
            common_1.expect(model.meta.canEdit).to.be(false);
        });
        common_1.it('getSaveModelClone should remove meta', function () {
            var clone = model.getSaveModelClone();
            common_1.expect(clone.meta).to.be(undefined);
        });
    });
    common_1.describe('when loading dashboard with old influxdb query schema', function () {
        var model;
        var target;
        common_1.beforeEach(function () {
            model = new model_1.DashboardModel({
                rows: [{
                        panels: [{
                                type: 'graph',
                                grid: {},
                                yaxes: [{}, {}],
                                targets: [{
                                        "alias": "$tag_datacenter $tag_source $col",
                                        "column": "value",
                                        "measurement": "logins.count",
                                        "fields": [
                                            {
                                                "func": "mean",
                                                "name": "value",
                                                "mathExpr": "*2",
                                                "asExpr": "value"
                                            },
                                            {
                                                "name": "one-minute",
                                                "func": "mean",
                                                "mathExpr": "*3",
                                                "asExpr": "one-minute"
                                            }
                                        ],
                                        "tags": [],
                                        "fill": "previous",
                                        "function": "mean",
                                        "groupBy": [
                                            {
                                                "interval": "auto",
                                                "type": "time"
                                            },
                                            {
                                                "key": "source",
                                                "type": "tag"
                                            },
                                            {
                                                "type": "tag",
                                                "key": "datacenter"
                                            }
                                        ],
                                    }]
                            }]
                    }]
            });
            target = model.rows[0].panels[0].targets[0];
        });
        common_1.it('should update query schema', function () {
            common_1.expect(target.fields).to.be(undefined);
            common_1.expect(target.select.length).to.be(2);
            common_1.expect(target.select[0].length).to.be(4);
            common_1.expect(target.select[0][0].type).to.be('field');
            common_1.expect(target.select[0][1].type).to.be('mean');
            common_1.expect(target.select[0][2].type).to.be('math');
            common_1.expect(target.select[0][3].type).to.be('alias');
        });
    });
    common_1.describe('when creating dashboard model with missing list for annoations or templating', function () {
        var model;
        common_1.beforeEach(function () {
            model = new model_1.DashboardModel({
                annotations: {
                    enable: true,
                },
                templating: {
                    enable: true
                }
            });
        });
        common_1.it('should add empty list', function () {
            common_1.expect(model.annotations.list.length).to.be(0);
            common_1.expect(model.templating.list.length).to.be(0);
        });
    });
    common_1.describe('Formatting epoch timestamp when timezone is set as utc', function () {
        var dashboard;
        common_1.beforeEach(function () {
            dashboard = new model_1.DashboardModel({ timezone: 'utc' });
        });
        common_1.it('Should format timestamp with second resolution by default', function () {
            common_1.expect(dashboard.formatDate(1234567890000)).to.be('2009-02-13 23:31:30');
        });
        common_1.it('Should format timestamp with second resolution even if second format is passed as parameter', function () {
            common_1.expect(dashboard.formatDate(1234567890007, 'YYYY-MM-DD HH:mm:ss')).to.be('2009-02-13 23:31:30');
        });
        common_1.it('Should format timestamp with millisecond resolution if format is passed as parameter', function () {
            common_1.expect(dashboard.formatDate(1234567890007, 'YYYY-MM-DD HH:mm:ss.SSS')).to.be('2009-02-13 23:31:30.007');
        });
    });
});
