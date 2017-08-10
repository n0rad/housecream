"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Observable_1 = require("../../Observable");
var repeatWhen_1 = require("../../operator/repeatWhen");
Observable_1.Observable.prototype.repeatWhen = repeatWhen_1.repeatWhen;
