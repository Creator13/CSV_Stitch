import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

@SuppressWarnings("rawtypes")
public class Frame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JTextField filenameField;
	private JTextField customHeaderField;
	private JComboBox<StitchMode> stitchModeSelector;
	private JList<String> fileList;
	private JButton createFileButton;
	private JButton selectFilesButton;
	private JButton selectDirectoryButton;
	private JLabel customHeaderLabel;
	private JFileChooser fc;
	
	private HashMap<String, Object> settings;
	
	protected StitchMode[] modes = 	new StitchMode[] {
			StitchMode.FIRST_FILE_HEADER, StitchMode.DETECT_HEADER, 
			StitchMode.CUSTOM_HEADER, StitchMode.NO_HEADER, StitchMode.COPY_ALL
	};

	/**
	 * Create the frame.
	 */
	public Frame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 589, 435);
		setResizable(false);
		setTitle("CSV Stitcher");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(9, 9, 9, 9));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled(true);
		
		JPanel settingsPanel = new JPanel();
		settingsPanel.setBorder(new EmptyBorder(0, 9, 0, 0));
		contentPane.add(settingsPanel, BorderLayout.CENTER);
		GridBagLayout gbl_settingsPanel = new GridBagLayout();
		gbl_settingsPanel.columnWidths = new int[]{0, 0, 100, 0, 0};
		gbl_settingsPanel.rowHeights = new int[]{0, 0, 0, 0, 19, 0, 0, 31, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_settingsPanel.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_settingsPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		settingsPanel.setLayout(gbl_settingsPanel);
		
		JLabel filenameLabel = new JLabel("File name:");
		filenameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_filenameLabel = new GridBagConstraints();
		gbc_filenameLabel.anchor = GridBagConstraints.EAST;
		gbc_filenameLabel.insets = new Insets(0, 0, 5, 5);
		gbc_filenameLabel.gridx = 0;
		gbc_filenameLabel.gridy = 2;
		settingsPanel.add(filenameLabel, gbc_filenameLabel);
		
		filenameField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 2;
		gbc_textField.gridy = 2;
		settingsPanel.add(filenameField, gbc_textField);
		filenameField.setColumns(10);
		
		JLabel extensionLabel = new JLabel(".csv");
		GridBagConstraints gbc_extensionLabel = new GridBagConstraints();
		gbc_extensionLabel.insets = new Insets(0, 0, 5, 0);
		gbc_extensionLabel.gridx = 3;
		gbc_extensionLabel.gridy = 2;
		settingsPanel.add(extensionLabel, gbc_extensionLabel);
		
		selectDirectoryButton = new JButton("Select directory");
		GridBagConstraints gbc_selectDirectoryButton = new GridBagConstraints();
		gbc_selectDirectoryButton.anchor = GridBagConstraints.WEST;
		gbc_selectDirectoryButton.insets = new Insets(0, 0, 5, 5);
		gbc_selectDirectoryButton.gridx = 2;
		gbc_selectDirectoryButton.gridy = 3;
		settingsPanel.add(selectDirectoryButton, gbc_selectDirectoryButton);
		
		JLabel headerLabel = new JLabel("Header:");
		GridBagConstraints gbc_headerLabel = new GridBagConstraints();
		gbc_headerLabel.anchor = GridBagConstraints.EAST;
		gbc_headerLabel.insets = new Insets(0, 0, 5, 5);
		gbc_headerLabel.gridx = 0;
		gbc_headerLabel.gridy = 5;
		settingsPanel.add(headerLabel, gbc_headerLabel);
		
		stitchModeSelector = new JComboBox<StitchMode>();
//		stitchModeSelector.setEditable(true);
		stitchModeSelector.setModel(new DefaultComboBoxModel<StitchMode>(modes));
		stitchModeSelector.setRenderer(new StitchModeComboboxRenderer());
		stitchModeSelector.addActionListener(new ModeSelectionListener());
		GridBagConstraints gbc_stitchModeSelector = new GridBagConstraints();
		gbc_stitchModeSelector.insets = new Insets(0, 0, 5, 5);
		gbc_stitchModeSelector.fill = GridBagConstraints.HORIZONTAL;
		gbc_stitchModeSelector.gridx = 2;
		gbc_stitchModeSelector.gridy = 5;
		settingsPanel.add(stitchModeSelector, gbc_stitchModeSelector);
		
		customHeaderLabel = new JLabel("Custom header:");
		customHeaderLabel.setEnabled(false);
		GridBagConstraints gbc_customHeaderLabel = new GridBagConstraints();
		gbc_customHeaderLabel.anchor = GridBagConstraints.EAST;
		gbc_customHeaderLabel.insets = new Insets(0, 0, 5, 5);
		gbc_customHeaderLabel.gridx = 0;
		gbc_customHeaderLabel.gridy = 6;
		settingsPanel.add(customHeaderLabel, gbc_customHeaderLabel);
		
		customHeaderField = new JTextField();
		customHeaderField.setEnabled(false);
		GridBagConstraints gbc_customHeaderField = new GridBagConstraints();
		gbc_customHeaderField.insets = new Insets(0, 0, 5, 5);
		gbc_customHeaderField.fill = GridBagConstraints.HORIZONTAL;
		gbc_customHeaderField.gridx = 2;
		gbc_customHeaderField.gridy = 6;
		settingsPanel.add(customHeaderField, gbc_customHeaderField);
		customHeaderField.setColumns(10);
		
		createFileButton = new JButton("Create file!");
		GridBagConstraints gbc_createFileButton = new GridBagConstraints();
		gbc_createFileButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_createFileButton.insets = new Insets(0, 0, 5, 5);
		gbc_createFileButton.gridx = 2;
		gbc_createFileButton.gridy = 8;
		settingsPanel.add(createFileButton, gbc_createFileButton);
		
		JPanel listPanel = new JPanel();
		contentPane.add(listPanel, BorderLayout.WEST);
		listPanel.setLayout(new BorderLayout(0, 0));
		
		fileList = new JList<String>();
		fileList.setModel(new DefaultListModel<String>());
		fileList.setPreferredSize(new Dimension(256, 0));
		listPanel.add(fileList);
		fileList.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		selectFilesButton = new JButton("Select files");
		selectFilesButton.addActionListener(new FileSelectorListener());
		listPanel.add(selectFilesButton, BorderLayout.SOUTH);
		
	}
	
	private void saveSettings() {
		//Get stitch mode and, if available, custom header.
		StitchMode mode = (StitchMode) stitchModeSelector.getSelectedItem();
		settings.put("stitchmode", mode);
		if (mode == StitchMode.CUSTOM_HEADER) {
			settings.put("customheader", customHeaderField.getText());
			
		}
		else {
			settings.put("customheader", null);
			
		}
		
		//Get filename
		settings.put("filename", filenameField.getText());
		
		//Get files
		
	}
	
	private class StitchModeComboboxRenderer extends JLabel implements ListCellRenderer {
		
		private static final long serialVersionUID = 1L;

		public StitchModeComboboxRenderer() {
			setOpaque(true);
			
		}

		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
			
			setText(((StitchMode) value).getTrivialName());
			setBorder(new EmptyBorder(2, 2, 2, 2));
			
			return this;
			
		}
		
	}
	
	private class ModeSelectionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == stitchModeSelector) {
				StitchMode mode = (StitchMode) stitchModeSelector.getSelectedItem();
				
				if (mode == StitchMode.CUSTOM_HEADER) {
					customHeaderField.setEnabled(true);
					customHeaderLabel.setEnabled(true);
					
				}
				else {
					customHeaderField.setEnabled(false);
					customHeaderLabel.setEnabled(false);
					
				}
					
			}
			
		}
		
	}
	
	private class FileSelectorListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == selectFilesButton) {
				if ((fc.showDialog(((JButton) e.getSource()).getParent(), "Select") == JFileChooser.APPROVE_OPTION)) {
					
					File[] files = fc.getSelectedFiles();
					for (File file : files) {
						((DefaultListModel<String>) fileList.getModel()).addElement(file.getName());
					}
					
				}
				
			}
			
		}
		
	}

}
