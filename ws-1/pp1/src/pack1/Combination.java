package pack1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * 功能是，将100个样本文档合并，得到：一个docid-type的对应表；
 * 结果返回两个文件，一个是逐行输出，一个是序列化后的文件
 * @author Lee
 *
 */
public class Combination {

	public static void main(String[] args) throws IOException {
		Combination combination = new Combination();
		HashMap<Integer, Integer> hm = combination.init("E:\\实验数据\\FR-数据\\样本文档\\百分之一");
		combination.write2(hm, "E:\\实验数据\\FR-数据\\样本文档\\sample-序列化结果");
		combination.write1(hm, "E:\\实验数据\\FR-数据\\样本文档\\sample-逐行输出");
	}

	public HashMap<Integer, Integer> init(String inputPath) throws IOException{
		HashMap<Integer, Integer> result = new HashMap<Integer, Integer>();
		for (int i = 0; i < 100; i++){
			String temppath = inputPath+"/"+i;
			BufferedReader br = new BufferedReader(new FileReader(temppath));
			String line = "";
			while ((line = br.readLine())!=null){
				String []strings = line.split("[\t|\n|\r| ]");
				if (strings.length >1){
					for (int j = 0; j < strings.length; j++){
						Integer docidInteger =0;
						try{
							docidInteger = Integer.parseInt(strings[j]);
						}catch(Exception exception){
							System.err.println(i+"==");
							System.err.println(exception.getMessage());
							continue;
					//		System.exit(1);
						}
						result.put(docidInteger, i);
					}
				}else {
					Integer docid =0;
					try{
						docid = Integer.parseInt(line);
					}catch(Exception exception){
						System.err.println(i+"++");
						System.err.println(exception.getMessage());
						continue;
				//		System.exit(1);
					}
					result.put(docid, i);
				}
			}
		}
		System.out.println("init结束");
		return result;
	}
	/**
	 * 逐行输出
	 * @param hm
	 * @param path
	 * @throws IOException
	 */
	public void write1(HashMap<Integer, Integer> hm, String path) throws IOException{
		BufferedWriter bw = new BufferedWriter(new FileWriter(path));
		TreeSet<Integer> ts = new TreeSet<Integer>(hm.keySet());
		Iterator it = ts.iterator();
		while (it.hasNext()){
			Integer docid = (Integer) it.next();
			int type = hm.get(docid);
			bw.write(docid +"\t" + type+"\n");
		}
		bw.close();
		System.out.println("write1结束");
	}
	/**
	 * 序列化结果(文档id - type)
	 * @param hm
	 * @param path
	 * @throws IOException
	 */
	public void write2(HashMap<Integer, Integer> hm, String path) throws IOException{
		FileOutputStream fos = new FileOutputStream(new File(path));
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(hm);
		oos.close();
		fos.close();
		System.out.println("write2结束");
	}
}
