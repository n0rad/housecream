"use strict";
///<reference path="../../../headers/common.d.ts" />
Object.defineProperty(exports, "__esModule", { value: true });
var moment_1 = require("moment");
var dateMath = require("app/core/utils/datemath");
function inputDateDirective() {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function ($scope, $elem, attrs, ngModel) {
            var format = 'YYYY-MM-DD HH:mm:ss';
            var fromUser = function (text) {
                if (text.indexOf('now') !== -1) {
                    if (!dateMath.isValid(text)) {
                        ngModel.$setValidity("error", false);
                        return undefined;
                    }
                    ngModel.$setValidity("error", true);
                    return text;
                }
                var parsed;
                if ($scope.ctrl.isUtc) {
                    parsed = moment_1.default.utc(text, format);
                }
                else {
                    parsed = moment_1.default(text, format);
                }
                if (!parsed.isValid()) {
                    ngModel.$setValidity("error", false);
                    return undefined;
                }
                ngModel.$setValidity("error", true);
                return parsed;
            };
            var toUser = function (currentValue) {
                if (moment_1.default.isMoment(currentValue)) {
                    return currentValue.format(format);
                }
                else {
                    return currentValue;
                }
            };
            ngModel.$parsers.push(fromUser);
            ngModel.$formatters.push(toUser);
        }
    };
}
exports.inputDateDirective = inputDateDirective;
