"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var common_1 = require("test/lib/common");
var core_1 = require("app/core/core");
common_1.describe("Emitter", function () {
    common_1.describe('given 2 subscribers', function () {
        common_1.it('should notfiy subscribers', function () {
            var events = new core_1.Emitter();
            var sub1Called = false;
            var sub2Called = false;
            events.on('test', function () {
                sub1Called = true;
            });
            events.on('test', function () {
                sub2Called = true;
            });
            events.emit('test', null);
            common_1.expect(sub1Called).to.be(true);
            common_1.expect(sub2Called).to.be(true);
        });
        common_1.it('when subscribing twice', function () {
            var events = new core_1.Emitter();
            var sub1Called = 0;
            function handler() {
                sub1Called += 1;
            }
            events.on('test', handler);
            events.on('test', handler);
            events.emit('test', null);
            common_1.expect(sub1Called).to.be(2);
        });
        common_1.it('should handle errors', function () {
            var events = new core_1.Emitter();
            var sub1Called = 0;
            var sub2Called = 0;
            events.on('test', function () {
                sub1Called++;
                throw "hello";
            });
            events.on('test', function () {
                sub2Called++;
            });
            try {
                events.emit('test', null);
            }
            catch (_) { }
            try {
                events.emit('test', null);
            }
            catch (_) { }
            common_1.expect(sub1Called).to.be(2);
            common_1.expect(sub2Called).to.be(0);
        });
    });
});
