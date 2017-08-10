"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Observable_1 = require("../../Observable");
var timeoutWith_1 = require("../../operator/timeoutWith");
Observable_1.Observable.prototype.timeoutWith = timeoutWith_1.timeoutWith;
