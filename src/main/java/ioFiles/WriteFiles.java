package ioFiles;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import Assign.Movement;
import OutEntities.OfflineAssign;
import OutEntities.OnlineMovement;
import cn.hutool.core.text.csv.CsvWriter;
/*
 * 将迁移结果写入文件
 * 初赛时使用
 */
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
	public static void writeOnlineFile(List<OnlineMovement> movements,String path) {
		writer=new CsvWriter(new File(path+"resultdata.csv"));
		List<String []> lines=new ArrayList<String []>();
		for(OnlineMovement om:movements) {
			String [] line=new String[3];
			line[0]=om.getMovetimes()+"";
			line[1]=inst+om.getInstId();
			line[2]=machine+om.getMachineid();
			lines.add(line);
		}
		writer.write(lines);
		writer.flush();
		writer.close();
	}
	public static void writeOffLineJobs(List<OfflineAssign> assigns,String path) {
		writer=new CsvWriter(new File(path+"resultdata.csv"),Charset.defaultCharset(),true);
		List<String []> lines=new ArrayList<String []>();
		for(OfflineAssign oa:assigns) {
			String [] line=new String[4];
			line[0]=oa.getId();
			line[1]=machine+oa.getMachineId();
			line[2]=oa.getStartTime()+"";
			line[3]=oa.getNumAssigned()+"";
			lines.add(line);
		}
		writer.write(lines);
		writer.flush();
		writer.close();
	}
}
