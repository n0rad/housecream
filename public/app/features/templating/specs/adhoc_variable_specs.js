"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var common_1 = require("test/lib/common");
var adhoc_variable_1 = require("../adhoc_variable");
common_1.describe('AdhocVariable', function () {
    common_1.describe('when serializing to url', function () {
        common_1.it('should set return key value and op seperated by pipe', function () {
            var variable = new adhoc_variable_1.AdhocVariable({
                filters: [
                    { key: 'key1', operator: '=', value: 'value1' },
                    { key: 'key2', operator: '!=', value: 'value2' },
                ]
            });
            var urlValue = variable.getValueForUrl();
            common_1.expect(urlValue).to.eql(["key1|=|value1", "key2|!=|value2"]);
        });
    });
    common_1.describe('when deserializing from url', function () {
        common_1.it('should restore filters', function () {
            var variable = new adhoc_variable_1.AdhocVariable({});
            variable.setValueFromUrl(["key1|=|value1", "key2|!=|value2"]);
            common_1.expect(variable.filters[0].key).to.be('key1');
            common_1.expect(variable.filters[0].operator).to.be('=');
            common_1.expect(variable.filters[0].value).to.be('value1');
            common_1.expect(variable.filters[1].key).to.be('key2');
            common_1.expect(variable.filters[1].operator).to.be('!=');
            common_1.expect(variable.filters[1].value).to.be('value2');
        });
    });
});
