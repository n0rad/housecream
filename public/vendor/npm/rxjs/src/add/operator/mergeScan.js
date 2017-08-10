"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Observable_1 = require("../../Observable");
var mergeScan_1 = require("../../operator/mergeScan");
Observable_1.Observable.prototype.mergeScan = mergeScan_1.mergeScan;
