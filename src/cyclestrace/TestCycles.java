package cyclestrace;


import java.util.List;



public class TestCycles {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String nodes[] = new String[6];
		boolean adjMatrix[][] = new boolean[6][6];

		for (int i = 0; i < 6; i++) {
			nodes[i] =  String.valueOf(i);
		}

		/*adjMatrix[0][1] = true;
		adjMatrix[1][2] = true;
		adjMatrix[2][0] = true;
		adjMatrix[2][4] = true;
		adjMatrix[1][3] = true;
		adjMatrix[3][6] = true;
		adjMatrix[6][5] = true;
		adjMatrix[5][3] = true;
		adjMatrix[6][7] = true;
		adjMatrix[7][8] = true;
		adjMatrix[7][9] = true;
		adjMatrix[9][6] = true;*/
		
        adjMatrix[0][1] = true;
        adjMatrix[1][2] = true;
        adjMatrix[2][3] = true;
        adjMatrix[3][4] = true;
        adjMatrix[4][5] = true;
        adjMatrix[2][1] = true;
        adjMatrix[3][2] = true;
        adjMatrix[3][3] = true;
        adjMatrix[1][3] = true;
        adjMatrix[1][4] = true;

		ElementaryCyclesSearch ecs = new ElementaryCyclesSearch(adjMatrix, nodes);
		List cycles = ecs.getElementaryCycles();
		for (int i = 0; i < cycles.size(); i++) {
			List cycle = (List) cycles.get(i);
			for (int j = 0; j < cycle.size(); j++) {
				String node = (String) cycle.get(j);
				if (j < cycle.size() - 1) {
					System.out.print(node + " -> ");
				} else {
					System.out.print(node);
				}
			}
			System.out.print("\n");
		}
	}

}
