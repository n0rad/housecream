"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var common_1 = require("test/lib/common");
var lodash_1 = require("lodash");
var config_1 = require("app/core/config");
var exporter_1 = require("../export/exporter");
var model_1 = require("../model");
common_1.describe('given dashboard with repeated panels', function () {
    var dash, exported;
    common_1.beforeEach(function (done) {
        dash = {
            rows: [],
            templating: { list: [] },
            annotations: { list: [] },
        };
        config_1.default.buildInfo = {
            version: "3.0.2"
        };
        dash.templating.list.push({
            name: 'apps',
            type: 'query',
            datasource: 'gfdb',
            current: { value: 'Asd', text: 'Asd' },
            options: [{ value: 'Asd', text: 'Asd' }]
        });
        dash.templating.list.push({
            name: 'prefix',
            type: 'constant',
            current: { value: 'collectd', text: 'collectd' },
            options: []
        });
        dash.templating.list.push({
            name: 'ds',
            type: 'datasource',
            query: 'testdb',
            current: { value: 'prod', text: 'prod' },
            options: []
        });
        dash.annotations.list.push({
            name: 'logs',
            datasource: 'gfdb',
        });
        dash.rows.push({
            repeat: 'test',
            panels: [
                { id: 2, repeat: 'apps', datasource: 'gfdb', type: 'graph' },
                { id: 3, repeat: null, repeatPanelId: 2 },
                {
                    id: 4,
                    datasource: '-- Mixed --',
                    targets: [{ datasource: 'other' }],
                },
                { id: 5, datasource: '$ds' },
            ]
        });
        dash.rows.push({
            repeat: null,
            repeatRowId: 1,
            panels: [],
        });
        var datasourceSrvStub = { get: common_1.sinon.stub() };
        datasourceSrvStub.get.withArgs('gfdb').returns(Promise.resolve({
            name: 'gfdb',
            meta: { id: "testdb", info: { version: "1.2.1" }, name: "TestDB" }
        }));
        datasourceSrvStub.get.withArgs('other').returns(Promise.resolve({
            name: 'other',
            meta: { id: "other", info: { version: "1.2.1" }, name: "OtherDB" }
        }));
        datasourceSrvStub.get.withArgs('-- Mixed --').returns(Promise.resolve({
            name: 'mixed',
            meta: { id: "mixed", info: { version: "1.2.1" }, name: "Mixed", builtIn: true }
        }));
        config_1.default.panels['graph'] = {
            id: "graph",
            name: "Graph",
            info: { version: "1.1.0" }
        };
        dash = new model_1.DashboardModel(dash, {});
        var exporter = new exporter_1.DashboardExporter(datasourceSrvStub);
        exporter.makeExportable(dash).then(function (clean) {
            exported = clean;
            done();
        });
    });
    common_1.it('exported dashboard should not contain repeated panels', function () {
        common_1.expect(exported.rows[0].panels.length).to.be(3);
    });
    common_1.it('exported dashboard should not contain repeated rows', function () {
        common_1.expect(exported.rows.length).to.be(1);
    });
    common_1.it('should replace datasource refs', function () {
        var panel = exported.rows[0].panels[0];
        common_1.expect(panel.datasource).to.be("${DS_GFDB}");
    });
    common_1.it('should replace datasource in variable query', function () {
        common_1.expect(exported.templating.list[0].datasource).to.be("${DS_GFDB}");
        common_1.expect(exported.templating.list[0].options.length).to.be(0);
        common_1.expect(exported.templating.list[0].current.value).to.be(undefined);
        common_1.expect(exported.templating.list[0].current.text).to.be(undefined);
    });
    common_1.it('should replace datasource in annotation query', function () {
        common_1.expect(exported.annotations.list[0].datasource).to.be("${DS_GFDB}");
    });
    common_1.it('should add datasource as input', function () {
        common_1.expect(exported.__inputs[0].name).to.be("DS_GFDB");
        common_1.expect(exported.__inputs[0].pluginId).to.be("testdb");
        common_1.expect(exported.__inputs[0].type).to.be("datasource");
    });
    common_1.it('should add datasource to required', function () {
        var require = lodash_1.default.find(exported.__requires, { name: 'TestDB' });
        common_1.expect(require.name).to.be("TestDB");
        common_1.expect(require.id).to.be("testdb");
        common_1.expect(require.type).to.be("datasource");
        common_1.expect(require.version).to.be("1.2.1");
    });
    common_1.it('should not add built in datasources to required', function () {
        var require = lodash_1.default.find(exported.__requires, { name: 'Mixed' });
        common_1.expect(require).to.be(undefined);
    });
    common_1.it('should add datasources used in mixed mode', function () {
        var require = lodash_1.default.find(exported.__requires, { name: 'OtherDB' });
        common_1.expect(require).to.not.be(undefined);
    });
    common_1.it('should add panel to required', function () {
        var require = lodash_1.default.find(exported.__requires, { name: 'Graph' });
        common_1.expect(require.name).to.be("Graph");
        common_1.expect(require.id).to.be("graph");
        common_1.expect(require.version).to.be("1.1.0");
    });
    common_1.it('should add grafana version', function () {
        var require = lodash_1.default.find(exported.__requires, { name: 'Grafana' });
        common_1.expect(require.type).to.be("grafana");
        common_1.expect(require.id).to.be("grafana");
        common_1.expect(require.version).to.be("3.0.2");
    });
    common_1.it('should add constant template variables as inputs', function () {
        var input = lodash_1.default.find(exported.__inputs, { name: 'VAR_PREFIX' });
        common_1.expect(input.type).to.be("constant");
        common_1.expect(input.label).to.be("prefix");
        common_1.expect(input.value).to.be("collectd");
    });
    common_1.it('should templatize constant variables', function () {
        var variable = lodash_1.default.find(exported.templating.list, { name: 'prefix' });
        common_1.expect(variable.query).to.be("${VAR_PREFIX}");
        common_1.expect(variable.current.text).to.be("${VAR_PREFIX}");
        common_1.expect(variable.current.value).to.be("${VAR_PREFIX}");
        common_1.expect(variable.options[0].text).to.be("${VAR_PREFIX}");
        common_1.expect(variable.options[0].value).to.be("${VAR_PREFIX}");
    });
});
