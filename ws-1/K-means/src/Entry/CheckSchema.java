import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * 查看type文件中包含的文件总数
 * @author Lee
 *
 */
public class CheckSchema {

	public static void main(String[] args) throws Exception {
		HashSet<String> hs = new HashSet<>();
		ArrayList<Integer> COUNT = new ArrayList<>();
		File file = new File("/home/Lee/data/FR结果/all-type");
		File [] files = file.listFiles();
		int count = 0;
		for (File file2:files){
		/*for (int i = 0; i < files.length; i++){
			File file2 = new File(file.getAbsolutePath()+"/" + i);
			if (!file2.exists()){
				COUNT.add(0);
				continue;
			}
			BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()+"/" + i));*/
			
			BufferedReader br = new BufferedReader(new FileReader(file2));
			String line = "";
			int templine = 0;
			while ((line = br.readLine())!=null){
				templine ++;
				if (hs.contains(line)){
					System.out.println(line);
					count ++;
				}else {
					hs.add(line);
				}
			}
			COUNT.add(templine);
		}
		System.out.println("END\t"+count+"\tALL:\t"+hs.size());
		Collections.sort(COUNT);
		COUNT.forEach((o1)->{
			System.out.println(o1);
		});
	}

}
