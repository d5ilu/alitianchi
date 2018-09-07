package ioFiles;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import InputEntities.App;
import InputEntities.AppInterference;
import InputEntities.Instance;
import InputEntities.Job;
import InputEntities.Machine;
import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvRow;
import machineCalc.MachineState;
/*
 * 读取数据文件的类，分别包含读取几种数据的方法
 * 使用开源的csvreader
 */
public class ReadFiles {
	private static final int Applength=4;
	private static final int MachineLength=8;
	private static final int instLength=5;
	private static final int pointNum=15;
	private static final CsvReader reader=new CsvReader();
	public static List<App> readAppFile(String path){
		List<App> apps=new ArrayList<App>();
		File file =new File(path);
		CsvData data=reader.read(file);
		List<CsvRow> rows=data.getRows();
		for(CsvRow row:rows) {
			int appId=Integer.parseInt(row.get(0).substring(Applength));
			List<Float> cpu=getList(row.get(1));
			List<Float> memory=getList(row.get(2));
			int disk=(int) Double.parseDouble(row.get(3));
			int P=Integer.parseInt(row.get(4));
			int M=Integer.parseInt(row.get(5));
			int PM=Integer.parseInt(row.get(6));
			apps.add(new App(appId, cpu, memory, disk, P, M, PM));
		}
		return apps;
	}
	public static List<MachineState> ReadMachinesFile(String path){
		List<MachineState> machines =new ArrayList<MachineState>();
		File file =new File(path);
		CsvData data=reader.read(file);
		List<CsvRow> rows=data.getRows();
		for(CsvRow row:rows) {
			int machineId=Integer.parseInt(row.get(0).substring(MachineLength));
			float cpu=Float.parseFloat(row.get(1));
			float memory=Float.parseFloat(row.get(2));
			int disk=Integer.parseInt(row.get(3));
			int P=Integer.parseInt(row.get(4));
			int M=Integer.parseInt(row.get(5));
			int PM=Integer.parseInt(row.get(6));
			machines.add(new MachineState(new Machine(machineId, cpu, memory, disk, P, M, PM),10000));
		}
		return machines;
	}
	public static List<Instance> readInstanceFile(String path){
		List<Instance> insts=new ArrayList<Instance>();
		File file =new File(path);
		CsvData data=reader.read(file);
		List<CsvRow> rows=data.getRows();
		for(CsvRow row:rows) {
			int instId=Integer.parseInt(row.get(0).substring(instLength));
			int appId=Integer.parseInt(row.get(1).substring(Applength));
			int machineId=0;
			if(row.size()==3&&row.get(2).length()!=0)
				machineId=Integer.parseInt(row.get(2).substring(MachineLength));
			insts.add(new Instance(instId, appId,machineId));
		}
		return insts;
	}
	public static List<Job> readJobFile(String path){
		List<Job> jobs=new ArrayList<Job>();
		File file =new File(path);
		CsvData data=reader.read(file);
		List<CsvRow> rows=data.getRows();
		for(CsvRow csvrow:rows) {
			String id=csvrow.get(0);
			float cpu=Float.parseFloat(csvrow.get(1));
			float memory=Float.parseFloat(csvrow.get(2));
			int instnum=Integer.parseInt(csvrow.get(3));
			int time=Integer.parseInt(csvrow.get(4));
			List<String> list=new ArrayList<String>();
			for(int i=5;i<csvrow.size();i++) {
				if(!csvrow.get(i).equals("")) {
					list.add(csvrow.get(i));
				}
			}
			jobs.add(new Job(id, cpu, memory, instnum, time, list));
		}
		return jobs;
	}
	public static List<AppInterference> readInterferenceFile(String path){
		List<AppInterference> interferences=new ArrayList<AppInterference>();
		File file =new File(path);
		CsvData data=reader.read(file);
		List<CsvRow> rows=data.getRows();
		for(CsvRow row:rows) {
			int appId1=Integer.parseInt(row.get(0).substring(Applength));
			int appId2=Integer.parseInt(row.get(1).substring(Applength));
			int num=Integer.parseInt(row.get(2));
			interferences.add(new AppInterference(appId1, appId2, num));
		}
		return interferences;
	}
	public static List<Float> getList(String str){
		List<Float> res=new ArrayList<Float>();
		int len=str.length();
		int left=0;
		int right=0;
		while(right<len) {
			if(str.charAt(right)=='|') {
				float value=Float.parseFloat(str.substring(left, right));
				for(int i=0;i<pointNum;i++) {
					res.add(value);
				}
				left=right+1;
			}
			right++;
		}
		for(int i=0;i<pointNum;i++) {
			res.add(Float.parseFloat(str.substring(left)));
		}
		return res;
	}
}
