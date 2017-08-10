"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Observable_1 = require("../../Observable");
var materialize_1 = require("../../operator/materialize");
Observable_1.Observable.prototype.materialize = materialize_1.materialize;
