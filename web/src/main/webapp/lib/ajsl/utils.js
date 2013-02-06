if (typeof String.prototype.ucFirst !== 'function') {
	String.prototype.ucFirst = function() {
		return this.charAt(0).toUpperCase() + this.slice(1);
	};
}

if (typeof String.prototype.lcFirst !== 'function') {
	String.prototype.lcFirst = function() {
		return this.charAt(0).toLowerCase() + this.slice(1);
	};
}

if (typeof String.prototype.startsWith !== 'function') {
	String.prototype.startsWith = function(str) {
		return (this.match("^" + str) == str);
	};
}

if (typeof String.prototype.trim !== 'function') {
	String.prototype.trim = function() {
		return this.replace(/^\s+|\s+$/g, '');
	};
}