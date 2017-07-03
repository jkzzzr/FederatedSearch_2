package xxx;

import java.io.File;

public class xxxxxxx {

	public static void main(String[] args) {
		System.out.println("12");
		File file  = new File("/home/Lee/data/结果备份/2/centroid92");
		File file2 = new File("/home/Lee/data/结果备份/2/centroid91/zzz");
		File[] files = file.listFiles();
		for (File file3 : files){
			String index = file3.getName();
		//	System.out.println(index);
			File file4 = new File("/home/Lee/data/结果备份/2/centroid92/" + ( Integer.parseInt(index) + 166));
			file3.renameTo(file4);
		}
	}

}
