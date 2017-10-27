<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container">
							<div id="payment">
					<h3>Please Provide Customer User Name:</h3>
					<div class="container">
						<form class="form-horizontal" method="post"
							action="${pageContext.request.contextPath}/tier2/fillTransactionRequest">


							<div class="form-group">
								<label class="control-label col-sm-2" for="to">From :</label>
								<div class="col-sm-2 dropdown">

									<input type="text" class="form-control" id="fromUser"
										placeholder="Enter user name" name="fromUser" required>

								</div>
							</div>

							<div class="form-group">
								<div class="col-sm-offset-2 col-sm-10">
									<button type="submit" class="btn btn-default">Submit</button>
								</div>
							</div>
							<input type="hidden" name="${_csrf.parameterName}"
								value="${_csrf.token}" />
						</form>
					</div>

				</div>
</div>
<div class="row" align="left">
			<a href="${pageContext.request.contextPath}/tier2">Go Back To Home Page</a>
		</div>
</body>
</html>