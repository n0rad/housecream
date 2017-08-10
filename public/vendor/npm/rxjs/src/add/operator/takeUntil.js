"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Observable_1 = require("../../Observable");
var takeUntil_1 = require("../../operator/takeUntil");
Observable_1.Observable.prototype.takeUntil = takeUntil_1.takeUntil;
