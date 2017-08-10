"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Observable_1 = require("../../Observable");
var dematerialize_1 = require("../../operator/dematerialize");
Observable_1.Observable.prototype.dematerialize = dematerialize_1.dematerialize;
