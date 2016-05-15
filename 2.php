<!DOCTYPE html>
<html>
	<head>
	</head>
	<body>
		<?php
			ini_set('display_errors', 'on');

			// global variables!!!
			$startTimestamp = 1441090800; // 09-01-2015 00:00:00
			$endTimestamp = 1443682800; // 10-01-2015 00:00:00
			$leftBound = $startTimestamp;
			$rightBound = $leftBound + (60 * 60 - 1);
			$arrayForHour = array();

			$conn = new PDO("mysql:host=localhost; dbname=allgrads", 'root', 'root');
			$sql = "SELECT * FROM mobile_signal_info ORDER BY time";

			foreach($conn->query($sql) as $row) {
				// convert date to timestamp
				$currentRowTimestamp = strtotime($row['time']);

				if($currentRowTimestamp < $rightBound && $currentRowTimestamp >= $leftBound) {
					array_push($arrayForHour, $row);
				}
				else {
					// print out all elements in $arrayForHour
					echo "<p>" . date('m/d/Y H:i', $leftBound) . " - " . date('m/d/Y H:i', $rightBound) . "</p>";

					// $value = row in MySQL
					foreach($arrayForHour as $value) {

					}

					$uniqueIdArray = array();

					// update bounds until next time
					while($currentRowTimestamp > $rightBound) {
						$leftBound = $rightBound + 1;
						$rightBound = $leftBound + (60 * 60 - 1);
					}

					$arrayForHour = array();
					array_push($arrayForHour, $row);
				}
			}

			echo "<p>string</p>";

			$conn = null;
		?>
	</body>
</html>