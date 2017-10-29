<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title> PII Page </title> 
</head>
<body>

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
		 
            <h1>SBS - Welcome SysAdmin</h1>

          <h2>PII:</h2>
		  <br>
			 <table class="table">
            <thead>
              <tr>
                <th>Username</th>
				<th>First Name</th>
				<th>Last Name</th>
				<th>DOB</th>
				<th>SSN</th>
				<th>Address</th>
				<th>City</th>
				<th>State</th>
				<th>Country</th>
				<th>Contact no</th>
                <th>Email</th>
                <th>Postal Code</th>
                           			
              </tr>
            </thead>
            <tbody>
        	  <c:forEach var="pii" items="${userInfo}" >
              <tr>
                <td>${pii.username}</td>
                <td>${pii.firstname}</td>
                <td>${pii.lastname}</td>
                <td>${pii.dob}</td>
                <td>${pii.ssn}</td>
                <td>${pii.address}</td>
                <td>${pii.city}</td>
                <td>${pii.state}</td>
                <td>${pii.country}</td>
                <td>${pii.contactno}</td>
				<td>${pii.email}</td>
				<td>${pii.postcode}</td>
              </tr>
             </c:forEach>
            </tbody>
            </table>
	<div class="row">
		<a href="${pageContext.request.contextPath}/admin/Welcome"> Go back to home page</a>
	</div>

</body>
</html>			