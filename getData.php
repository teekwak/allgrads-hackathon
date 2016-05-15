<?php
header('content-type: application/json; charset=utf-8');
header("access-control-allow-origin: *");

ini_set('display_errors', 'on');

if(isset($_POST['action']) && !empty($_POST['action'])) {
    $action = $_POST['action'];

    switch($action) {
        case 'processTimestamp' : processTimestamp(); break;
    }
}
else {
	echo json_encode(array("status" => "FAILED: action is empty"));
}

// do logic here
function processTimestamp() {
	$tableName = "mobile_signal_info_" . $_POST['month'];


	$mysqli = new mysqli("localhost", "root", "root", "allgrads");
	#$sql = "INSERT INTO test (deviceId, time, mobilityStatus, latitude, longitude) VALUES (1, 2, 3, 4, 5);";

	$result = $mysqli->query("SELECT * FROM TEST WHERE time > 0 AND time < 40");

	$temp = array();
	if ($result->num_rows > 0) {
	    // output data of each row
	    while($row = $result->fetch_assoc()) {
	        array_push($temp, $row["deviceId"]);
	    }
	} else {

	}

	echo json_encode(array("status"=>$temp));

	$mysqli->close();
}

?>