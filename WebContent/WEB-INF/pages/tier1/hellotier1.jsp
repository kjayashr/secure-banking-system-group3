<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<jsp:include page="../header.jsp"/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome Tier1 User</title>
</head>
<body>

    <sec:authorize access="hasAnyRole('ROLE_TIER1', 'ROLE_TIER1_APPROVED')">
<div class="row" align="center">
			<a href="${pageContext.request.contextPath}/tier1">Modify Personal Account</a>
</div>
<sec:authorize access="hasRole('ROLE_TIER1_APPROVED')">
    <div class="row" align="center">
			<a href="${pageContext.request.contextPath}/tier1/transactions">View Non Critical Transactions</a>
    </div>
<div class="row" align="center">
			<a href="${pageContext.request.contextPath}/tier1/tier1TransactionUser">Create Transaction</a>
</div>
<div class="row" align="center">
			<a href="${pageContext.request.contextPath}/tier1/createExternalUser">Create External User Account</a>
</div>
<div class="row" align="center">
	<a href="${pageContext.request.contextPath}/tier1/searchExternalUser">Modify External User Account</a>
</div>
</sec:authorize>
<div class="row" align="center">
			<a href="${pageContext.request.contextPath}/tier1">View User Account Requests</a>
</div>

    </sec:authorize>
</body>


</html>