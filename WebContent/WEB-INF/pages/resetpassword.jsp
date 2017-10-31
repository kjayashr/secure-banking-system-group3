<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page session="true"%>


<html>
<head>
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
	<script>
	   	 function validate() {
		   		var token = $("meta[name='_csrf']").attr("content");
	   			var header = $("meta[name='_csrf_header']").attr("content");
	   			var username = $("#username_holder").val();
	   			var dob = $("#dob").val();
				$.ajax({
					type:"POST",
					url:"${pageContext.request.contextPath}/prescheck",
					data:{username:username },
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
   	 </script>

<body onload='document.resetForm.username.focus();'>


	<div id="reset-box">
		<h2>Reset Password</h2>
	
	<c:choose>
	<c:when test="${reset_state eq 'start'}">
	<form role="form" name='resetForm' action="${pageContext.request.contextPath}/resetpassword" method="post" onsubmit="return validate()">
	<div class="row">
		  <table>
			<tr>
				<td>User:</td>
				<td><input type='text' name='username_holder' value='' required></td>
			</tr>
			<tr>
				<td>Date of Birth:</td>
				<td><input type='text' name='dob' placeholder="YYYY-MM-DD" required /></td>
			</tr>
		  </table>
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
	<form role="form" name='resetForm' action="${pageContext.request.contextPath}/resetpassword" method="post" onsubmit="return validate()">
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
		  		<span id="error" style="color:red"></span>
		  		<input type="hidden" name="password_set" value="DONE"/>
				<input name="submit" type="submit" class="btn btn-primary" value="Submit" />
			</td>
			<td></td>
	</table>
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
			<td></td>
	</table>
	</c:when>
	<c:when test="${reset_state eq 'finish'}">
	<table>
		<tr>
			<td>
		  		<span id="error" style="color:green">Password reset successfully</span>
			</td>
			<td></td>
	</table>
	</c:when>
	</c:choose>
	
  </div>

</body>
</html>