import java.io.IOException;

import diversity.MMR;

public class Entry_MMR {
	public static void main(String []args) throws IOException{
		/*System.out.println("START!!");
		int i = 176;
		MMR mmr = new MMR(i, "/home/lee/音乐/1000.res");
		mmr.div();
		mmr.write("/home/lee/音乐/Output-FR/diversity/123");
		System.out.println("正式的");
		for (i++; i <= 200; i++){
			System.out.println("kaishile \t"+i);
			mmr.clear(i);
			mmr.div();
			mmr.write("/home/lee/音乐/Output-FR/diversity/123");
		}
		*/

		
		System.out.println("START!!");
		int i = 201;
		MMR mmr = new MMR(i, "/home/Lee/data/结果备份/final.3.choose-100");
		mmr.div();
		mmr.write("/home/Lee/data/结果备份/type.3.index.mmr.100", "all-mmr");
		System.out.println("正式的");
		for (i++; i <= 250; i++){
			System.out.println("kaishile \t"+i);
			mmr.clear(i);
			mmr.div();
			mmr.write("/home/Lee/data/结果备份/type.3.index.mmr.100", "type-mmr");
		}
		
		
		
		
		
		
		
		
		
	}
}
