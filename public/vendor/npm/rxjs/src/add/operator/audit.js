"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Observable_1 = require("../../Observable");
var audit_1 = require("../../operator/audit");
Observable_1.Observable.prototype.audit = audit_1.audit;
