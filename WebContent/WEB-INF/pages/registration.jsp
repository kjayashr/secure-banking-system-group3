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
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="https://ajax.aspnetcdn.com/ajax/jQuery/jquery-3.2.1.min.js"></script>
        <style type="text/css">
 
            body {font-family:Arial, Sans-Serif;}
 
            #container {width:300px; margin:0 auto;}
 
            /* Nicely lines up the labels. */
            form label {display:inline-block; width:140px;}
 
            /* You could add a class to all the input boxes instead, if you like. That would be safer, and more backwards-compatible */
            form input[type="text"],
            form input[type="password"],
            form input[type="email"] {width:160px;}
 
            form .line {clear:both;}
            form .line.submit {text-align:right;}
 			div {padding-top:10px;}
        </style>
    
    <meta name="_csrf" content="${_csrf.token}"/>
	<!-- default header name is X-CSRF-TOKEN -->
	<meta name="_csrf_header" content="${_csrf.headerName}"/>
    </head>
    <body>
    <script>
   	 function checkAvailabilityUsername() {
	   		var token = $("meta[name='_csrf']").attr("content");
   			var header = $("meta[name='_csrf_header']").attr("content");
   			var username = $("#username").val();
			$.ajax({
				type:"POST",
				url:"${pageContext.request.contextPath}/checkusername",
				data:{username:username },
			    success:function(response){
			    		console.log(response);
			    		if(response=="false")
			    			$("#sameUser").html("Username unavailable")
			    		else
			    			$("#sameUser").html("")
			    },
			    error:function(error){
			    		console.log("Error "+error);
			    },
				beforeSend: function(xhr) {
		       	 xhr.setRequestHeader(header, token);
		    		}
			})
	  }
   	 
   	 function checkAvailabilityEmail(){
   		var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			var email = $("#email").val();
		$.ajax({
			type:"POST",
			url:"${pageContext.request.contextPath}/checkemail",
			data:{email:email },
		    success:function(response){
		    		console.log(response);
		    		if(response=="false")
		    			$("#sameEmail").html("Email already registered")
		    		else
		    			$("#sameEmail").html("")
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
   		 var emailError=$("#sameEmail").html()
   		 var userError=$("#sameUser").html()
   		 var phoneError=$("#invalidPhone").html()
   		 if(ssnError==""&&emailError==""&&userError==""&&phoneError==""){
   			 $("#error").html("")
   			 return true;
   		 }else{
   			 $("#error").html("Please correct above errors")
   			 return false;
   		 }
   	 }
	 
    
    </script>
            <form action="${pageContext.request.contextPath}/registration" method="post" onsubmit="return validate()">
              <h2 align="center">REGISTER</h2>
            <div id="container" class="container">
              <div class="row">
              <div class="col-md-4 pull-left">
                <div class="line"><label for="username">Username*: </label><input type="text" id="username"  name="username" onblur="checkAvailabilityUsername()" required /> </div>
                <div><span id="sameUser" name="sameUser" style="color:red"></span></div>
                <div class="line"><label for="pwd">Password*: </label><input type="password" id="password" name="password" required/></div>
                <!-- You may want to consider adding a "confirm" password box also -->
                <div class="line"><label for="firstname">FirstName*: </label><input type="text" id="firstname" name="firstname" required /></div>
                <div class="line"><label for="lastname">LastName*: </label><input type="text" id="lastname" name="lastname"  required/></div>
                <div class="line"><label for="dob">Date of Birth*: </label><input type="text" id="dateofbirth" name="dateofbirth" placeholder="YYYY-MM-DD" required /></div>
                <div class="line"><label for="email">Email*: </label><input type="email" id="email" name="email" onblur="checkAvailabilityEmail()" required /></div>
                <div><span id="sameEmail" name="sameEmail" style="color:red"></span></div>
                <!-- Valid input types: http://www.w3schools.com/html5/html5_form_input_types.asp -->
                <div class="line"><label for="add">Address*: </label><input type="text" id="address" name="address" required /></div>
                <div class="line"><label for="contactno">ContactNo*</label><input type="number" id="contactno" name="contactno" required onblur="checkNumber()" /></div>
        		    <div><span id="invalidPhone" name="invalidPhone" style="color:red"></span></div>
        		     <div class="line"><label for="ssn">SSN*: </label><input type="number" id="ssn" name="ssn"  required min=1 onblur="checkSSN()"/></div>
        		      <div><span id="invalidSSN" name="invalidSSN" style="color:red"></span></div>
        		  </div>
        		  <div class="col-md-4 pull-right container">
               
                <div class="line"><label for="city">City*: </label><input type="text" id="city" name="city" required /></div>
                <div class="line"><label for="state">State*: </label><input type="text" id="state" name="state" required /></div>
                <div class="line"><label for="country">Country*: </label><input type="text" id="country" name="country" required /></div>
                <div class="line"><label for="ptc">Post Code *: </label><input type="number" id="postcode" name="postcode" required /></div>
                <div class="line"><label class="control-label" for="accountType">Type*:</label>
					<select class="form-control" id="accountType" name="accountType" required>
						<option>Saving</option>
					</select>
				</div>
				
				<div class="line"><label class="control-label" for="userType">Type*:</label>
					<select class="form-control" id="userType" name="userType" required>
						<option>User</option>	
						<option>Merchant</option>
					</select>
				</div>
				 <div class="line"><label for="balance">Initial Balance*: </label><input type="number" id="balance" name="balance"  min=0 required/></div>
				  <div class="line"><label for="interest">Interest(%)*: </label><input type="number" id="interest" name="interest"  min=0 required/></div>
				<br/>
                <div class="line submit" style="padding-top:100px"><input type="submit" value="Submit" class="btn btn-primary" /></div>
                <span id="error" style="color: red"></span>
			</div> 
			</div>
			</div>
			<br/>
			<div class="row">
                <p align="center">Note: Please make sure your details are correct before submitting form and that all fields marked with * are completed!.</p>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
			</div>
            </form>
    </body>
</html>