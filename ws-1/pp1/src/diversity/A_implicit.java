package diversity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import similarity.Sim;

public abstract class A_implicit {
	//只计算前500篇文档的多样化结果
	public static int K = 1000;
	public double lambda = 0.5;
	public String inputPath;
	public A_implicit(String inputPath, int qid) {
		super();
		this.inputPath = inputPath;
		this.qid = qid;
		try {
			init(inputPath, qid);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int qid;
	//初时：记录原始结果文档中，每一行的数据
	//后来：将其中的得分和次序置换掉
	public ArrayList<Data_> alData_ = new ArrayList<Data_>();
	public ArrayList<Integer> resultList = new ArrayList<Integer>();
	public Sim sim = null;
	
	/**
	 * 多样化算法
	 */
	public abstract ArrayList<Integer> div();
	
	/**
	 * 初始化原始结果文档
	 * @throws IOException 
	 */
	public ArrayList<Data_> init(String inputPath, int qid) throws IOException{
		this.alData_.clear();
		BufferedReader br = new BufferedReader(new FileReader(inputPath));
		String line = "";
		int count = -1;
		while ((line = br.readLine())!=null){
			String []strings = line.split("[\t|\n|\r| ]");
			int tempqid = Integer.parseInt(strings[0]);
			if (tempqid < qid){
				continue;
			}else if (tempqid == qid){
				count ++;
				Data_ data_ = new Data_(line);
				if (count > K){
					break;
				}
				data_.setOrder(count);
				alData_.add(data_);
			}else {
				break;
			}
		}
		
		br.close();
		System.out.println("diversity.A_implicit.init(String, int)\t初始化结果文档结束 - 文档大小为：" + alData_.size());
		return this.alData_;
	}
	
	public void clear(){
		this.alData_.clear();
		this.resultList.clear();
	}
	public void clear(int i) throws IOException{
		System.out.println("CLEAR\t"+i);
		this.alData_.clear();
		this.resultList.clear();
		this.qid = i;
		this.sim.clear(i);
		init(inputPath, i);
	}
}
