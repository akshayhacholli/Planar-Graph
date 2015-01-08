package test.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sun.java.swing.plaf.windows.resources.windows;

public class Home implements EntryPoint {

	private Button btn;
	private Button shuffleBtn;
	private TextBox textbox_vertex;
	private TextBox textbox_edge;
	private TextBox max_edge;
	private TextBox min_edge;
	CheckBox edgeWeighted = new CheckBox("Edge Weighted Graph");
	Label maxEdge = new Label("Maximum Edge Weight");
	Label minEdge = new Label("Minimum Edge Weight");
	CheckBox directed = new CheckBox("Directed");
	CheckBox dag = new CheckBox("DAG");

	private boolean enabled = true;
	static int i = 0;
	Set<XYPoint> globalSet;

	public static native void renderGraph(JsArray arr, JsArray edgeArr,boolean directed, boolean weighted) /*-{
		$wnd.runD3code(arr, edgeArr,directed,weighted);
	}-*/;

	private static void getXYCoordinates(XYPoint test, Random position,
			int maxx, int maxy, Set<XYPoint> set) {
		while (true) {
			test.x = position.nextInt(maxx);
			test.y = position.nextInt(maxy);
			boolean flag = true;
			for (XYPoint pnt : set) {
				if (distanceTo(test.x, test.y, pnt.x, pnt.y) < 60) {
					flag = false;
					break;
				}
			}
			if (flag) {
				return;
			}
		}
	}

	private static double distanceTo(int x, int y, int x2, int y2) {
		double dx = x - x2;
		double dy = y - y2;
		double distance = Math.sqrt(dx * dx + dy * dy);
		return distance;
	}

	private final native JavaScriptObject newEntry(int x, int y, int label)/*-{
		return {
			x : x,
			y : y,
			label : label
		};
	}-*/;

	private final native JavaScriptObject edgeEntry(int x1, int y1, int x2,
			int y2, int weight)/*-{
		return {
			x1 : x1,
			y1 : y1,
			x2 : x2,
			y2 : y2,
			label : weight
		};
	}-*/;

	private void enableTextBox(boolean enable, boolean isChecked) {
		enable = (enable && isChecked) || (!enable && !isChecked);
		max_edge.setStyleDependentName("disabled", !enable);
		max_edge.setEnabled(enable);
		min_edge.setStyleDependentName("disabled", !enable);
		min_edge.setEnabled(enable);
	}

	public void onModuleLoad() {
		textbox_vertex = new TextBox();
		textbox_edge = new TextBox();
		max_edge = new TextBox();
		min_edge = new TextBox();
		max_edge.setEnabled(false);
		min_edge.setEnabled(false);
		btn = new Button("Generate Graph", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				boolean edgeOption = false;
				int edgeCount = 10;
				if (textbox_vertex.getValue().matches("-?\\d+(\\.\\d+)?")) {
					if (Integer.parseInt(textbox_vertex.getValue()) > 3
							&& Integer.parseInt(textbox_vertex.getValue()) <= 30) {
						JsArray vertexArray;
						JsArray edgeArray;

						int maxx = 500;
						int maxy = 500;

						int vertexCount = Integer.parseInt(textbox_vertex
								.getValue());

						if (!textbox_edge.getValue().equals("")) {
							if (!textbox_edge.getValue().matches(
									"-?\\d+(\\.\\d+)?")) {
								Window.alert("Please give a valid edge number");
								return;
							}
							edgeCount = Integer.parseInt(textbox_edge
									.getValue());
							if (edgeCount <= 0
									|| edgeCount > (3 * (vertexCount - 2))) {
								Window.alert("Edges should be between 1 and 3(n-2)");
								return;
							}
							edgeOption = true;

						}

						HashSet edgeSet;
						do {
							vertexArray = JavaScriptObject.createArray().cast();
							edgeArray = JavaScriptObject.createArray().cast();
							Set<XYPoint> set = new HashSet<XYPoint>();
							Random position = new Random();
							XYPoint test;
							do {
								test = new XYPoint();
								getXYCoordinates(test, position, maxx, maxy,
										set);

								set.add(test);
							} while (set.size() < vertexCount);

							List<Object> list = new ArrayList<Object>(set);
							Object[] coord = list.toArray();
							HashSet tree = new HashSet();
							int i = 1;
							for (XYPoint pt : set) {
								tree.add(pt);
								vertexArray.push(newEntry(pt.x, pt.y, i++));
							}

							DelaunayTriangulation del = new DelaunayTriangulation(
									vertexCount);

							edgeSet = del.getEdges(tree);
							globalSet = set;
							if (!edgeOption)
								break;
						} while (edgeSet.size() < edgeCount);

						int k = 0;
						for (Object ge : edgeSet) {
							GraphEdge edge = (GraphEdge) ge;
							edgeArray.push(edgeEntry(edge.a.x, edge.a.y,
									edge.b.x, edge.b.y,edge.weight));
							if (edgeOption) {
								if (++k == edgeCount)
									break;
							}
						}

						renderGraph(vertexArray, edgeArray, directed.getValue(),edgeWeighted.getValue());

					} else {
						Window.alert("Number of vertices should be between 3 and 30");
					}
				} else {
					Window.alert("Please provide a valid vertex number");
				}
			}
		});
		shuffleBtn = new Button("Shuffle Edges", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

			}

		});

				
		edgeWeighted.setValue(false);
		edgeWeighted.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				enableTextBox(enabled, edgeWeighted.getValue());

			}
		});

		Label vertexLabel = new Label("Enter Number of Vertices");
		Label edgeLabel = new Label("Enter Number of Edges");

		VerticalPanel panel = new VerticalPanel();
		panel.setSpacing(100);
		panel.add(vertexLabel);
		panel.add(textbox_vertex);
		panel.add(edgeLabel);
		panel.add(textbox_edge);
		panel.add(directed);
		panel.add(dag);
		panel.add(edgeWeighted);
		panel.add(minEdge);
		panel.add(min_edge);
		panel.add(maxEdge);
		panel.add(max_edge);

		panel.add(btn);
		DecoratorPanel decoratorPanel = new DecoratorPanel();
		decoratorPanel.add(panel);

		RootPanel.get("test1").add(decoratorPanel);

		VerticalPanel shufflePanel = new VerticalPanel();
		shufflePanel.add(shuffleBtn);
		RootPanel.get("shuffle").add(shufflePanel);
	}
}
