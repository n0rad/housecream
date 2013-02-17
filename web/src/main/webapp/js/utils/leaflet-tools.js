
(function() {
    var LeafMap;
    LeafMap = (function() {
        function LeafMap(options) {
            if(options == null) options = {};

            var _this = this;

            this.image = new Image();
            this.image.src = options.src;
            this.image.onload = function() {

                _this.imageData = {width: this.naturalWidth, height: this.naturalHeight}

                _this.map = L.map(options.id, {
                    maxBounds: [[0,0], [_this.imageData.width, _this.imageData.height]],
                    maxZoom: 26,
                    center: [_this.imageData.width/2, _this.imageData.height/2]
                }).setView([0, 0], 13);

                var canvasTiles = L.tileLayer.canvas();
                canvasTiles.drawTile = function(canvas, tilePoint, zoom) {
                    var ctx = canvas.getContext('2d');
                    ctx.drawImage(_this.image, 0, 0);
                    console.log(tilePoint);
                }
                canvasTiles.addTo(_this.map);
                _this.map.addEventListener("zoomstart", function(e) {
                    var currentZoom = e.target._zoom;
                    this.setMaxBounds([[0,0], [_this.imageData.width* currentZoom, _this.imageData.height* currentZoom]]);
                }, _this.map);
            };





//            L.tileLayer('http://{s}.tile.cloudmade.com/546dbb14a309452e8638390e7508c71b/997/256/{z}/{x}/{y}.png', {
//                attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://cloudmade.com">CloudMade</a>',
//                maxZoom: 18
//            }).addTo(this.map);
        }

        LeafMap.prototype.updateLayer = function(layerName) {

        };
        return LeafMap;
    })();

    window.LeafMap = LeafMap;
}).call(this);