<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page session="true"%>
<html>
<head>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	
<meta name="_csrf" content="${_csrf.token}"/>
	<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}"/>
</head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="https://ajax.aspnetcdn.com/ajax/jQuery/jquery-3.2.1.min.js"></script>
<body>

<script>
function grantApproval(x) {
	 	console.log("fdsfs");
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		var userName=x;
	$.ajax({
		type:"POST",
		url:"${pageContext.request.contextPath}/tier2/t1user/grantApproval",
		data:{userName:userName},
	    success:function(response){
	    		console.log(response);
	    },
	    error:function(error){
	    		console.log("Error "+error);
	    },
		beforeSend: function(xhr) {
       	 xhr.setRequestHeader(header, token);
    		}
	})
}

	
</script>


<sec:authorize access="hasRole('ROLE_TIER2')">
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

<div class="container">
				<div id="t1users" class="container">
				     <h3></h3>
				     <div class="container">
    				     <c:if test="${t1users.size()>0}">
    				         <col width = "80"/>
    				         <col width = "80"/>
    				         <col width = "80"/>
    				         <col width = "80"/>
    				         <col width = "80"/>
	    			         <table class="table">	
	    			             <tr>
	    			                 <th>Username</th>
	    			                 <th>User Role ID</th>
	    			            
	    			             </tr>	             
				                 <c:forEach items="${t1users}" var="t1user">
				                     <tr>
				                         <td>${t1user.userName}</td>
				                         <td>${t1user.userRoleId}</td>
 				                         <td><button id="accepted" onclick="grantApproval('${t1user.userName}')">Approve</button></td>
										
				                    </tr>
				                 </c:forEach>
				             </table>
				         </c:if>
				     </div>
				</div>
			</div>
        <div class="row" align="center">
			<a href="${pageContext.request.contextPath}/tier2">Go Back To Home Page</a>
		</div>

</sec:authorize>

</body>
</html>
