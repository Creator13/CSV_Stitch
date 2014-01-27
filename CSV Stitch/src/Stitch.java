public class Stitch {

	public static String stitch(String[]... fileContents) {
		StringBuilder sb = new StringBuilder();
		
		for (String[] file : fileContents) {
			for (String line : file) {
				sb.append(line + "\n");
				
			}
			
		}
		
		sb.deleteCharAt(sb.length() - 1);
		
		return sb.toString();
		
	}
	
}
