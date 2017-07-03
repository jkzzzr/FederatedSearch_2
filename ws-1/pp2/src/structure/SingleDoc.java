package structure;

import java.util.HashMap;

public class SingleDoc  extends Base{

	public SingleDoc(HashMap<Integer, Double> termMap, double wordCount) {
		super(termMap, wordCount);
	}

	public SingleDoc(HashMap<Integer, Double> termMap, double wordCount,
			Integer docid) {
		super(termMap, wordCount, docid);
	}
	
}
