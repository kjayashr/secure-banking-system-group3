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
    </head>
    <body>
        <div id="container" class="container">
            <form action="${pageContext.request.contextPath}/register" method="post">
                <h1>REGISTER</h1>
                <div class="line"><label for="username">Username *: </label><input type="text" id="username" name="username" required /></div>
                <div class="line"><label for="pwd">Password *: </label><input type="password" id="password" name="password" required/></div>
                <!-- You may want to consider adding a "confirm" password box also -->
                <div class="line"><label for="firstname">FirstName *: </label><input type="text" id="firstname" name="firstname" required /></div>
                <div class="line"><label for="lastname">LastName *: </label><input type="text" id="lastname" name="lastname"  required/></div>
                <div class="line"><label for="dob">Date of Birth *: </label><input type="text" id="dateofbirth" name="dateofbirth" placeholder="YYYY-MM-DD" required /></div>
                <div class="line"><label for="email">Email *: </label><input type="email" id="email" name="email" required /></div>
                <!-- Valid input types: http://www.w3schools.com/html5/html5_form_input_types.asp -->
                <div class="line"><label for="add">Address *: </label><input type="text" id="address" name="address" required /></div>
                <div class="line"><label for="contactno">contactno*</label><input type="text" id="contactno" name="contactno" required /></div>
                <div class="line"><label for="ssn">ssn *: </label><input type="text" id="ssn" name="ssn" required /></div>
                <div class="line"><label for="city">city *: </label><input type="text" id="city" name="city" required /></div>
                <div class="line"><label for="state">State *: </label><input type="text" id="state" name="state" required /></div>
                <div class="line"><label for="country">Country *: </label><input type="text" id="country" name="country" required /></div>
                <div class="line"><label for="ptc">Post Code *: </label><input type="text" id="postcode" name="postcode" required /></div>
                <div class="line submit"><input type="submit" value="Submit" class="btn btn-primary" /></div>
 
                <p>Note: Please make sure your details are correct before submitting form and that all fields marked with * are completed!.</p>
            </form>
        </div>
    </body>
</html>