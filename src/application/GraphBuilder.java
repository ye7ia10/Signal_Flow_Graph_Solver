package application;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cyclestrace.ElementaryCyclesSearch;

import java.util.Set;

public class GraphBuilder {
	
	private int maxSize = 100;
	private int endNode = 100;
	private List<List<Integer>> cycles;
	private TarjanSimpleCycles targan;
	private boolean visited[] = new boolean[maxSize];
	private ArrayList<ArrayList<Integer>> forwardPath = new ArrayList<>();
	private ArrayList<ArrayList<Double>> forwardPathWeight = new ArrayList<>();
	private HashMap<Integer, ArrayList<Edge>> graph = new HashMap<Integer, ArrayList<Edge>>();
	private ArrayList<Integer> tempPath = new ArrayList<Integer>();
	private ArrayList<Double> tempWeight = new ArrayList<Double>();
	private ArrayList<ArrayList< Integer> > touchWithPath = new ArrayList<>();
	private ArrayList<ArrayList< Double> > deltas = new ArrayList<ArrayList< Double> >();
	private ArrayList<Double> deltaInInt = new ArrayList<>();
	private boolean[][] arrayCycle;
	private String node[];
	public void buildGraph(int from, int to, double weight) {
		Edge edge = new Edge(to, weight);
		if (graph.containsKey(from)) {
			ArrayList<Edge> arr = graph.get(from);
			arr.add(edge);
			graph.put(from, arr);
		} else {
			ArrayList<Edge> arr = new ArrayList<>();
			arr.add(edge);
			graph.put(from, arr);
		}

	}

	public void getForwardPaths(int node) {
		if (visited[node])
			return;
		visited[node] = true;
		tempPath.add(node);
		if (node == endNode) {
			ArrayList<Integer> path = new ArrayList<Integer>();
			ArrayList<Double> weight = new ArrayList<Double>();
			for (int i = 0; i < tempPath.size(); i++)
				path.add(tempPath.get(i));
			for (int i = 0; i < tempWeight.size(); i++)
				weight.add(tempWeight.get(i));
			forwardPath.add(path);
			forwardPathWeight.add(weight);
			tempPath.remove(tempPath.size() - 1);
			//System.out.println("path :" + path);
			//System.out.println("weight :" + weight);
			visited[node] = false;
			return;
		}
		if (graph.containsKey(node))
			for (int i = 0; i < graph.get(node).size(); i++) {
				tempWeight.add(graph.get(node).get(i).weight);
				getForwardPaths(graph.get(node).get(i).end);
				tempWeight.remove(tempWeight.size() - 1);
			}
		tempPath.remove(tempPath.size() - 1);
		visited[node] = false;
	}

	public void setEndNode(int end) {
		endNode = end;
	}

	public List<List<Integer>> SCC() {
		toArray();
		ElementaryCyclesSearch ecs = new ElementaryCyclesSearch(arrayCycle, node);
		List cycles = ecs.getElementaryCycles();
		
		for (int i = 0; i < cycles.size(); i++) {
			List cycle = (List) cycles.get(i);
			List cycleI = new ArrayList<Integer>();
			for (int j = 0; j < cycle.size(); j++) {
				String node = (String) cycle.get(j);
				cycleI.add(Integer.parseInt(node));
				if (j < cycle.size() - 1) {
					System.out.print(node + " -> ");
					
				} else {
					System.out.print(node);
				}
			}
			this.cycles.add(cycleI);
			System.out.print("\n");
		}
		return this.cycles;
	}
	public void toArray() {
		arrayCycle = new boolean [endNode + 100][endNode + 100];
		this.cycles = new ArrayList<List<Integer>>();
		for( Map.Entry<Integer, ArrayList<Edge>> entry : graph.entrySet() ) {
			for(Edge i : entry.getValue()) {
				arrayCycle[entry.getKey()][i.end] = true;
				if(entry.getKey() == i.end) {
					List cycleI = new ArrayList<Integer>();
					cycleI.add(i.end);
					this.cycles.add(cycleI);
				}
			}
		}
		node = new String[endNode + 100];
		for (int i = 0; i < endNode + 100; i++) {
			node[i] =  String.valueOf(i);
		}
	}
	private List<List<Integer>> nontouching = new ArrayList<>();
	private List<Integer> step = new ArrayList<>();

	public List<List<Integer>> nontouching(int number, int index) {
		//System.out.println("hiiii");
		if (index == 0) {
			nontouching.clear();
		}
		if (step.size() == number) {
			List<Integer> step2 = new ArrayList<>();
			for (int i = 0; i < step.size(); i++) {
				step2.add(step.get(i));
			}
			nontouching.add(step2);
			return nontouching;
			//System.out.println(step2);
		}
		for (int i = index; i < SCC().size(); i++) {
			step.add(i);
			nontouching(number, i + 1);
			step.remove(step.size() - 1);

		}
		return nontouching;
	}

	private HashMap<Integer, ArrayList<Double>> nontouchedW = new HashMap<Integer, ArrayList<Double>>();
	private HashMap<Integer, ArrayList<Integer>> nontouchedC = new HashMap<Integer, ArrayList<Integer>>();

	public void makeNontouched() {
		for (int i = 2;; i++) {
			boolean cycleNotFound = true;
			List<List<Integer>> nontouching = nontouching(i, 0);
			if (nontouching.size() == 0)
				break;
			for (int j = 0; j < nontouching.size(); j++) {
				Set<Integer> hash_Set = new HashSet<Integer>();
				boolean nontouchedd = true;
				double weights = 1;
				for (int k = 0; k < nontouching.get(j).size(); k++) {
					ArrayList<Integer> acycle = (ArrayList<Integer>) cycles.get(nontouching.get(j).get(k));
					weights *= get_Weight(cycles, nontouching.get(j).get(k));
					//System.out.println("cycle");
					//System.out.println(acycle);
					for (int m = 0; m < acycle.size(); m++) {
						if (hash_Set.contains(acycle.get(m))) {
							nontouchedd = false;
						}
						hash_Set.add(acycle.get(m));
					}

				}
				if (nontouchedd) {
					cycleNotFound = false;
					if (nontouchedW.containsKey(i)) {
						ArrayList<Double> arr = nontouchedW.get(i);
						arr.add(weights);
						nontouchedW.put(i, arr);
					} else {
						ArrayList<Double> arr = new ArrayList<>();
						arr.add(weights);
						nontouchedW.put(i, arr);
					}
					if (nontouchedC.containsKey(i)) {
						ArrayList<Integer> arr = nontouchedC.get(i);
						for (int z = 0; z < nontouching.get(j).size(); z++) {
							arr.add(nontouching.get(j).get(z));
						}
						nontouchedC.put(i, arr);
					} else {
						ArrayList<Integer> arr = new ArrayList<>();
						for (int z = 0; z < nontouching.get(j).size(); z++) {
							arr.add(nontouching.get(j).get(z));
						}
						nontouchedC.put(i, arr);
					}

				}
			}
			if (cycleNotFound) {
				break;
			}
		}
	}

	public double get_path_Weight(ArrayList<Integer> path) {

		double weight = 1;
		for (int i = 0; i < path.size() - 1; i++) {
			int from = path.get(i);
			int to = path.get(i + 1);
			ArrayList<Edge> temp = graph.get(from);		
			for (int j = 0; j < temp.size(); j++) {
				if (temp.get(j).end == to) {
					weight *= temp.get(j).weight;
					break;
				}
			}

		}
		return weight;
	}

	public ArrayList<ArrayList<Integer>> getPathsList() {
		return forwardPath;
	}

	public HashMap<Integer, ArrayList<Edge>> getGraph() {
		// TODO Auto-generated method stub
		return graph;
	}
	
	public  HashMap<Integer, ArrayList<Integer>> getNontouched(){
		return nontouchedC;
	}
	
    public double get_Weight (List<List<Integer>> AllCyc, int index) {

        List < Integer > tempCycle = AllCyc.get(index);
        double weight = 1;
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
    
    public void getTouchedWithPath(){
    	for (int i = 0; i < forwardPath.size(); i++) {
    		ArrayList<Integer> tPath = forwardPath.get(i);
    		ArrayList<Integer> temp = new ArrayList<>();
    		for (int j = 0; j < cycles.size(); j++) {
    			List<Integer> tloop = cycles.get(j);
    			for (int k = 0; k < tloop.size(); k++) {
    				if (tPath.contains(tloop.get(k))) {
    					temp.add(j);
    					break;
    				}
    			}
    		}
    		touchWithPath.add(temp);
    	}
    }
    
    public ArrayList<ArrayList< Integer> > getTheTouchedWithPath(){
    	return touchWithPath;
    }
    
    
    public void calcDelta() {
    	ArrayList <Double> delta = new ArrayList<>();
    	delta.add((double) 1);
    	double x = 0;
    	for(int i  = 0; i < cycles.size(); i++) {
    		x += get_Weight(cycles, i);
    	}
    	delta.add(x);
    	int loops = 2;
    	while(nontouchedW.containsKey(loops)) {
    		x = 0;
    		for(int i = 0; i < nontouchedW.get(loops).size(); i++) {
    			x += nontouchedW.get(loops).get(i);
    		}
    		delta.add(x);
    		loops++;
    	}
    	deltas.add(delta);
    	for(int i = 0; i < touchWithPath.size(); i++) {
    		x = 0;
    		ArrayList <Double> deltaI = new ArrayList<>();
    		deltaI.add((double) 1);
        	for(int j  = 0; j < cycles.size(); j++) {
        		boolean canAdded = true;
        		for(int k = 0; k < touchWithPath.get(i).size();k++) {
        			if(touchWithPath.get(i).get(k).equals(j)) {
        				canAdded = false;
        			}
        		}
        		if(canAdded)
        			x += get_Weight(cycles, j);
        	}
        	deltaI.add(x);
        	loops = 2;
        	while(nontouchedC.containsKey(loops)) {
        		x = 0;
        		for(int l = 0; l < nontouchedC.get(loops).size(); l += loops) {
        			double weight = 0;
        			boolean canAdded = true;
        			for(int z = 0; z < loops; z++) {
        				for(int s = 0; s < touchWithPath.get(i).size(); s++) {
        					if(touchWithPath.get(i).get(s).equals(nontouchedC.get(loops).get(l+z))) {
        						canAdded = false;
        					}
        					if(weight == 0)
        						weight = 1;
        					weight *= get_Weight(cycles, nontouchedC.get(loops).get(l+z));
        				}
        			}
        			if(canAdded)
        				x += weight;
        			
        		}
        		deltaI.add(x);
        		loops++;
        	}
        	deltas.add(deltaI);
    	}
    	System.out.println(deltas);
    }
    
    
    public ArrayList<ArrayList< Double> > getDeltas (){
    	return deltas;
    }
    
    
    private  void calcDeltaInInteger(){
    	for(int i = 0; i < deltas.size(); i++) {
    		double sum = 0,sign = 1;
    		for(double j : deltas.get(i)) {
    			sum += sign * j;
    			sign *= -1;
    		}
    		deltaInInt.add(sum);
    	}
    }
    
    public double calcTF() {
    	double tf = 0;
    	//calcDeltaInInteger();
    	
    	for(int i = 1; i < deltaInInt.size(); i++) {
    		tf += ( deltaInInt.get(i) * get_path_Weight(forwardPath.get(i - 1)) );
    	}
    	System.out.println(deltaInInt.size() + " deltainssize");
        tf /= deltaInInt.get(0);
    	return tf;
    }

	public ArrayList<Double> get_DeltainInt() {
		// TODO Auto-generated method stub
		calcDeltaInInteger();
		return deltaInInt;
	}
    
    
}
