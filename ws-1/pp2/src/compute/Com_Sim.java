package compute;

import java.util.HashMap;
import java.util.Map.Entry;
/**
 * 计算文档之间的相似度
 * @author lee
 *
 */
public class Com_Sim {

	public HashMap<String, Double> simMap = new HashMap<String,Double>();
	public Doc_Origin_Builder builder = new Doc_Origin_Builder();
	//这个qid并没有什么意义，因为每个查询都要单独建立一个计算相似度的对象，所以姑且设置一个qid把，但是作用的话，真没有
	public int qid = 0;
	public Com_Sim(int q){
		this.qid = q;
	}
	/**
	 * 位置是有影响的!!
	 * 如果没有的话就计算，计算完了存储下来，如果存储有的话，就不用计算了
	 * @param i1 文档编号，miu为0
	 * @param i2 文档编号，miu为1000
	 * @return
	 */
	public double getSim(String i1, String i2){
		String key = i1+"+"+i2;
		if (simMap.containsKey(key)){
			return simMap.get(key);
		}
		Doc_Origin doc_Origin1 = builder.get(i1);
		Doc_Origin doc_Origin2 = builder.get(i2);
		Doc_Miu doc_Miu1 = new Doc_Miu(doc_Origin1, 0, this.qid);
		Doc_Miu doc_Miu2 = new Doc_Miu(doc_Origin2, 1000, this.qid);
	/*	System.out.println(i1+"\t"+i2);*/
		double result = getSim(doc_Miu1, doc_Miu2);
		simMap.put(key, result);
		return result;
	}
	public double getSim(Doc_Miu doc_Miu1 , Doc_Miu doc_Miu2){
		HashMap<String, Double> hashmap = doc_Miu1.WordProbability;
		double klValue = 0.0;
		for (Entry<String, Double> entry : hashmap.entrySet()){
			String wordid = entry.getKey();
			double score1 = entry.getValue();
			double score2 = doc_Miu2.get(wordid);
			double tempvalue = score1 * (Math.log(score1/score2));
			klValue +=tempvalue;
		}
		klValue = Math.exp( -klValue);
		return klValue;
	}
}
