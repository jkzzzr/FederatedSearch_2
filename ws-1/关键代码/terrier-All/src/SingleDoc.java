
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class SingleDoc implements Serializable{
	private HashMap<String, Double> termMap = new HashMap<String, Double>();
	private double wordCount = 0;
	public HashMap<String, Double> getTermMap() {
		return termMap;
	}

	public SingleDoc(HashMap<String, Double> termMap, double wordCount) {
		super();
		this.termMap = termMap;
		this.wordCount = wordCount;
	}

	public void setTermMap(HashMap<String, Double> termMap) {
		this.termMap = termMap;
	}
	public double tokenierCount(){
		//TODO
		return 0.0;
	}
}
