import java.util.ArrayList;


public class Test {

	private static ArrayList<String> list = new ArrayList<>();
	
	public static void main(String[] args) {
    	System.out.println();
    	
    	add(false, "Hello");
    	System.out.println(list.get(0));
    	add(false, "World!");
    	System.out.println(list.get(1));
    	add(false, "Hello");
    	System.out.println(list.get(2));
		
	}
	
	private static void add(boolean allowDuplicate, String s) {
		if (! allowDuplicate) {
			if (list.contains(s)) {
				System.out.println("Contianes");
				return;
			}
			
		}
		else {
			list.add(s);
			
		}
		
	}
	
}
