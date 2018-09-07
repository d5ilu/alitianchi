package Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Assign.InstanceAssign;
import Assign.Movement;
import Assign.OffLineJobAssign;
import Assign.OnlineTaskAssign;
import InputEntities.App;
import InputEntities.AppInterference;
import InputEntities.Instance;
import InputEntities.Job;
import InputEntities.Machine;
import InputEntities.ResourceProportion;
import OutEntities.OfflineAssign;
import OutEntities.OnlineMovement;
import ioFiles.ReadFiles;
import ioFiles.WriteFiles;
import machineCalc.MachineState;

/*
 * 主函数类，负责初始化读取相关数据，包含算法调用的结果输出
 */
public class Application {
	private  App[] apps;
	private  List<MachineState> machines; 
	private Map<Integer,ResourceProportion> machineType;
	private  List<Instance> insts;
	private  List<Job> jobs;
	private  Map<Integer,Map<Integer,Integer>> mapConstraints;
	private String path="src/main/resources/data/";
	public Application() {
		super();
		init();
	}
	private  void init() {
		List<App> appss=ReadFiles.readAppFile(path+"app_resources.csv");
		machines=ReadFiles.ReadMachinesFile(path+"machine_resources.c.csv");
		machineType=new HashMap<Integer,ResourceProportion>();
		List<MachineState> list=new ArrayList<MachineState>();
		jobs=ReadFiles.readJobFile(path+"job_info.c.csv");
		boolean state=false;
		int type=0;
		for(MachineState ms:machines) {
			for(MachineState ms2:list) {
				if(ms.sameMachine(ms2)) {
					state=true;
					break;
				}
			}
			if(state==false) {
				type++;
				ms.setType(type);
				list.add(ms);
				Machine machine=ms.getMachine();
				double sum=machine.getMaxCpu()+machine.getMaxmemory()+machine.getMaxDisk();
				machineType.put(type, new ResourceProportion(type, machine.getMaxCpu(), machine.getMaxmemory(), machine.getMaxDisk()));
			}else {
				state=false;
				ms.setType(type);
			}
		}
		insts=ReadFiles.readInstanceFile(path+"instance_deploy.c.csv");
		int len=appss.size();
		apps=new App[len+1];
		for(App app :appss) {
			apps[app.getAppId()]=app;
		}
		for(Instance inst:insts) {
			if(inst.getMachineid()>0) {
				machines.get(inst.getMachineid()-1).addInstance(inst, apps);
			}
		}
		for(MachineState ms:machines) {
			ms.scoreCalc();
		}
		List<AppInterference> aifs=ReadFiles.readInterferenceFile(path+"app_interference.csv");
		mapConstraints=new HashMap<Integer,Map<Integer,Integer>>();
		for(AppInterference aif:aifs) {
			if(mapConstraints.containsKey(aif.getAppId1())) {
				mapConstraints.get(aif.getAppId1()).put(aif.getAppId2(), aif.getNum());
			}else {
				HashMap<Integer, Integer> map=new HashMap<Integer,Integer>();
				map.put(aif.getAppId2(),aif.getNum() );
				mapConstraints.put(aif.getAppId1(), map);
			}
		}
	}
	
	
	public List<Movement> assign() {
		List<Movement> movement=null;
		movement=new InstanceAssign(apps, machines, insts, mapConstraints,machineType).assign();
		return movement;
	}
	public List<OnlineMovement> assign2(){
		return new OnlineTaskAssign(machineType, apps, machines, insts, mapConstraints).assignall();
	}
	public List<OfflineAssign> assign3(){
		return new OffLineJobAssign(machines, jobs).assign();
	}
	public void write(List<Movement> movement) {
		WriteFiles.writeFile(movement,path);
	}
	public App[] getApps() {
		return apps;
	}
	public List<MachineState> getMachines() {
		return machines;
	}
	public List<Instance> getInsts() {
		return insts;
	}
	public Map<Integer, Map<Integer, Integer>> getMapConstraints() {
		return mapConstraints;
	}
	public String getPath() {
		return path;
	}
	public List<Job> getJobs() {
		return jobs;
	}
}
