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
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>

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

    function checkAccountExists() {
   		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		var username = $("#username").val();
		var accounttype = $('#accounttype option:selected').text();
		
		$.ajax({
			type:"POST",
			url:"${pageContext.request.contextPath}/tier1/checkaccountexists",
			data:{username:username,accounttype:accounttype },
		    success:function(response){
		    		if(response=="true") {
		    			$("#sameaccount").html("Account already exists or username is not registered");		    			
		    		}
		    		else {
		    			$("#sameaccount").html("");
		    			
		    		}
		    },
		    error:function(error){
		    		console.log("Error "+error);
		    },
			beforeSend: function(xhr) {
	       	 xhr.setRequestHeader(header, token);
	    		}
		})
  }
   	   	 
   	 function validate(){
   		 var accountError=$("#sameaccount").html();
   		 if(accountError==""){
   			 $("#error").html("")
   			 return true;
   		 }else{
   			 $("#error").html("Please correct above errors")
   			 return false;
   		 }
   	 }
	 
    </script>
        <div id="container" class="container">
            <form action="${pageContext.request.contextPath}/tier1/createNewAccountType" method="post" onsubmit="return validate()">

            <div class="line"><label for="username">Username*: </label><input type="text" id="username"  name="username" onblur="checkAccountExists()" required /> </div>

            <div class="line"><label for="email">Email*: </label><input type="text" id="email"  name="email" required /> </div>
 
            <div class="line"><label class="control-label" for="accounttype">Type*:</label>
				<select class="form-control" id="accounttype" name="accounttype" onchange="checkAccountExists()" required>
					<option>Saving</option>
					<option>Checking</option>
					<option>Credit Card</option>
				</select>
			</div>
				
            <div><span id="sameaccount" name="sameaccount" style="color:red"></span></div>

            <div class="line"><label for="balance ">Balance: </label><input type="text" id="balance"  name="balance" required /> </div>
            <div class="line"><label for="interest">Interest*: </label><input type="text" id="interest"  name="interest" required /> </div>

            
                <!-- Valid input types: http://www.w3schools.com/html5/html5_form_input_types.asp -->
                <p>
                <div class="line submit"><input type="submit" value="Create" class="btn btn-primary" /></div>
                <span id="error" style="color: red"></span>
 				</p>
 				
                <a class="alert-link" href="${pageContext.request.contextPath}/tier1"> Click here to go to home page</a>

                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            </form>
        </div>
    </body>
</html>