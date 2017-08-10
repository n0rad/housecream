"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var common_1 = require("test/lib/common");
var query_part_1 = require("../query_part");
common_1.describe('InfluxQueryPart', function () {
    common_1.describe('series with mesurement only', function () {
        common_1.it('should handle nested function parts', function () {
            var part = query_part_1.default.create({
                type: 'derivative',
                params: ['10s'],
            });
            common_1.expect(part.text).to.be('derivative(10s)');
            common_1.expect(part.render('mean(value)')).to.be('derivative(mean(value), 10s)');
        });
        common_1.it('should nest spread function', function () {
            var part = query_part_1.default.create({
                type: 'spread'
            });
            common_1.expect(part.text).to.be('spread()');
            common_1.expect(part.render('value')).to.be('spread(value)');
        });
        common_1.it('should handle suffirx parts', function () {
            var part = query_part_1.default.create({
                type: 'math',
                params: ['/ 100'],
            });
            common_1.expect(part.text).to.be('math(/ 100)');
            common_1.expect(part.render('mean(value)')).to.be('mean(value) / 100');
        });
        common_1.it('should handle alias parts', function () {
            var part = query_part_1.default.create({
                type: 'alias',
                params: ['test'],
            });
            common_1.expect(part.text).to.be('alias(test)');
            common_1.expect(part.render('mean(value)')).to.be('mean(value) AS "test"');
        });
    });
});
