<?php	
	if($_SERVER['REQUEST_METHOD']=='POST')
	{
		$mobile = $_POST['mobile'];
                $source =$_POST['source'];
		$destination = $_POST['destination'];
		$type = $_POST['type'];
		$date = $_POST['date'];
		$time = $_POST['time'];
		$vname = $_POST['vname'];
		$vnumber = $_POST['vnumber'];
		$availableSeats = $_POST['availableSeats'];
		$chargeable = $_POST['chargeable'];
		$amount = $_POST['amount'];
	
		
		if($mobile == '' || $source == '' || $destination == '' || $type == '' || $date == ''|| $time == '' || $vname == '' || $availableSeats == '' || $chargeable == '')
		{
			echo 'please fill all values';
		}
                else
		{
			require_once('dbConnect.php');
			
			$sql = "INSERT INTO rides (mobile,source,destination,type,date,time,vehicle_name,vehicle_number,seats,chargeable,amount) 
			VALUES('$mobile', '$source', '$destination', '$type', '$date', '$time', '$vname', '$vnumber', $availableSeats, $chargeable, $amount);";
			if(mysqli_query($con,$sql))
			{
			
				echo 'successfully registered';
			}
			else
			{
				echo 'oops! Please try again!';
			}
			mysqli_close($con);
		}
	}
	else
	{
		echo 'error';
	}
?>