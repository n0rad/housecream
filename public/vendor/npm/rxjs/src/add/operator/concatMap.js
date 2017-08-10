"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Observable_1 = require("../../Observable");
var concatMap_1 = require("../../operator/concatMap");
Observable_1.Observable.prototype.concatMap = concatMap_1.concatMap;
