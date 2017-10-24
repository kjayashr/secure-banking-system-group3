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

	<script>
	
  function changetextbox()
	{	
		var type=document.getElementById('typeoftransfer');
		var typeVal=type.options[type.selectedIndex].value;
	    if (typeVal == "Internal") {
	        document.getElementById("recipient").disabled='true';
	        document.getElementById("to").removeAttribute('disabled')

	    } else {
	    	document.getElementById("error").innerHTML="";
	        document.getElementById("to").disabled='true';
	        document.getElementById("recipient").removeAttribute('disabled')
	    }
	}

  function validateTransfer(){
	console.log("inside");
	var type=document.getElementById('typeoftransfer')
	var typeVal=type.options[type.selectedIndex].value;
	if(typeVal=="Internal"){
		var fromSelect=document.getElementById('from');
		var fromVal=fromSelect.options[fromSelect.selectedIndex].value;
		var toSelect=document.getElementById('to');
		var toVal=fromSelect.options[toSelect.selectedIndex].value;
		if(fromVal==toVal){
			document.getElementById("error").innerHTML="From and To should be different"
			return false;
		}
	}	
	return true;
}
  
	
$('.dropdown-menu li a').click(function(){
	$(".btn:first-child").text($(this).text());
    $(".btn:first-child").val($(this).text());
  });
</script>

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
		
		<div class="container">
			<h2>Payment on behalf of ${customerUser}</h2>
			<ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#creditdebit">Credit/Debit</a></li>
				<li><a data-toggle="tab" href="#transfer">Transfer</a></li>
				<li><a data-toggle="tab" href="#payment">Payment</a></li>
			</ul>

			<div class="tab-content">
				<div id="creditdebit" class="tab-pane fade in active">

					<div class="container">
						<form class="form-horizontal" method="post"
							action="${pageContext.request.contextPath}/request"
							onsubmit="validateAmountCD()">
							<div class="form-group" style="padding-top: 40px">
								<label class="control-label col-sm-2" for="amount">Amount($):</label>
								<div class="col-sm-2">
									<input type="number" class="form-control" id="amount"
										placeholder="Enter amount" name="amount" min=1 required>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-sm-2" for="accountType">Account
									Type:</label>
								<div class="col-sm-2 dropdown">

									<select class="form-control" id="accountType"
										name="accountType">
										<option>Saving</option>
										<option>Checking</option>
										<option>Credit Card</option>
									</select>


								</div>
							</div>

							<div class="form-group">
								<label class="control-label col-sm-2" for="type"> Type:</label>
								<div class="col-sm-2 dropdown">
									<select class="form-control" id="type" name="type">
										<option>Credit</option>
										<option>Debit</option>
									</select>
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
				<div id="transfer" class="tab-pane fade">
					<h3>Tranfer Money</h3>
					<div class="container">
						<form class="form-horizontal" method="post"
							action="${pageContext.request.contextPath}/transfer"
							onsubmit=" return validateTransfer()">
							<div class="form-group" style="padding-top: 40px">
								<label class="control-label col-sm-2" for="amount">Amount($):</label>
								<div class="col-sm-2">
									<input type="number" class="form-control" id="amount"
										placeholder="Enter amount" name="amount" min=1 required>
								</div>
							</div>

							<div class="form-group">
								<label class="control-label col-sm-2" for="typeoftransfer">Type
									:</label>
								<div class="col-sm-2 dropdown">

									<select class="form-control" id="typeoftransfer"
										name="typeoftransfer" onChange="changetextbox();">
										<option>Internal</option>
										<option>External</option>
									</select>

								</div>
							</div>

							<div class="form-group">
								<label class="control-label col-sm-2" for="from">From :</label>
								<div class="col-sm-2 dropdown">

									<select class="form-control" id="from" name="from">
										<option>Saving</option>
										<option>Checking</option>
										<option>Credit Card</option>
									</select>


								</div>
							</div>

							<div class="form-group">
								<label class="control-label col-sm-2" for="to"> To:</label>
								<div class="col-sm-2 dropdown">
									<select class="form-control" id="to" name="to">
										<option>Saving</option>
										<option>Checking</option>
										<option>Credit Card</option>
									</select>
								</div>
							</div>

							<div class="form-group">
								<label class="control-label col-sm-2" for="recipient">
									Recipient:</label>
								<div class="col-sm-2 dropdown">
									<input type="email" class="form-control" id="recipient"
										placeholder="Enter email" name="recipient" required disabled>
								</div>
							</div>

							<span id="error" style="color: red"></span>

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

				<div id="payment" class="tab-pane fade">
					<h3>Make a Payment</h3>
					<div class="container">
						<form class="form-horizontal" method="post"
							action="${pageContext.request.contextPath}/payment">
							<div class="form-group" style="padding-top: 40px">
								<label class="control-label col-sm-2" for="amount">Amount($):</label>
								<div class="col-sm-2">
									<input type="number" class="form-control" id="amount"
										placeholder="Enter amount" name="amount" min=1 required>
								</div>
							</div>

							<div class="form-group">
								<label class="control-label col-sm-2" for="from">From: :</label>
								<div class="col-sm-2 dropdown">

									<select class="form-control" id="from" name="from">
										<option>Saving</option>
										<option>Checking</option>
										<option>Credit Card</option>
									</select>


								</div>
							</div>

							<div class="form-group">
								<label class="control-label col-sm-2" for="to">To :</label>
								<div class="col-sm-2 dropdown">

									<input type="text" class="form-control" id="to"
										placeholder="Enter email" name="to" required>

								</div>
							</div>

							<div class="form-group">
								<label class="control-label col-sm-2" for="comments">
									Comments:</label>
								<div class="col-sm-2 dropdown">
									<input type="text" class="form-control" id="comments"
										placeholder="Enter comment" name="comment">
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
		</div>


        <div class="row" align="center">
			<a href="${pageContext.request.contextPath}/tier1">Go Back To Home Page</a>
		</div>
	</sec:authorize>
</body>
</html>