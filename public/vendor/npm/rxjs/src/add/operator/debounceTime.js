"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Observable_1 = require("../../Observable");
var debounceTime_1 = require("../../operator/debounceTime");
Observable_1.Observable.prototype.debounceTime = debounceTime_1.debounceTime;
