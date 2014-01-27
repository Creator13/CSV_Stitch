import java.util.HashMap;

public class Stitcher {
	
	public StitchMode mode;
	public String customHeader;
	public String filename;	
	
	public Stitcher(HashMap<String, Object> settings, String[] files) {
		//Stitchmode
		this.mode = (StitchMode) ((settings.get("stitchmode") == null) ? null : settings.get("stitchmode"));
		if (this.mode == StitchMode.CUSTOM_HEADER) {
			if (settings.get("customheader") != null) {
				this.customHeader = (String) settings.get("customheader");
				
			}
			else {
				throw new IllegalArgumentException("Custom header may not be empty if stitchmode is StitchMode.CUSTOM_HEADER");
				
			}
			
		}
		
		//Filename
		String filename = (String) settings.get("filename");
		if (filename == null || filename == "") {
			throw new IllegalArgumentException("No filename specified");
			
		}
		else {
			this.filename =  filename;
		}
		
		//Get files
		//TODO
		
	}
	
	
	
}
