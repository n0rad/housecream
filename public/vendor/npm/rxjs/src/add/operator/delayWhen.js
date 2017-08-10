"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Observable_1 = require("../../Observable");
var delayWhen_1 = require("../../operator/delayWhen");
Observable_1.Observable.prototype.delayWhen = delayWhen_1.delayWhen;
