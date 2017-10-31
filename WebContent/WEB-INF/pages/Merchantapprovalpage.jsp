<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="https://ajax.aspnetcdn.com/ajax/jQuery/jquery-3.2.1.min.js"></script>

 <meta name="_csrf" content="${_csrf.token}"/>
	<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}"/>
</head>
<body>
<jsp:include page="Mheader.jsp"/>
<script>
   	 function changeStatus(x,y,z) {
   		 	console.log("dsa");
	   		var token = $("meta[name='_csrf']").attr("content");
   			var header = $("meta[name='_csrf_header']").attr("content");
   			var transactionId=x;
   			var status=y;
   			var amount=z;
			$.ajax({
				type:"POST",
				url:"${pageContext.request.contextPath}/MerchantapproveByUser",
				data:{transactionId:transactionId, status:status, amount:amount},
			    success:function(response){
			    		console.log(response);
			    		$("#accepted").attr("disabled", true);
			    		$("#declined").attr("disabled", true);
			    		$("#notify").html("Your request has been processed")

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

<div align="center" id="log">
	<c:choose>
	<c:when test="${empty emptyMsg}">
		<table border="1" cellpadding="5">
			<caption>
				<h2 id="title">${title}</h2>
			</caption>
			<tr>
				<th id="column1">${column1}</th>
				<th id="column2">${column2}</th>
				<th id="column3">${column3}</th>


			</tr>

			<c:forEach var="log" items="${list}">
				<tr>
					<td>${log.date}</td>

					<td>${log.sender}</td>

					<td>${log.amount}</td>
						<td><button id="accepted" onclick="changeStatus(${log.id}, this.id,${log.amount})">Approve</button></td>
					<td><button id="declined" onclick="changeStatus(${log.id}, this.id,${log.amount})">Decline</button></td>
					
				</tr>
			</c:forEach>
			</table>
			<div><span id="notify" name="notify" style="color:red"></span></div>
	  </c:when>
	  <c:otherwise>
	  			<table>
					<th>"No approvals needed at the moment"</th>
				</table>
	  </c:otherwise>
	  </c:choose>
	</div>

</body>
</html>