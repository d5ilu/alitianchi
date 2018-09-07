package ioFiles;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CombineFiles {
	public static void combine(List<String> files,String file) throws IOException {
		BufferedWriter writer=new BufferedWriter(new FileWriter(file));
		for(String path:files) {
			System.out.println(file);
			BufferedReader reader=new BufferedReader(new FileReader(path));
			String str=reader.readLine();
			while(str!=null&&!str.equals("#")) {
				writer.write(str);
				writer.newLine();
				str=reader.readLine();
				
			}
			reader.close();
			writer.write("#");
			writer.newLine();
		}
		writer.close();
	}
}
