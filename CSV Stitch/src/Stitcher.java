import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Scanner;

public class Stitcher {
	
	public static final int FILE_CREATED = 0xff;

//	private static JLabel progressLabel;
//	private static int total;
//	private static int index;
//	private static String current;
	
	public static int createFile(HashMap<String, Object> settings) throws IOException {
		//Create dialog displaying current file and overall progress		
//		JDialog dialog = new JDialog();
//		JPanel panel = new JPanel(new BorderLayout(8, 18));
//		panel.setBorder(new EmptyBorder(25, 50, 25, 50));
//		panel.add(new JLabel("Creating file..."), BorderLayout.NORTH);
//		progressLabel = new JLabel();
//		setLabelText();
//		panel.add(progressLabel, BorderLayout.CENTER);
//		
//		dialog.add(panel);
//		dialog.pack();
//		
//		dialog.setSize(new Dimension(300, 175));
//		dialog.setResizable(true);
//		dialog.setVisible(true);	
		
		//Create target file first
		String targetPath;
		
		Object targetDirectory = settings.get(Frame.SETTINGS_TARGET_DIRECTORY);
		Object targetFileName = settings.get(Frame.SETTINGS_FILENAME);
		if (targetDirectory instanceof File) {
			if (targetFileName instanceof String) {
				targetPath = ((File) targetDirectory).getPath() + "\\" + targetFileName;
				
			}
			else {
				throw new RuntimeException("Invalid type: " + targetFileName.getClass().getSimpleName());
				
			}
			
		}
		else {
			throw new RuntimeException("Invalid type: " + targetDirectory.getClass().getSimpleName());
			
		}
		
		File target = new File(targetPath);
		
		target.createNewFile();
		
		StitchMode mode = (StitchMode) settings.get(Frame.SETTINGS_STITCHMODE);
		PrintStream ps = new PrintStream(target);

        File[] files = (File[]) settings.get(Frame.SETTINGS_FILES);

		//Add custom header if necessary
		if (mode == StitchMode.CUSTOM_HEADER) {
			String header = (String) settings.get(Frame.SETTINGS_CUSTOM_HEADER);
			
			if (! header.equals("") || header == null) {
				ps.println(header);
				
			}
			
		}
		
		//Loop through all given files
		int i = 0;
		for (File file : files) {
			writeToTarget(ps, readFile(file, mode), mode, i++);
			
		}
		
		ps.close();
		return FILE_CREATED;
		
	}
	
	private static String[] readFile(File f, StitchMode mode) throws FileNotFoundException {
		Scanner scanner = new Scanner(f);
		String firstLine = null;
		StringBuilder sb = new StringBuilder();
		
		int i = 0;
		while (scanner.hasNextLine()) {
			String text = scanner.nextLine();
			
			if (i++ == 0) {
				firstLine = text;
				
			}
			else {
				sb.append(text + "\n");
				
			}
			
		}
		scanner.close();

		if (mode == StitchMode.COPY_ALL) {
			//Return body and first line as one string
			String body = sb.toString();
			
			if (firstLine != null) {
				body = firstLine + body;
				
			}
			else {
				throw new IllegalStateException("firstLine variable cannot be null");
				
			}
			
			return new String[] {body};
			
		}
		else if (mode == StitchMode.CUSTOM_HEADER || mode == StitchMode.NO_HEADER || mode == StitchMode.AUTO_DETECT) {
			//Return body only
			return new String[] {sb.toString()};
			
		}
		else if (mode == StitchMode.FIRST_FILE_HEADER) {
			//Return body and first line separately
			if (firstLine != null) {
				return new String[] {sb.toString(), firstLine};
				
			}
			else {
				throw new IllegalStateException("firstLine variable cannot be null");
				
			}
			
		}
		else {
			return null;
			
		}
		
	}

    private static String getFirstLine(File f) throws FileNotFoundException {
        Scanner s = new Scanner(f);

        if (s.hasNextLine()) {
            String text = s.nextLine();

            return text;

        }
        else return null;

    }
	
	private static void writeToTarget(PrintStream ps, String[] text, StitchMode mode, int fileIndex) throws FileNotFoundException {		
		if (mode == StitchMode.COPY_ALL || mode == StitchMode.NO_HEADER || mode == StitchMode.CUSTOM_HEADER) {
			if (text.length == 1) {
				ps.print(text[0]);
				
			}
			else {
				ps.close();
				throw new IllegalArgumentException("Length of array doesn't match the current StitchMode");
				
			}
			
		}
		else if (mode == StitchMode.FIRST_FILE_HEADER) {
			if (text.length == 2) {
				if (fileIndex == 0) {
					ps.println(text[1]);
					
				}
				
				ps.print(text[0]);
				
			}
			else {
				ps.close();
				throw new IllegalArgumentException("Length of array doesn't match the current StitchMode");
				
			}
			
		}
		
	}
	
}