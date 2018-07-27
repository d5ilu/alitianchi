package ioFiles;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import Assign.Movement;
import cn.hutool.core.text.csv.CsvWriter;

public class WriteFiles {
	private static  String inst="inst_";
	private static  String machine="machine_";
	private static CsvWriter writer;
	public static void writeFile(List<Movement> movement,String path) {
		writer=new CsvWriter(new File(path+"resultdata.csv"));
		List<String []> lines=new ArrayList<String []>();
		for(Movement mv:movement) {
			String [] line=new String[2];
			line[0]=inst+mv.getInstId();
			line[1]=machine+mv.getMachineId();
			lines.add(line);
		}
		writer.write(lines);
		writer.flush();
		writer.close();
	}
}
