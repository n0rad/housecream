"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Observable_1 = require("../../Observable");
var retryWhen_1 = require("../../operator/retryWhen");
Observable_1.Observable.prototype.retryWhen = retryWhen_1.retryWhen;
