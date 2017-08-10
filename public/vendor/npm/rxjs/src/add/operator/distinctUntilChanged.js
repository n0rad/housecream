"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Observable_1 = require("../../Observable");
var distinctUntilChanged_1 = require("../../operator/distinctUntilChanged");
Observable_1.Observable.prototype.distinctUntilChanged = distinctUntilChanged_1.distinctUntilChanged;
