"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var common_1 = require("test/lib/common");
var query_variable_1 = require("../query_variable");
common_1.describe('QueryVariable', function () {
    common_1.describe('when creating from model', function () {
        common_1.it('should set defaults', function () {
            var variable = new query_variable_1.QueryVariable({}, null, null, null, null);
            common_1.expect(variable.datasource).to.be(null);
            common_1.expect(variable.refresh).to.be(0);
            common_1.expect(variable.sort).to.be(0);
            common_1.expect(variable.name).to.be('');
            common_1.expect(variable.hide).to.be(0);
            common_1.expect(variable.options.length).to.be(0);
            common_1.expect(variable.multi).to.be(false);
            common_1.expect(variable.includeAll).to.be(false);
        });
        common_1.it('get model should copy changes back to model', function () {
            var variable = new query_variable_1.QueryVariable({}, null, null, null, null);
            variable.options = [{ text: 'test' }];
            variable.datasource = 'google';
            variable.regex = 'asd';
            variable.sort = 50;
            var model = variable.getSaveModel();
            common_1.expect(model.options.length).to.be(1);
            common_1.expect(model.options[0].text).to.be('test');
            common_1.expect(model.datasource).to.be('google');
            common_1.expect(model.regex).to.be('asd');
            common_1.expect(model.sort).to.be(50);
        });
        common_1.it('if refresh != 0 then remove options in presisted mode', function () {
            var variable = new query_variable_1.QueryVariable({}, null, null, null, null);
            variable.options = [{ text: 'test' }];
            variable.refresh = 1;
            var model = variable.getSaveModel();
            common_1.expect(model.options.length).to.be(0);
        });
    });
    common_1.describe('can convert and sort metric names', function () {
        var variable = new query_variable_1.QueryVariable({}, null, null, null, null);
        variable.sort = 3; // Numerical (asc)
        common_1.describe('can sort a mixed array of metric variables', function () {
            var input = [
                { text: '0', value: '0' },
                { text: '1', value: '1' },
                { text: null, value: 3 },
                { text: undefined, value: 4 },
                { text: '5', value: null },
                { text: '6', value: undefined },
                { text: null, value: '7' },
                { text: undefined, value: '8' },
                { text: 9, value: null },
                { text: 10, value: undefined },
                { text: '', value: undefined },
                { text: undefined, value: '' },
            ];
            var result = variable.metricNamesToVariableValues(input);
            common_1.it('should return in same order', function () {
                var i = 0;
                common_1.expect(result.length).to.be(11);
                common_1.expect(result[i++].text).to.be('');
                common_1.expect(result[i++].text).to.be('0');
                common_1.expect(result[i++].text).to.be('1');
                common_1.expect(result[i++].text).to.be('3');
                common_1.expect(result[i++].text).to.be('4');
                common_1.expect(result[i++].text).to.be('5');
                common_1.expect(result[i++].text).to.be('6');
            });
        });
    });
});
