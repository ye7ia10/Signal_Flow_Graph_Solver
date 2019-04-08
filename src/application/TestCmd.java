package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestCmd {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 GraphBuilder graph = new GraphBuilder();
		 graph.buildGraph(0, 1, 1);
		 graph.buildGraph(1, 2, 1);
		 graph.buildGraph(2, 3, 1);
		 graph.buildGraph(3, 4, 1);
	     graph.buildGraph(4, 5, 1);
	     graph.buildGraph(3, 3, -1);
	     graph.buildGraph(3, 2, -1);
	     graph.buildGraph(2, 1, -1);
	     graph.buildGraph(1, 3, 1);
	     graph.buildGraph(1, 4, 1);
	    /* graph.buildGraph(6, 7, 1);
	     graph.buildGraph(7, 8, 1);
	     graph.buildGraph(5, 2, 1);
	     graph.buildGraph(7, 4, 1);
	     graph.buildGraph(8, 6, 1);*/
	    /* graph.buildGraph(5, 6, 5);
	     graph.buildGraph(6, 7, 5);
	     graph.buildGraph(7, 8, 5);
	     graph.buildGraph(7, 4, 5);
	     graph.buildGraph(8, 6, 5);
	     graph.buildGraph(5, 2, 5);*/
	     /*graph.buildGraph(3, 1, 4);
	     graph.buildGraph(4, 8, 5);
	     graph.buildGraph(4, 5, 5);
	     graph.buildGraph(5, 6, 5);
	     graph.buildGraph(6, 7, 5);
	     graph.buildGraph(6, 1, 4);
	     graph.buildGraph(7, 1, 4);
	     graph.buildGraph(7, 5, 4);
	     graph.buildGraph(7, 3, 4);*/
	     //graph.buildGraph(8, 4, 4);
	     //graph.buildGraph(8, 6, 4);
	     graph.setEndNode(5);
	     graph.getForwardPaths(0);
	    // HashMap<Integer, ArrayList<Edge>> graph2 = graph.getGraph();
	     /*CycleAnalysis analysis = new CycleAnalysis();
	     ArrayList<ArrayList< Integer > > scc = analysis.getSCComponents(graph2);
         System.out.println(scc);*/
         
         System.out.println("loooops");
         List<List<Integer> > tt = graph.SCC();
         System.out.println(tt);
         
         graph.makeNontouched();
         //System.out.println("non loops");
         //System.out.println(graph.getNontouched().get(3));
         //System.out.println("loop 1 : " + cycles.get_Weight(cycles.findSimpleCycles(), 3));
         graph.getTouchedWithPath();
         System.out.println("teeeest" + graph.getTheTouchedWithPath());
         System.out.println("Delta " );
         graph.calcDelta();
         System.out.println("Total Transfer function");
         System.out.println(graph.calcTF()); 
	}
	

}
