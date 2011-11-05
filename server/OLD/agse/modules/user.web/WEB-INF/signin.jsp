<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<form action="<c:url value='/j_spring_security_check'/>" method="get">
	<fieldset>
		<ol>
			<li>
				<label for="j_username">
					<fmt:message key="signin.user"/>
				</label>
				<input type="text" id="j_username" name="j_username" value="testuser" />
			</li>
			<li>
				<label for="j_password">
					<fmt:message key="signin.password"/>
				</label>
				<input type="password" id="j_password" name="j_password" value="testpassword"/>
			</li>
			<li class="confirm">
				<label>
					<input type="checkbox" name="_spring_security_remember_me"/>
					<fmt:message key="signin.remember"/>
				</label>
			</li>
		</ol>
		<button type="submit">
			<fmt:message key="signin.submit"/>
		</button>
	</fieldset>
</form>
