package structure;

import java.util.StringTokenizer;

public class Data_ {

	private int qid;
	private int docid;
	//文档排名
	private int order;
	private double score;
	
	public Data_(String line){
		StringTokenizer st = new StringTokenizer(line);
		this.qid = Integer.parseInt(st.nextToken());
		st.nextToken();
		this.docid = Integer.parseInt(st.nextToken());
		this.order = Integer.parseInt(st.nextToken());
		this.score = Double.parseDouble(st.nextToken());
	}
	
	public Data_(int qid, int docid, int order, double score) {
		super();
		this.qid = qid;
		this.docid = docid;
		this.order = order;
		this.score = score;
	}

	public int getQid() {
		return qid;
	}
	public void setQid(int qid) {
		this.qid = qid;
	}
	public int getDocid() {
		return docid;
	}
	public void setDocid(int docid) {
		this.docid = docid;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public double getScore() {
		double result = Math.log(60.0 / (1 + this.order));
		return result;
	//	return score;
	}
	public void setScore(double score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return String.format("%-8s",this.qid) + "\t "
				+ String.format("%-8s","Q0") + "\t "
				+ String.format("%-8s", this.docid) + "\t "
				+ String.format("%-8s", this.order) + "\t "
				+ String.format("%-8s", this.score) + "\t "
				+ String.format("%-8s", "FR---MMR") + "\t ";
	}


	public String toString2(String docString) {
		return String.format("%-8s",this.qid) + "\t "
				+ String.format("%-8s","Q0") + "\t "
				+ String.format("%-8s", docString) + "\t "
				+ String.format("%-8s", this.order) + "\t "
				+ String.format("%-8s", this.score) + "\t "
				+ String.format("%-8s", "FR---MMR") + "\t ";
	}
}
