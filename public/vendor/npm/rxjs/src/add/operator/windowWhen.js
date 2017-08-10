"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Observable_1 = require("../../Observable");
var windowWhen_1 = require("../../operator/windowWhen");
Observable_1.Observable.prototype.windowWhen = windowWhen_1.windowWhen;
