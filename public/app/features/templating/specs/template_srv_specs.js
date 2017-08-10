"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var common_1 = require("test/lib/common");
require("../all");
var core_1 = require("app/core/core");
common_1.describe('templateSrv', function () {
    var _templateSrv, _variableSrv;
    common_1.beforeEach(common_1.angularMocks.module('grafana.core'));
    common_1.beforeEach(common_1.angularMocks.module('grafana.services'));
    common_1.beforeEach(common_1.angularMocks.module(function ($provide) {
        $provide.value('timeSrv', {});
    }));
    common_1.beforeEach(common_1.angularMocks.inject(function (variableSrv, templateSrv) {
        _templateSrv = templateSrv;
        _variableSrv = variableSrv;
    }));
    function initTemplateSrv(variables) {
        _variableSrv.init({
            templating: { list: variables },
            events: new core_1.Emitter(),
        });
    }
    common_1.describe('init', function () {
        common_1.beforeEach(function () {
            initTemplateSrv([{ type: 'query', name: 'test', current: { value: 'oogle' } }]);
        });
        common_1.it('should initialize template data', function () {
            var target = _templateSrv.replace('this.[[test]].filters');
            common_1.expect(target).to.be('this.oogle.filters');
        });
    });
    common_1.describe('replace can pass scoped vars', function () {
        common_1.beforeEach(function () {
            initTemplateSrv([{ type: 'query', name: 'test', current: { value: 'oogle' } }]);
        });
        common_1.it('should replace $test with scoped value', function () {
            var target = _templateSrv.replace('this.$test.filters', { 'test': { value: 'mupp', text: 'asd' } });
            common_1.expect(target).to.be('this.mupp.filters');
        });
        common_1.it('should replace $test with scoped text', function () {
            var target = _templateSrv.replaceWithText('this.$test.filters', { 'test': { value: 'mupp', text: 'asd' } });
            common_1.expect(target).to.be('this.asd.filters');
        });
    });
    common_1.describe('replace can pass multi / all format', function () {
        common_1.beforeEach(function () {
            initTemplateSrv([{ type: 'query', name: 'test', current: { value: ['value1', 'value2'] } }]);
        });
        common_1.it('should replace $test with globbed value', function () {
            var target = _templateSrv.replace('this.$test.filters', {}, 'glob');
            common_1.expect(target).to.be('this.{value1,value2}.filters');
        });
        common_1.it('should replace $test with piped value', function () {
            var target = _templateSrv.replace('this=$test', {}, 'pipe');
            common_1.expect(target).to.be('this=value1|value2');
        });
        common_1.it('should replace $test with piped value', function () {
            var target = _templateSrv.replace('this=$test', {}, 'pipe');
            common_1.expect(target).to.be('this=value1|value2');
        });
    });
    common_1.describe('variable with all option', function () {
        common_1.beforeEach(function () {
            initTemplateSrv([{
                    type: 'query',
                    name: 'test',
                    current: { value: '$__all' },
                    options: [
                        { value: '$__all' }, { value: 'value1' }, { value: 'value2' }
                    ]
                }]);
        });
        common_1.it('should replace $test with formatted all value', function () {
            var target = _templateSrv.replace('this.$test.filters', {}, 'glob');
            common_1.expect(target).to.be('this.{value1,value2}.filters');
        });
    });
    common_1.describe('variable with all option and custom value', function () {
        common_1.beforeEach(function () {
            initTemplateSrv([{
                    type: 'query',
                    name: 'test',
                    current: { value: '$__all' },
                    allValue: '*',
                    options: [
                        { value: 'value1' }, { value: 'value2' }
                    ]
                }]);
        });
        common_1.it('should replace $test with formatted all value', function () {
            var target = _templateSrv.replace('this.$test.filters', {}, 'glob');
            common_1.expect(target).to.be('this.*.filters');
        });
        common_1.it('should not escape custom all value', function () {
            var target = _templateSrv.replace('this.$test', {}, 'regex');
            common_1.expect(target).to.be('this.*');
        });
    });
    common_1.describe('lucene format', function () {
        common_1.it('should properly escape $test with lucene escape sequences', function () {
            initTemplateSrv([{ type: 'query', name: 'test', current: { value: 'value/4' } }]);
            var target = _templateSrv.replace('this:$test', {}, 'lucene');
            common_1.expect(target).to.be("this:value\\\/4");
        });
    });
    common_1.describe('format variable to string values', function () {
        common_1.it('single value should return value', function () {
            var result = _templateSrv.formatValue('test');
            common_1.expect(result).to.be('test');
        });
        common_1.it('multi value and glob format should render glob string', function () {
            var result = _templateSrv.formatValue(['test', 'test2'], 'glob');
            common_1.expect(result).to.be('{test,test2}');
        });
        common_1.it('multi value and lucene should render as lucene expr', function () {
            var result = _templateSrv.formatValue(['test', 'test2'], 'lucene');
            common_1.expect(result).to.be('("test" OR "test2")');
        });
        common_1.it('multi value and regex format should render regex string', function () {
            var result = _templateSrv.formatValue(['test.', 'test2'], 'regex');
            common_1.expect(result).to.be('(test\\.|test2)');
        });
        common_1.it('multi value and pipe should render pipe string', function () {
            var result = _templateSrv.formatValue(['test', 'test2'], 'pipe');
            common_1.expect(result).to.be('test|test2');
        });
        common_1.it('multi value and distributed should render distributed string', function () {
            var result = _templateSrv.formatValue(['test', 'test2'], 'distributed', { name: 'build' });
            common_1.expect(result).to.be('test,build=test2');
        });
        common_1.it('multi value and distributed should render when not string', function () {
            var result = _templateSrv.formatValue(['test'], 'distributed', { name: 'build' });
            common_1.expect(result).to.be('test');
        });
        common_1.it('slash should be properly escaped in regex format', function () {
            var result = _templateSrv.formatValue('Gi3/14', 'regex');
            common_1.expect(result).to.be('Gi3\\/14');
        });
    });
    common_1.describe('can check if variable exists', function () {
        common_1.beforeEach(function () {
            initTemplateSrv([{ type: 'query', name: 'test', current: { value: 'oogle' } }]);
        });
        common_1.it('should return true if exists', function () {
            var result = _templateSrv.variableExists('$test');
            common_1.expect(result).to.be(true);
        });
    });
    common_1.describe('can hightlight variables in string', function () {
        common_1.beforeEach(function () {
            initTemplateSrv([{ type: 'query', name: 'test', current: { value: 'oogle' } }]);
        });
        common_1.it('should insert html', function () {
            var result = _templateSrv.highlightVariablesAsHtml('$test');
            common_1.expect(result).to.be('<span class="template-variable">$test</span>');
        });
        common_1.it('should insert html anywhere in string', function () {
            var result = _templateSrv.highlightVariablesAsHtml('this $test ok');
            common_1.expect(result).to.be('this <span class="template-variable">$test</span> ok');
        });
        common_1.it('should ignore if variables does not exist', function () {
            var result = _templateSrv.highlightVariablesAsHtml('this $google ok');
            common_1.expect(result).to.be('this $google ok');
        });
    });
    common_1.describe('updateTemplateData with simple value', function () {
        common_1.beforeEach(function () {
            initTemplateSrv([{ type: 'query', name: 'test', current: { value: 'muuuu' } }]);
        });
        common_1.it('should set current value and update template data', function () {
            var target = _templateSrv.replace('this.[[test]].filters');
            common_1.expect(target).to.be('this.muuuu.filters');
        });
    });
    common_1.describe('fillVariableValuesForUrl with multi value', function () {
        common_1.beforeEach(function () {
            initTemplateSrv([{ type: 'query', name: 'test', current: { value: ['val1', 'val2'] } }]);
        });
        common_1.it('should set multiple url params', function () {
            var params = {};
            _templateSrv.fillVariableValuesForUrl(params);
            common_1.expect(params['var-test']).to.eql(['val1', 'val2']);
        });
    });
    common_1.describe('fillVariableValuesForUrl with multi value and scopedVars', function () {
        common_1.beforeEach(function () {
            initTemplateSrv([{ type: 'query', name: 'test', current: { value: ['val1', 'val2'] } }]);
        });
        common_1.it('should set scoped value as url params', function () {
            var params = {};
            _templateSrv.fillVariableValuesForUrl(params, { 'test': { value: 'val1' } });
            common_1.expect(params['var-test']).to.eql('val1');
        });
    });
    common_1.describe('replaceWithText', function () {
        common_1.beforeEach(function () {
            initTemplateSrv([
                { type: 'query', name: 'server', current: { value: '{asd,asd2}', text: 'All' } },
                { type: 'interval', name: 'period', current: { value: '$__auto_interval', text: 'auto' } }
            ]);
            _templateSrv.setGrafanaVariable('$__auto_interval', '13m');
            _templateSrv.updateTemplateData();
        });
        common_1.it('should replace with text except for grafanaVariables', function () {
            var target = _templateSrv.replaceWithText('Server: $server, period: $period');
            common_1.expect(target).to.be('Server: All, period: 13m');
        });
    });
    common_1.describe('built in interval variables', function () {
        common_1.beforeEach(function () {
            initTemplateSrv([]);
        });
        common_1.it('should replace $__interval_ms with interval milliseconds', function () {
            var target = _templateSrv.replace('10 * $__interval_ms', { "__interval_ms": { text: "100", value: "100" } });
            common_1.expect(target).to.be('10 * 100');
        });
    });
});
