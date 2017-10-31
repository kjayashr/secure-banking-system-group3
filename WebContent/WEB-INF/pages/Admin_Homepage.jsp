<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Administration Confirmation Title</title></head>
<body>
	<h1>
	<div align="center">
		Administrator Homepage
	</div>
	</h1>
	
<h2>Welcome ${pageContext.request.userPrincipal.name}</h2>
    <sec:authorize access="hasRole('ROLE_ADMIN')">
		<!-- For login user -->
		<c:url value="/j_spring_security_logout" var="logoutUrl" />
		<form action="${logoutUrl}" method="post" id="logoutForm">
			<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />
		</form>
		<script>
			function formSubmit() {
				document.getElementById("logoutForm").submit();
			}
		</script>

		<c:if test="${pageContext.request.userPrincipal.name != null}">
			<h3>
				<a x href="javascript:formSubmit()"> Logout</a>
			</h3>
		</c:if>
    </sec:authorize>
     	
	<div align="center" class="row"><a href="${pageContext.request.contextPath}/admin/registration">Create Internal User Account</a> </div>
	<br></br>
	<div align="center" class="row"><a href="${pageContext.request.contextPath}/admin/searchprofile">View/Modify/Delete Internal User Account</a> </div>
	<br></br>
	<div align="center" class="row"><a href="${pageContext.request.contextPath}/admin/viewrequests">Authorize/Decline Internal Users Request</a> </div>
	<br></br>
	<div align="center" class="row"><a href="${pageContext.request.contextPath}/admin/viewlogs">Access Log File</a> </div>
	<br></br>	
	<div align="center" class="row"><a href="${pageContext.request.contextPath}/admin/pii">Access PII</a> </div>
	<br></br>
	<div align="center" class="row"><a href="${pageContext.request.contextPath}/admin/unblockUser">Unblock Locked Users</a> </div>
	<br></br>	

</body>
</html>