$( document ).ready(function() {

	var w = 960,
    h = 700,
    p = 15;
	
	d3.select("#graph").selectAll("*").remove();
	var vis = d3.select("#graph").append("svg").attr("width", w + p * 2).attr("height", h + p * 2).append("g").attr("transform", "translate(" + [p, p] + ")");

	var lines = vis.append("g"),
		nodes = vis.append("g");

	var vertices = new Array();
	var edges = new Array();
	
	$('#gen_graph').click(function() {
		n = parseInt($('#vertices').val());
		if(!n || n < 1 || n > 30)
		{
			alert("Number of vertices must be 0-30");
			return;
		}
		
		generate_vertices(n);
		print_graph();
		
	});
	
	function generate_vertices( n )
	{
		vertices = new Array();
		edges = new Array();
		
		for( var i=0; i<n; i++)
			vertices[i] = get_vertex(0,0);
			
		for( var i=0; i<n; i++)
			for( var j=i+1; j<n; j++)
			{
				if( !intersect ( vertices [i], vertices [j] ) )
					edges.push ( [ vertices [i], vertices [j] ] );
			}
	}
	
	function print_graph()
	{
		var line = lines.selectAll("line").data(edges);
			line.enter().append("line");
			line.exit().remove();
			line.attr("x1", function(d) { return d[0][0]; }).attr("y1", function(d) { return d[0][1]; }).attr("x2", function(d) { return d[1][0]; })
			.attr("y2", function(d) { return d[1][1]; }).attr("class","intersection");

		var node = nodes.selectAll("circle").data(vertices);
			node.enter().append("circle").attr("r", p - 1);
			node.exit().remove();
			node.attr("cx", function(d) { return d[0]; }).attr("cy", function(d) { return d[1]; }).attr("class","intersection");
	}
	
	function intersect ( p1, p2 )
	{
		for ( var i = 0; i < edges.length; i++ )
		{
			var p3 = edges[i][0],
				p4 = edges[i][1],
				r = [ p2 [0] - p1 [0], p2 [1] - p1 [1] ],
				s = [ p4 [0] - p3 [0], p4 [1] - p3 [1] ],
				rxs = cross(r, s),
				q_p = [p3[0] - p1[0], p3[1] - p1[1]],
				t = cross(q_p, s) / rxs,
				u = cross(q_p, r) / rxs,
				epsilon = 1e-6;
			
			if ( t > epsilon && t < 1 - epsilon && u > epsilon && u < 1 - epsilon )
				return true;
		}
		
		console.log( "( " + p1[0] + "," , p1[1] + " ) and ( " + p2[0] + "," , p2[1] + " )");
		return false;
	}
	
	function cross(a, b)
	{
		return a[0] * b[1] - a[1] * b[0];
	}
	
	function get_vertex(x,y)
	{
		if ( x == 0 )
		{
			x = Math.round(Math.random() * 900) + 1;
		}
		if ( y == 0 )
		{
			y = Math.round(Math.random() * 700) + 1;
		}
		
		for ( var i = 0; i < vertices.length; i++ )
		{
			if ( Math.abs( vertices[i][0] - x ) < 6 )
				return get_vertex ( 0, y );
			if ( Math.abs( vertices[i][1] - y ) < 6  )
				return get_vertex ( x, 0 );
		}
		
		console.log(x,y);
		return ( [ x, y ] );
	}
});