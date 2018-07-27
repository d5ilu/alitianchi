package ioFiles;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import Entities.App;
import Entities.AppInterference;
import Entities.Instance;
import Entities.Machine;
import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvRow;
import machineCalc.MachineState;

public class ReadFiles {
	private static final int Applength=4;
	private static final int MachineLength=8;
	private static final int instLength=5;
	private static final CsvReader reader=new CsvReader();
	public static List<App> readAppFile(String path){
		List<App> apps=new ArrayList<App>();
		File file =new File(path+"app_resources.csv");
		CsvData data=reader.read(file);
		List<CsvRow> rows=data.getRows();
		for(CsvRow row:rows) {
			int appId=Integer.parseInt(row.get(0).substring(Applength));
			List<Double> cpu=getList(row.get(1));
			List<Double> memory=getList(row.get(2));
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
		File file =new File(path+"machine_resources.csv");
		CsvData data=reader.read(file);
		List<CsvRow> rows=data.getRows();
		for(CsvRow row:rows) {
			int machineId=Integer.parseInt(row.get(0).substring(MachineLength));
			double cpu=Double.parseDouble(row.get(1));
			double memory=Double.parseDouble(row.get(2));
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
		File file =new File(path+"instance_deploy.csv");
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
	public static List<AppInterference> readInterferenceFile(String path){
		List<AppInterference> interferences=new ArrayList<AppInterference>();
		File file =new File(path+"app_interference.csv");
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
	public static List<Double> getList(String str){
		List<Double> res=new ArrayList<Double>();
		int len=str.length();
		int left=0;
		int right=0;
		while(right<len) {
			if(str.charAt(right)=='|') {
				double value=Double.parseDouble(str.substring(left, right));
				res.add(value);
				left=right+1;
			}
			right++;
		}
		res.add(Double.parseDouble(str.substring(left)));
		return res;
	}

}
