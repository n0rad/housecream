"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Observable_1 = require("../../Observable");
var concatAll_1 = require("../../operator/concatAll");
Observable_1.Observable.prototype.concatAll = concatAll_1.concatAll;
