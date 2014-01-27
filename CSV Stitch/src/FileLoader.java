import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileLoader {
	
	public static String[] readFile(String path) {
		ArrayList<String> lines = new ArrayList<>();
		
		try {
			Scanner scanner = new Scanner(new FileInputStream(path));
			while (scanner.hasNextLine()) {
				lines.add(scanner.nextLine());
				
			}
			scanner.close();
			
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
			
		}
		
		return lines.toArray(new String[lines.size()]);
		
	}
	
}
