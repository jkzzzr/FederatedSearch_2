package similarity;

import java.util.ArrayList;
import java.util.HashMap;

import process2.Pre_resultTOsingledoc;
import structure.SingleDoc;
import compute.Com_Sim_K_means;

public class Sim {

	public static void main(String[] args) {
		//示例：
		Sim sim = new Sim(0, "");
		sim.getSim(0, 0);
	}

	private int qid = 0;
	private String inputPathString = "";
	
	public Sim(int qid, String inputPathString) {
		super();
		this.qid = qid;
		this.inputPathString = inputPathString;
	}

	private HashMap<Integer, HashMap<Integer, Double>> simMap = new HashMap<Integer, HashMap<Integer,Double>>();
	private Com_Sim_K_means CSK = null;
	
	/**
	 * 初始化，计算相似度的对象Com_Sim_K_means
	 * @param inputPath
	 * @param qid
	 * @return
	 */
	public Com_Sim_K_means init(String inputPath, int qid){
		Pre_resultTOsingledoc p = new Pre_resultTOsingledoc();
		ArrayList<Integer> alArrayList = new ArrayList<Integer>();
		ArrayList<SingleDoc> result = new ArrayList<SingleDoc>();
		try {
			alArrayList = p.init_al_int(inputPath, qid);
			result = p.init_al_singledoc(alArrayList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Com_Sim_K_means csk = new Com_Sim_K_means(qid, true);
		csk.allDocs = result;
		return csk;
	}
	public double getSim(int index1, int index2){
		if (index1 > index2){
			int temp = index1;
			index1 = index2;
			index2 = temp;
		}
		if (simMap.containsKey(index1)){
			HashMap<Integer, Double> tempHashMap = simMap.get(index1);
			if (tempHashMap.containsKey(index2)){
				double result = tempHashMap.get(index2);
				return result;
			}
		}else {
			simMap.put(index1, new HashMap<Integer, Double>());
		}
		double result = computeSim(index1, index2);
		simMap.get(index1).put(index2, result);
		return result;
	}
	private double computeSim(int index1, int index2){
		if (CSK == null){
			this.CSK = init(this.inputPathString, this.qid);
		}
		double result = this.CSK.getSim(index1, index2);
		return result;
	}
	
	public void clear(int qid){
		this.qid = qid;
		CSK = null;
		this.simMap.clear();
	}
}
