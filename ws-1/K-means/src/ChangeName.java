import java.io.File;

public class ChangeName {
	public static void main(String[] args) {
	/*	String outputPath = "/home/Lee/data/FR结果/all-2/";
		File file = new File("/home/Lee/data/FR结果/all-2/centroid44");
		File[] files = file.listFiles();
		for (File file2:files){
			String string = file2.getName();
			Integer integer = Integer.parseInt(string);
			file2.renameTo(new File(outputPath+ (integer + 1000) ));
		}*/
		sortname();
	}
	public static void sortname(){
		String outputPath = "/home/Lee/data/output-FR/K-means/abc111-final";
		File file = new File("/home/Lee/data/output-FR/K-means/abc111");
		File[] files = file.listFiles();
		int i = 0;
		for (File file2:files){
				file2.renameTo(new File(outputPath+ ("/"+(i++)) ));
		}
	}

}
