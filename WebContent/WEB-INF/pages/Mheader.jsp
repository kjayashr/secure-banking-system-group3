<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<head>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
</head>
<body>
<nav class="navbar navbar-default">
  <div class="container-fluid">
  <div class="row">
  	<div class="col-md-4">
  	<h4>
  	 <span class="glyphicon glyphicon-home"></span>
  	 	 <a href="${pageContext.request.contextPath}/Merchant/Welcome">
  	 		Welcome ${pageContext.request.userPrincipal.name}
  		 </a>
  	 </h4>
  	 </div>
     <div class="col-md-8">
		<!-- For login user -->
			<c:url value="/j_spring_security_logout" var="logoutUrl" />
				<form action="${logoutUrl}" method="post" id="logoutForm" >
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
			<script>
				function formSubmit() {
					document.getElementById("logoutForm").submit();
				}
			</script>
			<div class="row">
    		<sec:authorize access="hasRole('ROLE_MERCHANT')">
				<a href="${pageContext.request.contextPath}/MerchanteditProfile" class="btn btn-default navbar-btn pull-right"  >Edit Profile</a>		
				<a href="${pageContext.request.contextPath}/Merchantviewtransaction" class="btn btn-default navbar-btn pull-right"  >View Transactions</a>
				<a href="${pageContext.request.contextPath}/Merchantuserapprovals" class="btn btn-default navbar-btn pull-right"  >Approvals Needed</a>
				
			</sec:authorize>
				<c:if test="${pageContext.request.userPrincipal.name != null}">
						<a href="javascript:formSubmit()" type="button" class="btn btn-default navbar-btn pull-right" >
    						  <span class="glyphicon glyphicon-log-out"></span>Logout
    						</a>
				</c:if>
			</div>
		</div>
	</div>
	</div>
</nav>
</body>
</html>