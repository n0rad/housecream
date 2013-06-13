function getHostname(str) {
	var re = new RegExp('^(?:f|ht)tp(?:s)?\://([^/]+)', 'im');
	var matched = str.match(re);
	if (matched) {
		return matched[1].toString();
	}
	return null;
}

if (typeof String.prototype.trim !== 'function') {
	String.prototype.trim = function() {
		return this.replace(/^\s+|\s+$/g, '');
	}
}

Object.size = function(obj) {
	var size = 0, key;
	for (key in obj) {
		if (obj.hasOwnProperty(key))
			size++;
	}
	return size;
};

function strip(html) {
	var tmp = document.createElement("DIV");
	tmp.innerHTML = html;
	return tmp.textContent || tmp.innerText;
}

function exists(obj, prop) {
	var parts = prop.split('.');
	for ( var i = 0, l = parts.length; i < l; i++) {
		var part = parts[i];
		if (obj !== null && typeof obj === "object" && part in obj) {
			obj = obj[part];
		} else {
			return false;
		}
	}
	return true;
}

function ISODateString(d) {
	function pad(n) {
		return n < 10 ? '0' + n : n;
	}
	return d.getUTCFullYear() + '-' + pad(d.getUTCMonth() + 1) + '-' + pad(d.getUTCDate()) + 'T' + pad(d.getUTCHours())
			+ ':' + pad(d.getUTCMinutes()) + ':' + pad(d.getUTCSeconds()) + 'Z';
}

function get_gravatar(email, size) {
	var MD5 = function(s) {
		function L(k, d) {
			return (k << d) | (k >>> (32 - d))
		}
		function K(G, k) {
			var I, d, F, H, x;
			F = (G & 2147483648);
			H = (k & 2147483648);
			I = (G & 1073741824);
			d = (k & 1073741824);
			x = (G & 1073741823) + (k & 1073741823);
			if (I & d) {
				return (x ^ 2147483648 ^ F ^ H)
			}
			if (I | d) {
				if (x & 1073741824) {
					return (x ^ 3221225472 ^ F ^ H)
				} else {
					return (x ^ 1073741824 ^ F ^ H)
				}
			} else {
				return (x ^ F ^ H)
			}
		}
		function r(d, F, k) {
			return (d & F) | ((~d) & k)
		}
		function q(d, F, k) {
			return (d & k) | (F & (~k))
		}
		function p(d, F, k) {
			return (d ^ F ^ k)
		}
		function n(d, F, k) {
			return (F ^ (d | (~k)))
		}
		function u(G, F, aa, Z, k, H, I) {
			G = K(G, K(K(r(F, aa, Z), k), I));
			return K(L(G, H), F)
		}
		function f(G, F, aa, Z, k, H, I) {
			G = K(G, K(K(q(F, aa, Z), k), I));
			return K(L(G, H), F)
		}
		function D(G, F, aa, Z, k, H, I) {
			G = K(G, K(K(p(F, aa, Z), k), I));
			return K(L(G, H), F)
		}
		function t(G, F, aa, Z, k, H, I) {
			G = K(G, K(K(n(F, aa, Z), k), I));
			return K(L(G, H), F)
		}
		function e(G) {
			var Z;
			var F = G.length;
			var x = F + 8;
			var k = (x - (x % 64)) / 64;
			var I = (k + 1) * 16;
			var aa = Array(I - 1);
			var d = 0;
			var H = 0;
			while (H < F) {
				Z = (H - (H % 4)) / 4;
				d = (H % 4) * 8;
				aa[Z] = (aa[Z] | (G.charCodeAt(H) << d));
				H++
			}
			Z = (H - (H % 4)) / 4;
			d = (H % 4) * 8;
			aa[Z] = aa[Z] | (128 << d);
			aa[I - 2] = F << 3;
			aa[I - 1] = F >>> 29;
			return aa
		}
		function B(x) {
			var k = "", F = "", G, d;
			for (d = 0; d <= 3; d++) {
				G = (x >>> (d * 8)) & 255;
				F = "0" + G.toString(16);
				k = k + F.substr(F.length - 2, 2)
			}
			return k
		}
		function J(k) {
			k = k.replace(/rn/g, "n");
			var d = "";
			for ( var F = 0; F < k.length; F++) {
				var x = k.charCodeAt(F);
				if (x < 128) {
					d += String.fromCharCode(x)
				} else {
					if ((x > 127) && (x < 2048)) {
						d += String.fromCharCode((x >> 6) | 192);
						d += String.fromCharCode((x & 63) | 128)
					} else {
						d += String.fromCharCode((x >> 12) | 224);
						d += String.fromCharCode(((x >> 6) & 63) | 128);
						d += String.fromCharCode((x & 63) | 128)
					}
				}
			}
			return d
		}
		var C = Array();
		var P, h, E, v, g, Y, X, W, V;
		var S = 7, Q = 12, N = 17, M = 22;
		var A = 5, z = 9, y = 14, w = 20;
		var o = 4, m = 11, l = 16, j = 23;
		var U = 6, T = 10, R = 15, O = 21;
		s = J(s);
		C = e(s);
		Y = 1732584193;
		X = 4023233417;
		W = 2562383102;
		V = 271733878;
		for (P = 0; P < C.length; P += 16) {
			h = Y;
			E = X;
			v = W;
			g = V;
			Y = u(Y, X, W, V, C[P + 0], S, 3614090360);
			V = u(V, Y, X, W, C[P + 1], Q, 3905402710);
			W = u(W, V, Y, X, C[P + 2], N, 606105819);
			X = u(X, W, V, Y, C[P + 3], M, 3250441966);
			Y = u(Y, X, W, V, C[P + 4], S, 4118548399);
			V = u(V, Y, X, W, C[P + 5], Q, 1200080426);
			W = u(W, V, Y, X, C[P + 6], N, 2821735955);
			X = u(X, W, V, Y, C[P + 7], M, 4249261313);
			Y = u(Y, X, W, V, C[P + 8], S, 1770035416);
			V = u(V, Y, X, W, C[P + 9], Q, 2336552879);
			W = u(W, V, Y, X, C[P + 10], N, 4294925233);
			X = u(X, W, V, Y, C[P + 11], M, 2304563134);
			Y = u(Y, X, W, V, C[P + 12], S, 1804603682);
			V = u(V, Y, X, W, C[P + 13], Q, 4254626195);
			W = u(W, V, Y, X, C[P + 14], N, 2792965006);
			X = u(X, W, V, Y, C[P + 15], M, 1236535329);
			Y = f(Y, X, W, V, C[P + 1], A, 4129170786);
			V = f(V, Y, X, W, C[P + 6], z, 3225465664);
			W = f(W, V, Y, X, C[P + 11], y, 643717713);
			X = f(X, W, V, Y, C[P + 0], w, 3921069994);
			Y = f(Y, X, W, V, C[P + 5], A, 3593408605);
			V = f(V, Y, X, W, C[P + 10], z, 38016083);
			W = f(W, V, Y, X, C[P + 15], y, 3634488961);
			X = f(X, W, V, Y, C[P + 4], w, 3889429448);
			Y = f(Y, X, W, V, C[P + 9], A, 568446438);
			V = f(V, Y, X, W, C[P + 14], z, 3275163606);
			W = f(W, V, Y, X, C[P + 3], y, 4107603335);
			X = f(X, W, V, Y, C[P + 8], w, 1163531501);
			Y = f(Y, X, W, V, C[P + 13], A, 2850285829);
			V = f(V, Y, X, W, C[P + 2], z, 4243563512);
			W = f(W, V, Y, X, C[P + 7], y, 1735328473);
			X = f(X, W, V, Y, C[P + 12], w, 2368359562);
			Y = D(Y, X, W, V, C[P + 5], o, 4294588738);
			V = D(V, Y, X, W, C[P + 8], m, 2272392833);
			W = D(W, V, Y, X, C[P + 11], l, 1839030562);
			X = D(X, W, V, Y, C[P + 14], j, 4259657740);
			Y = D(Y, X, W, V, C[P + 1], o, 2763975236);
			V = D(V, Y, X, W, C[P + 4], m, 1272893353);
			W = D(W, V, Y, X, C[P + 7], l, 4139469664);
			X = D(X, W, V, Y, C[P + 10], j, 3200236656);
			Y = D(Y, X, W, V, C[P + 13], o, 681279174);
			V = D(V, Y, X, W, C[P + 0], m, 3936430074);
			W = D(W, V, Y, X, C[P + 3], l, 3572445317);
			X = D(X, W, V, Y, C[P + 6], j, 76029189);
			Y = D(Y, X, W, V, C[P + 9], o, 3654602809);
			V = D(V, Y, X, W, C[P + 12], m, 3873151461);
			W = D(W, V, Y, X, C[P + 15], l, 530742520);
			X = D(X, W, V, Y, C[P + 2], j, 3299628645);
			Y = t(Y, X, W, V, C[P + 0], U, 4096336452);
			V = t(V, Y, X, W, C[P + 7], T, 1126891415);
			W = t(W, V, Y, X, C[P + 14], R, 2878612391);
			X = t(X, W, V, Y, C[P + 5], O, 4237533241);
			Y = t(Y, X, W, V, C[P + 12], U, 1700485571);
			V = t(V, Y, X, W, C[P + 3], T, 2399980690);
			W = t(W, V, Y, X, C[P + 10], R, 4293915773);
			X = t(X, W, V, Y, C[P + 1], O, 2240044497);
			Y = t(Y, X, W, V, C[P + 8], U, 1873313359);
			V = t(V, Y, X, W, C[P + 15], T, 4264355552);
			W = t(W, V, Y, X, C[P + 6], R, 2734768916);
			X = t(X, W, V, Y, C[P + 13], O, 1309151649);
			Y = t(Y, X, W, V, C[P + 4], U, 4149444226);
			V = t(V, Y, X, W, C[P + 11], T, 3174756917);
			W = t(W, V, Y, X, C[P + 2], R, 718787259);
			X = t(X, W, V, Y, C[P + 9], O, 3951481745);
			Y = K(Y, h);
			X = K(X, E);
			W = K(W, v);
			V = K(V, g)
		}
		var i = B(Y) + B(X) + B(W) + B(V);
		return i.toLowerCase()
	};
	var size = size || 80;
	return 'http://www.gravatar.com/avatar/' + MD5(email) + '.jpg?s=' + size;
}

function objectEquals(obj1, obj2) {
	for ( var i in obj1) {
		if (obj1.hasOwnProperty(i)) {
			if (!obj2.hasOwnProperty(i))
				return false;
			if (obj1[i] != obj2[i])
				return false;
		}
	}
	for ( var i in obj2) {
		if (obj2.hasOwnProperty(i)) {
			if (!obj1.hasOwnProperty(i))
				return false;
			if (obj1[i] != obj2[i])
				return false;
		}
	}
	return true;
}

String.prototype.ucFirst = function() {
	return this.charAt(0).toUpperCase() + this.slice(1);
};

String.prototype.lcFirst = function() {
	return this.charAt(0).toLowerCase() + this.slice(1);
};

String.prototype.startsWith = function(str) {
	return (this.match("^" + str) == str);
};

function AsynCall(what, callback) {
	window.setTimeout(function() {
		res = what();
		callback(res);
	}, 0);
}

function AsynCall(what) {
	window.setTimeout(function() {
		what();
	}, 0);
}

// TODO remove after tests
function sleep(delay) {
	var start = new Date().getTime();
	while (new Date().getTime() < start + delay)
		;
}

function isObjectEmpty(o) {
	for ( var p in o) {
		// skip proto elements
		if (o[p] != o.constructor.prototype[p])
			return false;
	}
	return true;
}

function printStackTrace(ex, options) {
	// var ex = (options && options.e) ? options.e : null;
	var guess = options ? !!options.guess : true;

	var p = new printStackTrace.implementation();
	var result = p.run(ex);
	return (guess) ? p.guessFunctions(result) : result;
}

printStackTrace.implementation = function() {
};

printStackTrace.implementation.prototype = {
	run : function(ex) {
		// Use either the stored mode, or resolve it
		var mode = this._mode || this.mode();
		if (mode === 'other') {
			return this.other(arguments.callee);
		} else {
			ex = ex || (function() {
				try {
					(0)();
				} catch (e) {
					return e;
				}
			})();
			return this[mode](ex);
		}
	},

	mode : function() {
		try {
			(0)();
		} catch (e) {
			if (e.arguments) {
				return (this._mode = 'chrome');
			} else if (e.stack) {
				return (this._mode = 'firefox');
			} else if (window.opera && !('stacktrace' in e)) { // Opera 9-
				return (this._mode = 'opera');
			}
		}
		return (this._mode = 'other');
	},

	chrome : function(e) {
		return e.stack.replace(/^.*?\n/, '').replace(/^.*?\n/, '').replace(/^.*?\n/, '').replace(/^[^\(]+?[\n$]/gm, '')
				.replace(/^\s+at\s+/gm, '').replace(/^Object.<anonymous>\s*\(/gm, '{anonymous}()@').split('\n');
	},

	firefox : function(e) {
		return e.stack.replace(/^.*?\n/, '').replace(/(?:\n@:0)?\s+$/m, '').replace(/^\(/gm, '{anonymous}(')
				.split('\n');
	},

	// Opera 7.x and 8.x only!
	opera : function(e) {
		var lines = e.message.split('\n'), ANON = '{anonymous}', lineRE = /Line\s+(\d+).*?script\s+(http\S+)(?:.*?in\s+function\s+(\S+))?/i, i, j, len;

		for (i = 4, j = 0, len = lines.length; i < len; i += 2) {
			if (lineRE.test(lines[i])) {
				lines[j++] = (RegExp.$3 ? RegExp.$3 + '()@' + RegExp.$2 + RegExp.$1 : ANON + '()@' + RegExp.$2 + ':'
						+ RegExp.$1)
						+ ' -- ' + lines[i + 1].replace(/^\s+/, '');
			}
		}

		lines.splice(j, lines.length - j);
		return lines;
	},

	// Safari, Opera 9+, IE, and others
	other : function(curr) {
		var ANON = '{anonymous}', fnRE = /function\s*([\w\-$]+)?\s*\(/i, stack = [], j = 0, fn, args;

		var maxStackSize = 10;
		while (curr && stack.length < maxStackSize) {
			fn = fnRE.test(curr.toString()) ? RegExp.$1 || ANON : ANON;
			args = Array.prototype.slice.call(curr['arguments']);
			stack[j++] = fn + '(' + printStackTrace.implementation.prototype.stringifyArguments(args) + ')';

			// Opera bug: if curr.caller does not exist, Opera returns curr
			// (WTF)
			if (curr === curr.caller && window.opera) {
				// TODO: check for same arguments if possible
				break;
			}
			curr = curr.caller;
		}
		return stack;
	},

	/**
	 * @return given arguments array as a String, subsituting type names for
	 *         non-string types.
	 */
	stringifyArguments : function(args) {
		for ( var i = 0; i < args.length; ++i) {
			var argument = args[i];
			if (typeof argument == 'object') {
				args[i] = '#object';
			} else if (typeof argument == 'function') {
				args[i] = '#function';
			} else if (typeof argument == 'string') {
				args[i] = '"' + argument + '"';
			}
		}
		return args.join(',');
	},

	sourceCache : {},

	/**
	 * @return the text from a given URL.
	 */
	ajax : function(url) {
		var req = this.createXMLHTTPObject();
		if (!req) {
			return;
		}
		req.open('GET', url, false);
		req.setRequestHeader('User-Agent', 'XMLHTTP/1.0');
		req.send('');
		return req.responseText;
	},

	createXMLHTTPObject : function() {
		// Try XHR methods in order and store XHR factory
		var xmlhttp, XMLHttpFactories = [ function() {
			return new XMLHttpRequest();
		}, function() {
			return new ActiveXObject('Msxml2.XMLHTTP');
		}, function() {
			return new ActiveXObject('Msxml3.XMLHTTP');
		}, function() {
			return new ActiveXObject('Microsoft.XMLHTTP');
		} ];
		for ( var i = 0; i < XMLHttpFactories.length; i++) {
			try {
				xmlhttp = XMLHttpFactories[i]();
				// Use memoization to cache the factory
				this.createXMLHTTPObject = XMLHttpFactories[i];
				return xmlhttp;
			} catch (e) {
			}
		}
	},

	getSource : function(url) {
		if (!(url in this.sourceCache)) {
			this.sourceCache[url] = this.ajax(url).split('\n');
		}
		return this.sourceCache[url];
	},

	guessFunctions : function(stack) {
		for ( var i = 0; i < stack.length; ++i) {
			var reStack = /{anonymous}\(.*\)@(\w+:\/\/([-\w\.]+)+(:\d+)?[^:]+):(\d+):?(\d+)?/;
			var frame = stack[i], m = reStack.exec(frame);
			if (m) {
				var file = m[1], lineno = m[4]; // m[7] is character position in
												// Chrome
				if (file && lineno) {
					var functionName = this.guessFunctionName(file, lineno);
					stack[i] = frame.replace('{anonymous}', functionName);
				}
			}
		}
		return stack;
	},

	guessFunctionName : function(url, lineNo) {
		try {
			return this.guessFunctionNameFromLines(lineNo, this.getSource(url));
		} catch (e) {
			return 'getSource failed with url: ' + url + ', exception: ' + e.toString();
		}
	},

	guessFunctionNameFromLines : function(lineNo, source) {
		var reFunctionArgNames = /function ([^(]*)\(([^)]*)\)/;
		var reGuessFunction = /['"]?([0-9A-Za-z_]+)['"]?\s*[:=]\s*(function|eval|new Function)/;
		// Walk backwards from the first line in the function until we find the
		// line which
		// matches the pattern above, which is the function definition
		var line = "", maxLines = 10;
		for ( var i = 0; i < maxLines; ++i) {
			line = source[lineNo - i] + line;
			if (line !== undefined) {
				var m = reGuessFunction.exec(line);
				if (m && m[1]) {
					return m[1];
				} else {
					m = reFunctionArgNames.exec(line);
					if (m && m[1]) {
						return m[1];
					}
				}
			}
		}
		return '(?)';
	}
};

//
// window.onerror = function(errorMsg, url, lineNumber) {
// if (window.console != undefined) {
// console.info("uncaught exception " + url + " :" + lineNumber);
// }
// return false;
// };

