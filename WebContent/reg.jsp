<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Registration</title>
<style type="text/css">
/* Style all input fields */
input {
	width: 100%;
	padding: 12px;
	border: 1px solid #ccc;
	border-radius: 4px;
	box-sizing: border-box;
	margin-top: 6px;
	margin-bottom: 16px;
}

/* Style the submit button */
input[type=submit] {
	background-color: #4CAF50;
	color: white;
}

/* Style the container for inputs */
.container {
	background-color: #f1f1f1;
	padding: 20px;
}

/* The message box is shown when the user clicks on the password field */
#message {
	display: none;
	background: #f1f1f1;
	color: #000;
	position: relative;
	padding: 20px;
	margin-top: 10px;
}

#message p {
	padding: 0px 35px;
	font-size: 18px;
}

/* Add a green text color and a checkmark when the requirements are right */
.valid {
	color: green;
}

.valid:before {
	position: relative;
	left: -35px;
	content: "✔";
}

/* Add a red text color and an "x" when the requirements are wrong */
.invalid {
	color: red;
}

.invalid:before {
	position: relative;
	left: -35px;
	content: "✖";
}
</style>
</head>
<body>

	<form method="post" action="register">
		<center>
			<table border="1" width="30%" cellpadding="5">
				<thead>
					<tr>
						<th colspan="2">Enter Information Here</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>Login Name</td>
						<td><input type="text" placeholder="Zadaj svoj login name..."
							required="required" name="login" value="" /></td>
					</tr>
					<tr>
						<td>Full Name</td>
						<td><input type="text" placeholder="Zadaj svoje meno..."
							required="required" name="fullName" value="" /></td>
					</tr>
					<tr>
						<td>Password</td>
						<td><input type="password" id="psw"
							placeholder="Zadaj svoje heslo..." required="required"
							name="pass" value=""
							pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}"
							title="Musí splňať nižšie uvedené podmienky" /></td>
					</tr>
					<tr>
						<td><input type="submit" value="Submit" /></td>
						<td><input type="reset" value="Reset" /></td>
					</tr>
					<tr>
						<td colspan="2">
								<p>${param.message}</p>								
							 <a href="index2.jsp">Login: Here</a></td>
					</tr>
				</tbody>
			</table>
		</center>

		<div id="message">
			<h3>Heslo musí splňať nasledujúce vlastnosti:</h3>
			<p id="letter" class="invalid">
				<b>malé písmeno</b>
			</p>
			<p id="capital" class="invalid">
				<b>veľké písmeno</b>
			</p>
			<p id="number" class="invalid">
				<b>čislicu</b>
			</p>
			<p id="length" class="invalid">
				Minimálne <b>8 znakov</b>
			</p>
		</div>

		<script>
			var myInput = document.getElementById("psw");
			var letter = document.getElementById("letter");
			var capital = document.getElementById("capital");
			var number = document.getElementById("number");
			var length = document.getElementById("length");

			// When the user clicks on the password field, show the message box
			myInput.onfocus = function() {
				document.getElementById("message").style.display = "block";
			}

			// When the user clicks outside of the password field, hide the message box
			myInput.onblur = function() {
				document.getElementById("message").style.display = "none";
			}

			// When the user starts to type something inside the password field
			myInput.onkeyup = function() {
				// Validate lowercase letters
				var lowerCaseLetters = /[a-z]/g;
				if (myInput.value.match(lowerCaseLetters)) {
					letter.classList.remove("invalid");
					letter.classList.add("valid");
				} else {
					letter.classList.remove("valid");
					letter.classList.add("invalid");
				}

				// Validate capital letters
				var upperCaseLetters = /[A-Z]/g;
				if (myInput.value.match(upperCaseLetters)) {
					capital.classList.remove("invalid");
					capital.classList.add("valid");
				} else {
					capital.classList.remove("valid");
					capital.classList.add("invalid");
				}

				// Validate numbers
				var numbers = /[0-9]/g;
				if (myInput.value.match(numbers)) {
					number.classList.remove("invalid");
					number.classList.add("valid");
				} else {
					number.classList.remove("valid");
					number.classList.add("invalid");
				}

				// Validate length
				if (myInput.value.length >= 8) {
					length.classList.remove("invalid");
					length.classList.add("valid");
				} else {
					length.classList.remove("valid");
					length.classList.add("invalid");
				}
			}
		</script>
	</form>
</body>
</html>