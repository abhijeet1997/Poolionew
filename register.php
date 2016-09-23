<?php	
if($_SERVER['REQUEST_METHOD']=='POST'){
		$fname = $_POST['fname'];
                $lname=$_POST['lname'];
		$mobile = $_POST['mobile'];
		$password = $_POST['password'];
		$email = $_POST['email'];
	
		
		if($lname == '' || $fname == '' || $mobile == '' || $password == '' || $email == ''){
			echo 'please fill all values';
		}
                     else{
			require_once('dbConnect.php');
			$sql = "SELECT * FROM registrations WHERE mobile='$mobile'";
			
			$check = mysqli_fetch_array(mysqli_query($con,$sql));
			
			if(isset($check))
				{
					echo 'Mobile Number already exist';
				}
				else{				
					$sql = "INSERT INTO registrations (first_name,last_name,mobile,password,email) VALUES('$fname','$lname','$mobile','$password','$email')";
					if(mysqli_query($con,$sql)){
						echo 'successfully registered';
					}
					else{
						echo 'oops! Please try again!';
					}
				}
			mysqli_close($con);
			}
}
else{
echo 'error';
}
?>