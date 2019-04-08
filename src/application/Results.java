package application;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import com.google.common.graph.Graph;

import java.awt.GridLayout;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JTextArea;
import java.awt.TextArea;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.ScrollPane;

public class Results extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private ScrollPane forwardPaths;
	private static DefaultTableModel table_model;
	private GraphBuilder builder;
	private ArrayList<ArrayList<Integer> > forward;
	private List<List<Integer>> allLoops;
	public HashMap<Integer, ArrayList<Integer>> nontouchingloops;
	private ArrayList<ArrayList< Integer> > touchWithPath;
	private ArrayList<ArrayList<Double>> allDelta;
	private JTable loopsTable;
	private static DefaultTableModel loops_table_model;
	private JLabel lblNontouchingLoops;
	private ScrollPane nontouching;
	private JTable nonloopsTable;
	private static DefaultTableModel non_loops_table_model;
	private ArrayList<String> DeltatoPass = new ArrayList<>();


	/**
	 * Create the frame.
	 */
	public Results(ArrayList<ArrayList<Integer> > forward, GraphBuilder builder,
			List<List<Integer>> allLoops ,HashMap<Integer, ArrayList<Integer>> nontouchingloops ,
			ArrayList<ArrayList< Integer> > touchWithPath, ArrayList<ArrayList<Double>> deltas) {
		
		this.nontouchingloops = nontouchingloops;
		this.forward = forward;
		this.builder = builder;
		this.allLoops = allLoops;
		this.touchWithPath = touchWithPath;
		this.allDelta = deltas;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 737, 801);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		JLabel lblResults = new JLabel("Paths & Loops");
		lblResults.setBounds(250, 16, 212, 35);
		lblResults.setForeground(new Color(51, 153, 51));
		lblResults.setHorizontalAlignment(SwingConstants.CENTER);
		lblResults.setFont(new Font("Comic Sans MS", Font.BOLD | Font.ITALIC, 31));
		contentPane.add(lblResults);
		
		JLabel lblForwardPaths = new JLabel("Forward Paths :");
		lblForwardPaths.setBounds(15, 74, 212, 35);
		lblForwardPaths.setFont(new Font("Showcard Gothic", Font.ITALIC, 21));
		contentPane.add(lblForwardPaths);
		
		makePathsTable();
		forwardPaths = new ScrollPane();
		forwardPaths.setBounds(15, 111, 690, 170);
		forwardPaths.add(new JScrollPane(table));
		contentPane.add(forwardPaths);
		
		JLabel lblLoops = new JLabel("Loops :");
		lblLoops.setBounds(15, 299, 212, 35);
		lblLoops.setFont(new Font("Showcard Gothic", Font.ITALIC, 21));
		contentPane.add(lblLoops);
		
		makeLoopsTable();
		ScrollPane loops = new ScrollPane();
		loops.setBounds(15, 340, 690, 160);
		loops.add(new JScrollPane(loopsTable));
		contentPane.add(loops);
		
		lblNontouchingLoops = new JLabel("Non-touching loops :");
		lblNontouchingLoops.setBounds(15, 519, 254, 35);
		lblNontouchingLoops.setFont(new Font("Showcard Gothic", Font.ITALIC, 21));
		contentPane.add(lblNontouchingLoops);
		
		make_non_table();
		nontouching = new ScrollPane();
		nontouching.setBounds(15, 560, 690, 160);
		nontouching.add(new JScrollPane(nonloopsTable));
		contentPane.add(nontouching);
		
		System.out.println("DELTA = " + setViewDelta());
		String dtoPass = "DELTA = " + setViewDelta();
		for (int i = 0; i < forward.size(); i++) {
			System.out.println("delta " + String.valueOf(i+1) + " = " + getDeltaForPath(i));
			if ((getDeltaForPath(i)).trim().length() == 1) {
				DeltatoPass.add(getDeltaForPath(i) + " - 0");
			} else {
				DeltatoPass.add((getDeltaForPath(i)));
			}
			
		}
		
		Transefer transefer = new Transefer(dtoPass, DeltatoPass, builder);
		transefer.setVisible(true);
	}
	
	
	public void set_forward_table () {
		System.out.println(forward.size() + "paths");
		for (int i = 0; i < forward.size(); i++) {
			String path = forward.get(i).toString();
			String weight = String.valueOf(builder.get_path_Weight(forward.get(i)));
			table_model.addRow(new Object[]{"M" + (i+1) + " : "+path, weight});

		}
	}
	
	public void makeLoopsTable() {
		String column_names[]= {"Loop","Loop Weight"};
		loops_table_model = new DefaultTableModel(column_names,0);
		set_loop_table();
		loopsTable =new JTable(loops_table_model);
		TableColumn column = loopsTable.getColumnModel().getColumn(0);
		column.setPreferredWidth(250);
		loopsTable.setRowHeight(40);
		JTableHeader header = loopsTable.getTableHeader();
		header.setPreferredSize(new Dimension(100, 30));
		loopsTable.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		header.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		loopsTable.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
		loopsTable.setEnabled(false);
	}
	
	public void set_loop_table () {
		for (int i = 0; i < allLoops.size(); i++) {
			List<Integer> loop = allLoops.get(i);
			String weight = String.valueOf(builder.get_Weight(allLoops, i));
			if (loop.size() > 2 || (loop.size() == 2 && (loop.get(0) != loop.get(1)) )) {
				int x = loop.get(0);
				loop.add(x);			
			} else if (loop.size() == 1) {
				int x = loop.get(0);
				loop.add(x);	
			}
			
			loops_table_model.addRow(new Object[]{"L" + (i+1) + " : "+loop.toString(), weight});
		}
	}
	
	public void makePathsTable () {
		String column_names[]= {"Path","Path Weight"};
		table_model = new DefaultTableModel(column_names,0);
		set_forward_table();
		table =new JTable(table_model);
		TableColumn column = table.getColumnModel().getColumn(0);
		column.setPreferredWidth(250);
		table.setRowHeight(40);
		JTableHeader header = table.getTableHeader();
		header.setPreferredSize(new Dimension(100, 30));
		table.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		header.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		table.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
		table.setEnabled(false);
	}
	
	public void make_non_table () {
		
		non_loops_table_model = new DefaultTableModel();
		set_non_table();
		nonloopsTable =new JTable(non_loops_table_model);
		nonloopsTable.setRowHeight(40);
		JTableHeader header = nonloopsTable.getTableHeader();
		header.setPreferredSize(new Dimension(100, 30));
		nonloopsTable.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		header.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		for (int i = 0; i < nontouchingloops.size() ; i++) {
			nonloopsTable.getColumnModel().getColumn(i).setCellRenderer( centerRenderer );
		}
		nonloopsTable.setEnabled(false);
	}
	
	public void set_non_table () {
		int counter = 2;
		for (int i = 0; i < nontouchingloops.size(); i++) {
			ArrayList<Integer> temp = nontouchingloops.get(i+2);
			Object arr [] = new Object[temp.size() / counter];
			StringBuilder str = new StringBuilder();
			for (int j = 0; j < temp.size() ; j += counter) {
				int c = j;
				for (int k = 0; k < counter ; k++) {
					str.append("L");
					str.append(temp.get(c) + 1);
					str.append(" , ");
					c++;
				}
				str.deleteCharAt(str.length() - 2);
				arr[j/counter] = str.toString();
				str = new StringBuilder();
			}
			counter++;
			String name = String.valueOf( i + 2);
			name += " non-touching";
			non_loops_table_model.addColumn(name, arr);
		}
	}
	
	public String setViewDelta() {
		StringBuilder delta = new StringBuilder();
		delta.append("1");
		if (allLoops.size() == 0) {
			return delta.toString();
		} else {
			delta.append(" -");
			delta.append(" ( ");
			for (int i = 0;i < allLoops.size(); i++) {
				delta.append("L" + (i+1) + " + ");
			}
			delta.deleteCharAt(delta.length() - 2);
			delta.deleteCharAt(delta.length() - 1);
			delta.append(") ");
		}
		
		if (nontouchingloops.size() == 0) {
			return delta.toString();
		} else {
			int counter = 2;
			for (int i = 0; i < nontouchingloops.size(); i++) {
				if (counter % 2 == 0) {
					delta.append(" + ( ");
				}else {
					delta.append(" - ( ");
				}
				ArrayList<Integer> temp = nontouchingloops.get(i+2);
				for (int j = 0; j < temp.size() ; j += counter) {
					int c = j;
					for (int k = 0; k < counter ; k++) {
						delta.append("L");
						delta.append(temp.get(c) + 1);
						c++;
					}
					delta.append(" + ");
				}
				counter++;
				delta.deleteCharAt(delta.length() - 2);
				delta.deleteCharAt(delta.length() - 1);
				delta.append(")");
			}
		}
		return delta.toString();
	}
	
	public String getDeltaForPath (int nomPath) {
		StringBuilder res = new StringBuilder();
		boolean f = false;
		ArrayList<Integer> touch = touchWithPath.get(nomPath);
		res.append("1");
		if (allLoops.size() == 0) {
			return res.toString();
		} else {
			res.append(" -");
			res.append(" ( ");
			for (int i = 0;i < allLoops.size(); i++) {
				if (!touch.contains(i)) {
					res.append("L" + (i+1) + " + ");
					f = true;
				}
			}
			if (!f) {return "1";}
			res.deleteCharAt(res.length() - 2);
			res.deleteCharAt(res.length() - 1);
			res.append(") ");	
		}
		
		if (nontouchingloops.size() == 0) {
			return res.toString();
		} else {
			int counter = 2;
			f = false;
			boolean check = false;
			for (int i = 0; i < nontouchingloops.size(); i++) {
				if (counter % 2 == 0) {
					res.append(" + ( ");
				}else {
					res.append(" - ( ");
				}
				ArrayList<Integer> temp = nontouchingloops.get(i+2);
				for (int j = 0; j < temp.size() ; j += counter) {
					int c = j;
					int y = j;
					f = false;
					
					for (int x = 0; x < counter; x++) {
						if (touch.contains(temp.get(y))) {
							f = true;
							break;
						}
						y++;
					}
					
					if (!f) {
						check = true;
						for (int k = 0; k < counter ; k++) {	
							res.append("L");
							res.append(temp.get(c) + 1);
							c++;
						}
						res.append( " + ");
					}
				
				}	
					counter++;
					if (!check) {
						res.deleteCharAt(res.length() - 1);
						res.deleteCharAt(res.length() - 1);
						res.deleteCharAt(res.length() - 1);
						res.deleteCharAt(res.length() - 1);
						res.deleteCharAt(res.length() - 1);
					} else {
						res.deleteCharAt(res.length() - 1);
						res.deleteCharAt(res.length() - 1);
						res.append(")");
						check = false;
					}			
			}
		}
		return res.toString();
	}
	
}
