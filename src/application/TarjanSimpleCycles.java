package application;

import java.util.*;
public class TarjanSimpleCycles{
	private HashMap<Integer, ArrayList<Edge>> graph = new HashMap<Integer, ArrayList<Edge> >();

    private List<List<Integer>> cycles;
    private Set<Integer> marked;
    private ArrayDeque<Integer> markedStack;
    private ArrayDeque<Integer> pointStack;
    private Map<Integer, Integer> vToI;
    private Map<Integer, Set<Integer>> removed;
    
    public TarjanSimpleCycles(HashMap<Integer, ArrayList<Edge>> graph)
    {
        this.graph = graph;
    }
    
    public List<List<Integer>> findSimpleCycles()
    {
        if (graph == null) {
            throw new IllegalArgumentException("Null graph.");
        }
        initState();
        for (Map.Entry<Integer, ArrayList<Edge>> start : graph.entrySet()) {
        	backtrack(start.getKey(), start.getKey());
            while (!markedStack.isEmpty()) {
                marked.remove(markedStack.pop());
            }
		}

        List<List<Integer>> result = cycles;
        clearState();
        return result;
    }

    private boolean backtrack(Integer start, Integer vertex)
    {
    	 boolean foundCycle = false;
    	try {
       
        pointStack.push(vertex);
        marked.add(vertex);
        markedStack.push(vertex);
        if(graph.containsKey(vertex))
			for(int i = 0; i < graph.get(vertex).size(); i++) {
				Edge e = graph.get(vertex).get(i);
	            Integer currentVertex = e.end;
	            if (getRemoved(vertex).contains(currentVertex)) {
	                continue;
	            }
	            int comparison = toI(currentVertex).compareTo(toI(start));
	            if (comparison < 0) {
	                getRemoved(vertex).add(currentVertex);
	            } else if (comparison == 0) {
	                foundCycle = true;
	                List<Integer> cycle = new ArrayList<>();
	                Iterator<Integer> it = pointStack.descendingIterator();
	                Integer v;
	                while (it.hasNext()) {
	                    v = it.next();
	                    if (start.equals(v)) {
	                        break;
	                    }
	                }
	               
	                cycle.add(start);
	                while (it.hasNext()) {
	                    cycle.add(it.next());
	                }
	                //Collections.sort(cycle);
	                cycles.add(cycle);
	            } else if (!marked.contains(currentVertex)) {
	                boolean gotCycle = backtrack(start, currentVertex);
	                foundCycle = foundCycle || gotCycle;
	            }
        }

        if (foundCycle) {
            while (!markedStack.peek().equals(vertex)) {
                marked.remove(markedStack.pop());
            }
            marked.remove(markedStack.pop());
        }

        pointStack.pop();
        return foundCycle;
     } catch (Exception e) {
		// TODO: handle exception
	 }
    	 return foundCycle;
    }

    private void initState()
    {
        cycles = new ArrayList<>();
        marked = new HashSet<>();
        markedStack = new ArrayDeque<>();
        pointStack = new ArrayDeque<>();
        vToI = new HashMap<>();
        removed = new HashMap<>();
        int index = 0;
        for (Map.Entry<Integer, ArrayList<Edge>> start : graph.entrySet()) {
        	vToI.put(start.getKey(), index++);
		}
    }

    private void clearState()
    {
        cycles = null;
        marked = null;
        markedStack = null;
        pointStack = null;
        vToI = null;
    }

    private Integer toI(Integer v)
    {
        return vToI.get(v);
    }

    private Set<Integer> getRemoved(Integer v)
    {
        
        return removed.computeIfAbsent(v, k -> new HashSet<>());
    }
    public int get_Weight (List<List<Integer>> AllCyc, int index) {

        List < Integer > tempCycle = AllCyc.get(index);
        int weight = 1;
        for (int i = 0; i < tempCycle.size() - 1; i++) {

            int from = tempCycle.get(i);
            int to  = tempCycle.get(i+1);

            ArrayList<Edge> temp = graph.get(from);

            for (int j = 0; j < temp.size(); j++) {
                if ( temp.get(j).end == to) {
                    weight *= temp.get(j).weight; 
                    break;
                }
            }

        }
        ArrayList<Edge> temp = graph.get(tempCycle.get(tempCycle.size()-1));
        for (int j = 0; j < temp.size(); j++) {
            if ( temp.get(j).end == tempCycle.get(0)) {
                weight *= temp.get(j).weight; 
                break;
            }
        }
        return weight;
    }
}