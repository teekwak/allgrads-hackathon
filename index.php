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
	    </style>
	</head>
	<body>
		<div style="text-align: center; width=100%;">
			<h1 style="margin-bottom: 0;">App Usage by Motion</h1>
		</div>

		<div class="container">
			<div id="map"></div>

			<table>
				<tr>
					<td style="z-index:100; top:50px; position: absolute; right: 340px;">
						<input class="stringTime" type="text" value="2015-09-01 04:00:00"/>
						<button onclick="submitStringTime();">Submit</button>
					</td>
				</tr>
				<tr>
					<td>
						<div id="treemap"></div>
					</td>
				</tr>
			</table>
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

		<!-- Google Maps stuff -->
		<!-- Google Maps stuff -->
		<!-- Google Maps stuff -->

	    <script>
	    	// google maps stuff
	    	var map;
	      	function initMap() {
	        	map = new google.maps.Map(document.getElementById('map'), {
	         	 center: {lat: 40.795358, lng: -73.95627},
	          	zoom: 11
	        	});

	        	var cityLatLong = [{lat: 40.795358, lng: -73.95627, mobility:"c"}, {lat: 40.805358, lng: -73.95627, mobility:"w"}, {lat: 40.785358, lng: -73.95627, mobility:"h"}];

		        for(var x in cityLatLong) {
		        	var actualColor = "";

		        	if(cityLatLong[x].mobility == "c") {
		        		actualColor = "#ff6600";
		        	}
		        	else if(cityLatLong[x].mobility == "w") {
    					actualColor = "#00ff00";
		        	}
		        	else if(cityLatLong[x].mobility == "h"){
		        		actualColor = "#ff0000";
		        	}

		      		var cityCircle = new google.maps.Circle({
					   	strokeColor: '#000000',
					   	strokeOpacity: 0.8,
					   	strokeWeight: 1,
					   	fillColor: actualColor,
					   	fillOpacity: 0.8,
					   	map: map,
					   	center: cityLatLong[x],
					   	radius: 110
					});
		        }
	    	}
	    </script>
		<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAhOGbVuv-zvQzhnOeDNRlC8289-VzR6rw&callback=initMap" async defer></script>

		<!-- my stuff -->
		<!-- my stuff -->
		<!-- my stuff -->

		<script>
			// my stuff
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