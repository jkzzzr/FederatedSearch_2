import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

public class TongJi {
	public static void main(String args[]) throws Exception{

		BufferedReader br = new BufferedReader(new FileReader("/home/lee/音乐/Output-FR/K-means/type/1"));
		String line = "";
		int count = 0;
		HashMap<Integer, Integer> hm = new HashMap<>();
		while ((line = br.readLine())!=null){
			String string[] = line.split(",");
			for (String s:string){
				String str[] = s.split("=");
				if (str.length < 2){
					System.out.println(s);
					continue;
				}
				Integer in = Integer.parseInt(str[1]);
				count +=in;
				hm.put(Integer.parseInt(str[0]), in);
			}
		}
		br.close();
		int count2 = 0;
		BufferedWriter bw = new BufferedWriter(new FileWriter("/home/lee/音乐/Output-FR/K-means/type/2"));
		for (int i = 0; i < 500; i++){
			if (hm.containsKey(i)){
				count2 +=hm.get(i);
				bw.write(i+"\t"+hm.get(i)+"\n");
				hm.remove(i);
			}
		}
		bw.close();
		System.out.println(count+"\t"+count2);
	}
}
