"use strict";
var __extends = (this && this.__extends) || (function () {
    var extendStatics = Object.setPrototypeOf ||
        ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
        function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
Object.defineProperty(exports, "__esModule", { value: true });
var async_1 = require("../scheduler/async");
var isDate_1 = require("../util/isDate");
var OuterSubscriber_1 = require("../OuterSubscriber");
var subscribeToResult_1 = require("../util/subscribeToResult");
/* tslint:disable:max-line-length */
/**
 * @param due
 * @param withObservable
 * @param scheduler
 * @return {Observable<R>|WebSocketSubject<T>|Observable<T>}
 * @method timeoutWith
 * @owner Observable
 */
function timeoutWith(due, withObservable, scheduler) {
    if (scheduler === void 0) { scheduler = async_1.async; }
    var absoluteTimeout = isDate_1.isDate(due);
    var waitFor = absoluteTimeout ? (+due - scheduler.now()) : Math.abs(due);
    return this.lift(new TimeoutWithOperator(waitFor, absoluteTimeout, withObservable, scheduler));
}
exports.timeoutWith = timeoutWith;
var TimeoutWithOperator = (function () {
    function TimeoutWithOperator(waitFor, absoluteTimeout, withObservable, scheduler) {
        this.waitFor = waitFor;
        this.absoluteTimeout = absoluteTimeout;
        this.withObservable = withObservable;
        this.scheduler = scheduler;
    }
    TimeoutWithOperator.prototype.call = function (subscriber, source) {
        return source.subscribe(new TimeoutWithSubscriber(subscriber, this.absoluteTimeout, this.waitFor, this.withObservable, this.scheduler));
    };
    return TimeoutWithOperator;
}());
/**
 * We need this JSDoc comment for affecting ESDoc.
 * @ignore
 * @extends {Ignored}
 */
var TimeoutWithSubscriber = (function (_super) {
    __extends(TimeoutWithSubscriber, _super);
    function TimeoutWithSubscriber(destination, absoluteTimeout, waitFor, withObservable, scheduler) {
        var _this = _super.call(this) || this;
        _this.destination = destination;
        _this.absoluteTimeout = absoluteTimeout;
        _this.waitFor = waitFor;
        _this.withObservable = withObservable;
        _this.scheduler = scheduler;
        _this.timeoutSubscription = undefined;
        _this.index = 0;
        _this._previousIndex = 0;
        _this._hasCompleted = false;
        destination.add(_this);
        _this.scheduleTimeout();
        return _this;
    }
    Object.defineProperty(TimeoutWithSubscriber.prototype, "previousIndex", {
        get: function () {
            return this._previousIndex;
        },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(TimeoutWithSubscriber.prototype, "hasCompleted", {
        get: function () {
            return this._hasCompleted;
        },
        enumerable: true,
        configurable: true
    });
    TimeoutWithSubscriber.dispatchTimeout = function (state) {
        var source = state.subscriber;
        var currentIndex = state.index;
        if (!source.hasCompleted && source.previousIndex === currentIndex) {
            source.handleTimeout();
        }
    };
    TimeoutWithSubscriber.prototype.scheduleTimeout = function () {
        var currentIndex = this.index;
        var timeoutState = { subscriber: this, index: currentIndex };
        this.scheduler.schedule(TimeoutWithSubscriber.dispatchTimeout, this.waitFor, timeoutState);
        this.index++;
        this._previousIndex = currentIndex;
    };
    TimeoutWithSubscriber.prototype._next = function (value) {
        this.destination.next(value);
        if (!this.absoluteTimeout) {
            this.scheduleTimeout();
        }
    };
    TimeoutWithSubscriber.prototype._error = function (err) {
        this.destination.error(err);
        this._hasCompleted = true;
    };
    TimeoutWithSubscriber.prototype._complete = function () {
        this.destination.complete();
        this._hasCompleted = true;
    };
    TimeoutWithSubscriber.prototype.handleTimeout = function () {
        if (!this.closed) {
            var withObservable = this.withObservable;
            this.unsubscribe();
            this.destination.add(this.timeoutSubscription = subscribeToResult_1.subscribeToResult(this, withObservable));
        }
    };
    return TimeoutWithSubscriber;
}(OuterSubscriber_1.OuterSubscriber));
