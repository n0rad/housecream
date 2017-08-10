"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Observable_1 = require("../../Observable");
var pairwise_1 = require("../../operator/pairwise");
Observable_1.Observable.prototype.pairwise = pairwise_1.pairwise;
