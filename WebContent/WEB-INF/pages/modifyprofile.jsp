<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
    <!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

<!-- Optional theme -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">

<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
<script src="https://ajax.aspnetcdn.com/ajax/jQuery/jquery-3.2.1.min.js"></script>
        <style type="text/css">
 
            body {font-family:Arial, Sans-Serif;}
 
            #container {width:300px; margin:0 auto;}
 
            /* Nicely lines up the labels. */
            form label {display:inline-bform-group; width:140px;}
 
            /* You could add a class to all the input boxes instead, if you like. That would be safer, and more backwards-compatible */
            form input[type="text"],
            form input[type="password"],
            form input[type="email"] {width:160px;}
 
            form .line {clear:both;}
            form .line.submit {text-align:right;}
 
        </style>
    
    <meta name="_csrf" content="${_csrf.token}"/>
	<!-- default header name is X-CSRF-TOKEN -->
	<meta name="_csrf_header" content="${_csrf.headerName}"/>
    </head>
    <body>
    
    <script>

  	 function checkDOB(){
   		 var dob=$("#dob").val();
  		 re = /^\d{4}\-\d{1,2}\-\d{1,2}$/;
  	     if(dob != '' && !dob.match(re)) {
  		    $("#invalidDOB").html("Invalid date format");
   		 }else{
    		$("#invalidDOB").html("");
    	 }  	 
  	 }
   	 function checkNumber(){
   		 var contact=$("#contactno").val();
   		 if(contact.length != 10){
   			$("#invalidPhone").html("Enter 10-digit Number");
   		 }else{
   			$("#invalidPhone").html("");
   		 }
   	 }
   	 
   	function checkSSN(){
  		 var ssn=$("#ssn").val();
  		 if(ssn.length != 9){
  			$("#invalidSSN").html("Enter 9-digit SSN");
  		 }else{
  			$("#invalidSSN").html("");
  		 }
  	 }
   	   	 
   	 function validate(){
   		 var ssnError=$("#invalidSSN").html()
   		 var phoneError=$("#invalidPhone").html()
   		 if(ssnError=="" && phoneError==""){
   			 $("#error").html("")
   			 return true;
   		 }else{
   			 $("#error").html("Please correct above errors")
   			 return false;
   		 }
   	 }
	 
    </script>
</head>
<body>

<div class="container">
  <h2>Update or Delete Profile</h2>
	<form action="${pageContext.request.contextPath}/admin/updateordeleteprofile" method="post" onsubmit="return validate()">

			<input type="hidden" id="username" name="username" value=${userInfo.username}>
	
			<div class="form-group">
				<label for="firstname">First Name: </label><input type="text" id="firstname"
					class="form-control" value = ${userInfo.firstname} name="firstname" required />
			</div>
			<div class="form-group">
				<label for="lastname">Last Name: </label><input type="text" id="lastname"
					class="form-control" value = ${userInfo.lastname} name="lastname" required />
			</div>

			<div class="form-group">
				<label for="dob">Data of Birth *: </label><input type="text" id="dob"
					class="form-control" value = ${userInfo.dateofbirth} name="dob" onblur="checkDOB()" required />
			</div>
   		    <span id="invalidDOB" name="invalidDOB" style="color:red"></span>

			<div class="form-group">
				<label for="ssn">SSN *: </label><input type="text"
					class="form-control" value = ${userInfo.ssn} id="ssn" name="ssn" onblur="checkSSN()" required />
			</div>
   		    <span id="invalidSSN" name="invalidSSN" style="color:red"></span>

			<div class="form-group">
				<label for="contactno">Contact Number *: </label><input type="text"
					class="form-control" value = ${userInfo.contactno} id="contactno" name="contactno" onblur="checkNumber()" required />
			</div>
   		    <span id="invalidPhone" name="invalidPhone" style="color:red"></span>

			<div class="form-group">
				<label for="add">Address *: </label><input type="text" id="address"
					class="form-control" value = ${userInfo.address} name="address" required />
			</div>
			
			<div class="form-group">
				<label for="city">city *: </label><input type="text" id="city"
					class="form-control" value = ${userInfo.city} name="city" required />
			</div>
			<div class="form-group">
				<label for="state">State *: </label><input type="text" id="state"
					class="form-control" value = ${userInfo.state} name="state" required />
			</div>
			<div class="form-group">
				<label for="country">Country *: </label><input type="text"
					class="form-control" value = ${userInfo.country} id="country" name="country" required />
			</div>

			<div class="form-group">
				<label for="postcode">PSC *: </label><input type="text"
					class="form-control" value = ${userInfo.postcode} id="postcode" name="postcode" required />
			</div>
				
            <span id="error" style="color: red"></span>

			<div class="row">
				<input type="submit" name="operation" value="Update" class="btn btn-success" />
				<input type="submit" name="operation" value="Delete" class="btn btn-danger" />
				<a href="${pageContext.request.contextPath}/admin/Welcome"> Go back to home page</a>
			</div>

			<p>Note: Please make sure your details are correct before
				submitting form and that all fields marked with * are completed!.</p>

            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />				
  </form>
</div>
	
	
</body>
</html>