<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page session="true"%>


<html>
<head>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<title>Reset Password</title>
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

#reset-box {
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
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="https://ajax.aspnetcdn.com/ajax/jQuery/jquery-3.2.1.min.js"></script>
 <meta name="_csrf" content="${_csrf.token}"/>
	<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}"/>
	
<body>
	<script>
	   	 function validate1() {
	   		 
		   		var token = $("meta[name='_csrf']").attr("content");
	   			var header = $("meta[name='_csrf_header']").attr("content");
	   			var username = $("#username_holder").val();
	   			var contactno = $("#contactno").val();
	   			var phoneError=$("#invalidPhone").html()
	   			if(phoneError!=""){
	      			 $("#error").html("Please correct above errors")
	      			 return false;
	      		 }
	   			
				$.ajax({
					type:"POST",
					url:"${pageContext.request.contextPath}/prescheck",
					data:{username:username, contactno:contactno},
				    success:function(response){
				    		console.log(response);
				    		if(response=="false")
				    			$("#error").html("Wrong Username or Date of Birth")
				    		else
				    			$("#error").html("")
				    },
				    error:function(error){
				    		console.log("Error "+error);
				    },
					beforeSend: function(xhr) {
			       	 xhr.setRequestHeader(header, token);
			    		}
				})
		  }
	   	 
	   	function checkNumber(){
	   		 var contact=$("#contactno").val();
	   		 if(contact.length != 10 || isNaN(contact)){
	   			$("#invalidPhone").html("Enter 10-digit Number");
	   		 }else{
	   			$("#invalidPhone").html("");
	   		 }
	   	 }
	   	
	   	function validate(){
	   		validate1();
	   		 
	   		 var error=$("#error").html();
	   		 
	   		 if(error==""){
	   			 $("#error").html("")
	   			 return true;
	   		 }else{
	   			 $("#error").html("Please correct above errors")
	   			 return false;
	   		 }
	   	 }
	   	
   	 </script>


	<div id="reset-box">
		<h2>Reset Password</h2>
	
	<c:choose>
	<c:when test="${reset_state eq 'start'}">
	<form role="form" name='resetForm' action="${pageContext.request.contextPath}/resetpassword" method="post" onsubmit="return validate()">
	<div class="row">
		  <table>
			<tr>
				<td>Username:</td>
				<td><input type='text' id='username_holder' name='username_holder' value='' required></td>
			</tr>
			<tr>
				<td>Contact:</td>
				<td><input type='text' id='contactno' name='contactno' required onblur="checkNumber()"/></td>
			</tr>
		  </table>
		  <span id="invalidPhone" name="invalidPhone" style="color:red"></span>
	</div>
	<div class="row">
	<table>
		<tr>
			<td>
		  		<span id="error" style="color:red"></span>
				<input name="submit" type="submit" class="btn btn-primary" value="Submit" />
			</td>
			<td></td>
	</table>
	</div>
		  <input type="hidden" name="${_csrf.parameterName}"
			value="${_csrf.token}" />

	</form>
	</c:when>
	<c:when test="${reset_state eq 'takePass'}">
	<form role="form" name='resetForm' action="${pageContext.request.contextPath}/resetpassword" method="post">
	<div class="row">
		  <table>
			<tr>
				<td>Hello ${username_holder} </td>
				<td><input type="hidden" name="username_holder" value="${username_holder}"/></td>
			</tr>
			<tr>
				<td>New Password:</td>
				<td><input type='password' name='new_password' required /></td>
			</tr>
		  </table>
	</div>
	<div class="row">
	<table>
		<tr>
			<td>
		  		<input type="hidden" name="password_set" value="DONE"/>
				<input name="submit" type="submit" class="btn btn-primary" value="Submit" />
			</td>
			<td></td>
	</table>
	<span id="error" style="color:red"></span>
	</div>
		  <input type="hidden" name="${_csrf.parameterName}"
			value="${_csrf.token}" />

	</form>
	</c:when>
	<c:when test="${reset_state eq 'wrongOTP'}">
	<table>
		<tr>
			<td>
		  		<span id="error" style="color:red">Wrong OTP entered. Maximum try over.</span>
			</td>
			<td></td>
	</table>
	</c:when>
	<c:when test="${reset_state eq 'wrong'}">
	<table>
		<tr>
			<td>
		  		<span id="error" style="color:red">Password didn't reset</span>
			</td>
			</tr>
			<tr>
			<td>
				<a href="${pageContext.request.contextPath}/login">Go Back To Login Page</a>
			</td>
			</tr>
	</table>
	</c:when>
	<c:when test="${reset_state eq 'finish'}">
	<table>
		<tr>
			<td>
		  		<span id="error" style="color:green">Password reset successfully</span>
			</td>
			</tr>
			<tr>
			<td>
				<a href="${pageContext.request.contextPath}/login">Go Back To Login Page</a>
			</td>
			</tr>
	</table>
	</c:when>
	</c:choose>
	
  </div>

</body>
</html>