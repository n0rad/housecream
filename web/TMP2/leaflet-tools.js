'use strict';

(function() {
  var LeafMap;
  LeafMap = (function() {
    function LeafMap(options) {
      if (options === null) {
        options = {};
      }
      if (options.zoom === null) {
        options.zoom = 1;
      }
      var _this = this;
      this.image = new Image();
      this.image.src = options.src;
      this.image.onload = function() {

        _this.imageData = {
          width : this.naturalWidth,
          height : this.naturalHeight
        };
        var tileSize = _this.imageData.width / 2;
        _this.map = L.map(options.id, {
          maxBounds : [ [ 0, 0 ], [ _this.imageData.width, _this.imageData.height ] ],
          zoom : options.zoom,
          center : [ 0, 0 ]
        });

        var canvasTiles = L.tileLayer.canvas({
          tileSize : tileSize
        });
        canvasTiles.drawTile = function(canvas, tilePoint, zoom) {
          var ctx = canvas.getContext('2d');
          var virtualTileSize = (tileSize / zoom);
          var sx = tilePoint.x * virtualTileSize;
          var sy = tilePoint.y * virtualTileSize;
          ctx.drawImage(_this.image, sx, sy, virtualTileSize, virtualTileSize, 0, 0, tileSize, tileSize);
          console.log(tilePoint);
        };
        canvasTiles.addTo(_this.map);
        _this.map.addEventListener('zoomend',
            function(e) {
              var currentZoom = e.target._zoom;
              this.setMaxBounds([ [ 0, 0 ],
                  [ _this.imageData.width * currentZoom, _this.imageData.height * currentZoom ] ]);
            }, _this.map);
      };

      // L.tileLayer('http://{s}.tile.cloudmade.com/546dbb14a309452e8638390e7508c71b/997/256/{z}/{x}/{y}.png',
      // {
      // attribution: 'Map data &copy; <a
      // href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a
      // href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>,
      // Imagery © <a href="http://cloudmade.com">CloudMade</a>',
      // maxZoom: 18
      // }).addTo(this.map);
    }

    return LeafMap;
  })();

  window.LeafMap = LeafMap;
}).call(this);