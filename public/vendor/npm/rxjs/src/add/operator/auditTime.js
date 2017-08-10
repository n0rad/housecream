"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Observable_1 = require("../../Observable");
var auditTime_1 = require("../../operator/auditTime");
Observable_1.Observable.prototype.auditTime = auditTime_1.auditTime;
