<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>

	<div align="center" id="log">
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
					<td>${log.transactiondate}</td>

					<td>${log.detail}</td>

					<td>${log.amount}</td>

				</tr>
			</c:forEach>


		</table>
	</div>

	<div class="row" align="center">
		<a href="${pageContext.request.contextPath}/welcome">Go Back To
			Home Page</a>

	</div>
	
	<div class="row" align="center">
		<a href="${pageContext.request.contextPath}/downloadPDF">PDF
			Home Page</a>

	</div>

	

</body>
</html>