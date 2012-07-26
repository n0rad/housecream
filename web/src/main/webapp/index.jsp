<%@ page contentType="text/html; charset=UTF-8"%><%@ page pageEncoding="UTF-8"%><!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Housecream</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">

<!-- Le styles -->
<link href="<%=request.getContextPath( )%>/css/bootstrap.css" rel="stylesheet">
<link class="links-css" href="style.css" rel="stylesheet">
<style type="text/css">
body {
	padding-top: 60px;
	padding-bottom: 40px;
}
</style>
<link href="<%=request.getContextPath( )%>/css/bootstrap-responsive.css" rel="stylesheet">

<style type="text/css">
@media ( min-width : 320px) {
	.nav-collapse .dropdown-menu {
		background-color: #FFFFFF;
		border: 1px solid rgba(0, 0, 0, 0.2);
		border-radius: 5px 5px 5px 5px;
		box-shadow: 0 5px 10px rgba(0, 0, 0, 0.2);
	}
	.nav-collapse .dropdown-menu a.btn {
		color: #fff;
	}
	.nav-collapse .dropdown-menu a.btn:hover {
		background-color: #49AFCD;
		background-position: 0 -32px;
		color: #FFFFFF;
	}
	.nav-collapse .dropdown-menu a:hover {
		color: #4572a7;
		background: transparent;
	}
}

@media ( min-width : 767px) {
	.nav-collapse .dropdown-menu {
		background-color: #FFFFFF;
		border: 1px solid rgba(0, 0, 0, 0.2);
		border-radius: 5px 5px 5px 5px;
		box-shadow: 0 5px 10px rgba(0, 0, 0, 0.2);
	}
}
</style>

<!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

<link rel="shortcut icon" href="favicon.ico">
<!--     <link rel="apple-touch-icon-precomposed" sizes="144x144" href="../assets/ico/apple-touch-icon-144-precomposed.png"> -->
<!--     <link rel="apple-touch-icon-precomposed" sizes="114x114" href="../assets/ico/apple-touch-icon-114-precomposed.png"> -->
<!--     <link rel="apple-touch-icon-precomposed" sizes="72x72" href="../assets/ico/apple-touch-icon-72-precomposed.png"> -->
<!--     <link rel="apple-touch-icon-precomposed" href="../assets/ico/apple-touch-icon-57-precomposed.png"> -->

<script type="text/javascript" src="<%=request.getContextPath( )%>/form2object.js"></script>

<script type="text/javascript">
	curl = {
		baseUrl : '<%=request.getContextPath( )%>'
	};
</script>
<script type="text/javascript" src="<%=request.getContextPath( )%>/curl.js"></script>
<script type="text/javascript">
	var require = curl;
	require([ 'bootstrap' ], function(main) {
		main.start();
	});
</script>

</head>
<body>
	<div class="navbar navbar-fixed-top">
		<div class="navbar-inner">
			<div class="container-fluid">
			
			
				<a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse"> <span class="icon-bar"></span> <span
					class="icon-bar"></span> <span class="icon-bar"></span>
				</a> <a class="brand" href="/">Housecream</a>
				<div class="nav-collapse">
					<ul class="nav pull-right">
						<li class="active"><a href="/">Home</a></li>
						<li><a href="/admin">Admin</a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>

	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span4" id="widget">
				<div id="accordion4" class="accordion">
					<div class="accordion-group">
						<div class="accordion-heading">
							<a href="#event" data-toggle="collapse" class="accordion-toggle" data-original-title=""> <i
								class="icon-bookmark icon-white"></i> <span class="divider-vertical"></span> Earning <i
								class="icon-chevron-down icon-white pull-right"></i>
							</a>
						</div>
						<div class="accordion-body collapse in" id="event">
							<div class="accordion-inner">
						
						                          <button class="btn btn-inverse btn-toolbar btn-large"> <img src="<%=request.getContextPath( )%>/img/icon/iconset-promo.png" alt=""></button>
                                                        <button class="btn btn-inverse btn-toolbar btn-large"> <img src="<%=request.getContextPath( )%>/img/icon/icons-pic2.png" alt=""></button>
                                                        <button class="btn btn-inverse btn-toolbar btn-large"> <img src="<%=request.getContextPath( )%>/img/icon/iconset_menu_16.png" alt=""></button>
                                                        <button class="btn btn-inverse btn-toolbar btn-large"> <img src="<%=request.getContextPath( )%>/img/icon/iconset-contact.png" alt=""></button>
                                                        <button class="btn btn-inverse btn-toolbar btn-large"> <img src="<%=request.getContextPath( )%>/img/icon/iconset-delete.png" alt=""></button>
                                                        <button class="btn btn-inverse btn-toolbar btn-large"> <img src="<%=request.getContextPath( )%>/img/icon/iconset-home.png" alt=""></button>
                                                        <button class="btn btn-inverse btn-toolbar btn-large"> <img src="<%=request.getContextPath( )%>/img/icon/iconset-info.png" alt=""></button>
                                                        <button class="btn btn-inverse btn-toolbar btn-large"> <img src="<%=request.getContextPath( )%>/img/icon/iconset-lemp.png" alt=""></button>
                                                        <button class="btn btn-inverse btn-toolbar btn-large"> <img src="<%=request.getContextPath( )%>/img/icon/iconset-link.png" alt=""></button>
                                                        <button class="btn btn-inverse btn-toolbar btn-large"> <img src="<%=request.getContextPath( )%>/img/icon/iconset-mac.png" alt=""></button>
                                                        <button class="btn btn-inverse btn-toolbar btn-large"> <img src="<%=request.getContextPath( )%>/img/icon/iconset-mic.png" alt=""></button>
                                                        <button class="btn btn-inverse btn-toolbar btn-large"> <img src="<%=request.getContextPath( )%>/img/icon/iconset-miz.png" alt=""></button>
                                                        <button class="btn btn-inverse btn-toolbar btn-large"> <img src="<%=request.getContextPath( )%>/img/icon/iconset-play.png" alt=""></button>
                                                        <button class="btn btn-inverse btn-toolbar btn-large"> <img src="<%=request.getContextPath( )%>/img/icon/iconset-users.png" alt=""></button>      
            
							</div>
						</div>
					</div>
					<div class="accordion-group">
						<div class="accordion-heading">
							<a href="#statements" data-toggle="collapse" class="accordion-toggle" data-original-title=""> <i
								class="icon-globe icon-white"></i> <span class="divider-vertical"></span> Notifications <i
								class="icon-chevron-down icon-white pull-right"></i>
							</a>
						</div>
						<div class="accordion-body collapse in" id="statements">
							<div class="accordion-inner">
								<table class="table table-striped">

									<tbody>
										<tr>
											<td><i class="icon-user"></i></td>
											<td><a href="" data-original-title=""><strong>Hanafi ALMOJRIM</strong> added <strong>23
														users</strong> </a><em>4 hours ago</em></td>
										</tr>
										<tr>
											<td><i class="icon-envelope"></i></td>
											<td><a href="" data-original-title=""><strong>Abderrahim NIBET</strong> sent you <strong>message</strong>
											</a><em>Yesterday</em></td>
										</tr>
										<tr>
											<td><i class="icon-tag"></i></td>
											<td><a href="" data-original-title=""><strong>Youssef BASSIR</strong> invite you to <strong>dinner</strong>
											</a><em>2 days ago</em></td>
										</tr>
										<tr>
											<td></td>
											<td><a class="pull-right" href="" data-original-title="">Show all</a></td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>

			<div id="content" class="span8 section-body">
				<div id="section-body" class="tabbable">
					<ul class="nav nav-tabs">
						<li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="#tab1">La défense<b
								class="caret"></b></a>
							<ul class="dropdown-menu">
								<li class="active"><a href="#">La défense</a></li>
								<li><a href="#">ChÃ¢telet</a></li>
							</ul></li>
						<li class="dropdown "><a class="dropdown-toggle" data-toggle="dropdown" href="#tab1">Ariane<b
								class="caret"></b></a>
							<ul class="dropdown-menu">
								<li class=""><a href="#">Coeur défense</a></li>
								<li class="active"><a href="#">Ariane</a></li>
							</ul></li>
						<li class="dropdown active "><a class="dropdown-toggle" data-toggle="dropdown" href="#tab1">21 Floor<b
								class="caret"></b></a>
							<ul class="dropdown-menu">
								<li class="active"><a href="#">21 Floor</a></li>
								<li><a href="#">22 Floor</a></li>
							</ul></li>
						<li class="dropdown "><a class="dropdown-toggle" data-toggle="dropdown" href="#tab1">Rooms <b
								class="caret"></b></a>
							<ul class="dropdown-menu">
								<li class=""><a href="#">2101 Guest room</a></li>
								<li><a href="#">2102 Meeting</a></li>
							</ul></li>
					</ul>
					<div class="tab-pane active">
						<div class="span12">
							<img alt="" width="100%" height="100%" src="<%=request.getContextPath( )%>/img/apartmentdfloorplanbyzodevdesign.jpg">
						</div>
					</div>
				</div>
			</div>

		</div>
		<footer>
			<p>
				<strong>© <a href="http://housecream.awired.net">Housecream</a></strong>
			</p>
		</footer>
	</div>


	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath( )%>/js/bootstrap.js"></script>
	<script type="text/javascript" src="http://twitter.github.com/bootstrap/assets/js/bootstrap-dropdown.js"></script>
	<script type="text/javascript">
		$('.dropdown-toggle').dropdown();
	</script>
</body>
</html>