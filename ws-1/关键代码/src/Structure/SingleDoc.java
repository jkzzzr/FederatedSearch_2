package Structure;

import java.util.ArrayList;
import java.util.HashMap;

public class SingleDoc  extends Base{

	public SingleDoc(HashMap<String, Double> termMap, double wordCount) {
		super(termMap, wordCount);
	}

	public SingleDoc(HashMap<String, Double> termMap, double wordCount,
			String docid) {
		super(termMap, wordCount, docid);
	}
	
}
