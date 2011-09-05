<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>
		<fmt:message>
			<tiles:insertAttribute name="title"/>
		</fmt:message>
	</title>
	<link type="text/css" rel="stylesheet" href="<c:url value="/resources/dijit/themes/tundra/tundra.css" />" />
	<style type="text/css" media="screen">
        @import url("<c:url value="/resources/css-framework/css/tools.css" />");
        @import url("<c:url value="/resources/css-framework/css/typo.css" />");
        @import url("<c:url value="/resources/css-framework/css/layout-navleft-1col.css" />");
        @import url("<c:url value="/resources/css-framework/css/layout.css" />");
        @import url("<c:url value="/resources/css-framework/css/nav-vertical.css" />");
        @import url("<c:url value="/resources/styles/style-green.css" />");
    </style>
	<script type="text/javascript" src="<c:url value="/resources/dojo/dojo.js" />"></script>
	<script type="text/javascript" src="<c:url value="/resources/spring/Spring.js" />"></script>
	<script type="text/javascript" src="<c:url value="/resources/spring/Spring-Dojo.js" />"></script>	
</head>
<body class="tundra">
	<div id="page">
		<div id="header" class="clearfix">
			<div id="branding">
				<img src="<c:url value="/resources/images/banner.png"/>" />
			</div>
			<div id="personalization">
			</div>			
		</div>
		<div id="content" class="clearfix">
			<div id="main">
				<tiles:insertAttribute name="main" />
			</div>
			<div id="nav">
				<tiles:importAttribute name="navigationTab" />
				<ul class="clearfix">
					<li>
						<a href="<c:url value="/"/>">
							<fmt:message key="navigate.home"/>
						</a>
					</li>
					<li>
						<a href="<c:url value="/app/signin"/>">
							<fmt:message key="navigate.signin"/>
						</a>
					</li>					
				</ul>
			</div>
		</div>
		<div id="footer" class="clearfix">
			<p>Powered by Spring - <a href="http://www.springsource.org">www.springsource.org</a></p>
		</div>
	</div>
</body>
</html>
