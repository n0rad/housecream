"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Observable_1 = require("../../Observable");
var concat_1 = require("../../operator/concat");
Observable_1.Observable.prototype.concat = concat_1.concat;
