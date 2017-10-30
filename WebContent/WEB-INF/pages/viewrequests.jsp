<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

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

  <h2>User Requests </h2>
		 <table class="table table-striped">
            <thead>
              <tr>
                <th></th>
                <th>ID</th>
				<th>Requested by</th>
				<th>Status</th>                           			
              </tr>
            </thead>
            <tbody>
        	  <c:forEach var="request" items="${userrequests}" >
              <tr>
	        	<td><a href="<c:url value='/admin/showrequestdetails/${request.id}/${request.requesterusername}' />" >Show details</a></td>
	        	<td>${request.id}</td>
                <td>${request.requesterusername}</td>
                <td>${request.status}</td>
              </tr>
             </c:forEach>
            </tbody>
            </table>
	<p>Note: Please make sure your details are correct before
		submitting form and that all fields marked with * are completed!.</p>

            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />				
  </form>
	
	
</body>
</html>