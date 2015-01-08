package test.client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class DelaunayTriangulation {
	int[][] adjMatrix;
	List<GraphEdge> graphList;

	DelaunayTriangulation(int size) {
		this.adjMatrix = new int[size][size];
		this.graphList = new ArrayList<GraphEdge>();
	}

	public int[][] getAdj() {
		return this.adjMatrix;
	}

	public HashSet getEdges(int n, int[] x, int[] y, int[] z) {
		HashSet result = new HashSet();

		if (n == 2) {
			this.adjMatrix[0][1] = 1;
			this.adjMatrix[1][0] = 1;
			int randomVal = (int) (Math.random() * 100);
			result.add(new GraphEdge(new GraphPoint(x[0], y[0]),
					new GraphPoint(x[1], y[1]), randomVal));

			return result;
		}

		for (int i = 0; i < n - 2; i++) {
			for (int j = i + 1; j < n; j++) {
				for (int k = i + 1; k < n; k++) {
					if (j == k) {
						continue;
					}
					int xn = (y[j] - y[i]) * (z[k] - z[i]) - (y[k] - y[i])
							* (z[j] - z[i]);

					int yn = (x[k] - x[i]) * (z[j] - z[i]) - (x[j] - x[i])
							* (z[k] - z[i]);

					int zn = (x[j] - x[i]) * (y[k] - y[i]) - (x[k] - x[i])
							* (y[j] - y[i]);
					boolean flag;
					if (flag = (zn < 0 ? 1 : 0) != 0) {
						for (int m = 0; m < n; m++) {
							flag = (flag)
									&& ((x[m] - x[i]) * xn + (y[m] - y[i]) * yn
											+ (z[m] - z[i]) * zn <= 0);
						}

					}

					if (!flag) {
						continue;
					}

					int randomVal = (int) (Math.random() * 100);
					GraphEdge graphEdge = new GraphEdge(new GraphPoint(x[i],
							y[i]), new GraphPoint(x[j], y[j]), randomVal);
					if (checkDuplicate(graphEdge)) {
						result.add(graphEdge);
						graphList.add(graphEdge);
					}

					randomVal = (int) (Math.random() * 100);
					graphEdge = new GraphEdge(new GraphPoint(x[j], y[j]),
							new GraphPoint(x[k], y[k]), randomVal);
					if (checkDuplicate(graphEdge)) {
						result.add(graphEdge);
						graphList.add(graphEdge);
					}
					randomVal = (int) (Math.random() * 100);
					graphEdge = new GraphEdge(new GraphPoint(x[k], y[k]),
							new GraphPoint(x[i], y[i]), randomVal);
					if (checkDuplicate(graphEdge)) {
						result.add(graphEdge);
						graphList.add(graphEdge);
					}

					this.adjMatrix[i][j] = 1;
					this.adjMatrix[j][i] = 1;
					this.adjMatrix[k][i] = 1;
					this.adjMatrix[i][k] = 1;
					this.adjMatrix[j][k] = 1;
					this.adjMatrix[k][j] = 1;
				}

			}

		}

		return result;
	}

	private boolean checkDuplicate(GraphEdge graphEdge) {
		for (GraphEdge gE : graphList) {
			if (gE.a.x == graphEdge.a.x && gE.a.y == graphEdge.a.y
					&& gE.b.x == graphEdge.b.x && gE.b.y == graphEdge.b.y)
				return false;
			if (gE.b.x == graphEdge.a.x && gE.b.y == graphEdge.a.y
					&& gE.a.x == graphEdge.b.x && gE.a.y == graphEdge.b.y)
				return false;
		}
		return true;

	}

	public HashSet getEdges(HashSet XYPointsSet) {
		if ((XYPointsSet != null) && (XYPointsSet.size() > 0)) {
			int n = XYPointsSet.size();

			int[] x = new int[n];
			int[] y = new int[n];
			int[] z = new int[n];

			int i = 0;

			Iterator iterator = XYPointsSet.iterator();
			while (iterator.hasNext()) {
				XYPoint XYPoint = (XYPoint) iterator.next();

				x[i] = (int) XYPoint.getX();
				y[i] = (int) XYPoint.getY();
				z[i] = (x[i] * x[i] + y[i] * y[i]);

				i++;
			}

			return getEdges(n, x, y, z);
		}

		return null;
	}
}