package filterCollection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
/**
 * 从文档列表中选择1%的文档
 * @author Administrator
 *
 */
public class Filter {

	public 	ArrayList<String> docList = new ArrayList<String>();
	public static void main(String[] args) {
		try{
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 将arraylist输出到文件中
	 * @param alist
	 * @param outputPath
	 * @throws Exception
	 */
	public void writeFile (String outputPath) throws Exception{
		FileOutputStream fos = new FileOutputStream(outputPath);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(docList);
		oos.flush();
		oos.close();
	}
	/**
	 * 按概率读取文件记录到arraylist
	 * @param inputPath
	 * @param percent
	 * @return
	 * @throws Exception
	 */
	public void readFile (String inputPath, double percent) throws Exception{
		BufferedReader bReader = new BufferedReader(new FileReader(inputPath));
		String line = "";
		while ((line = bReader.readLine())!=null){
			if (ischoose(percent)){
				docList.add(line);
			}
		}
		bReader.close();
	}
	/**
	 * 如果小于10%的话，就选择了，不是的话，就不选择了
	 * @param percent
	 * @return
	 */
	public boolean ischoose(double percent){
		double random = Math.random();
		if (random < percent){
			return false;
		}else {
			return true;
		}
	}

}
