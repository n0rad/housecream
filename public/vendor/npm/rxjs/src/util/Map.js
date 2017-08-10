"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var root_1 = require("./root");
var MapPolyfill_1 = require("./MapPolyfill");
exports.Map = root_1.root.Map || (function () { return MapPolyfill_1.MapPolyfill; })();
