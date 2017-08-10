"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Observable_1 = require("../../Observable");
var merge_1 = require("../../operator/merge");
Observable_1.Observable.prototype.merge = merge_1.merge;
