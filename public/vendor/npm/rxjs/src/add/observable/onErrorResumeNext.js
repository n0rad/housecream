"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Observable_1 = require("../../Observable");
var onErrorResumeNext_1 = require("../../operator/onErrorResumeNext");
Observable_1.Observable.onErrorResumeNext = onErrorResumeNext_1.onErrorResumeNextStatic;
