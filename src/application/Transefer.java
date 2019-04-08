package application;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import java.awt.Font;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.ScrollPane;

public class Transefer extends JFrame {

	private JPanel contentPane;
    private String BigDelta;
    private ArrayList<String> allDeltas;
    private ArrayList<Double> DelataWeights;
    private JTable deltas;
	private static DefaultTableModel delta_table_model;
	private ScrollPane scrollPane;
	

	/**
	 * Create the frame.
	 */
	public Transefer(String BigDelta, ArrayList<String> allDeltas, GraphBuilder builder) {
		this.BigDelta = BigDelta;
		this.allDeltas = allDeltas;
		DelataWeights = builder.get_DeltainInt();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 830, 683);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblDeltaAndTf = new JLabel("Delta and T.F");
		lblDeltaAndTf.setForeground(new Color(0, 153, 51));
		lblDeltaAndTf.setFont(new Font("Comic Sans MS", Font.BOLD | Font.ITALIC, 28));
		lblDeltaAndTf.setHorizontalAlignment(SwingConstants.CENTER);
		lblDeltaAndTf.setBounds(293, 16, 205, 40);
		contentPane.add(lblDeltaAndTf);
		
		JTextArea textArea = new JTextArea();
		textArea.setForeground(Color.RED);
		textArea.setBounds(28, 84, 749, 113);
		textArea.setText(BigDelta + "\n        = " + DelataWeights.get(0));
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setOpaque(false);
		textArea.setEditable(false);
		textArea.setFocusable(false);
	    textArea.setBackground(UIManager.getColor("Label.background"));
		textArea.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		textArea.setBorder(UIManager.getBorder("Label.border"));
		contentPane.add(textArea);
		
		JLabel lblDeltas = new JLabel("DELTAS : ");
		lblDeltas.setFont(new Font("Showcard Gothic", Font.ITALIC, 21));
		lblDeltas.setBounds(28, 224, 168, 33);
		contentPane.add(lblDeltas);
		
		makePathsTable();
	    scrollPane = new ScrollPane();
		scrollPane.setBounds(28, 263, 749, 157);
		scrollPane.add(new JScrollPane(deltas));
		contentPane.add(scrollPane);
		
		JTextArea textArea_1 = new JTextArea();
		textArea_1.setForeground(Color.RED);
		textArea_1.setBounds(28, 450, 749, 161);
		textArea_1.setText(prepareTf() + String.valueOf(builder.calcTF()));
		textArea_1.setWrapStyleWord(true);
		textArea_1.setLineWrap(true);
		textArea_1.setOpaque(false);
		textArea_1.setEditable(false);
		textArea_1.setFocusable(false);
		textArea_1.setBackground(UIManager.getColor("Label.background"));
		textArea_1.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		textArea_1.setBorder(UIManager.getBorder("Label.border"));
		contentPane.add(textArea_1);
		
		
	}
	
	
	public void set_forward_table () {
		for (int i = 0; i < allDeltas.size(); i++) {
			String delta = allDeltas.get(i).toString();
			String weight = String.valueOf(DelataWeights.get(i+1));
			delta_table_model.addRow(new Object[]{"Delta" + (i+1) + " : "+delta, weight});
		}
	}
	
	public void makePathsTable () {
		String column_names[]= {"Delta","Delta Value"};
		delta_table_model = new DefaultTableModel(column_names,0);
		set_forward_table();
		deltas =new JTable(delta_table_model);
		TableColumn column = deltas.getColumnModel().getColumn(0);
		column.setPreferredWidth(250);
		deltas.setRowHeight(40);
		JTableHeader header = deltas.getTableHeader();
		header.setPreferredSize(new Dimension(100, 30));
		deltas.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		header.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		deltas.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
		deltas.setEnabled(false);
	}
	
	private String prepareTf() {
		StringBuilder tf = new StringBuilder();
		tf.append("T.F = ");
		tf.append("( ");
		for (int i =0; i < allDeltas.size(); i++) {
			tf.append("M" + (i+1) + "D" + (i+1) );
			if (i != allDeltas.size() - 1) {
				tf.append(" + ");
			}
		}
		tf.append(" )");
		tf.append(" / Delta");
		tf.append("\n");
		tf.append("     = ");
		return tf.toString();
	}
}
