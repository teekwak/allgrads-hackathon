<!DOCTYPE html>
<html>
	<head>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.3/jquery.min.js"></script>
	</head>
	<body>
		<input class="stringTime" type="text" value="2015-09-01 04:00:00"/>
		<button onclick="submitStringTime();">Submit</button>

		<script>
			function submitStringTime() {
				var text = $('.stringTime').val().replace(/-/g, '/');

				text = new Date(text).getTime() / 1000;
/*
				$.ajax({
					type: "post",
					url: "http://localhost:9000/getData.php",
					data: {
						action: "processTimestamp",
						timestamp: text
					}
				}).success(function(data) {
					console.log("success! " + data.status);
				}).fail(function(jqXHR, textStatus, errorThrown) {
                    console.log("failed postSubmission: " + textStatus + " " + errorThrown);
                });
			}
*/
				$.ajax({
					type: "post",
					url: "http://localhost:9000/2.php",
					data: {}
				}).success(function(data) {
					console.log("success: " + data.status);
				}).fail(function(jqXHR, textStatus, errorThrown) {
                    console.log("failed: " + textStatus + " " + errorThrown);
                });

		</script>
	</body>
</html>