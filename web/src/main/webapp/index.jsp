<%@ page contentType="text/html; charset=UTF-8"%><%@ page pageEncoding="UTF-8"%><!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Housecream</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<!-- Le styles -->
<link href="<%=request.getContextPath( )%>/css/bootstrap.css" rel="stylesheet">
<%-- <link href="<%=request.getContextPath( )%>/style.css" rel="stylesheet"> --%>
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

<script type="text/javascript" src="<%=request.getContextPath( )%>/ajsl-utils.js"></script>
<script type="text/javascript" src="<%=request.getContextPath( )%>/jquery-1.7.2.js"></script>
<script type="text/javascript" src="<%=request.getContextPath( )%>/jquery.dataSelector.js"></script>
<script type="text/javascript" src="<%=request.getContextPath( )%>/jquery.bootstrap.select.js"></script>
<script type="text/javascript" src="<%=request.getContextPath( )%>/bootstrap-modal.js"></script>
<script type="text/javascript" src="<%=request.getContextPath( )%>/js/bootstrap.js"></script>
<script type="text/javascript" src="<%=request.getContextPath( )%>/bootstrap-dropdown.js"></script>
<script type="text/javascript" src="<%=request.getContextPath( )%>/jquery.noty.js"></script>
<script type="text/javascript" src="<%=request.getContextPath( )%>/jquery.noty.layout.js"></script>
<script type="text/javascript" src="<%=request.getContextPath( )%>/jquery.noty.theme.js"></script>

<!-- script type="text/javascript" src="<%=request.getContextPath( )%>/history.js"></script -->

<!-- script type="text/javascript" src="<%=request.getContextPath( )%>/history.adapter.jquery.js"></script> -->
<script type="text/javascript" src="<%=request.getContextPath( )%>/form2js.js"></script>

<script type="text/javascript">
    curl = {
        baseUrl : '<%=request.getContextPath( )%>'
    };
</script>
<script type="text/javascript" src="<%=request.getContextPath( )%>/curl.js"></script>
<script type="text/javascript">
    var require = curl;
    require([ 'bootstrap' ], function(main) {
        main.start({context : '<%=request.getContextPath( )%>', root :'<%=request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath()) %>'});
    });  
</script>

</head>
<body>
    <div class="navbar navbar-fixed-top" id="topNav" style="display:none">
        <div class="navbar-inner">
            <div class="container-fluid">
                <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse"> <span class="icon-bar"></span> <span
                    class="icon-bar"></span> <span class="icon-bar"></span>
                </a> <a class="brand" href="<%=request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath()) %>">Housecream</a>
                <div class="nav-collapse">
                    <ul class="nav">
                        <li class="active"><a class="pushState" href="<%=request.getContextPath( )%>/">Home</a></li>
                        <li><a class="pushState" href="<%=request.getContextPath( )%>/admin">Admin</a></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
    <div class="container-fluid">
        <div id="content" class="row-fluid"></div>
        <footer>
            <p>
                <strong>© <a href="http://housecream.awired.net">Housecream</a></strong>
            </p>
        </footer>
    </div>

</body>
</html>