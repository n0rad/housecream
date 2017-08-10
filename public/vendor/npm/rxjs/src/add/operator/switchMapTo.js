"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Observable_1 = require("../../Observable");
var switchMapTo_1 = require("../../operator/switchMapTo");
Observable_1.Observable.prototype.switchMapTo = switchMapTo_1.switchMapTo;
