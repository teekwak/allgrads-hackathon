<!DOCTYPE html>
<html>
	<head>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.3/jquery.min.js"></script>
		<script src="https://d3js.org/d3.v3.min.js" charset="utf-8"></script>
		<style>
	      html, body {
	        height: 100%;
	        margin: 0;
	        padding: 0;
	      }
	      #map {
	        height: 500px;
	        width: 500px;
	      }
	      body {
			  font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
			  margin: auto;
			  position: relative;
			}

			form {
			  position: absolute;
			  right: 10px;
			  top: 10px;
			}

			.node {
			  border: solid 1px white;
			  font: 10px sans-serif;
			  line-height: 12px;
			  overflow: hidden;
			  position: absolute;
			  text-indent: 2px;
			}

			.container {
				text-align: center;
			}
			.container div, .container table {
				display: inline-block;
			}

			#treemap {
				margin-top: -30px;
				z-index: -1;
			}
			#currentDate {
				margin-left: 10px;
				margin-right: 10px;
			}

			.legend div, .legend div>div{
				display: inline-block;
			}
	    </style>
	</head>
	<body>
		<div style="text-align: center; width=100%;">
			<h1>App Usage by Motion</h1>
		</div>

		<div class="container">
			<div id="map"></div>

			<table>
				<tr>
					<td style="position: relative; z-index:100; margin-bottom: -50px; width: 100%; text-align: center;">
						<button onclick="buttonPress(-1);"> - </button>
						<span id="currentDate">Tue Sep 01 2015 00:00:00 GMT-0700 (PDT)</span>
						<button onclick="buttonPress(1);"> + </button>
					</td>
				</tr>
				<tr>
					<td style="position: relative; z-index:-1;">
						<div id="treemap"></div>
					</td>
				</tr>
			</table>
		</div>

		<div class="legend" style="width: 100%; text-align: center; margin: 15px;">
			<div>
				<div style="background-color: #ff6600; width: 15px; height: 15px;"></div> Commuting
			</div>
			<div style="margin-left: 15px; margin-right: 15px;">
				<div style="background-color: #00ff00; width: 15px; height: 15px;"></div> Work
			</div>
			<div>
				<div style="background-color: #ff0000; width: 15px; height: 15px;"></div> Home
			</div>
		</div>

		<div style="width: 100%; text-align: center;">
			<h3>Number of data points on map: <span id="dataPointCount">0</span></h3>
		</div>


		<!-- D3 stuff -->
		<!-- D3 stuff -->
		<!-- D3 stuff -->

		<script>
			// d3 stuff
		var margin = {top: 40, right: 10, bottom: 10, left: 10},
		    width = 480 - margin.left - margin.right,
		    height = 500 - margin.top - margin.bottom;

		//var color = d3.scale.category20c();

		var treemap = d3.layout.treemap()
		    .size([width, height])
		    .sticky(true)
		    .value(function(d) { return d.count; });

		var div = d3.select("#treemap").append("div")
		    .style("position", "relative")
		    .style("width", (width + margin.left + margin.right) + "px")
		    .style("height", (height + margin.top + margin.bottom) + "px")
		    .style("left", margin.left + "px")
		    .style("top", margin.top + "px")
		    .style("z-index", -1);

		d3.json("data.json", function(error, root) {
		  if (error) throw error;

		  var node = div.datum(root).selectAll(".node")
		      .data(treemap.nodes)
		      .enter().append("div")
		      .attr("class", "node")
		      .call(position)
		      .style("background", function(d) { return d.children ? null : d.color; })
		      .text(function(d) { return d.children ? null : d.name; }); // if node does not have children, show
		});

		function position() {
		  this.style("left", function(d) { return d.x + "px"; })
		      .style("top", function(d) { return d.y + "px"; })
		      .style("width", function(d) { return Math.max(0, d.dx - 1) + "px"; })
		      .style("height", function(d) { return Math.max(0, d.dy - 1) + "px"; });
		}

		</script>

	    <!--
		<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAhOGbVuv-zvQzhnOeDNRlC8289-VzR6rw&callback=initMap" async defer></script>
		-->
		<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAhOGbVuv-zvQzhnOeDNRlC8289-VzR6rw"></script>

		<!-- my stuff -->
		<!-- my stuff -->
		<!-- my stuff -->

		<script>
			var map;
	    	var latLongPairs = [];

			// my stuff
			var monthNames = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];

			// INPUT MUST BE MILLISECONDS!
			function submitStringTime(timestamp) {
				var month = new Date(timestamp).getMonth();

				$.ajax({
					type: "post",
					url: "http://localhost:9000/getData.php",
					data: {
						action: "processTimestamp",
						month: monthNames[month].toLowerCase(),
						timestamp: timestamp/1000 // send in seconds because PHP handles seconds
					}
				}).success(function(data) {
					//console.log("status: " + data.status); // returned as EPOCH timestamp (IN SECONDS)
					latLongPairs = [];
					totalCount = 0;

					console.log(data);
					// TRIM each element when they come back

					for(var x in data.status) {
						latLongPairs.push({lat:data.status[x].latitude, lng:data.status[x].longitude, mobility:data.status[x].mobility, count:data.status[x].count});
					}

					map = new google.maps.Map(document.getElementById('map'), {
		         		center: {lat: 40.95604846533964, lng: -73.81782531738281},
		          		zoom: 11
		        	});

		        	//latLongPairs = [{lat: 40.795358, lng: -73.95627, mobility:" Commuting"}, {lat: 40.805358, lng: -73.95627, mobility:" Work"}, {lat: 40.785358, lng: -73.95627, mobility:" Home"}];

		        	console.log("latLongPairs length: " + latLongPairs.length);

			        for(var x in latLongPairs) {
			        	var actualColor = "";

			        	if(latLongPairs[x].mobility.trim() == "Commuting") {
			        		actualColor = "#ff6600";
			        	}
			        	else if(latLongPairs[x].mobility.trim() == "Work") {
	    					actualColor = "#00ff00";
			        	}
			        	else if(latLongPairs[x].mobility.trim() == "Home"){
			        		actualColor = "#ff0000";
			        	}

			      		var cityCircle = new google.maps.Circle({
						   	strokeColor: '#000000',
						   	strokeOpacity: 0.8,
						   	strokeWeight: 1,
						   	fillColor: actualColor,
						   	fillOpacity: 0.8,
						   	map: map,
						   	center: {lat: parseFloat(latLongPairs[x].lat), lng: parseFloat(latLongPairs[x].lng)},
						   	radius: 200 * (parseInt(latLongPairs[x].count) * 0.2)
						   	// radisu: 200
						});

			      		totalCount += parseInt(latLongPairs[x].count);
			        }

			        $('#dataPointCount').text(totalCount);

				}).fail(function(jqXHR, textStatus, errorThrown) {
                    console.log("failed postSubmission: " + textStatus + " " + errorThrown);
                });
			}

			function buttonPress(increment) {
				// check if time is below wanted interval
				if(new Date($('#currentDate').text()).getTime() + (increment * 3600000) < 1441090800000) {
					return;
				}

				// create timestamp
				var ts = new Date($('#currentDate').text()).getTime();

				// increment/decrement timestamp
				if(increment == 1) {
					ts += 3600000;
				} else if(increment == -1) {
					ts -= 3600000;
				}

				$('#currentDate').text(new Date(ts));

				submitStringTime(ts);
			}

			submitStringTime(new Date($('#currentDate').text()).getTime());
		</script>
	</body>
</html>