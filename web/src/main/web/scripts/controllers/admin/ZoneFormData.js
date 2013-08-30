					var data = new FormData();
					jQuery.each($('#fileInput', this)[0].files, function(i, file) {
					    data.append('file-'+i, file);
					});
					
					var self = this; 
					
					progressHandlerFunction = function(e) {
						var loaded = Math.round((e.loaded / e.total) * 100); // on calcul le pourcentage de progression
						var $bar = $('.progress .bar', self);
						
//						if ($bar.width()==400) {
//					        clearInterval(progress);
//					        $('.progress').removeClass('active');
//					    } else {
//					        $bar.width($bar.width()+40);
//					    }
						$bar.width(loaded + '%');
					    $bar.text(loaded + "%");
					};
					
					$.ajax({
					    url: "/hcs/ws/zone/test",
					    data: data,
					    cache: false,
					    contentType: false,
					    processData: false,
					    type: 'POST',
					    xhr: function() {
					        myXhr = $.ajaxSettings.xhr();
					        if(myXhr.upload){
					            myXhr.upload.addEventListener('progress',progressHandlerFunction, false);
					        }
					        return myXhr;
					    },
					    success: function(data){
//					        alert(data);
					    }
					});
