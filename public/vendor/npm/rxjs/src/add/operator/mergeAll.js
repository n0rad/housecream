"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Observable_1 = require("../../Observable");
var mergeAll_1 = require("../../operator/mergeAll");
Observable_1.Observable.prototype.mergeAll = mergeAll_1.mergeAll;
