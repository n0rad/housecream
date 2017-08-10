"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Observable_1 = require("../../Observable");
var retry_1 = require("../../operator/retry");
Observable_1.Observable.prototype.retry = retry_1.retry;
