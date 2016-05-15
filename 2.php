
		<?php
			ini_set('display_errors', 'on');

			// global variables!!!
			$startTimestamp = 1446361200; // 09-01-2015 00:00:00
			$endTimestamp = 1448956800; // 10-01-2015 00:00:00

			$leftBound = $startTimestamp;
			$rightBound = $leftBound + (60 * 60 - 1);
			$arrayForHour = array();
			$h = 1;

			$conn = new PDO("mysql:host=localhost; dbname=allgrads", 'root', 'root');
			$sql = "SELECT * FROM mobile_signal_info_november";

			foreach($conn->query($sql) as $row) {
				// convert date to timestamp
				$currentRowTimestamp = strtotime($row['time']);

				if($currentRowTimestamp < $rightBound && $currentRowTimestamp >= $leftBound) {
					array_push($arrayForHour, $row);
				}
				else {
					// print out all elements in $arrayForHour
					//echo "<p>" . date('m/d/Y H:i', $leftBound) . " - " . date('m/d/Y H:i', $rightBound) . "</p>";

					// $value = row in MySQL
					foreach($arrayForHour as $value) {
						$sql = "INSERT INTO modified (deviceId, hour, time, latitude, longitude) VALUES (" . $value['deviceId'] . ", " . $h . ", " . strtotime($value['time']) . ", " . $value['latitude'] . ", " . $value['longitude'] . ");";

						$conn->query($sql);
					}

					//$h += 1;

					$uniqueIdArray = array();

					// update bounds until next time
					while($currentRowTimestamp > $rightBound) {
						$leftBound = $rightBound + 1;
						$rightBound = $leftBound + (60 * 60 - 1);
						$h += 1;
					}

					$arrayForHour = array();
					array_push($arrayForHour, $row);
				}
			}

			$conn = null;
		?>