import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ioFiles.CombineFiles;

public class FileCombine {
	public static void main(String[] args) {
		String path="src/main/resources/data/";
		List<String> files=new ArrayList<String>();
		files.add(path+"a 6595.csv");
		files.add(path+"b 6354.csv");
		files.add(path+"c 9555.csv");
		files.add(path+"d 9210.csv");
		files.add(path+"e 9469.csv");
		String file=path+"result.csv";
		try {
			CombineFiles.combine(files, file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
