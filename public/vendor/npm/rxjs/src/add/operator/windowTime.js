"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Observable_1 = require("../../Observable");
var windowTime_1 = require("../../operator/windowTime");
Observable_1.Observable.prototype.windowTime = windowTime_1.windowTime;
