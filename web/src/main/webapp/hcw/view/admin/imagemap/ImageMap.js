define(['jquery', 'underscore', 'ajsl/event', 'text!./ImageMap.html', 'text!./ImageMapRow.html', 'js!./imgmap.js', 'js!./lang_en.js'], 
function($, _, event, ImageMapTpl, ImageMapRow) {
	
	

	/** GLOBALS SECTION ***********************************************************/

	var myimgmap, props, outputmode;

	/** FUNCTION SECTION **********************************************************/


	/**
	 *	Handles mouseover on props row.
	 */
	function gui_row_mouseover(e) {
		if (myimgmap.is_drawing) {return;}//exit if in drawing state
		if (myimgmap.viewmode === 1) {return;}//exit if preview mode
		var obj = (myimgmap.isMSIE) ? window.event.srcElement : e.currentTarget;
		if (typeof obj.aid == 'undefined') {obj = obj.parentNode;}
		//console.log(obj.aid);
		myimgmap.highlightArea(obj.aid);
	}

	/**
	 *	Handles mouseout on props row.
	 */
	function gui_row_mouseout(e) {
		if (myimgmap.is_drawing) {return;}//exit if in drawing state
		if (myimgmap.viewmode === 1) {return;}//exit if preview mode
		var obj = (myimgmap.isMSIE) ? window.event.srcElement : e.currentTarget;
		if (typeof obj.aid == 'undefined') {obj = obj.parentNode;}
		myimgmap.blurArea(obj.aid);
	}

	/**
	 *	Handles click on props row.
	 */
	function gui_row_click(e) {
		if (myimgmap.viewmode === 1) {return;}//exit if preview mode
		var obj = (myimgmap.isMSIE) ? window.event.srcElement : e.currentTarget;
		//var multiple = (e.originalTarget.name == 'img_active');
		//myimgmap.log(e.originalTarget);
		if (typeof obj.aid == 'undefined') {obj = obj.parentNode;}
		//gui_row_select(obj.aid, false, multiple);
		gui_row_select(obj.aid, false, false);
		myimgmap.currentid = obj.aid;
	}

	/**
	 *	Handles click on a property row.
	 *	@author	Adam Maschek (adam.maschek(at)gmail.com)
	 *	@date	2006-06-06 16:55:29
	 */
	function gui_row_select(id, setfocus, multiple) {
		if (myimgmap.is_drawing) {return;}//exit if in drawing state
		if (myimgmap.viewmode === 1) {return;}//exit if preview mode
		if (!document.getElementById('img_active_'+id)) {return;}
		//if (!multiple) 
		gui_cb_unselect_all();
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

//	/**
//	 *	Handles delete keypress when focus is on the leading checkbox/radio.
//	 *	@author	adam 
//	 */
//	function gui_cb_keydown(e) {
//		if (myimgmap.viewmode === 1) {return;}//exit if preview mode
//		var key = (myimgmap.isMSIE) ? event.keyCode : e.keyCode;
//		//alert(key);
//		if (key == 46) {
//			//delete pressed
//			myimgmap.removeArea(myimgmap.currentid);
//		}
//	}

	/**
	 *	Unchecks all checboxes/radios.
	 */
	function gui_cb_unselect_all() {
		for (var i = 0; i < props.length; i++) {
			if (props[i]) {
				document.getElementById('img_active_'+i).checked = false;
			}
		}
	}

	/**
	 *	Handles arrow keys on img_coords input field.
	 *	Changes the coordinate values by +/- 1 and updates the corresponding canvas area.
	 *	@author	adam
	 *	@date	25-09-2007 17:12:43
	 */
	function gui_coords_keydown(e) {
		if (myimgmap.viewmode === 1) {return;}//exit if preview mode
		var key = (myimgmap.isMSIE || myimgmap.isOpera) ? event.keyCode : e.keyCode;
		var obj = (myimgmap.isMSIE || myimgmap.isOpera) ? window.event.srcElement : e.originalTarget;
		//obj is the input field
		//this.log(key);
		//this.log(obj);
		if (key == 40 || key == 38) {
			//down or up pressed
			//get the coords
			var coords = obj.value.split(',');
			var s = getSelectionStart(obj);//helper function
			var j = 0;
			for (var i=0, le = coords.length; i<le; i++) {
				j+=coords[i].length;
				if (j > s) {
					//this is the coord we want
					if (key == 40 && coords[i] > 0) {coords[i]--;}
					if (key == 38) {coords[i]++;}
					break;
				}
				//jump one more because of comma
				j++;
			}
			obj.value = coords.join(',');
			if (obj.value != myimgmap.areas[obj.parentNode.aid].lastInput) {
				myimgmap._recalculate(obj.parentNode.aid, obj.value);//contains repaint
			}
			//set cursor back to its original position
			setSelectionRange(obj, s);
			return true;
		}
	}

	/**
	 *	Gets the position of the cursor in the input box.
	 *	@author	Diego Perlini
	 *	@url	http://javascript.nwbox.com/cursor_position/
	 */
	function getSelectionStart(obj) {
		if (obj.createTextRange) {
			var r = document.selection.createRange().duplicate();
			r.moveEnd('character', obj.value.length);
			if (r.text === '') {return obj.value.length;}
			return obj.value.lastIndexOf(r.text);
		}
		else {
			return obj.selectionStart;
		}
	}

	/**
	 *	Sets the position of the cursor in the input box.
	 *	@link	http://www.codingforums.com/archive/index.php/t-90176.html
	 */
	function setSelectionRange(obj, start, end) {
		if (typeof end == "undefined") {end = start;}
		if (obj.setSelectionRange) {
			obj.focus(); // to make behaviour consistent with IE
			obj.setSelectionRange(start, end);
		}
		else if (obj.createTextRange) {
			var range = obj.createTextRange();
			range.collapse(true);
			range.moveEnd('character', end);
			range.moveStart('character', start);
			range.select();
		}
	}


	/**
	 *	Called when one of the properties change, and the recalculate function
	 *	must be called.
	 */
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

//	/**
//	 *	Called from imgmap when a new area is added.
//	 */
//	function gui_addArea(id) {
//		//var id = props.length;
//		//id = 1;
//		props[id] = document.createElement('DIV');
//		document.getElementById('form_container').appendChild(props[id]);
//
//		props[id].id        = 'img_area_' + id;
//		props[id].aid       = id;
//		props[id].className = 'img_area';
//		//hook ROW event handlers
//		myimgmap.addEvent(props[id], 'mouseover', gui_row_mouseover);
//		myimgmap.addEvent(props[id], 'mouseout',  gui_row_mouseout);
//		myimgmap.addEvent(props[id], 'click',     gui_row_click);
//		var temp = '<input type="text"  name="img_id" class="img_id" value="' + id + '" readonly="1"/>';
//		//temp+= '<input type="checkbox" name="img_active" class="img_active" id="img_active_'+id+'" value="'+id+'">';
//		//could be checkbox in the future
//		temp+= '<input type="radio" name="img_active" class="img_active" id="img_active_'+id+'" value="'+id+'">';
//		temp+= '<select name="img_shape" class="img_shape">';
//		temp+= '<option value="rect">rectangle</option>';
//		if (document.getElementById('dd_output').value != 'css') {
//			temp+= '<option value="circle">circle</option>';
//			temp+= '<option value="poly">polygon</option>';
//			temp+= '<option value="bezier1">bezier</option>';
//		}
//		temp+= '</select>';
//		temp+= 'Coords: <input type="text" name="img_coords" class="img_coords" value="">';
////		temp+= 'Href: <input type="text" name="img_href" class="img_href" value="">';
////		temp+= 'Alt: <input type="text" name="img_alt" class="img_alt" value="">';
////		temp+= 'Target: <select name="img_target" class="img_target">';
//		temp+= '<option value=""  >&lt;not set&gt;</option>';
//		temp+= '<option value="_self"  >this window</option>';
//		temp+= '<option value="_blank" >new window</option>';
//		temp+= '<option value="_top"   >top window</option>';
//		temp+= '</select>';
//		props[id].innerHTML = temp;
//		//hook more event handlers to individual inputs
//		
//		myimgmap.addEvent(props[id].getElementsByTagName('input')[1],  'keydown', gui_cb_keydown);
//		myimgmap.addEvent(props[id].getElementsByTagName('input')[2],  'keydown', gui_coords_keydown);
//		myimgmap.addEvent(props[id].getElementsByTagName('input')[2],  'change', gui_input_change);
////		myimgmap.addEvent(props[id].getElementsByTagName('input')[3],  'change', gui_input_change);
////		myimgmap.addEvent(props[id].getElementsByTagName('input')[4],  'change', gui_input_change);
//		myimgmap.addEvent(props[id].getElementsByTagName('select')[0], 'change', gui_input_change);
////		myimgmap.addEvent(props[id].getElementsByTagName('select')[1], 'change', gui_input_change);
//		if (myimgmap.isSafari) {
//			//need these for safari
//			myimgmap.addEvent(props[id].getElementsByTagName('select')[0], 'change', gui_row_click);
////			myimgmap.addEvent(props[id].getElementsByTagName('select')[1], 'change', gui_row_click);
//		}
//		//set shape as nextshape if set
//		if (myimgmap.nextShape) {props[id].getElementsByTagName('select')[0].value = myimgmap.nextShape;}
//		//alert(this.props[id].parentNode.innerHTML);
//		gui_row_select(id, true);
//	}

	/**
	 *	Called from imgmap when an area was removed.
	 */
	function gui_removeArea(id) {
		if (props[id]) {
			//shall we leave the last one?
			var pprops = props[id].parentNode;
			pprops.removeChild(props[id]);
			var lastid = pprops.lastChild.aid;
			props[id] = null;
			try {
				gui_row_select(lastid, true);
				myimgmap.currentid = lastid;
			}
			catch (err) {
				//alert('noparent');
			}
		}
	}

	/**
	 *	Called from imgmap with the new html code when changed.
	 */
	function gui_htmlChanged(str) {
		var out = document.getElementById('dd_output').value;
		if (document.getElementById('html_container')) {
			document.getElementById('html_container').value = str;
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


	/**
	 *	Called when the grand HTML code loses focus, and the changes must be reflected.
	 */
	function gui_htmlBlur() {
		var elem = document.getElementById('html_container');
		var oldvalue = elem.getAttribute('oldvalue');
		if (oldvalue != elem.value && document.getElementById('dd_output').value == 'imagemap') {
			//dirty
			myimgmap.setMapHTML(elem.value);
		}
	}


	/**
	 *	Called when the optional html container gets focus.
	 *	We need to memorize its old value in order to be able to
	 *	detect changes in the code that needs to be reflected.
	 */
	function gui_htmlFocus() {
		var elem = document.getElementById('html_container');
		elem.setAttribute('oldvalue', elem.value);
		elem.select();
	}

	function gui_htmlShow() {
		toggleFieldset(document.getElementById('fieldset_html'), 1);
		document.getElementById('html_container').focus();
	}

//	/**
//	 *	Change the labeling mode directly in imgmap config then repaint all areas.
//	 */
//	function changelabeling(obj) {
//		myimgmap.config.label = obj.value;
//		myimgmap._repaintAll();
//	}

//	function gui_selectArea(obj) {
//		gui_row_select(obj.aid, true, false);
//	}

//	function gui_loadImage(src) {
//		//reset zoom dropdown
//		document.getElementById('dd_zoom').value = '1';
//		var pic = document.getElementById('pic_container').getElementsByTagName('img')[0];
//		if (typeof pic != 'undefined') {
//			//delete already existing pic
//			pic.parentNode.removeChild(pic);
//			delete myimgmap.pic;
//		}
//		myimgmap.loadImage(src);
//	}

	function gui_outputChanged() {
		var temp, i;
		var output = document.getElementById('dd_output').value;
		temp = 'This is the generated image map HTML code. ';
		document.getElementById('output_help').innerHTML = temp;
		//this will reload areas and sets dropdown restrictions
		myimgmap.setMapHTML(myimgmap.getMapHTML());
		outputmode = output;
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
//					gui_htmlChanged(str);
				},
				onAddArea : function(id)  {
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
//					temp+= 'Href: <input type="text" name="img_href" class="img_href" value="">';
//					temp+= 'Alt: <input type="text" name="img_alt" class="img_alt" value="">';
//					temp+= 'Target: <select name="img_target" class="img_target">';
//					temp+= '<option value=""  >&lt;not set&gt;</option>';
//					temp+= '<option value="_self"  >this window</option>';
//					temp+= '<option value="_blank" >new window</option>';
//					temp+= '<option value="_top"   >top window</option>';
//					temp+= '</select>';
					props[id].innerHTML = temp;
					//hook more event handlers to individual inputs
					
//					myimgmap.addEvent(props[id].getElementsByTagName('input')[1],  'keydown', gui_cb_keydown);
//					myimgmap.addEvent(props[id].getElementsByTagName('input')[2],  'keydown', gui_coords_keydown);
					myimgmap.addEvent(props[id].getElementsByTagName('input')[2],  'change', gui_input_change);
//					myimgmap.addEvent(props[id].getElementsByTagName('input')[3],  'change', gui_input_change);
//					myimgmap.addEvent(props[id].getElementsByTagName('input')[4],  'change', gui_input_change);
					myimgmap.addEvent(props[id].getElementsByTagName('select')[0], 'change', gui_input_change);
//					myimgmap.addEvent(props[id].getElementsByTagName('select')[1], 'change', gui_input_change);
					if (myimgmap.isSafari) {
						//need these for safari
						myimgmap.addEvent(props[id].getElementsByTagName('select')[0], 'change', gui_row_click);
//						myimgmap.addEvent(props[id].getElementsByTagName('select')[1], 'change', gui_row_click);
					}
					//set shape as nextshape if set
					if (myimgmap.nextShape) {props[id].getElementsByTagName('select')[0].value = myimgmap.nextShape;}
					//alert(this.props[id].parentNode.innerHTML);
					gui_row_select(id, true);					
				},
				onRemoveArea : function(id)  {
//					gui_removeArea(id);
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
						'onHtmlChanged'   : function(str) {gui_htmlChanged(str);},
//						'onModeChanged'   : this.imgMapCallbacks.onModeChanged,
						'onAddArea'       : this.imgMapCallbacks.onAddArea,
						'onRemoveArea'    : function(id)  {gui_removeArea(id);},
						'onAreaChanged'   : function(obj) {gui_areaChanged(obj);},
						'onSelectArea'    : this.imgMapCallbacks.onSelectArea
					},
					pic_container: document.getElementById('pic_container'),
					bounding_box : false
				});
				myimgmap.strings = imgmapStrings;

				props = [];
				outputmode = 'imgmap';
//				gui_outputChanged();

//				myimgmap.addEvent(document.getElementById('html_container'), 'blur',  gui_htmlBlur);
//				myimgmap.addEvent(document.getElementById('html_container'), 'focus', gui_htmlFocus);

				myimgmap.loadImage('http://localhost:8080/hcw/hcw/view/admin/imagemap/example1_files/sample1.jpg');
				
			}
	};
	
	return ImageMap;
});