import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class Main {
	
	public static void main(String[] args) {
		setLookAndFeel();
		
		Frame frame = new Frame();
		frame.setVisible(true);

//		String[] fileContents1 = FileLoader.readFile("C:\\test\\csv1.csv");
//		String[] fileContents2 = FileLoader.readFile("C:\\test\\csv2.csv");
//		String[] fileContents3 = FileLoader.readFile("C:\\test\\csv3.csv");
//		
//		String newFile = Stitch.stitch(fileContents1, fileContents2, fileContents3);
//		
//		System.out.println(newFile);
//		
//		createNewFile("test", newFile);
		
	}
	
	public static void createNewFile(String name, String contents) {
		try {
			File newFile = new File("C:\\test\\" + name + ".csv");
			newFile.createNewFile();
			
			PrintWriter writer = new PrintWriter(newFile);
			writer.print(contents);
			writer.close();
			
			System.out.println("file succesfully made!");
			
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
	
}
