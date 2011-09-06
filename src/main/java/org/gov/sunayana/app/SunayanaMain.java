/*
 *  Created on July 06, 2011
 */
package org.gov.sunayana.app;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharacterCodingException;
import java.util.List;
import java.util.Properties;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.GroupLayout.Alignment;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * @author Shylesh
 */
public class SunayanaMain extends javax.swing.JFrame {

	private static final long serialVersionUID = 1L;
	private static String appVersion = "Sunayana v0.1";
	private javax.swing.JButton jButtonReset;
	private javax.swing.JMenuBar jMenuBar;
	private javax.swing.JMenu jMenuFile;
	private javax.swing.JMenuItem jMenuItem1;
	private javax.swing.JPanel mainPanel;
	private javax.swing.JSpinner jSpinnerNameMatchValue;
	private javax.swing.JSpinner jSpinnerPfaMatchValue;
	private javax.swing.JFormattedTextField jTextFieldPincode;
	private JTabbedPane tabbedPane;
	private JScrollPane tab3ScrollPane;
	private JButton btnDiagnosticCheck;
	private JPanel diagnosticsPanel;
	private JTextPane textPane;
	private JPanel reportPanel;
	private JButton btnDailyReport;
	private JTextField dateField;
	private JLabel tab5Message;
	private JLabel dateLabel;
	private JButton btnExport;
	private JTable tableReport;
	private JTable uidTable;

	// Console Log instance variable
	private StringBuffer consoleLogBuffer = null;

	protected String KEY_LOCATION_CODE = "locationCode";
	protected String KEY_SESSION_ID = "sessionId";
	protected String KEY_MACHINE_ID = "machineId";
	protected String KEY_PARALLE = "parallel";
	protected String KEY_SERIAL = "serial";
	protected String[] lisOfSensors = null;
	protected boolean isParallel = false;
	protected boolean isSerial = true;
	ExecutorService executor = Executors.newCachedThreadPool();
	protected boolean firstIteration = true;

	// ------------- Device Log instance variables -------------------
	private String laptopID = "";
	private String location = "";
	// ----------------------------------------------------------------
	// Date Chooser
	private DateChooser dateChooser;
	protected InfiniteProgressPanel glassPane;
	private boolean deviceCheck = false;

	public SunayanaMain() throws URISyntaxException {
		setResizable(false);
		getContentPane().setBackground(SystemColor.inactiveCaption);
		setTitle(appVersion);
		initComponents();
		loadPreferences();
		glassPane = new InfiniteProgressPanel("Please wait...");
		this.setGlassPane(glassPane);
		jSpinnerNameMatchValue.setEnabled(false);
		jSpinnerNameMatchValue.setValue(100);
		jSpinnerPfaMatchValue.setEnabled(false);
		jSpinnerPfaMatchValue.setValue(100);
		jTextFieldPincode.setFocusLostBehavior(JFormattedTextField.COMMIT);
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
		} catch (ClassNotFoundException e) {
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		}

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SunayanaMain frame = new SunayanaMain();
					frame.setVisible(true);
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
					frame.setSize(1024, 600);
				} catch (URISyntaxException ex) {
					Logger.getLogger(SunayanaMain.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		});
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	private void initComponents() {
		jButtonReset = new javax.swing.JButton();
		mainPanel = new javax.swing.JPanel();
		jSpinnerNameMatchValue = new javax.swing.JSpinner();
		jTextFieldPincode = new javax.swing.JFormattedTextField();
		jSpinnerPfaMatchValue = new javax.swing.JSpinner();
		jMenuBar = new javax.swing.JMenuBar();
		jMenuFile = new javax.swing.JMenu();
		jMenuFile.setForeground(Color.BLACK);
		jMenuFile.setFont(new Font("Tahoma", Font.PLAIN, 12));
		jMenuItem1 = new javax.swing.JMenuItem();
		jMenuItem1.setFont(new Font("Tahoma", Font.PLAIN, 12));

		jButtonReset.setText("Reset to default");

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setBackground(SystemColor.inactiveCaption);

		mainPanel.setBackground(Color.LIGHT_GRAY);
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBackground(Color.YELLOW);

		javax.swing.GroupLayout gl_mainPanel = new javax.swing.GroupLayout(mainPanel);
		gl_mainPanel
				.setHorizontalGroup(gl_mainPanel.createParallelGroup(Alignment.LEADING).addGroup(
						gl_mainPanel.createSequentialGroup().addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 1006, GroupLayout.PREFERRED_SIZE).addContainerGap(2210,
								Short.MAX_VALUE)));
		gl_mainPanel.setVerticalGroup(gl_mainPanel.createParallelGroup(Alignment.LEADING).addGroup(
				gl_mainPanel.createSequentialGroup().addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 546, GroupLayout.PREFERRED_SIZE).addContainerGap(175, Short.MAX_VALUE)));

		diagnosticsPanel = new JPanel();
		diagnosticsPanel.setBackground(new Color(0, 153, 102));
		diagnosticsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, appVersion, javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11)));
		tabbedPane.addTab("Log Diagnostics", null, diagnosticsPanel, null);
		tabbedPane.setBackgroundAt(0, Color.ORANGE);
		tabbedPane.setForegroundAt(0, Color.BLACK);
		diagnosticsPanel.setLayout(null);

		tab3ScrollPane = new JScrollPane();
		tab3ScrollPane.setBounds(25, 72, 949, 417);
		diagnosticsPanel.add(tab3ScrollPane);

		textPane = new JTextPane();
		tab3ScrollPane.setViewportView(textPane);
		textPane.setFont(new Font("Tahoma", Font.PLAIN, 20));
		textPane.setBackground(SystemColor.info);
		textPane.setEditable(false);
		textPane.setVisible(true);

		btnDiagnosticCheck = new JButton("Locate Log File");
		btnDiagnosticCheck.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnDiagnosticCheck.setBounds(25, 23, 135, 25);
		btnDiagnosticCheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDiagnosticCheckActionPerformed(e);
			}
		});
		diagnosticsPanel.add(btnDiagnosticCheck);

		JButton btnCommit = new JButton("Store Logs Into DB");
		btnCommit.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnCommit.setBounds(182, 23, 167, 25);
		diagnosticsPanel.add(btnCommit);

		reportPanel = new JPanel();
		reportPanel.setBackground(new Color(0, 153, 102));
		reportPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, appVersion, javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11)));
		tabbedPane.addTab("Log Reports", null, reportPanel, null);
		reportPanel.setLayout(null);
		JScrollPane reportScrollPane = new JScrollPane();
		reportScrollPane.setBounds(311, 56, 680, 206);
		reportPanel.add(reportScrollPane);
		tableReport = new JTable();
		tableReport.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Date", "Location", "Area Code", "Camera#", "Total# Clicks", "Dummy1", "Dummy2" }));
		tableReport.setRowSelectionAllowed(false);
		tableReport.setEnabled(false);
		tableReport.setFillsViewportHeight(true);
		tableReport.setBackground(Color.WHITE);
		reportScrollPane.setViewportView(tableReport);
		tab5Message = new JLabel("");
		tab5Message.setFont(new Font("Tahoma", Font.BOLD, 16));
		tab5Message.setBounds(230, 28, 689, 24);
		tab5Message.setVisible(false);
		reportPanel.add(tab5Message);
		dateField = new JTextField();
		dateField.setFont(new Font("Tahoma", Font.BOLD, 18));
		dateField.setBounds(100, 28, 120, 23);
		dateField.setEditable(false);
		reportPanel.add(dateField);
		dateField.setColumns(10);
		btnDailyReport = new JButton("Generate Report");
		btnDailyReport.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnDailyReport.setBounds(34, 248, 233, 59);
		btnDailyReport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String dt = dateField.getText();
				if (dt == null || dt == "" || dt.length() == 0) {
					tab5Message.setVisible(true);
					tab5Message.setForeground(new java.awt.Color(255, 0, 0));
					tab5Message.setText("Please select a date");
				} else {
					tab5Message.setVisible(false);
					displayReport(dt);
				}
			}
		});
		reportPanel.add(btnDailyReport);
		dateChooser = new DateChooser(dateField, tab5Message);
		dateChooser.setBounds(10, 56, 291, 167);
		reportPanel.add(dateChooser);
		dateChooser.setBackground(SystemColor.inactiveCaption);
		dateLabel = new JLabel("Date :");
		dateLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
		dateLabel.setHorizontalAlignment(SwingConstants.LEFT);
		dateLabel.setBounds(10, 28, 70, 23);
		reportPanel.add(dateLabel);
		btnExport = new JButton("Export Report");
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (tableReport.getRowCount() == 0) {
					tab5Message.setVisible(true);
					tab5Message.setForeground(new java.awt.Color(255, 0, 0));
					tab5Message.setText("Please generate daily report first");
				} else {
					new DailyReport().generateCsvFile(dateField.getText(), laptopID, location);
					tab5Message.setVisible(true);
					tab5Message.setForeground(new java.awt.Color(0, 100, 0));
					tab5Message.setText("Exported successfully. Please check C:\\AUTHPOC2_LOGS\\EODReports directory");
				}
			}
		});
		btnExport.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnExport.setBounds(34, 327, 233, 59);
		reportPanel.add(btnExport);
		dateField.setText(dateChooser.getCurrentDate());

		JButton btnDeleteDailyReport = new JButton("Delete Report");
		btnDeleteDailyReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Object[] options = { "Yes", "No" };
				int response = JOptionPane.showOptionDialog(null, "The daily report will be deleted from the database for the selected date. \nPlease confirm.", "Confirm",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
				if (response == 0) {
					int i = new DailyReport().deleteDailyReport(dateField.getText());
					if (i > 0) {
						displayReport(dateField.getText());
						tab5Message.setVisible(true);
						tab5Message.setForeground(new java.awt.Color(0, 100, 0));
						tab5Message.setText("Report deleted successfully for the date " + dateField.getText());
					} else {
						tab5Message.setVisible(true);
						tab5Message.setForeground(new java.awt.Color(255, 0, 0));
						tab5Message.setText("No report available for the date " + dateField.getText());
					}
				}
			}
		});
		btnDeleteDailyReport.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnDeleteDailyReport.setBounds(82, 483, 136, 24);
		reportPanel.add(btnDeleteDailyReport);

		JScrollPane uidScrollPane = new JScrollPane();
		uidScrollPane.setBounds(311, 273, 680, 206);
		reportPanel.add(uidScrollPane);

		uidTable = new JTable();
		uidTable.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Dummy3", "Dummy4", "Dummy5" }));
		uidTable.setRowSelectionAllowed(false);
		uidTable.setEnabled(false);
		uidTable.setFillsViewportHeight(true);
		uidTable.setBackground(Color.WHITE);
		uidScrollPane.setViewportView(uidTable);

		mainPanel.setLayout(gl_mainPanel);
		jMenuFile.setText("File");
		jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.ALT_MASK));
		jMenuItem1.setText("Exit");
		jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				exit(evt);
			}
		});
		jMenuFile.add(jMenuItem1);
		jMenuBar.add(jMenuFile);
		jMenuFile.add(jMenuItem1);
		jMenuBar.add(jMenuFile);
		setJMenuBar(jMenuBar);
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
		getContentPane().add(mainPanel);
		pack();
	}

	private void btnDiagnosticCheckActionPerformed(java.awt.event.ActionEvent evt) {
		JFileChooser chooser;
		chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle("Choose the log file");
		int rc = chooser.showOpenDialog(this);
		textPane.setText(null);

		try {
			if (rc == JFileChooser.APPROVE_OPTION) {
				getFileChecked(textPane, chooser.getSelectedFile(), chooser.getSelectedFile().getName());
			}
		} catch (FileNotFoundException fx) {
			textPane.setText(fx.getMessage());
			fx.printStackTrace();
		} catch (IOException iex) {
			textPane.setText(iex.getMessage());
			iex.printStackTrace();
		}

	}

	private void getFileChecked(JTextPane textPane, File file, String fileName) throws FileNotFoundException, IOException {
		String color = "black";
		StringBuffer stb = new StringBuffer();
		stb.append("Log file: " + fileName + "\n");
		printMessage(textPane, color, 18, stb.toString(), true);
		stb = null;

		stb = new StringBuffer();
		stb.append("------------------------------------------------------------------------------------------------------");
		stb.append("------------------------------------------------------------------------------------------------------");
		printMessage(textPane, color, 12, stb.toString(), false);

		// TODO: process
		// 0. Cleanup the files for \n chars
		// 1. Split the events at % token
		// 2. Pattern matcher to count the occurences of all events
		// 3. Last timestamp and battery charge
		// 4. The location, area code and Camera# identified by # token
		// 5. Last timestamp of Camera click event (M2 or S2)

		//Removes the new line characters and creates a new file in the file system.
		cleanUpFile(file);
		
		File f = new File(file.getAbsolutePath() + ".modified");
		BufferedReader br = new BufferedReader(new FileReader(f));
		String inputLine;
		int p1 = 0, p2 = 0, p0 = 0, m1 = 0, m2 = 0, m0 = 0, s1 = 0, s2 = 0, s0 = 0;
		String tsM2 = "";
		String tsM1 = "";
		String tsM0 = "";
		String tsS2 = "";
		String tsS1 = "";
		String tsS0 = "";
		String lastBattery = "";
		String tsLast = "";
		String lastEvent = "";
		String location = "";
		String areaCode = "";
		String cameraNo = "";

		while ((inputLine = br.readLine()) != null) {
			if ("".equals(inputLine)) {
				continue;
			}
			String[] strArray = inputLine.split("%");
			for (int i = 0; i < strArray.length; i++) {
				if (strArray[i].startsWith("p1m1")) {
					p1++;
					m1++;
					tsM1 = strArray[i].substring(4, 16);
				} else if (strArray[i].startsWith("p2m1")) {
					p2++;
					m1++;
					tsM1 = strArray[i].substring(4, 16);
				} else if (strArray[i].startsWith("p0m2")) {
					p0++;
					m2++;
					tsM2 = strArray[i].substring(4, 16);
				} else if (strArray[i].startsWith("p1m0")) {
					p1++;
					m0++;
					tsM0 = strArray[i].substring(4, 16);
				} else if (strArray[i].startsWith("p2m0")) {
					p2++;
					m0++;
					tsM0 = strArray[i].substring(4, 16);
				} else if (strArray[i].startsWith("p1s1")) {
					p1++;
					s1++;
					tsS1 = strArray[i].substring(4, 16);
				} else if (strArray[i].startsWith("p2s1")) {
					p2++;
					s1++;
					tsS1 = strArray[i].substring(4, 16);
				} else if (strArray[i].startsWith("p0s2")) {
					p0++;
					s2++;
					tsS2 = strArray[i].substring(4, 16);
				} else if (strArray[i].startsWith("p1s0")) {
					p1++;
					s0++;
					tsS0 = strArray[i].substring(4, 16);
				} else if (strArray[i].startsWith("p2s0")) {
					p2++;
					s0++;
					tsS0 = strArray[i].substring(4, 16);
				} else if (strArray[i].startsWith("#")) {
					location = strArray[i].substring(1, 3);
					areaCode = strArray[i].substring(3, 5);
					cameraNo = strArray[i].substring(5, 8);
				}
			}
			lastBattery = strArray[strArray.length - 1].substring(16);
			tsLast = strArray[strArray.length - 1].substring(4, 16);
			lastEvent = strArray[strArray.length - 1].substring(0, 4);
		}

		br.close();

		// Make sure the file or directory exists and isn't write protected
		if (!f.exists()) {
			throw new IllegalArgumentException("Delete: no such file or directory: " + f.getName());
		}

		if (!f.canWrite()) {
			throw new IllegalArgumentException("Delete: write protected: " + f.getName());
		}

		// Attempt to delete it
		boolean success = f.delete();

		if (!success) {
			throw new IllegalArgumentException("Delete: deletion failed");
		}

		String lastEvent1 = lastEvent.substring(0, 2);
		if (lastEvent1.equalsIgnoreCase("p1")) {
			lastEvent1 = "Left Sensor ON";
		} else if (lastEvent1.equalsIgnoreCase("p2")) {
			lastEvent1 = "Right Sensor ON";
		} else if (lastEvent1.equalsIgnoreCase("p0")) {
			lastEvent1 = "Middle Sensor ON";
		}

		if (lastEvent1.equalsIgnoreCase("a0")) {
			lastEvent1 = "Active Sensor ON";
		}

		String lastEvent2 = lastEvent.substring(2);
		if (lastEvent2.equalsIgnoreCase("m1") || lastEvent2.equalsIgnoreCase("s1")) {
			lastEvent2 = "Camera ON";
		} else if (lastEvent2.equalsIgnoreCase("m2") || lastEvent2.equalsIgnoreCase("s2")) {
			lastEvent2 = "Camera Click";
		} else if (lastEvent2.equalsIgnoreCase("m0") || lastEvent2.equalsIgnoreCase("s0")) {
			lastEvent2 = "Camera OFF";
		}

		printMessage(textPane, color, 18, "\n", false);
		printMessage(textPane, color, 18, "Location :  ", false);
		printMessage(textPane, "green", 22, location + "\n", false);

		printMessage(textPane, color, 18, "Area Code :  ", false);
		printMessage(textPane, "green", 22, areaCode + "\n", false);

		printMessage(textPane, color, 18, "Camera Number :  ", false);
		printMessage(textPane, "green", 22, cameraNo + "\n", false);

		printMessage(textPane, color, 18, "Total number of Camera clicks :  ", false);
		printMessage(textPane, "green", 22, m2 + "\n", false);

		printMessage(textPane, color, 18, "Total number of Camera ONs :  ", false);
		printMessage(textPane, "green", 22, m1 + "\n", false);

		printMessage(textPane, color, 18, "Total number of Camera OFFs :  ", false);
		printMessage(textPane, "green", 22, m0 + "\n", false);

		printMessage(textPane, color, 18, "Total number of Left Sensor Activities :  ", false);
		printMessage(textPane, "green", 22, p1 + "\n", false);

		printMessage(textPane, color, 18, "Total number of Middle Sensor Activities :  ", false);
		printMessage(textPane, "green", 22, p0 + "\n", false);

		printMessage(textPane, color, 18, "Total number of Right Sensor Activities :  ", false);
		printMessage(textPane, "green", 22, p2 + "\n", false);

		tsM2 = tsM2.substring(0, 2) + ":" + tsM2.substring(2, 4) + "  " + tsM2.substring(4, 6) + "-" + tsM2.substring(6, 8) + "-" + tsM2.substring(8);
		printMessage(textPane, color, 18, "Last camera click was at :  ", false);
		printMessage(textPane, "green", 22, tsM2 + "\n", false);

		printMessage(textPane, color, 18, "Last battery status was :  ", false);
		printMessage(textPane, "green", 22, lastBattery + "%\n", false);

		tsLast = tsLast.substring(0, 2) + ":" + tsLast.substring(2, 4) + "  " + tsLast.substring(4, 6) + "-" + tsLast.substring(6, 8) + "-" + tsLast.substring(8);
		printMessage(textPane, color, 18, "Last event was :  ", false);
		printMessage(textPane, "green", 22, lastEvent2 + "  At  " + tsLast + "\n", false);

		stb.append("\n");
		stb = null;

		textPane.setCaretPosition(0);
	}

	/**
	 * Equivalent to calling <code>bytesToHex(b, 0, b.length)</code>.
	 */
	private String bytesToHex(byte[] b) {
		return bytesToHex(b, 0, b.length);
	}

	/**
	 * Converts a single byte to a hex string representation, can be decoded
	 * with Byte.parseByte().
	 * 
	 * @param b
	 *            the byte to encode
	 * @return a
	 */
	private String bytesToHex(byte[] b, int index, int length) {
		StringBuffer buf = new StringBuffer();
		byte leading, trailing;
		for (int pos = index; pos >= 0 && pos < index + length && pos < b.length; ++pos) {
			leading = (byte) ((b[pos] >>> 4) & 0x0f);
			trailing = (byte) (b[pos] & 0x0f);
			buf.append((0 <= leading) && (leading <= 9) ? (char) ('0' + leading) : (char) ('A' + (leading - 10)));
			buf.append((0 <= trailing) && (trailing <= 9) ? (char) ('0' + trailing) : (char) ('A' + (trailing - 10)));
		}
		return buf.toString();
	}

	private void exit(java.awt.event.ActionEvent evt) {
		System.exit(0);
	}

	private void loadPreferences() {
		FileInputStream is = null;
		try {
			File preferencesFile = new File("authclient.properties");
			if (preferencesFile.exists()) {
				is = new FileInputStream(preferencesFile);
				Properties p = new Properties();
				p.load(is);
			}

		} catch (IOException ex) {
			Logger.getLogger(SunayanaMain.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException ex) {
				Logger.getLogger(SunayanaMain.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	private void printMessage(JTextPane pane, String colorName, int fontSize, String text) {

		try {
			if (!deviceCheck) {
				Element root = pane.getDocument().getDefaultRootElement();
				while (root.getElementCount() > 101) {
					Element firstLine = root.getElement(0);
					try {
						pane.getDocument().remove(0, firstLine.getEndOffset());
					} catch (BadLocationException ble) {
						System.out.println(ble);
					}
				}
			}

			StyledDocument doc = (StyledDocument) pane.getDocument();
			Style style = doc.addStyle("StyleName", null);
			StyleConstants.setFontSize(style, fontSize);
			// StyleConstants.setBold(style, true);
			if (colorName.equalsIgnoreCase("GREEN")) {
				StyleConstants.setForeground(style, new java.awt.Color(0, 100, 0));
			} else if (colorName.equalsIgnoreCase("red")) {
				StyleConstants.setForeground(style, Color.RED);
			} else if (colorName.equalsIgnoreCase("gray")) {
				StyleConstants.setForeground(style, Color.DARK_GRAY);
			} else {
				StyleConstants.setForeground(style, Color.BLACK);
			}
			doc.insertString(doc.getLength(), "\n" + text, style);
			pane.setCaretPosition(pane.getDocument().getLength());

		} catch (Exception x) {
		}
	}

	private void printMessageGeneral(JTextPane pane, String text) {
		printMessage(pane, "black", 12, text);
	}

	private void printMessageSuccess(JTextPane pane, String text) {
		printMessage(pane, "green", 12, text);
	}

	private void printMessageErrorFatal(JTextPane pane, String text) {
		printMessage(pane, "red", 18, "FATAL: " + text);
	}

	private void printMessageError(JTextPane pane, String text) {
		printMessage(pane, "red", 12, text);
	}

	private boolean validate(JTextField[] txtComp) {
		for (int i = 0; i < txtComp.length; i++) {
			if (txtComp[i].getText().length() == 0) {
				JOptionPane.showMessageDialog(null, "The field '" + txtComp[i].getName() + "' can not be left blank.", "Open Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		return true;
	}

	private void displayReport(String dt) {

		List<DailyReport> dailyReportList = null;
		dailyReportList = new DailyReport().getDailyReport(dt);

		if (dailyReportList != null && !dailyReportList.isEmpty()) {
			int r = 0;
			if (dailyReportList.size() == 1) {
				r = 6;
			}
			if (dailyReportList.size() == 2) {
				r = 5;
			}
			if (dailyReportList.size() == 3) {
				r = 4;
			}
			if (dailyReportList.size() == 4) {
				r = 3;
			}
			if (dailyReportList.size() == 5) {
				r = 2;
			}
			if (dailyReportList.size() == 6) {
				r = 1;
			}

			Object[][] data = null;
			data = new Object[dailyReportList.size() + r][dailyReportList.size() + r];

			for (int i = 0; i < dailyReportList.size(); i++) {
				data[i][0] = dailyReportList.get(i).getReportDate();
				data[i][1] = dailyReportList.get(i).getDevice();
				data[i][2] = dailyReportList.get(i).getRecordCount();
				data[i][3] = dailyReportList.get(i).getResponseY();
				data[i][4] = dailyReportList.get(i).getResponse300();
				data[i][5] = dailyReportList.get(i).getResponseOther();
				data[i][6] = dailyReportList.get(i).getAvgResTime();
			}

			tableReport.setModel(new DefaultTableModel(data, new String[] { "Date", "Location", "Area Code", "Camera#", "Total# Clicks", "Dummy1", "Dummy2" }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			});

			DefaultTableModel tableModel = (DefaultTableModel) tableReport.getModel();
			if (tableModel.getValueAt(6, 0) == null) {
				tableModel.removeRow(6);
			}
			if (tableModel.getValueAt(5, 0) == null) {
				tableModel.removeRow(5);
			}
			if (tableModel.getValueAt(4, 0) == null) {
				tableModel.removeRow(4);
			}
			if (tableModel.getValueAt(3, 0) == null) {
				tableModel.removeRow(3);
			}
			if (tableModel.getValueAt(2, 0) == null) {
				tableModel.removeRow(2);
			}
			if (tableModel.getValueAt(1, 0) == null) {
				tableModel.removeRow(1);
			}
			if (tableModel.getValueAt(0, 0) == null) {
				tableModel.removeRow(0);
			}

			List<DailyReport> uidList = new DailyReport().getDistinctUid(dt);
			if (uidList != null && !uidList.isEmpty()) {
				int r1 = 0;
				if (uidList.size() == 1) {
					r1 = 2;
				}
				if (uidList.size() == 2) {
					r1 = 1;
				}
				Object[][] data1 = null;
				data1 = new Object[uidList.size() + r1][uidList.size() + r1];

				for (int j = 0; j < uidList.size(); j++) {
					data1[j][0] = j + 1;
					data1[j][1] = uidList.get(j).getDevice();
					data1[j][2] = uidList.get(j).getUid();
				}
				uidTable.setModel(new DefaultTableModel(data1, new String[] { "Dummy3", "Dummy4", "Dummy5" }) {
					@Override
					public boolean isCellEditable(int row, int column) {
						return false;
					}
				});
				DefaultTableModel tableModel1 = (DefaultTableModel) uidTable.getModel();
				if (tableModel1.getValueAt(2, 0) == null) {
					tableModel1.removeRow(2);
				}
				if (tableModel1.getValueAt(1, 0) == null) {
					tableModel1.removeRow(1);
				}
				if (tableModel1.getValueAt(0, 0) == null) {
					tableModel1.removeRow(0);
				}
			} else {
				uidTable.setModel(new DefaultTableModel(new Object[1][1], new String[] { "Dummy3", "Dummy4", "Dummy5" }));
				if (uidTable.getModel().getValueAt(0, 0) == null) {
					DefaultTableModel tm = (DefaultTableModel) uidTable.getModel();
					tm.removeRow(0);
				}
			}
		} else {
			tab5Message.setForeground(new java.awt.Color(255, 0, 0));
			tab5Message.setText("No report available for the date " + dt);
			tab5Message.setVisible(true);
			tableReport.setModel(new DefaultTableModel(new Object[1][1], new String[] { "Date", "Location", "Area Code", "Camera#", "Total# Clicks", "Dummy1", "Dummy2" }));
			if (tableReport.getModel().getValueAt(0, 0) == null) {
				DefaultTableModel tm = (DefaultTableModel) tableReport.getModel();
				tm.removeRow(0);
			}
			uidTable.setModel(new DefaultTableModel(new Object[1][1], new String[] { "Dummy3", "Dummy4", "Dummy5" }));
			if (uidTable.getModel().getValueAt(0, 0) == null) {
				DefaultTableModel tm = (DefaultTableModel) uidTable.getModel();
				tm.removeRow(0);
			}
		}
	}

	private void checkHeapMemory() {
		try {
			MemoryMXBean mbean = ManagementFactory.getMemoryMXBean();
			MemoryUsage usage = mbean.getHeapMemoryUsage();

			long max = usage.getMax() / (1024 * 1024);

			long used = usage.getUsed() / (1024 * 1024);

			long free = max - used;
		} catch (Exception e) {
		}
	}

	private long cpuUsage() {
		long memVal = 0;
		try {
			OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
			Method method = operatingSystemMXBean.getClass().getDeclaredMethod("getFreePhysicalMemorySize");
			method.setAccessible(true);

			Object value = method.invoke(operatingSystemMXBean);
			memVal = Long.parseLong((String.valueOf(value)));
		} catch (Exception e) {
		}
		// checkHeapMemory();
		return memVal;
	}

	private void printMessage(JTextPane pane, String colorName, int fontSize, String text, boolean bold) {
		try {
			StyledDocument doc = (StyledDocument) pane.getDocument();
			Style style = doc.addStyle("StyleName", null);
			StyleConstants.setFontSize(style, fontSize);
			StyleConstants.setBold(style, bold);
			if (colorName.equalsIgnoreCase("GREEN")) {
				StyleConstants.setForeground(style, new java.awt.Color(0, 100, 0));
			} else if (colorName.equalsIgnoreCase("red")) {
				StyleConstants.setForeground(style, Color.RED);
			} else if (colorName.equalsIgnoreCase("gray")) {
				StyleConstants.setForeground(style, Color.DARK_GRAY);
			} else {
				StyleConstants.setForeground(style, Color.BLACK);
			}
			doc.insertString(doc.getLength(), text, style);
			pane.setCaretPosition(pane.getDocument().getLength());
		} catch (Exception x) {
		}
	}

	private void createFile(String filename, byte[] dataLine) {
		FileChannel wChannel;
		try {
			ByteBuffer bbuf = ByteBuffer.wrap(dataLine);
			File outFile = new File(filename);
			wChannel = new FileOutputStream(outFile).getChannel();
			wChannel.write(bbuf);
			wChannel.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (CharacterCodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void cleanUpFile(File file) throws IOException {
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		FileWriter fw = new FileWriter(file.getAbsolutePath() + ".modified");
		System.out.println("Input File: " + file.getAbsolutePath());
		System.out.println("Out File: " + file.getAbsolutePath() + ".modified");
		BufferedWriter bw = new BufferedWriter(fw);
		String line = null;
		while ((line = br.readLine()) != null) {
			line.replaceAll("/n", "");
			bw.write(line);
			bw.flush();
		}
		br.close();
		fr.close();
		bw.close();
		fw.close();
	}
}
