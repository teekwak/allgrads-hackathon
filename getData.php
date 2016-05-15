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
	//$sql = "select app_short, COUNT(*) AS `num` from september_first_half WHERE deviceId=84756 GROUP BY app_short;";

	//$sql = "SELECT deviceId, latitude, longitude, mobility from september_mobile_signal WHERE time >= " . $_POST['timestamp'] . " AND time < " . ($_POST['timestamp'] + 3600) . " GROUP BY deviceId, mobility";

	$sql = "SELECT deviceId, time, latitude, longitude, mobility, COUNT(*) as `count` from ".$_POST['month']."_mobile_signal WHERE time >= " . $_POST['timestamp'] . " AND time < " . ($_POST['timestamp'] + 3600) . " GROUP BY deviceId, latitude, longitude, mobility";

	//$sql = "SELECT deviceId, time, latitude, longitude, mobility from september_mobile_signal WHERE time >= " . $_POST['timestamp'] . " AND time < " . ($_POST['timestamp'] + 3600) . " GROUP BY deviceId, time, latitude, longitude, mobility;";

	$result = $mysqli->query($sql);

	$temp = array();
	if ($result->num_rows > 0) {
	    // output data of each row
	    while($row = $result->fetch_assoc()) {
	        array_push($temp, $row);
	    }
	} else {

	}

	$sql2 = "SELECT app_short, mobility, COUNT(*) as `count` from ".$_POST['month']."_app WHERE time >= " . $_POST['timestamp'] . " AND time < " . ($_POST['timestamp'] + 3600) . " GROUP BY app_short, mobility";
	$result2 = $mysqli->query($sql2);

	$temp2 = array();
	if ($result2->num_rows > 0) {
	    // output data of each row
	    while($row2 = $result2->fetch_assoc()) {
	        array_push($temp2, $row2);
	    }
	} else {

	}

	//array_push($temp, array("outerKey"=>array("innerKey"=>"innerValue")));
	//echo json_encode(array("status"=>$temp));

	// we are returning a large array with 2 inner arrays
	// 1st inner array: lat long data with mobility status
	// 2nd inner array: list of application names and count and mobility (can have Facebook = home and Facebook = work)

	// sql command to get the count of unique app_short from september_first_half given condition
	// select app_short, COUNT(*) AS `num` from september_first_half WHERE deviceId=84756 GROUP BY app_short;

	// sql command to get count of unique deviceId/mobility pairs
	// select deviceId, mobility, COUNT(*) AS `count` from september_mobile_signal GROUP BY deviceId, mobility ORDER BY mobility;


	echo json_encode(array("status"=>array("map"=>$temp, "tree"=>$temp2)));

	$mysqli->close();
}

?>