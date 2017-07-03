package process1;

public class Test {

	public static void main(String[] args) {
		String x = "a, b , c,";
		String []abc = x.split("\\s*,\\s*");
		for (int i = 0; i < abc.length; i++){
			System.out.println(abc[i]+"!");
		}
	}

}
