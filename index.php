<!DOCTYPE html>
<html>
	<head>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.3/jquery.min.js"></script>
		<style>
	      html, body {
	        height: 100%;
	        margin: 0;
	        padding: 0;
	      }
	      #map {
	        height: 400px;
	        width: 400px;
	      }
	    </style>
	</head>
	<body>
		<div id="map"></div>
	    <script>
	      var map;
	      function initMap() {
	        map = new google.maps.Map(document.getElementById('map'), {
	          center: {lat: 40.795358, lng: -73.95627},
	          zoom: 11
	        });

	        var marker1 = new google.maps.Marker({
	        	position: {lat: 40.795358, lng: -73.95627},
	        	map: map,
	        	icon: "http://maps.google.com/mapfiles/ms/icons/green-dot.png"
	        });

	        var marker2 = new google.maps.Marker({
	        	position: {lat: 40.805358, lng: -73.95627},
	        	map: map,
	        	icon: "http://maps.google.com/mapfiles/ms/icons/red-dot.png"
	        });

	        var marker3 = new google.maps.Marker({
	        	position: {lat: 40.785358, lng: -73.95627},
	        	map: map,
	        	icon: "http://maps.google.com/mapfiles/ms/icons/blue-dot.png"
	        });
	      }
	    </script>
		<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAhOGbVuv-zvQzhnOeDNRlC8289-VzR6rw&callback=initMap" async defer></script>


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
					console.log(data.status[3].outerKey.innerKey);
				}).fail(function(jqXHR, textStatus, errorThrown) {
                    console.log("failed postSubmission: " + textStatus + " " + errorThrown);
                });
			}
		</script>
	</body>
</html>