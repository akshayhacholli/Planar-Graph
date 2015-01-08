package test.client;

public class GraphEdge {
	GraphPoint a;
	GraphPoint b;
	int weight;
	GraphEdge(GraphPoint a,GraphPoint b,int weight)
	{
		this.a=a;
		this.b=b;
		this.weight = weight;
	}
}
