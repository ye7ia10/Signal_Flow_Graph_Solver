package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class CycleAnalysis {

	private int maxsize = 100;
	private int preCount = 0;
	private int V;
	private HashMap<Integer, ArrayList<Edge>> graph = new HashMap<Integer, ArrayList<Edge>>();
	private ArrayList<ArrayList<Integer>> sccComp = new ArrayList<>();
	private Stack<Integer> stack = new Stack<>();
	boolean visited[] = new boolean[maxsize];
	int low[] = new int[maxsize];

	public ArrayList<ArrayList<Integer>> getSCComponents(HashMap<Integer, ArrayList<Edge>> graph) {
		V = graph.size();
		this.graph = graph;
		stack = new Stack<Integer>();
		sccComp = new ArrayList<>();

		for (int v = 1; v <= V; v++)
			if (!visited[v])
				tarjan(v);

		return sccComp;
	}

	public void tarjan(int v) {
		low[v] = preCount++;
		visited[v] = true;
		stack.push(v);
		int min = low[v];
		if (graph.containsKey(v))
			for (int i = 0; i < graph.get(v).size(); i++) {
				int w = graph.get(v).get(i).end;
				if (!visited[w])
					tarjan(w);
				if (low[w] < min)
					min = low[w];
			}

		if (min < low[v]) {
			low[v] = min;
			return;
		}
		List<Integer> component = new ArrayList<Integer>();
		int w;
		do {
			w = stack.pop();
			component.add(w);
			low[w] = V;
		} while (w != v);
		sccComp.add((ArrayList<Integer>) component);
	}

}