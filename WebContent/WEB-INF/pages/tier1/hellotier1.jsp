<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome Tier1 User</title>
</head>
<body>
<jsp:include page="../header.jsp"/>

    <sec:authorize access="hasRole('ROLE_USER')">
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
			<h2>
				<a x href="javascript:formSubmit()"> Logout</a>
			</h2>
		</c:if>
    </sec:authorize>
<div class="row" align="center">
	<a href="${pageContext.request.contextPath}/tier1">View External User Account</a>
</div>
<div class="row" align="center">
			<a href="${pageContext.request.contextPath}/tier1">Modify Personal Account</a>
</div>
<div class="row" align="center">
			<a href="${pageContext.request.contextPath}/tier1/transactions">View Non Critical Transactions</a>
</div>
<div class="row" align="center">
			<a href="${pageContext.request.contextPath}/tier1/tier1TransactionUser">Create Transaction</a>
</div>
<div class="row" align="center">
			<a href="${pageContext.request.contextPath}/tier1">Create External User Account</a>
</div>
<div class="row" align="center">
			<a href="${pageContext.request.contextPath}/tier1">View User Account Requests</a>
			
        <div class="row" align="center">
			<a href="${pageContext.request.contextPath}/tier1">Go Back To Home Page</a>
		</div>
</div>
</body>


</html>