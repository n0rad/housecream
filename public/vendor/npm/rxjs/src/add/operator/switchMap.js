"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Observable_1 = require("../../Observable");
var switchMap_1 = require("../../operator/switchMap");
Observable_1.Observable.prototype.switchMap = switchMap_1.switchMap;
