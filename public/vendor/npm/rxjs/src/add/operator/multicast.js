"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Observable_1 = require("../../Observable");
var multicast_1 = require("../../operator/multicast");
Observable_1.Observable.prototype.multicast = multicast_1.multicast;
