import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;

@SuppressWarnings("rawtypes")
public class Frame extends JFrame {

	private static final long serialVersionUID = 1L;

	public static final String SETTINGS_CUSTOM_HEADER = "customheader";
	public static final String SETTINGS_FILENAME = "filename";
	public static final String SETTINGS_FILES = "files";
	public static final String SETTINGS_STITCHMODE = "stitchmode";
	public static final String SETTINGS_TARGET_DIRECTORY = "targetdir";
    public static final int SETTINGS_AUTO_DETECT_MAX_FAILURES = 0;
	
	private JPanel contentPane;
	private JTextField filenameField;
	private JTextField customHeaderField;
	private JComboBox<StitchMode> stitchModeSelector;
	private JList<String> fileJList;
	private DefaultListModel<String> fileJListModel;
	private JButton createFileButton;
	private JButton selectFilesButton;
	private JButton selectDirectoryButton;
	private JLabel customHeaderLabel;
	private JFileChooser fc;
	private JPanel fileSelectorPanel;
	private JButton removeFileButton;
	
	private Stitcher stitcher;
	private HashMap<String, Object> settings;
	private ArrayList<File> files;
	private HashMap<String, Integer> indexList;
	private File targetDirectory;
	
	protected StitchMode[] modes = 	new StitchMode[] {
			StitchMode.FIRST_FILE_HEADER, /*StitchMode.AUTO_DETECT,*/
			StitchMode.CUSTOM_HEADER, StitchMode.NO_HEADER, StitchMode.COPY_ALL
	};
	
	//TODO: Finish this option to check for duplicates and remove them.
	private JLabel infoIcon;
	private JCheckBox checkBox;
	private JLabel lblCheckForDuplicate;

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
		fc.setFileFilter(new ExtensionFileFilter("Comma-separated files (\".csv\", \".txt\")", new String[] {"CSV", "TXT"}));
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled(true);
		
		files = new ArrayList<>();
		indexList = new HashMap<>();
		settings = new HashMap<>();
		
		JPanel settingsPanel = new JPanel();
		settingsPanel.setBorder(new EmptyBorder(0, 9, 0, 0));
		contentPane.add(settingsPanel, BorderLayout.CENTER);
		GridBagLayout gbl_settingsPanel = new GridBagLayout();
		gbl_settingsPanel.columnWidths = new int[]{0, 0, 100, 0, 0};
		gbl_settingsPanel.rowHeights = new int[]{0, 0, 0, 0, 19, 0, 0, 27, 0, 0, 0, 0, 0, 0, 0, 0};
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
		selectDirectoryButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				
				if (chooser.showOpenDialog(Frame.this) == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					
					if (file.isDirectory()) {
						targetDirectory = file;
						
					}
					
				}
				
			}
			
		});
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
		stitchModeSelector.setModel(new DefaultComboBoxModel<StitchMode>(modes));
		stitchModeSelector.setRenderer(new StitchModeComboboxRenderer());
		stitchModeSelector.addActionListener(new ModeSelectionListener());
		GridBagConstraints gbc_stitchModeSelector = new GridBagConstraints();
		gbc_stitchModeSelector.insets = new Insets(0, 0, 5, 5);
		gbc_stitchModeSelector.fill = GridBagConstraints.HORIZONTAL;
		gbc_stitchModeSelector.gridx = 2;
		gbc_stitchModeSelector.gridy = 5;
		settingsPanel.add(stitchModeSelector, gbc_stitchModeSelector);
		
		infoIcon = new JLabel();
		infoIcon.setIcon(new ImageIcon("res/img/info.png"));
		infoIcon.setToolTipText(((StitchMode) stitchModeSelector.getSelectedItem()).getDescription());
		infoIcon.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				//Unused
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				//Unused
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				//Unused
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				//Unused
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(
						Frame.this, 
						((StitchMode) stitchModeSelector.getSelectedItem()).getDescription(), 
						"Information - " + ((StitchMode) stitchModeSelector.getSelectedItem()).getTrivialName(), 
						JOptionPane.INFORMATION_MESSAGE);
				
			}
			
		});
		GridBagConstraints gbc_infoIcon = new GridBagConstraints();
		gbc_infoIcon.insets = new Insets(0, 0, 5, 0);
		gbc_infoIcon.gridx = 3;
		gbc_infoIcon.gridy = 5;
		settingsPanel.add(infoIcon, gbc_infoIcon);
		
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
		createFileButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkInputs()) {
					saveSettings();
					
					try {
						if (Stitcher.createFile(settings) == Stitcher.FILE_CREATED) {
							JOptionPane.showMessageDialog(Frame.this, "File created!");
							
						}
						
					}
					catch (IOException ex) {
						ex.printStackTrace();
						
					}
					
				}
				
			}
			
		});
		
		checkBox = new JCheckBox("");
		checkBox.setEnabled(false);
		GridBagConstraints gbc_checkBox = new GridBagConstraints();
		gbc_checkBox.insets = new Insets(0, 0, 5, 5);
		gbc_checkBox.gridx = 1;
		gbc_checkBox.gridy = 7;
		settingsPanel.add(checkBox, gbc_checkBox);
		
		lblCheckForDuplicate = new JLabel("Check for \n duplicate files");
		lblCheckForDuplicate.setEnabled(false);
		GridBagConstraints gbc_lblCheckForDuplicate = new GridBagConstraints();
		gbc_lblCheckForDuplicate.anchor = GridBagConstraints.WEST;
		gbc_lblCheckForDuplicate.insets = new Insets(0, 0, 5, 5);
		gbc_lblCheckForDuplicate.gridx = 2;
		gbc_lblCheckForDuplicate.gridy = 7;
		settingsPanel.add(lblCheckForDuplicate, gbc_lblCheckForDuplicate);
		GridBagConstraints gbc_createFileButton = new GridBagConstraints();
		gbc_createFileButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_createFileButton.insets = new Insets(0, 0, 5, 5);
		gbc_createFileButton.gridx = 2;
		gbc_createFileButton.gridy = 9;
		settingsPanel.add(createFileButton, gbc_createFileButton);
		
		JPanel listPanel = new JPanel();
		contentPane.add(listPanel, BorderLayout.WEST);
		listPanel.setLayout(new BorderLayout(0, 0));
		
		fileJListModel = new DefaultListModel<>();
		
		fileJList = new JList<String>();
		fileJList.setModel(fileJListModel);
		fileJList.setPreferredSize(new Dimension(256, 0));
		listPanel.add(fileJList);
		fileJList.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		fileSelectorPanel = new JPanel();
		FlowLayout fl_fileSelectorPanel = (FlowLayout) fileSelectorPanel.getLayout();
		fl_fileSelectorPanel.setVgap(0);
		fl_fileSelectorPanel.setHgap(0);
		listPanel.add(fileSelectorPanel, BorderLayout.SOUTH);
		
		selectFilesButton = new JButton("Add files");
		selectFilesButton.addActionListener(new FileSelectorListener());
		fileSelectorPanel.add(selectFilesButton);
		
		removeFileButton = new JButton("Remove files");
		removeFileButton.addActionListener(new FileRemoverListener());
		fileSelectorPanel.add(removeFileButton);
		
	}
	
	private void saveSettings() {
		//Get stitch mode and, if available, custom header.
		StitchMode mode = (StitchMode) stitchModeSelector.getSelectedItem();
		System.out.println(mode);
		settings.put(SETTINGS_STITCHMODE, mode);
		if (mode == StitchMode.CUSTOM_HEADER) {
			settings.put(SETTINGS_CUSTOM_HEADER, customHeaderField.getText());
			
		}
		else {
			settings.put(SETTINGS_CUSTOM_HEADER, null);
			
		}
		
		//Get filename & path
		String filename = filenameField.getText() + ".csv";
		settings.put(SETTINGS_FILENAME, filename);
		settings.put(SETTINGS_TARGET_DIRECTORY, targetDirectory);
		
		//Get files
		settings.put(SETTINGS_FILES, files.toArray(new File[files.size()]));
		
		//Clear files to save RAM space, because there might be quite a lot (supposing user will not create a second file with the same sources)
		//TODO: add option to keep files
		files.clear();
		fileJListModel.clear();
		
	}
	
	public boolean checkInputs() {
		//Check filename field
		if (filenameField.getText().equals("")) {
			JOptionPane.showMessageDialog(this, "No filename specified", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
			
		}
		
		//Check directory
		if (targetDirectory == null) {
			//Create default directory
			JFileChooser fc = new JFileChooser();
			FileSystemView fsv = fc.getFileSystemView();
			File defDir = fsv.getDefaultDirectory();
			int option = JOptionPane.showConfirmDialog(
					this, 
					"No directory for target file specified\n\n Use \"" + defDir.getAbsolutePath() + "\" instead?", 
					"Error", 
					JOptionPane.OK_CANCEL_OPTION, 
					JOptionPane.ERROR_MESSAGE);
			
			if (option == JOptionPane.OK_OPTION) {
				targetDirectory = defDir;
				
			}
			else if (option == JOptionPane.CANCEL_OPTION) {
				return false;
				
			}
			
		}
		
		//Check selected files
		if (files.isEmpty()) {
			JOptionPane.showMessageDialog(this, "No files selected", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
			
		}
		
		//If custom header needed, check if there is one
		if (stitchModeSelector.getSelectedItem() == StitchMode.CUSTOM_HEADER) {
			if (customHeaderField.getText().equals("")) {
				JOptionPane.showMessageDialog(this, "No custom header specified", "Error", JOptionPane.ERROR_MESSAGE);
				return false;
				
			}
			
		}
		
		return true;
		
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
			setToolTipText(((StitchMode) value).getDescription());
			setBorder(new EmptyBorder(2, 2, 2, 2));
			
			return this;
			
		}
		
	}
	
	private class ExtensionFileFilter extends FileFilter {
		String description;
		
		String extensions[];
		
		public ExtensionFileFilter(String description, String extension) {
			  this(description, new String[] {extension});
			  
		}
		
		public ExtensionFileFilter(String description, String extensions[]) {
			if (description == null) {
				this.description = extensions[0];
				
			}
			else {
				this.description = description;
				
			}
			this.extensions = (String[]) extensions.clone();
			toLower(this.extensions);
			
		}

		private void toLower(String array[]) {
			for (int i = 0, n = array.length; i < n; i++) {
				array[i] = array[i].toLowerCase();
				
			}
			
		}

		public String getDescription() {
			return description;
			
		}

		public boolean accept(File file) {
			if (file.isDirectory()) {
				return true;
				
			}
			else {
				String path = file.getAbsolutePath().toLowerCase();
				
				for (int i = 0, n = extensions.length; i < n; i++) {
					String extension = extensions[i];
					if ((path.endsWith(extension) && (path.charAt(path.length() - extension.length() - 1)) == '.')) {
						return true;
						
					}
					
				}
				
			}
			
			return false;
			
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
				
				infoIcon.setToolTipText(mode.getDescription());
				
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
						if (! Frame.this.files.contains(file)) {
							//Add to JList
							fileJListModel.addElement(file.getName());
							
							//Add to filelist and make pointer
							Frame.this.files.add(file);
							indexList.put(file.getName(), Frame.this.files.indexOf(file));
							
						}
						
					}
					
					//TODO: Make alert when files could not be added because they already were. Show list of all files to which this applies.
					
				}
				
			}
			
		}
		
	}
	
	private class FileRemoverListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == removeFileButton) {
				List<String> selected = fileJList.getSelectedValuesList();
				
				for (String s : selected) {
					//Remove item form JList
					fileJListModel.removeElement(s);
					
					//Remove item using the pointer and remove pointer
					int index = indexList.get(s);
					files.remove(index);
					indexList.remove(s);
					
					refreshPointers();
					
				}
				
			}
			
		}
		
		public void refreshPointers() {
			for (File file : files) {
				if (indexList.containsKey(file.getName())) {
					indexList.put(file.getName(), files.indexOf(file));
					
				}
				
			}
			
		}
		
	}

}
