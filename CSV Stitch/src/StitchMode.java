public enum StitchMode {
	
	COPY_ALL("Copy all headers", "Copy the full files into a new file, including their headers (some files may not have headers, then this is the right option)", true), 
	FIRST_FILE_HEADER("First file's header", "The first line of the first file will be used as the header. The first lines of other files wont be copied.", true), 
	NO_HEADER("Remove headers", "Remove the first line of every file.", false), 
//	DETECT_HEADER("Detect header", "Detects the header automatically using an algorithm.", false), 
	CUSTOM_HEADER("Custom header", "Remove the first line of every file, and add a custom header as the new first line.", true),
    AUTO_DETECT("Auto-detect header", "Automatically detects the header of the file", true);
	
	private String trivialName;
	private String description;
	private boolean recommended;
	
	private StitchMode(String trivialName, String desc, boolean recommended) {
		this.trivialName = trivialName;
		this.description = desc;
		this.recommended = recommended;
		
	}
	
	public String getTrivialName() {
		return this.trivialName;
		
	}
	
	public String getDescription() {
		return this.description;
		
	}
	
	public boolean isRecommended() {
		return recommended;
		
	}
	
}
