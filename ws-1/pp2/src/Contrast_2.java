package pack1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 由样本文档所属类别，得到两个文件：
 * 		一：样本文档的检索结果
 * 		二：样本文档的所属类别信息
 * @author Lee
 *
 */
public class Contrast_2 {

	private HashMap<Integer, Integer> sample_map = null;
	public static void main(String[] args) throws Exception {
		Contrast_2 contrast_2 = new Contrast_2();
		contrast_2.init2("E:\\实验数据\\FR-数据\\样本文档\\sample-序列化结果");
		contrast_2.init_all("E:\\实验数据\\123.res", "E:\\实验数据\\res1", "E:\\实验数据\\res2");
	}
	
	/**
	 * 序列化方式,初始化样本文档map (文档ID - type)
	 * @param path_map
	 * @return
	 * @throws Exception
	 */
	public HashMap<Integer, Integer> init2(String path_map) throws Exception{
		FileInputStream fis = new FileInputStream(new File(path_map));
		ObjectInputStream ois = new ObjectInputStream(fis);
		HashMap<Integer, Integer> hm = (HashMap<Integer, Integer>) ois.readObject();
		ois.close();
		fis.close();
		this.sample_map = hm;
		System.out.println("所有样本文档总数：\t" + this.sample_map.size());
		return hm;
	}

	/**
	 * 
	 * @param path_all	原始检索结果 - clueweb被替换成id
	 * @param outputPath1	样本文档检索结果
	 * @param outputPath2	(qid	docid	count	type	score)
	 * @return
	 * @throws IOException
	 */
	public HashMap<Integer, ArrayList<Integer>> init_all(String path_all, String outputPath1, String outputPath2) throws IOException{
		if (this.sample_map == null){
			return null;
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath1));
		BufferedWriter bw2 = new BufferedWriter(new FileWriter(outputPath2));
		bw2.write(String.format("%-8s","qid")
				+ String.format("%-8s","docid")
				+ String.format("%-8s","origin-count")
				+ String.format("%-8s","count")
				+ String.format("%-8s","type")
				+ String.format("%-8s","score")+"\n");
		HashMap<Integer, ArrayList<Integer>> result = new HashMap<Integer, ArrayList<Integer>>();
		BufferedReader br = new BufferedReader(new FileReader(path_all));
		String line = "";
		String qid = "-1";
		int count = 0;
		ArrayList<Integer> tempList = new ArrayList<Integer>();
		while ((line = br.readLine())!=null){
			String[] strings = line.split("\t| ");
			String qidTemp = strings[0];
			if (!qidTemp.equals(qid)){
			//	if (!qid.endsWith("-1")){
					result.put(Integer.parseInt(qid), tempList);
					qid = qidTemp;
					count = 0;
					tempList = new ArrayList<Integer>();
					bw.flush();
					bw2.flush();
			//	}
			}
			Integer docid = Integer.parseInt(strings[2]);
			if (this.sample_map.containsKey(docid)){
				bw.write(line + "\n");
				bw2.write(String.format("%-8s",strings[0] )
						+ String.format("%-9s",strings[2])
						+ String.format("%-9s",strings[3])
						+String.format("%-9s",(count ++)+"")
						+String.format("%-8s",this.sample_map.get(docid)) 
						+String.format("%-8s",strings[4]) 
						+ "\n");
			}
			tempList.add(docid);
		}
		//最终结束的时候，也要添加进来
		if (!qid.endsWith("-1")){
			result.put(Integer.parseInt(qid), tempList);
		}
		br.close();
		bw.close();
		bw2.close();
		return result;
	}
	
	
}
