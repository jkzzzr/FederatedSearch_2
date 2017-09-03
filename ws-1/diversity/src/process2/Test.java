package process2;

public class Test {

	public static void main(String[] args) {
		String abc = "a\tbaa  c ";
		String [] strings = abc.split("[a*|\t]");
		System.out.println(strings.length);
		for (int i = 0; i < strings.length; i++){
			System.out.println(strings[i]+"!");
		}
	}

}
