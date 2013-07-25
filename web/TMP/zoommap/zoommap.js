/*
* Copyright (C) 2009 Joel Sutherland.
* Liscenced under the MIT liscense
* TODO:
* 1. Create API
* 2. Address accesibility automatically
* 3. Make object oriented
*/
(function($) {
	$.fn.zoommap = function(settings) {
		settings = $.extend({
			zoomDuration: 1000,
			zoomClass: 'zoomable',
			popupSelector: 'div.popup',
			popupCloseSelector: 'a.close',
			bulletWidthOffset: '10px',
			bulletHeightOffset: '10px',
			showReturnLink: true,
			returnId: 'returnlink',
			returnText: 'Return to Previous Map'
		}, settings);
		
		$(this).each(function(){
			var map = $(this);
			$(this).data('currentId', '');
			
			function showMapById(id){
				var region = findRegion(settings.map, id);
				if(region != -1){
					displayMap(region);
				}
			}

			// recursive id find
			function findRegion(root, id){
				if(root.id == id){
					return root;
				}else{
					if(root.maps != undefined){
						for(var i=0; i<root.maps.length; i++){
							var possible = findRegion(root.maps[i], id);
							if(possible != -1)
								return possible;
						}
					}
				}
				return -1;
			}
			
			// region is a map
			// This gets called every time we zoom
			function displayMap(region){
				//Set Current Region Id
				$(this).data('currentId', region.id);
				
				//Clear the Map and Set the Background Image
				map.empty().css({
					backgroundImage: 'url(' + region.image + ')',
					width: settings.width,
					height: settings.height
				});
				var check = map.css('background-image');
				
				//Load RegionData
				loadRegionData(region);
			}
			/************************************************************************************/

			
			//Show Return Link
			function showReturnLink(region){
				map.append('<a href="javascript:void(0);" id="' + settings.returnId + '">' + settings.returnText + '</a>');
				$('#' + settings.returnId).hide().fadeIn().click(function(){
					showMapById(region.parent);
				});
			}
				
			
			//Load the Bullets 
			function loadRegionData(region){
				var url = region.data;
				map.load(url, {}, function(){
					//place bullets
					$(this).children('a.bullet').each(function(){
						var coords = $(this).attr('rel').split('-');
						$(this).css({left: addpx(Number(coords[0]) - rempx(settings.bulletWidthOffset)), top: addpx(Number(coords[1]) - rempx(settings.bulletHeightOffset))})
							   .hide()
							   .click(function(){showPopup($(this).attr('id'));})
							   .fadeIn('fast');							
					});
					//Set up each submap as an item to click
					if(region.maps != undefined){
						for(var i=0; i<region.maps.length; i++){
							addZoom(region.maps[i]);
						}
					}
					//Create Return Link
					if(settings.showReturnLink && region.parent != undefined){
						showReturnLink(region);
					}						
				});
			}
			
			function showPopup(id, leftbul, topbul){
				map.find(settings.popupSelector).fadeOut(); 
				var boxid = '#' + id + '-box';
				
				$(boxid).fadeIn();
				$(settings.popupCloseSelector).click(function(){
					$(this).parent().fadeOut();
				});
			}
			
			//add a clickable image for a region on the current map
			function addZoom(region){
				$('<img />').addClass(settings.zoomClass)
					.attr({
						src: settings.blankImage,
						id: region.id
					}).css({
						position: 'absolute',
						width: region.width,
						height: region.height,
						top: region.top,
						left: region.left,
						cursor: 'pointer'
					}).appendTo(map).click(function(){
						//hide neighboring bullets and zoomables
						var width = settings.width;
						var height = settings.height;
						if(region.scan){
							width = region.scanwidth;
							height = region.scanheight;
						}
						$(this).siblings().fadeOut();
						$(this).hide()
							   .attr('src', region.image).load(function(){
									$(this).fadeIn('fast')
										   .animate({
												width: width,
												height: height,
												top: '0px',
												left: '0px'
											}, settings.zoomDuration, '', function(){
												displayMap(region);
											});
								});
					});
			}
			
			function rempx(string){
				return Number(string.substring(0, (string.length - 2)));
			}
			
			function addpx(string){
				return string + 'px';
			}
			
			function showHash(string){
				string = string.replace('#', '');
				showMapById(string);
			}
			
			//initialize map
			var hash = self.document.location.hash;
			if(hash.length > 0)
				showHash(hash);
			else{
				displayMap(settings.map);
			}
			
			return this;
		});
	}
})(jQuery);
