<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page session="true"%>


<html>
<head>
<title>Login Page</title>
<style>
.error {
	padding: 15px;
	margin-bottom: 20px;
	border: 1px solid transparent;
	border-radius: 4px;
	color: #a94442;
	background-color: #f2dede;
	border-color: #ebccd1;
}

.msg {
	padding: 15px;
	margin-bottom: 20px;
	border: 1px solid transparent;
	border-radius: 4px;
	color: #31708f;
	background-color: #d9edf7;
	border-color: #bce8f1;
}
.expired {
	padding: 15px;
	margin-bottom: 20px;
	border: 1px solid transparent;
	border-radius: 4px;
	color: #31708f;
	background-color: #d9edf7;
	border-color: #bce8f1;
}

#login-box {
	width: 300px;
	padding: 20px;
	margin: 100px auto;
	background: #fff;
	-webkit-border-radius: 2px;
	-moz-border-radius: 2px;
	border: 1px solid #000;
}
</style>
</head>
<body onload='document.loginForm.username.focus();'>


	<div id="login-box">
		<h2>Login</h2>
		<c:choose>
		<c:when test="${not empty SPRING_SECURITY_LAST_EXCEPTION}">
      		<div class="error">${SPRING_SECURITY_LAST_EXCEPTION.message}</div	>	
    		 </c:when>
		<c:when test="${not empty error}">
			<div class="error">${error}</div>
		</c:when>
		<c:when test="${not empty msg}">
			<div class="msg">${msg}</div>
		</c:when>
		<c:when test="${not empty expired}">
			<div class="msg">${expired}</div>
		</c:when>
		</c:choose>
	
	<form role="form" name='loginForm' action="<c:url value='j_spring_security_check' />" method='POST'>
	<div class="row">
		  <table>
			<tr>
				<td>Username:</td>
				<td><input type='text' name='username' value=''></td>
			</tr>
			<tr>
				<td>Password:</td>
				<td><input type='password' name='password' /></td>
			</tr>
		  </table>
	</div>
	<div class="row">
	<table>
		<tr>
			<td>
				<input name="submit" type="submit" class="btn btn-primary" value="Submit" />
			</td>
			<td></td>
			<td>
				<a href="${pageContext.request.contextPath}/registration"> Sign Up </a>
			</td>
	</table>
	</div>
		  <input type="hidden" name="${_csrf.parameterName}"
			value="${_csrf.token}" />

	</form>
  </div>

</body>
</html>