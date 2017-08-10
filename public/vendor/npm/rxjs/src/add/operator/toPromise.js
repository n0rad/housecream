"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Observable_1 = require("../../Observable");
var toPromise_1 = require("../../operator/toPromise");
Observable_1.Observable.prototype.toPromise = toPromise_1.toPromise;
