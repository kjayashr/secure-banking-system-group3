<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
	integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
	crossorigin="anonymous">

<!-- Optional theme -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css"
	integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp"
	crossorigin="anonymous">

<!-- Latest compiled and minified JavaScript -->
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
	integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
	crossorigin="anonymous"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="https://ajax.aspnetcdn.com/ajax/jQuery/jquery-3.2.1.min.js"></script>
<style type="text/css">
body {
	font-family: Arial, Sans-Serif;
}

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
</style>

<meta name="_csrf" content="${_csrf.token}" />
<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}" />
</head>
<body>

	<script>
		function validate() {
			checkFinalOTP();
			var otpError = $("#invalidOTP").html();
			if (otpError == "") {
				$("#error").html("");
				return true;
			} else {
				$("#error").html("Please correct the OTP");
				return false;
			}
		}

		function checkFinalOTP() {
			var userOTP = $("#userOTP").val();
			if (userOTP.length != 6 || isNaN(userOTP)) {
				$("#invalidOTP").html("Your OTP is 6 digit number");
			} else {
				$("#invalidOTP").html("");
			}
		}

		function checkOTP() {
			var userOTP = $("#userOTP").val();
			if (userOTP.length > 6 || isNaN(userOTP)) {
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
	</script>
	<form action="${pageContext.request.contextPath}/OTP" method="post"
		onsubmit="checkOTP()" onsubmit="return validate()">
		<h2 align="center">OTP</h2>
		<div id="container" class="container">
			<div class="row">

				<div class="line">
					<label for="userOTP">OTP : </</label>
					<br> 
					<input type="text" id="userOTP" style="width:100px;"
						name="userOTP" onblur="checkOTP()" />
				</div>
				<div>
					<span id="invalidOTP" name="invalidOTP" style="color: red"></span>
				</div>

				<div class="789">
					<button id="number" class="num" type="button"
						onclick="numberPressed(9);">9</button>
					<button id="number" class="num" type="button"
						onclick="numberPressed(8);">8</button>
					<button id="number" class="num" type="button"
						onclick="numberPressed(7);">7</button>
				</div>
				<div class="456">
					<button id="number" class="num" type="button"
						onclick="numberPressed(6);">6</button>
					<button id="number" class="num" type="button"
						onclick="numberPressed(5);">5</button>
					<button id="number" class="num" type="button"
						onclick="numberPressed(4);">4</button>
				</div>
				<div class="123">
					<button id="number" class="num" type="button"
						onclick="numberPressed(3);">3</button>
					<button id="number" class="num" type="button"
						onclick="numberPressed(2);">2</button>
					<button id="number" class="num" type="button"
						onclick="numberPressed(1);">1</button>
				</div>
				<div class="0Bck">
					<button id="number" class="num" type="button"
						onclick="numberPressed(0);">0</button>
					<button id="bckspc" class="backspace" type="button"
						onclick="bckspPressed();">Back</button>
				</div>
				<br />
				<div class="line submit">
					<input type="submit" value="OK" class="btn btn-primary" />
				</div>
				<span id="error" style="color: red"></span>
			</div>
		</div>
	</form>
</body>
</html>