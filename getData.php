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

function processTimestamp() {
	echo json_encode(array("status" => $_POST['timestamp']));
}

?>