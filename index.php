<!DOCTYPE html>
<html>
	<head>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.3/jquery.min.js"></script>
	</head>
	<body>
		<input class="stringTime" type="text" value="2015-09-01 04:00:00"/>
		<button onclick="submitStringTime();">Submit</button>

		<script>
			var monthNames = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];

			function submitStringTime() {
				// get string from input box
				var text = $('.stringTime').val().replace(/-/g, '/');

				// get month number for picking table later
				var mon = new Date(text).getMonth();

				// convert string to timestamp
				var ts = new Date(text).getTime() / 1000;

				$.ajax({
					type: "post",
					url: "http://localhost:9000/getData.php",
					data: {
						action: "processTimestamp",
						month: monthNames[mon].toLowerCase(),
						timestamp: ts
					}
				}).success(function(data) {
					console.log("success!");
					console.log(data);
				}).fail(function(jqXHR, textStatus, errorThrown) {
                    console.log("failed postSubmission: " + textStatus + " " + errorThrown);
                });
			}
		</script>
	</body>
</html>