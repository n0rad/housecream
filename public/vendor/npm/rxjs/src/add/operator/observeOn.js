"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Observable_1 = require("../../Observable");
var observeOn_1 = require("../../operator/observeOn");
Observable_1.Observable.prototype.observeOn = observeOn_1.observeOn;
