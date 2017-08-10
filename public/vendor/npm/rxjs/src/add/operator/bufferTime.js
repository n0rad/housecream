"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Observable_1 = require("../../Observable");
var bufferTime_1 = require("../../operator/bufferTime");
Observable_1.Observable.prototype.bufferTime = bufferTime_1.bufferTime;
