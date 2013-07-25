$(document).ready(function(){

/* Show jQuery is running */
$('h1').css({textDecoration: 'underline'});

$('#map').zoommap({
		// Width and Height of the Map
		width: '500px',
		height: '580px',
			
		//Misc Settings
		blankImage: 'images/blank.gif',
		zoomDuration: 100,
		bulletWidthOffset: '10px',
		bulletHeightOffset: '10px',
		
		//ids and classes
		zoomClass: 'zoomable',
		popupSelector: 'div.popup',
		popupCloseSelector: 'a.close',
		
		//Return to Parent Map Link
		showReturnLink: true,
		returnId: 'returnlink',
		returnText: 'return to campus map',
		
		//Initial Region to be shown
		map: {
			id: 'campus',
			image: 'images/campus.jpg',
			data: 'popups/campus.html',
			maps: [
			{
				id: 'quads',
				parent: 'campus',
				image: 'images/quads.png',
				data: 'popups/quads.html',
				width: '200px',
				height: '232px',
				top: '18px',
				left: '176px'
				/* More maps can be nested
				maps : [ ]
				*/
			}
			]
		}
	});


});
