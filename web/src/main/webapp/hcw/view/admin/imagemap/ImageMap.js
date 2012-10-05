define(['jquery', 'underscore', 'ajsl/event', 'text!./ImageMap.html', 'text!./ImageMapRow.html', 'js!./imgmap.js', 'js!./lang_en.js'], 
function($, _, event, ImageMapTpl, ImageMapRow) {

//this will reload areas and sets dropdown restrictions
//	self.myimgmap.setMapHTML(self.myimgmap.getMapHTML());

//	self.myimgmap.setMapHTML(elem.value);
//	self.myimgmap.removeArea
//	self.myimgmap.areas[obj.parentNode.aid].lastInput)
//	self.myimgmap._recalculate(obj.parentNode.aid, obj.value);
	function ImageMap(context) {
		var self = this;
		this.context = context;
		this.single = false;

		this.myimgmap;
		this.props = [];

		
		function gui_row_mouseover(e) {
			if (self.myimgmap.is_drawing) {return;}//exit if in drawing state
			if (self.myimgmap.viewmode === 1) {return;}//exit if preview mode
			var obj = (self.myimgmap.isMSIE) ? window.event.srcElement : e.currentTarget;
			if (typeof obj.aid == 'undefined') {obj = obj.parentNode;}
			//console.log(obj.aid);
			self.myimgmap.highlightArea(obj.aid);
		}

		function gui_row_mouseout(e) {
			if (self.myimgmap.is_drawing) {return;}//exit if in drawing state
			if (self.myimgmap.viewmode === 1) {return;}//exit if preview mode
			var obj = (self.myimgmap.isMSIE) ? window.event.srcElement : e.currentTarget;
			if (typeof obj.aid == 'undefined') {obj = obj.parentNode;}
			self.myimgmap.blurArea(obj.aid);
		}

		function gui_row_click(e) {
			if (self.myimgmap.viewmode === 1) {return;}//exit if preview mode
			var obj = (self.myimgmap.isMSIE) ? window.event.srcElement : e.currentTarget;
			if (typeof obj.aid == 'undefined') {obj = obj.parentNode;}
			gui_row_select(obj.aid);
			self.myimgmap.currentid = obj.aid;
		}

		function gui_row_select(id) {
			if (self.myimgmap.is_drawing) {return;}//exit if in drawing state
			if (self.myimgmap.viewmode === 1) {return;}//exit if preview mode
			if (!document.getElementById('img_active_'+id)) {return;}

			for (var i = 0; i < self.props.length; i++) {
				if (self.props[i]) {
					document.getElementById('img_active_'+i).checked = false;
				}
			}

			document.getElementById('img_active_'+id).checked = 1;
			//remove all background styles
			for (var i = 0; i < self.props.length; i++) {
				if (self.props[i]) {
					self.props[i].style.background = '';
				}
			}
			//put highlight on actual props row
			self.props[id].style.background = '#e7e7e7';
		}

		function gui_input_change(e) {
			if (self.myimgmap.viewmode === 1) {return;}//exit if preview mode
			if (self.myimgmap.is_drawing) {return;}//exit if drawing
			var obj = (self.myimgmap.isMSIE) ? window.event.srcElement : e.currentTarget;
			var id = obj.parentNode.aid;
			if (obj.name == 'img_href')        {self.myimgmap.areas[id].ahref   = obj.value;}
			else if (obj.name == 'img_alt')    {self.myimgmap.areas[id].aalt    = obj.value;}
			else if (obj.name == 'img_title')  {self.myimgmap.areas[id].atitle  = obj.value;}
			else if (obj.name == 'img_target') {self.myimgmap.areas[id].atarget = obj.value;}
			else if (obj.name == 'parentZoneCoordinatesShape') {
				if (self.myimgmap.areas[id].shape != obj.value && self.myimgmap.areas[id].shape != 'undefined') {
					//shape changed, adjust coords intelligently inside _normCoords
					var coords = '';
					if (self.props[id]) {
						coords  =  self.props[id].getElementsByTagName('input')[2].value;
					}
					else {
						coords = self.myimgmap.areas[id].lastInput || '' ;
					}
					coords = self.myimgmap._normCoords(coords, obj.value, 'from'+self.myimgmap.areas[id].shape);
					if (self.props[id]) {
						self.props[id].getElementsByTagName('input')[2].value  = coords;
					}
					self.myimgmap.areas[id].shape = obj.value;
					self.myimgmap._recalculate(id, coords);
					self.myimgmap.areas[id].lastInput = coords;
				}
				else if (self.myimgmap.areas[id].shape == 'undefined') {
					self.myimgmap.nextShape = obj.value;
				}
			}
			if (self.myimgmap.areas[id] && self.myimgmap.areas[id].shape != 'undefined') {
				self.myimgmap._recalculate(id, self.props[id].getElementsByTagName('input')[2].value);
				self.myimgmap.fireEvent('onHtmlChanged', self.myimgmap.getMapHTML());//temp ## shouldnt be here
			}
		}

		
		
		this.events = {
			'.imageMapZoom|change' : function() {
				var scale = $(this).val();
				var pic = $('#pic_container IMG', self.context)[0];
				if (typeof pic.oldwidth == 'undefined' || !pic.oldwidth) {
					pic.oldwidth = pic.width;
				}
				if (typeof pic.oldheight == 'undefined' || !pic.oldheight) {
					pic.oldheight = pic.height;
				}
				pic.width  = pic.oldwidth * scale;
				pic.height = pic.oldheight * scale;
				self.myimgmap.scaleAllAreas(scale);				
			},
			'.bounding|click' : function() {
				self.myimgmap.config.bounding_box = self.myimgmap.config.bounding_box ? false : true;
				self.myimgmap.relaxAllAreas();
			},
			'.labeling|change' : function() {
				self.myimgmap.config.label = $(this).val();
				self.myimgmap._repaintAll();
			},
			'.imgmapadd|click' : function() {
				self.myimgmap.addNewArea();
			},
			'.imgmapdel|click' : function() {
				 self.myimgmap.removeArea(self.myimgmap.currentid);
			}
		};
		
		this.imgMapCallbacks = {
				onStatusMessage : function(str) {
					$('#status_container', self.context).text(str);
				},
				onHtmlChanged : function(str) {
					document.getElementById('html_container').value = str;
				},
				onAddArea : function(id)  {
					if (self.single && id != 0) {
						return;
					}

//					self.props[id].id        = 'img_area_' + id;
//					self.props[id].aid       = id;
//					self.props[id].className = 'img_area';
//
//					$('#form_container', self.context).append(_.template(ImageMapRow, {id : id}));
//					
//					return;
					
					self.props[id] = document.createElement('DIV');
					document.getElementById('form_container').appendChild(self.props[id]);

					self.props[id].id        = 'img_area_' + id;
					self.props[id].aid       = id;
					self.props[id].className = 'img_area';
					//hook ROW event handlers
					self.myimgmap.addEvent(self.props[id], 'mouseover', gui_row_mouseover);
					self.myimgmap.addEvent(self.props[id], 'mouseout',  gui_row_mouseout);
					self.myimgmap.addEvent(self.props[id], 'click',     gui_row_click);
					var temp = '<input type="text" class="input-mini" value="' + id + '" readonly="1"/>';
					temp+= '<input type="radio" id="img_active_'+id+'" value="'+id+'">';
					temp+= '<select name="parentZoneCoordinatesShape" class="input-small">';
					temp+= '<option value="rect">rectangle</option>';
					temp+= '<option value="circle">circle</option>';
					temp+= '<option value="poly">polygon</option>';
					temp+= '<option value="bezier1">bezier</option>';
					temp+= '</select>';
					temp+= 'Coords: <input type="text" name="parentZoneCoordinates" class="parentZoneCoordinates" value="">';
					self.props[id].innerHTML = temp;
					//hook more event handlers to individual inputs
					
					self.myimgmap.addEvent(self.props[id].getElementsByTagName('input')[2],  'change', gui_input_change);
					self.myimgmap.addEvent(self.props[id].getElementsByTagName('select')[0], 'change', gui_input_change);
					if (self.myimgmap.isSafari) {
						//need these for safari
						self.myimgmap.addEvent(self.props[id].getElementsByTagName('select')[0], 'change', gui_row_click);
					}
					//set shape as nextshape if set
					if (self.myimgmap.nextShape) {self.props[id].getElementsByTagName('select')[0].value = self.myimgmap.nextShape;}
					gui_row_select(id);					
				},
				onRemoveArea : function(id)  {
					var pprops = self.props[id].parentNode;
					var lastid = pprops.lastChild.aid;
					pprops.removeChild(self.props[id]);
					self.props[id] = null;
					gui_row_select(lastid);
					self.myimgmap.currentid = lastid;
				},
				onAreaChanged : function(area) {
					var id = area.aid;
					if (area.shape) {
						self.props[id].getElementsByTagName('select')[0].value = area.shape;
					}
					if (area.lastInput) {
						self.props[id].getElementsByTagName('input')[2].value  = area.lastInput;
					}
				},
				onSelectArea : function(obj) {
					gui_row_select(obj.aid);
				}
		};

		this.changeImage = function(url) {
			self.myimgmap.loadImage(url);
		};
		this.display = function() {
			this.context.append(ImageMapTpl);
			event.register(this.events, this.context);

			self.myimgmap = new imgmap({
				mode : "editor",
				custom_callbacks : this.imgMapCallbacks,
				pic_container: $('#pic_container', this.context)[0],
				bounding_box : false
			});
			self.myimgmap.strings = imgmapStrings;

			self.myimgmap.loadImage('http://localhost:8080/hcw/hcw/view/admin/imagemap/example1_files/sample1.jpg');				
		};

	}
	
	return ImageMap;
});