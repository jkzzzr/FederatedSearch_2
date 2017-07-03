import java.io.File;

/**
 * 根据给定的文件夹下面的type文件，找到centroid中，对应序号的质心，并统一序号
 * @author Lee
 *
 */
public class ChooseCentroid {

	public static void main(String[] args) {
		File file = new File("/home/Lee/data/output-FR/K-means/abc");
		System.out.println(file.isDirectory());
		File files[] = file.listFiles();
	//	String centroidpath = "/home/Lee/data/FR结果/iter-final";
	//	String outputpath = "/home/Lee/data/output-FR/K-means/abc/";
		for (File file2:files){
	//		File filetemp = new File(centroidpath+"/" + file2.getName());
			String name = file2.getName();
			int inde = name.indexOf('.');
			if (inde < 0){
				
			}else {
				name = name.substring(0, inde);
			}
			System.out.println(name);
			file2.renameTo(new File("/home/Lee/data/output-FR/K-means/abc111/"+ name));
		}
	}

}
