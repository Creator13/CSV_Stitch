import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class Main {
	
	public static void main(String[] args) {
		if (getOSName() != OSEnum.WINDOWS) {
			JOptionPane.showMessageDialog(
					null, 
					"This application cannot run on the operating system you are using.", 
					"Unsupported", 
					JOptionPane.ERROR_MESSAGE);
			return;
			
		}
		
		setLookAndFeel();
		
		Frame frame = new Frame();
		frame.setVisible(true);
		
	}
	
	public static void createNewFile(String name, String contents) {
		try {
			File newFile = new File("C:\\test\\" + name + ".csv");
			newFile.createNewFile();
			
			PrintWriter writer = new PrintWriter(newFile);
			writer.print(contents);
			writer.close();
			
			System.out.println("File succesfully made!");
			
		}
		catch (IOException e) {
			e.printStackTrace();
			return;
			
		}
		
	}
	
	/**
	 * Sets the swing look and feel to the current system's look and feel.
	 * Works best on Windows.
	 */
	private static void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			//Failed to set look and feel, will use the default swing look and 
			//feel automatically instead.
			
		}
		
	}
	
	private static OSEnum getOSName() {
		String os_ = System.getProperty("os.name").toLowerCase();
		
		if (os_.indexOf("win") >= 0) {
			return OSEnum.WINDOWS;
	 
		} 
		else if (os_.indexOf("mac") >= 0) {
			return OSEnum.MACOS;
			
		} 
		else if (os_.indexOf("nix") >= 0 
				|| os_.indexOf("nux") >= 0 
				|| os_.indexOf("aix") >= 0 ) {
			return OSEnum.LINUX;
			
		}
		else {
			return OSEnum.OTHER;
			
		}
		
	}
	
}
