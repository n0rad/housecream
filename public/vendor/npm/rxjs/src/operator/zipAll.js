"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var zip_1 = require("./zip");
/**
 * @param project
 * @return {Observable<R>|WebSocketSubject<T>|Observable<T>}
 * @method zipAll
 * @owner Observable
 */
function zipAll(project) {
    return this.lift(new zip_1.ZipOperator(project));
}
exports.zipAll = zipAll;
