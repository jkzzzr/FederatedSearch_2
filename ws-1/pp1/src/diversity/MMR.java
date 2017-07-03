package diversity;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import similarity.Sim;

public class MMR extends A_implicit {

	
	
	public MMR(int qid, String inputPath) {
		super(inputPath, qid);
		this.sim = new Sim(super.qid, super.inputPath);
	}
	@Override
	public ArrayList<Integer> div() {
		
		System.err.println("diversity.MMR.div() -\t 多样化开始："
				+ "\n\t结果文的文档数量为：		" + super.alData_.size()
				+ "\n\t查询号为:		"+super.qid);
		int K = super.alData_.size();
		//存储剩余文档，文档结果序号
		ArrayList<Integer> left_List = new ArrayList<Integer>();
		//存储结果列表的，文档结果序号
		ArrayList<Integer> result = new ArrayList<Integer>();
//		ArrayList<Data_> result = new ArrayList<Data_>();
		for (int i = 0; i < K; i++){
			left_List.add(i);
		}
		//选择出K个结果
		System.out.println("倒计时：");int daojishi = 0;
		for (int i = 0; i < K; i++){
			if (i %(K/20)==0){
				System.out.println("\t" + (daojishi++));
			}
			double max_score = Double.MAX_VALUE;
			int max_index = 0;
			//每个结果，都是从剩余中找出最大得分的一个
			for (int j = 0; j < left_List.size(); j++){
				int index = left_List.get(j);
				double part1 = super.lambda * super.alData_.get(index).getScore();
				double part2 = (1 - super.lambda) * max(j, left_List);
				double temp_score = part1 - part2;
				if (max_score > temp_score){
					max_score = temp_score;
					max_index = j;
				}
			}
			result.add(left_List.get(max_index));
			modify(left_List.get(max_index), i, max_score);
			left_List.remove(max_index);
		}
		super.resultList = result;
		return result;
	}
	/**
	 * 原始结果中排名j的文档，与之相似度得分最低的,得分
	 * @param j
	 * @return
	 */
	public double max(int j, ArrayList<Integer> left_al){
		double maxscore = -Double.MAX_VALUE;
		for (int i = 0; i < left_al.size(); i++){
			if (i == j){
				continue;
			}
			double tempSim = this.sim.getSim(left_al.get(i), left_al.get(j));
			if (maxscore < tempSim){
				maxscore = tempSim;
			}
		}
		return maxscore;
	}
	
	/**
	 * 
	 * @param index
	 * @param order
	 * @param score
	 */
	public void modify(int index, int order, double score){
		Data_ data_ = super.alData_.get(index);
		data_.setScore(score);
		data_.setOrder(order);
	}
	
	
	
	public void write(String outputPath, String systemName) throws IOException{
		System.out.println("写出");
		BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath, true));
		for (int i = 0; i < super.resultList.size(); i++){
			int index = super.resultList.get(i);
			Data_ data_ = super.alData_.get(index);
			bw.write(data_.toString(systemName) + "\n");
		}
		bw.close();
	}
	
	
	
	public static void main(String args[]) throws IOException{
		//示例：
		MMR mmr = new MMR(0, "原始文档结果列表输入(docid)");
		mmr.div();
		mmr.write("输出路径", "mmr");
	}
}
