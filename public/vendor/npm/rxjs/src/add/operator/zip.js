"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Observable_1 = require("../../Observable");
var zip_1 = require("../../operator/zip");
Observable_1.Observable.prototype.zip = zip_1.zipProto;
