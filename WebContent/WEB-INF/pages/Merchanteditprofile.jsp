<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	
<meta name="_csrf" content="${_csrf.token}"/>
	<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}"/>
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
</style>
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
			    		if(response=="true")
			    			$("#sameUser").html("No account is linked with this username")
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
   	   	 
   	 function validate(){
   		 var userError=$("#sameUser").html()
   		 if(userError==""){
   			 $("#error").html("")
   			 return true;
   		 }else{
   			 $("#error").html("Please correct above errors")
   			 return false;
   		 }
   	 }
	 
    </script>
	
	<jsp:include page="Mheader.jsp"/>
	<div id="container" class="container">
		<form action="${pageContext.request.contextPath}/Merchant/changedDetails"
			method="get">
			<h1>Edit Profile</h1>
			<div class="line">
				<label for="email">Email *: </label><input type="email" id="email"
					value=${userInfo.email} name="email" />
			</div>
			<div class="line">
				<label for="add">Address *: </label><input type="text" id="address"
					value=${userInfo.address} name="address" />
			</div>
			
			<div class="line">
				<label for="city">city *: </label><input type="text" id="city"
					value=${userInfo.city} name="city"  />
			</div>
			<div class="line">
				<label for="state">State *: </label><input type="text" id="state"
					value=${userInfo.state} name="state" />
			</div>
			<div class="line">
				<label for="country">Country *: </label><input type="text"
					value=${userInfo.country} id="country" name="country" />
			</div>
			<div class="line">
				<label for="ptc">Post Code *: </label><input type="text"
					value=${userInfo.postcode} id="postcode" name="postcode"  />
			</div>
			<div class="line submit">
				<input type="submit" value="Submit" class="btn btn-primary" />
			</div>

			<p>Note: Please make sure your details are correct before
				submitting form and that all fields marked with * are completed!.</p>
				
			<div class="row">
			<a href="${pageContext.request.contextPath}/Merchant/Welcome"> Go back to home page</a>
		</div>
		</form>
	</div>
</body>
</html>