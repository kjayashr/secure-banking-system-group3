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
            form label {display:inline-block; width:140px;}
 
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
        <div id="container" class="container">
            <form action="${pageContext.request.contextPath}/registration" method="post" onsubmit="return validate()">
                <h1>DELETE PROFILE</h1>
                <div class="line"><label for="username">Username *: </label><input type="text" id="username"  name="username" onblur="checkAvailabilityUsername()" required /> </div>
                <div class="line"><label for="pwd">Password *: </label><input type="password" id="password" name="password" required/></div>
                <div class="line"><label for="firstname">FirstName *: </label><input type="text" id="firstname" name="firstname" required /></div>
                <div class="line"><label for="lastname">LastName *: </label><input type="text" id="lastname" name="lastname"  required/></div>
                <div class="line"><label for="email">Email *: </label><input type="email" id="email" name="email" onblur="checkAvailabilityEmail()" required /></div>
                <div><span id="sameEmail" name="sameEmail" style="color:red"></span></div>
                <!-- Valid input types: http://www.w3schools.com/html5/html5_form_input_types.asp -->
                <p>
                <div class="line submit"><input type="submit" value="Delete" class="btn btn-primary" /></div>
                <span id="error" style="color: red"></span>
 				</p>
                <p>Note: Please make sure your details are correct before submitting form and that all fields marked with * are completed!.</p>
                <input type="hidden" name="${_csrf.parameterName}"
								value="${_csrf.token}" />
            </form>
        </div>
    </body>
</html>