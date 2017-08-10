"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Observable_1 = require("../../Observable");
var expand_1 = require("../../operator/expand");
Observable_1.Observable.prototype.expand = expand_1.expand;
