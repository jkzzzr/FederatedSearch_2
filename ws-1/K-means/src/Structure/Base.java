package Structure;

import java.io.Serializable;
import java.util.HashMap;

public class Base implements Serializable{
	/**
	 * 词项信息
	 */
	public HashMap<Integer, Double> termMap = new HashMap<Integer, Double>();
	/**
	 * 词项总数
	 */
	public double wordCount = 0;
	/**
	 * 文档名称
	 */
	public Integer docid = 0;
	public HashMap<Integer, Double> getTermMap() {
		return termMap;
	}
	public void setTermMap(HashMap<Integer, Double> termMap) {
		this.termMap = termMap;
	}
	public double getWordCount() {
		return wordCount;
	}
	public void setWordCount(double wordCount) {
		this.wordCount = wordCount;
	}
	public Integer getDocid() {
		return docid;
	}
	public void setDocid(Integer docid) {
		this.docid = docid;
	}
	public Base(HashMap<Integer, Double> termMap, double wordCount, Integer docid) {
		super();
		this.termMap = termMap;
		this.wordCount = wordCount;
		this.docid = docid;
	}
	public Base(HashMap<Integer, Double> termMap, double wordCount) {
		super();
		this.termMap = termMap;
		this.wordCount = wordCount;
	}
	public Base() {
	}
	
}
