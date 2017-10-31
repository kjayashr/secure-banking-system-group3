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
</head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="https://ajax.aspnetcdn.com/ajax/jQuery/jquery-3.2.1.min.js"></script>
 <meta name="_csrf" content="${_csrf.token}"/>
	<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}"/>
<body>

<jsp:include page="header.jsp"/>
<sec:authorize access="hasRole('ROLE_ADMIN')">
<script>
   	 function unblock(x) {
   		 	console.log("In changeStatus");
	   		var token = $("meta[name='_csrf']").attr("content");
   			var header = $("meta[name='_csrf_header']").attr("content");
   			var username=x;
			$.ajax({
				type:"POST",
				url:"${pageContext.request.contextPath}/userUnblockApproved",
				data:{username:username},
			    success:function(response){
			    	$("#unblock").attr("disabled", true);
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

		<div class="container">
				<div id="unblockUser" class="container">
				     <h3>Unlock blocked Users</h3>
				     <div class="container">
    				     <c:if test="${blockedUsers.size()>0}">
    				         <col width = "50"/>
    				         <col width = "50"/>
	    			         <table class="table">	
	    			             <tr>
	    			                 <th>Blocked Username</th>
	    			                 <th>    </th>
	    			             </tr>	             
				                 <c:forEach items="${blockedUsers}" var="blockedUser">
				                     <tr>
				                         <td>${blockedUser}</td>
				                         <td>
					                          <button id="unblock" onclick="unblock('${blockedUser}')">Unlock</button>
				                         </td>
				                     </tr>
				                 </c:forEach>
				             </table>
				         </c:if>
				     </div>
				</div>
			</div>
        <div class="row" align="center">
			<a href="${pageContext.request.contextPath}/admin/Welcome">Go Back To Home Page</a>
			
			
		</div>
		</sec:authorize>
</body>
</html>