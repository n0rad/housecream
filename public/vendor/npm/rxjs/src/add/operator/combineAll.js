"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Observable_1 = require("../../Observable");
var combineAll_1 = require("../../operator/combineAll");
Observable_1.Observable.prototype.combineAll = combineAll_1.combineAll;
