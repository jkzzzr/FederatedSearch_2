import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Get1000 {

	public static int number = 100;
	public int qid ;
	public static void main(String[] args) throws IOException {
		Get1000 get1000 = new Get1000();
		for (int i = 201; i <=250; i++){
			get1000.qid  = i;
			get1000.f1("/home/Lee/data/结果备份/final.3.choose", "/home/Lee/data/结果备份/final.3.choose-100");
		}
	}

	
	public void f1(String path, String out) throws IOException{
		BufferedWriter bw = new BufferedWriter(new FileWriter(out, true));
		BufferedReader bReader = new BufferedReader(new FileReader(path));
		String line = "";
		int count = 0;
		while ((line = bReader.readLine())!=null){
			if (line.startsWith(qid+"")){
				if (count < number){
					count ++;
					bw.write(line+"\n");
				}else {
					break;
				}
			}
		}
		bReader.close();
		bw.close();
		return;
	}
}
