define(['jquery', 'underscore', 'ajsl/event', 'text!./ImageMap.html', 'text!./ImageMapRow.html', 'js!./imgmap.js', 'js!./lang_en.js'], 
function($, _, event, ImageMapTpl, ImageMapRow) {
	
	var myimgmap, props;

	function gui_row_mouseover(e) {
		if (myimgmap.is_drawing) {return;}//exit if in drawing state
		if (myimgmap.viewmode === 1) {return;}//exit if preview mode
		var obj = (myimgmap.isMSIE) ? window.event.srcElement : e.currentTarget;
		if (typeof obj.aid == 'undefined') {obj = obj.parentNode;}
		//console.log(obj.aid);
		myimgmap.highlightArea(obj.aid);
	}

	function gui_row_mouseout(e) {
		if (myimgmap.is_drawing) {return;}//exit if in drawing state
		if (myimgmap.viewmode === 1) {return;}//exit if preview mode
		var obj = (myimgmap.isMSIE) ? window.event.srcElement : e.currentTarget;
		if (typeof obj.aid == 'undefined') {obj = obj.parentNode;}
		myimgmap.blurArea(obj.aid);
	}

	function gui_row_click(e) {
		if (myimgmap.viewmode === 1) {return;}//exit if preview mode
		var obj = (myimgmap.isMSIE) ? window.event.srcElement : e.currentTarget;
		if (typeof obj.aid == 'undefined') {obj = obj.parentNode;}
		gui_row_select(obj.aid, false, false);
		myimgmap.currentid = obj.aid;
	}

	function gui_row_select(id, setfocus, multiple) {
		if (myimgmap.is_drawing) {return;}//exit if in drawing state
		if (myimgmap.viewmode === 1) {return;}//exit if preview mode
		if (!document.getElementById('img_active_'+id)) {return;}

		for (var i = 0; i < props.length; i++) {
			if (props[i]) {
				document.getElementById('img_active_'+i).checked = false;
			}
		}

		document.getElementById('img_active_'+id).checked = 1;
		if (setfocus) {
			document.getElementById('img_active_'+id).focus();
		}
		//remove all background styles
		for (var i = 0; i < props.length; i++) {
			if (props[i]) {
				props[i].style.background = '';
			}
		}
		//put highlight on actual props row
		props[id].style.background = '#e7e7e7';
	}

	function gui_input_change(e) {
		if (myimgmap.viewmode === 1) {return;}//exit if preview mode
		if (myimgmap.is_drawing) {return;}//exit if drawing
		//console.log('blur');
		var obj = (myimgmap.isMSIE) ? window.event.srcElement : e.currentTarget;
		//console.log(obj);
		var id = obj.parentNode.aid;
		//console.log(this.areas[id]);
		if (obj.name == 'img_href')        {myimgmap.areas[id].ahref   = obj.value;}
		else if (obj.name == 'img_alt')    {myimgmap.areas[id].aalt    = obj.value;}
		else if (obj.name == 'img_title')  {myimgmap.areas[id].atitle  = obj.value;}
		else if (obj.name == 'img_target') {myimgmap.areas[id].atarget = obj.value;}
		else if (obj.name == 'img_shape') {
			if (myimgmap.areas[id].shape != obj.value && myimgmap.areas[id].shape != 'undefined') {
				//shape changed, adjust coords intelligently inside _normCoords
				var coords = '';
				if (props[id]) {
					coords  =  props[id].getElementsByTagName('input')[2].value;
				}
				else {
					coords = myimgmap.areas[id].lastInput || '' ;
				}
				coords = myimgmap._normCoords(coords, obj.value, 'from'+myimgmap.areas[id].shape);
				if (props[id]) {
					props[id].getElementsByTagName('input')[2].value  = coords;
				}
				myimgmap.areas[id].shape = obj.value;
				myimgmap._recalculate(id, coords);
				myimgmap.areas[id].lastInput = coords;
			}
			else if (myimgmap.areas[id].shape == 'undefined') {
				myimgmap.nextShape = obj.value;
			}
		}
		if (myimgmap.areas[id] && myimgmap.areas[id].shape != 'undefined') {
			myimgmap._recalculate(id, props[id].getElementsByTagName('input')[2].value);
			myimgmap.fireEvent('onHtmlChanged', myimgmap.getMapHTML());//temp ## shouldnt be here
		}
	}

	function gui_areaChanged(area) {
		var id = area.aid;
		if (props[id]) {
			if (area.shape)  {props[id].getElementsByTagName('select')[0].value = area.shape;}
			if (area.lastInput) {props[id].getElementsByTagName('input')[2].value  = area.lastInput;}
			if (area.ahref)   {props[id].getElementsByTagName('input')[3].value  = area.ahref;}
			if (area.aalt)    {props[id].getElementsByTagName('input')[4].value  = area.aalt;}
			if (area.atarget) {props[id].getElementsByTagName('select')[1].value = area.atarget;}
		}
	}

	function gui_outputChanged() {
		var temp, i;
		var output = document.getElementById('dd_output').value;
		temp = 'This is the generated image map HTML code. ';
		document.getElementById('output_help').innerHTML = temp;
		//this will reload areas and sets dropdown restrictions
		myimgmap.setMapHTML(myimgmap.getMapHTML());
//		outputmode = output;
		return true;
	}
	
	
	function ImageMap(context) {
		var self = this;
		this.context = context;
		
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
				myimgmap.scaleAllAreas(scale);				
			},
			'.bounding|click' : function() {
				myimgmap.config.bounding_box = myimgmap.config.bounding_box ? false : true;
				myimgmap.relaxAllAreas();
			},
			'.labeling|change' : function() {
				myimgmap.config.label = $(this).val();
				myimgmap._repaintAll();
			},
			'.imgmapadd|click' : function() {
				myimgmap.addNewArea();
			},
			'.imgmapdel|click' : function() {
				 myimgmap.removeArea(myimgmap.currentid);
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
//					myimgmap.setMapHTML(elem.value);
//					myimgmap.removeArea
//					myimgmap.areas[obj.parentNode.aid].lastInput)
//					myimgmap._recalculate(obj.parentNode.aid, obj.value);

//					props[id].id        = 'img_area_' + id;
//					props[id].aid       = id;
//					props[id].className = 'img_area';
//
//					$('#form_container', self.context).append(_.template(ImageMapRow, {id : id}));
//					
//					return;
					
					//var id = props.length;
					//id = 1;
					props[id] = document.createElement('DIV');
					document.getElementById('form_container').appendChild(props[id]);

					props[id].id        = 'img_area_' + id;
					props[id].aid       = id;
					props[id].className = 'img_area';
					//hook ROW event handlers
					myimgmap.addEvent(props[id], 'mouseover', gui_row_mouseover);
					myimgmap.addEvent(props[id], 'mouseout',  gui_row_mouseout);
					myimgmap.addEvent(props[id], 'click',     gui_row_click);
					var temp = '<input type="text"  name="img_id" class="img_id input-mini" value="' + id + '" readonly="1"/>';
					//temp+= '<input type="checkbox" name="img_active" class="img_active" id="img_active_'+id+'" value="'+id+'">';
					//could be checkbox in the future
					temp+= '<input type="radio" name="img_active" class="img_active" id="img_active_'+id+'" value="'+id+'">';
					temp+= '<select name="img_shape" class="img_shape input-small">';
					temp+= '<option value="rect">rectangle</option>';
					temp+= '<option value="circle">circle</option>';
					temp+= '<option value="poly">polygon</option>';
					temp+= '<option value="bezier1">bezier</option>';
					temp+= '</select>';
					temp+= 'Coords: <input type="text" name="img_coords" class="img_coords" value="">';
					props[id].innerHTML = temp;
					//hook more event handlers to individual inputs
					
					myimgmap.addEvent(props[id].getElementsByTagName('input')[2],  'change', gui_input_change);
//					myimgmap.addEvent(props[id].getElementsByTagName('input')[3],  'change', gui_input_change);
//					myimgmap.addEvent(props[id].getElementsByTagName('input')[4],  'change', gui_input_change);
					myimgmap.addEvent(props[id].getElementsByTagName('select')[0], 'change', gui_input_change);
					if (myimgmap.isSafari) {
						//need these for safari
						myimgmap.addEvent(props[id].getElementsByTagName('select')[0], 'change', gui_row_click);
					}
					//set shape as nextshape if set
					if (myimgmap.nextShape) {props[id].getElementsByTagName('select')[0].value = myimgmap.nextShape;}
					gui_row_select(id, true);					
				},
				onRemoveArea : function(id)  {
					var pprops = props[id].parentNode;
					pprops.removeChild(props[id]);
					var lastid = pprops.lastChild.aid;
					props[id] = null;
					gui_row_select(lastid, true);
					myimgmap.currentid = lastid;
				},
				onAreaChanged : function(obj) {
//					gui_areaChanged(obj);
				},
				onSelectArea : function(obj) {
					gui_row_select(obj.aid, true, false);
				}
		};
		
	}

	ImageMap.prototype = {
			changeImage : function(url) {
				myimgmap.loadImage(url);
			},
			display : function() {
				this.context.append(ImageMapTpl);
				event.register(this.events, this.context);

				myimgmap = new imgmap({
					mode : "editor",
					custom_callbacks : {
						'onStatusMessage' : this.imgMapCallbacks.onStatusMessage,
						'onHtmlChanged'   : this.imgMapCallbacks.onHtmlChanged,
//						'onModeChanged'   : this.imgMapCallbacks.onModeChanged,
						'onAddArea'       : this.imgMapCallbacks.onAddArea,
						'onRemoveArea'    : this.imgMapCallbacks.onRemoveArea,
						'onAreaChanged'   : function(obj) {gui_areaChanged(obj);},
						'onSelectArea'    : this.imgMapCallbacks.onSelectArea
					},
					pic_container: document.getElementById('pic_container'),
					bounding_box : false
				});
				myimgmap.strings = imgmapStrings;

				props = [];
//				outputmode = 'imgmap';
//				gui_outputChanged();

				myimgmap.loadImage('http://localhost:8080/hcw/hcw/view/admin/imagemap/example1_files/sample1.jpg');
				
			}
	};
	
	return ImageMap;
});