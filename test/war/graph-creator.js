function runD3code(nodes,links,directed,weighted) {
	
	d3.select("svg").remove();
	var svgContainer = d3.select("#graph").append("svg")
                                    .attr("width", 700)
                                     .attr("height", 700);
	
	var topx = 20;
	var topy = 20;
	/*var nodes = [
    {x: 10, y: 50, label:1},
    {x: 70, y: 10, label:2},
    {x: 140, y: 50, label:3},
	{x: 240, y: 250, label:4}	
	]*/

	/*var links = [
    {source: nodes[0], dst: nodes[1]},
	{source: nodes[1], dst: nodes[2]},
	{source: nodes[2], dst: nodes[3]},
	{source: nodes[0], dst: nodes[4]}
	]*/
	
	
	if(directed){
		// define marker
	svgContainer.append("svg:defs").selectAll("marker")
    .data(["arrow"])
	.enter().append("svg:marker")
    .attr("id", String)
    .attr("viewBox", "3 -5 10 10")
    .attr("refX", 20)
    .attr("refY", 0)
    .attr("markerWidth", 20)
    .attr("markerHeight", 15)
    .attr("orient", "auto")
    .append("svg:path")
    .attr("d", "M0,-5L10,0L0,5");
	}
	 svgContainer.selectAll("g")
	.data(links)
	.enter()
	.append("line")
    .attr("x1", function(d) { return topx+d.x1; })
    .attr("y1", function(d) { return topy+d.y1; })
    .attr("x2", function(d) { return topx+d.x2; })
    .attr("y2", function(d) { return topy+d.y2; })
    .style("stroke", "black")
     .attr("class", "link arrow")
    .attr("marker-end", "url(#arrow)");
	
	 if(weighted){
	 svgContainer.selectAll("g").data(links).enter()
		.append("text")
		.attr("transform", function(d) {
			var deltaY = d.y2 - d.y1;
			var deltaX = d.x2 - d.x1;
			var angleInDegrees = Math.atan2(deltaY, deltaX) * 180 / Math.PI;
		 return "translate("+(d.x1+topx+d.x2+topx)/2+","+(d.y1+topy+d.y2+topy	)/2+")" +
				"rotate(" + (0) + ")";
			})
		.text(function(d) { return d.label; });
		
	 }
	 
	svgContainer.selectAll("g")
   .data(nodes)
   .enter()
   .append("circle")
   .attr("cx", function(d) { return topx+d.x; })
   .attr("cy", function(d) { return topy+d.y; })
   .attr("r", "15")
   .style("stroke", "black")
   .style("fill","white")
   .style("stroke-width","3");
   
	svgContainer.selectAll("g")
	   .data(nodes)
	   .enter()
	   .append("text").attr("x", function(d) {return topx+d.x-4; })
	                 .attr("y", function(d) { return topy+d.y+4; })
	                 .text( function (d) { return d.label; })
	                 .attr("font-family", "sans-serif")
	                 .attr("font-size", "12px")
	                 .attr("fill", "black");
	
  					 
};