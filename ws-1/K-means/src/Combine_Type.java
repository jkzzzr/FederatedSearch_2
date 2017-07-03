import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * 得到的3部分的，type，合并到一起，
 * @author Lee
 *
 */
public class Combine_Type {

	public static void main(String[] args) throws Exception {
		String outputPath = "/media/LIANGLEE/重新得到的type/iter0-final/";
		BufferedWriter bWriter = null;
		
		
		
		File file = new File("/media/LIANGLEE/重新得到的type/iter0-3");
		File[] files = file.listFiles();
		for (File file2: files){
			bWriter = new BufferedWriter(new FileWriter(outputPath + file2.getName(), true));
			
			BufferedReader br = new BufferedReader(new FileReader(file2));
			String line = "";
			while ((line = br.readLine())!=null){
				bWriter.write(line);
				bWriter.write("\n");
			}
			bWriter.close();
			br.close();
		}
	}

}
