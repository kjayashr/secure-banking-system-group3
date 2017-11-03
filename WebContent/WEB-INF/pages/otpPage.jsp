<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="https://ajax.aspnetcdn.com/ajax/jQuery/jquery-3.2.1.min.js"></script>
<style type="text/css">

#container {
	width: 300px;
	margin: 0 auto;
}

/* Nicely lines up the labels. */
form label {
	display: inline-block;
	width: 140px;
}

/* You could add a class to all the input boxes instead, if you like. That would be safer, and more backwards-compatible */
form input[type="text"], form input[type="password"], form input[type="email"]
	{
	width: 160px;
}

form .line {
	clear: both;
}

form .line.submit {
	text-align: right;
}

div {
	padding-top: 10px;
}

.num {
	width: 30px;
	height: 30px;
}

.backspace {
	width: 65px;
	height: 30px;
}

.submit {
	width: 47px;
	height: 40px;
}
</style>

<meta name="_csrf" content="${_csrf.token}" />
<!-- default header name is X-CSRF-TOKEN -->
<!-- <meta name="_csrf_header" content="${_csrf.headerName}" /> -->
</head>
<body onload="checkOTP()">
<jsp:include page="header.jsp" />
	<script>
		function validate() {
			checkFinalOTP();
			var otpError = $("#invalidOTP").html();
			if (otpError == "") {
				return true;
			} else {
				$("#error").html("Please correct the OTP");
				return false;
			}
		}

		function checkFinalOTP() {
			var userOTP = $("#userOTP").val();
			if (userOTP.length != 6 || isNaN(userOTP) || userOTP.indexOf('.') > -1 || userOTP.indexOf('-') > -1 || userOTP.indexOf('+') > -1) {
				$("#invalidOTP").html("Your OTP is 6 digit number");
			} else {
				$("#invalidOTP").html("");
			}
		}

		function checkOTP() {
			var userOTP = $("#userOTP").val();
			if (userOTP.length > 6 || isNaN(userOTP) || userOTP.indexOf('.') > -1 || userOTP.indexOf('-') > -1 || userOTP.indexOf('+') > -1) {
				$("#invalidOTP").html("Your OTP is 6 digit number");
			} else {
				$("#invalidOTP").html("<br>");
			}
		}

		function numberPressed(num) {
			var otpBox = document.getElementById('userOTP');
			var val = otpBox.value;
			val = val + num;
			otpBox.value = val;
			checkOTP();
		}

		function bckspPressed() {
			var otpBox = document.getElementById('userOTP');
			var val = otpBox.value;
			val = val.substring(0, val.length - 1);
			otpBox.value = val;
			checkOTP();
		}
		
		function clearPressed() {
			var otpBox = document.getElementById('userOTP');
			otpBox.value = "";
			checkOTP();
		}
	</script>
	<form action="${pageContext.request.contextPath}/${page}" method="post"
		onsubmit="return validate()">
		<div id="container" class="container">
			<div class="row">

				<div class="line">
					<label for="userOTP">Enter your OTP : </</label> <input
						type="text" id="userOTP" style="width: 100px;" name="userOTP"
						onblur="checkOTP()" />
				</div>
				<div>
					<p>${otp_attempts} chance(s) left</p>
					<span id="invalidOTP" style="color: red"></span>
				</div>
				<input type="hidden" name="otp_attempts" value="${otp_attempts}"/>
				<input type="hidden" name="username_holder" value="${username_holder}"/>

				<div id="numRow1">
					<button id="number" class="num" type="button"
						onclick="numberPressed(9);">9</button>
					<button id="number" class="num" type="button"
						onclick="numberPressed(8);">8</button>
					<button id="number" class="num" type="button"
						onclick="numberPressed(7);">7</button>
				</div>
				<div id="numRow1">
					<button id="number" class="num" type="button"
						onclick="numberPressed(6);">6</button>
					<button id="number" class="num" type="button"
						onclick="numberPressed(5);">5</button>
					<button id="number" class="num" type="button"
						onclick="numberPressed(4);">4</button>
				</div>
				<div id="numRow1">
					<button id="number" class="num" type="button"
						onclick="numberPressed(3);">3</button>
					<button id="number" class="num" type="button"
						onclick="numberPressed(2);">2</button>
					<button id="number" class="num" type="button"
						onclick="numberPressed(1);">1</button>
				</div>
				<div id="numRow1">
					<button id="number" class="num" type="button"
						onclick="numberPressed(0);">0</button>
					<button id="bckspc" class="backspace" type="button"
						onclick="bckspPressed();">Back</button>
				</div>
				<br />
				<div class="line">
					<button id="clear" class="submit" type="button"
						onclick="clearPressed();">Clear</button>
					<input type="submit" value="OK" class="submit" />
				</div>
				<input type="hidden" name="${_csrf.parameterName}"
					value="${_csrf.token}" />

			</div>
		</div>
	</form>
</body>
</html>