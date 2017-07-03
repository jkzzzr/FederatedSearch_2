package Structure;

import java.util.HashMap;

public class Base {
	/**
	 * 词项信息
	 */
	public HashMap<String, Double> termMap = new HashMap<String, Double>();
	/**
	 * 词项总数
	 */
	public double wordCount = 0;
	/**
	 * 文档名称
	 */
	public String docid = "";
	public HashMap<String, Double> getTermMap() {
		return termMap;
	}
	public void setTermMap(HashMap<String, Double> termMap) {
		this.termMap = termMap;
	}
	public double getWordCount() {
		return wordCount;
	}
	public void setWordCount(double wordCount) {
		this.wordCount = wordCount;
	}
	public String getDocid() {
		return docid;
	}
	public void setDocid(String docid) {
		this.docid = docid;
	}
	public Base(HashMap<String, Double> termMap, double wordCount, String docid) {
		super();
		this.termMap = termMap;
		this.wordCount = wordCount;
		this.docid = docid;
	}
	public Base(HashMap<String, Double> termMap, double wordCount) {
		super();
		this.termMap = termMap;
		this.wordCount = wordCount;
	}
	public Base() {
		// TODO Auto-generated constructor stub
	}
	
}
