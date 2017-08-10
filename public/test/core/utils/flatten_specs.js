"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var common_1 = require("test/lib/common");
var flatten_1 = require("app/core/utils/flatten");
common_1.describe("flatten", function () {
    common_1.it('should return flatten object', function () {
        var flattened = flatten_1.default({
            level1: 'level1-value',
            deeper: {
                level2: 'level2-value',
                deeper: {
                    level3: 'level3-value'
                }
            }
        }, null);
        common_1.expect(flattened['level1']).to.be('level1-value');
        common_1.expect(flattened['deeper.level2']).to.be('level2-value');
        common_1.expect(flattened['deeper.deeper.level3']).to.be('level3-value');
    });
});
