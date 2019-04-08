package application;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultListenableGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedPseudograph;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxEdgeStyle;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.SwingConstants;
import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.ScrollPane;

public class Drawer extends JFrame {

	private GraphBuilder builder = new GraphBuilder();
	private JPanel contentPane;
	private JTextField nomNodes;
	private JTextField from;
	private JTextField to;
	private JTextField weight;
	private ScrollPane pane;
	private TextArea edges;
	private int nom_nodes = 0;
	private static Drawer frame;
	private int outputNode = 0;

	private ArrayList<ArrayList<Integer>> foraward_paths = new ArrayList<>();
	public List<List<Integer>> All_cycles;
	public HashMap<Integer, ArrayList<Integer>> non_touching_loops = new HashMap<>();
	private ArrayList<ArrayList<Double>> deltas = new ArrayList<>();
	private ArrayList<ArrayList< Integer> > touchPath = new ArrayList<>();

	private ListenableGraph<String, MyEdge> g = new DefaultListenableGraph<>(
			new DirectedWeightedPseudograph<String, MyEdge>(MyEdge.class));
	
	private JTextField outNode;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new Drawer();
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
					frame.setResizable(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Drawer() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 980, 1031);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JLabel lblNewLabel = new JLabel("Signal Flow Graph");
		lblNewLabel.setBounds(299, 16, 348, 73);
		lblNewLabel.setForeground(Color.RED);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Comic Sans MS", Font.BOLD | Font.ITALIC, 31));

		nomNodes = new JTextField();
		nomNodes.setBounds(478, 116, 115, 41);
		nomNodes.setFont(new Font("Tahoma", Font.BOLD, 18));
		nomNodes.setColumns(10);
		nomNodes.setEditable(true);

		JLabel lblNumberOfNodes = new JLabel("Number Of Nodes :");
		lblNumberOfNodes.setBounds(187, 105, 233, 57);
		lblNumberOfNodes.setFont(new Font("Comic Sans MS", Font.BOLD, 24));

		JButton setNode = new JButton("Set");
		setNode.setBounds(651, 115, 143, 44);
		setNode.setForeground(new Color(255, 255, 255));
		setNode.setBackground(new Color(0, 0, 255));
		setNode.setFont(new Font("Lucida Bright", Font.BOLD, 22));
		setNode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setNodenum();
			}
		});
		contentPane.setLayout(null);
		contentPane.add(lblNewLabel);
		contentPane.add(nomNodes);
		contentPane.add(lblNumberOfNodes);
		contentPane.add(setNode);

		JLabel lblEdges = new JLabel("Edges : ");
		lblEdges.setFont(new Font("Comic Sans MS", Font.BOLD, 28));
		lblEdges.setBounds(32, 195, 125, 41);
		contentPane.add(lblEdges);

		from = new JTextField();
		from.setFont(new Font("Tahoma", Font.BOLD, 18));
		from.setBounds(275, 196, 74, 40);
		contentPane.add(from);
		from.setColumns(10);

		to = new JTextField();
		to.setFont(new Font("Tahoma", Font.BOLD, 18));
		to.setBounds(449, 195, 74, 41);
		contentPane.add(to);
		to.setColumns(10);

		weight = new JTextField();
		weight.setFont(new Font("Tahoma", Font.BOLD, 18));
		weight.setBounds(661, 195, 74, 41);
		contentPane.add(weight);
		weight.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("From :");
		lblNewLabel_1.setForeground(Color.RED);
		lblNewLabel_1.setFont(new Font("Viner Hand ITC", Font.BOLD | Font.ITALIC, 23));
		lblNewLabel_1.setBounds(186, 202, 74, 41);
		contentPane.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("To :");
		lblNewLabel_2.setFont(new Font("Viner Hand ITC", Font.BOLD | Font.ITALIC, 21));
		lblNewLabel_2.setForeground(Color.RED);
		lblNewLabel_2.setBounds(385, 204, 49, 37);
		contentPane.add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("Weight :");
		lblNewLabel_3.setForeground(Color.RED);
		lblNewLabel_3.setFont(new Font("Viner Hand ITC", Font.BOLD | Font.ITALIC, 21));
		lblNewLabel_3.setBounds(553, 205, 93, 35);
		contentPane.add(lblNewLabel_3);

		JButton AddEdgeBtn = new JButton("Add Edge");
		AddEdgeBtn.setBackground(new Color(0, 0, 255));
		AddEdgeBtn.setForeground(Color.WHITE);
		AddEdgeBtn.setFont(new Font("Lucida Bright", Font.BOLD, 22));
		AddEdgeBtn.setBounds(774, 195, 156, 44);
		AddEdgeBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				createAndShowGui();

			}
		});
		contentPane.add(AddEdgeBtn);

		edges = new TextArea();
		edges.setFont(new Font("Arial", Font.BOLD, 18));
		edges.setBounds(815, 460, 133, 505);
		contentPane.add(edges);

		JButton GetResultbtn = new JButton("Get Results");
		GetResultbtn.setBackground(Color.GREEN);
		GetResultbtn.setFont(new Font("Lucida Bright", Font.BOLD, 24));
		GetResultbtn.setForeground(new Color(255, 255, 255));
		GetResultbtn.setBounds(335, 363, 292, 57);
		GetResultbtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (CanShow()) {
					builder.setEndNode(getOutputNode());
					builder.getForwardPaths(0);
					setPathsList(builder.getPathsList());
					All_cycles = builder.SCC();
					builder.makeNontouched();
					non_touching_loops = builder.getNontouched();
					builder.getTouchedWithPath();
					builder.calcDelta();
					touchPath = builder.getTheTouchedWithPath();
					deltas = builder.getDeltas();
					frame.setState(Frame.ICONIFIED);
					Results results = new Results(getDpaths(), builder, All_cycles, non_touching_loops
							, touchPath, deltas);
					results.setVisible(true);
					results.setLocationRelativeTo(null);
					results.setResizable(false);
				}
				

			}
		});
		contentPane.add(GetResultbtn);

		JLabel lblEdges_1 = new JLabel("ADDED EDGES");
		lblEdges_1.setForeground(Color.BLUE);
		lblEdges_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblEdges_1.setFont(new Font("Comic Sans MS", Font.BOLD | Font.ITALIC, 18));
		lblEdges_1.setBounds(787, 415, 171, 35);
		contentPane.add(lblEdges_1);
		// createAndShowGui();

		pane = new ScrollPane(ScrollPane.SCROLLBARS_ALWAYS);
		pane.setBounds(10, 460, 784, 505);
		pane.setBackground(Color.WHITE);
		contentPane.add(pane);

		JLabel lblNewLabel_4 = new JLabel("Output Node :");
		lblNewLabel_4.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
		lblNewLabel_4.setBounds(187, 270, 233, 35);
		contentPane.add(lblNewLabel_4);

		outNode = new JTextField();
		outNode.setFont(new Font("Tahoma", Font.BOLD, 18));
		outNode.setBounds(478, 271, 115, 41);
		contentPane.add(outNode);
		outNode.setColumns(10);

		JButton btnNewButton = new JButton("Set Node");
		btnNewButton.setBackground(Color.BLUE);
		btnNewButton.setForeground(Color.WHITE);
		btnNewButton.setFont(new Font("Lucida Bright", Font.BOLD, 22));
		btnNewButton.setBounds(651, 271, 143, 41);
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (checkString(outNode.getText().toString().trim()) && 
						Integer.parseInt(outNode.getText().toString().trim()) <= getNom_nodes()) {
					setOutputNode(Integer.parseInt(outNode.getText().toString().trim()));
				}
			}
		});
		contentPane.add(btnNewButton);

	}

	public void createAndShowGui() {
		if (nom_nodes == 0) {
			JOptionPane.showMessageDialog(null, "Please set the number of nodes");
			return;
		}
		//nomNodes.setText(String.valueOf(nom_nodes));
		String froms = from.getText();
		String tos = to.getText();
		String we = weight.getText();

		if (!(checkString(froms) && checkString(tos))) {
			return;
		}

		if (Integer.parseInt(froms.trim()) > nom_nodes) {
			JOptionPane.showMessageDialog(null, "Not Valid Value");
			return;
		}

		if (Integer.parseInt(tos.trim()) > nom_nodes) {
			JOptionPane.showMessageDialog(null, "Not Valid Value");
			return;
		}
		
		g.addVertex(froms);
		g.addVertex(tos);

		builder.buildGraph(Integer.parseInt(froms), Integer.parseInt(tos), Double.parseDouble(we));

		MyEdge e = g.addEdge(froms, tos);
		g.setEdgeWeight(e, Double.parseDouble(we));

		JGraphXAdapter<String, MyEdge> graphAdapter = new JGraphXAdapter<String, MyEdge>(g);

		mxGraphComponent component = new mxGraphComponent(graphAdapter);
		component.zoom(1.7);
		component.zoomIn();
		component.getViewport().setOpaque(true);
		component.getViewport().setBackground(Color.WHITE);
		component.getGraph().setAllowLoops(true);
		new mxCircleLayout(graphAdapter).execute(graphAdapter.getDefaultParent());
		new mxParallelEdgeLayout(graphAdapter).execute(graphAdapter.getDefaultParent());
		pane.removeAll();
		pane.revalidate();
		pane.add(component);
		edges.append(froms + " --> " + tos + " = " + we + "\n");

	}

	public int getNom_nodes() {
		return nom_nodes;
	}

	public void setNom_nodes(int nom_nodes) {
		this.nom_nodes = nom_nodes;
	}

	public static class MyEdge extends DefaultWeightedEdge {
		@Override
		public String toString() {
			return String.valueOf("w : " + getWeight());
		}

	}

	public void setNodenum() {
		String num = nomNodes.getText();
		if (num.equals("") || num == null) {
			JOptionPane.showMessageDialog(null, "Please enter number of nodes");
		} else if (num.trim().matches("[0-9]+") && num.length() >= 1) {
			setNom_nodes(Integer.parseInt(num) - 1);
			nomNodes.setEditable(false);
		} else {
			JOptionPane.showMessageDialog(null, "Not valid value");
		}
	}

	public boolean checkString(String str) {
		if (str.equals("") || str == null) {
			JOptionPane.showMessageDialog(null, "Please enter the missing values");
			return false;
		} else if (str.trim().matches("-?[0-9]+") && str.length() >= 1) {
			return true;
		} else {
			JOptionPane.showMessageDialog(null, "Not valid value");
			return false;
		}
	}

	public ArrayList<ArrayList<Integer>> getDpaths() {
		return foraward_paths;
	}

	public void setPathsList(ArrayList<ArrayList<Integer>> list) {
		this.foraward_paths = list;
	}

	public int getOutputNode() {
		if (outputNode == 0) {
			outputNode = getNom_nodes();
		}
		return outputNode;
	}

	public void setOutputNode(int outputNode) {
		this.outputNode = outputNode;
	}
	
	public boolean CanShow () {
		if (getNom_nodes() == 0) {
			JOptionPane.showMessageDialog(null, "Please enter number of nodes");
			return false;
		} else if (edges.getText() == null || edges.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Please enter edge at least");
			return false;
		} else {
			return true;
		}
	}
	
}
