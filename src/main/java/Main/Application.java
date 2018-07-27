package Main;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Assign.InstanceAssign;
import Assign.Movement;
import Entities.App;
import Entities.AppInterference;
import Entities.Instance;
import Entities.Machine;
import Entities.ResourceProportion;
import comparator.InstanceComparator;
import ioFiles.ReadFiles;
import ioFiles.WriteFiles;
import machineCalc.MachineState;

public class Application {
	private  App[] apps;
	private  List<MachineState> machines; 
	private  List<MachineState> machinesInit;
	private Map<Integer,ResourceProportion> machineType;
	private  List<Instance> insts;
	private  List<Instance> instsInit;
	private  Map<Integer,Map<Integer,Integer>> mapConstraints;
	private String path="src/main/resources/data2/";
	private  void init() {
		List<App> appss=ReadFiles.readAppFile(path);
		machines=ReadFiles.ReadMachinesFile(path);
		machinesInit=ReadFiles.ReadMachinesFile(path);
		machineType=new HashMap<Integer,ResourceProportion>();
		List<MachineState> list=new ArrayList<MachineState>();
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
		insts=ReadFiles.readInstanceFile(path);
		instsInit=ReadFiles.readInstanceFile(path);
		List<AppInterference> aifs=ReadFiles.readInterferenceFile(path);
		mapConstraints=new HashMap<Integer,Map<Integer,Integer>>();
		int len=appss.size();
		apps=new App[len+1];
		for(App app :appss) {
			apps[app.getAppId()]=app;
		}
		for(AppInterference aif:aifs) {
			if(mapConstraints.containsKey(aif.getAppId1())) {
				mapConstraints.get(aif.getAppId1()).put(aif.getAppId2(), aif.getNum());
			}else {
				HashMap<Integer, Integer> map=new HashMap<Integer,Integer>();
				map.put(aif.getAppId2(),aif.getNum() );
				mapConstraints.put(aif.getAppId1(), map);
			}
		}
		for(Instance inst:instsInit) {
			if(inst.getMachineid()!=0) {
				machinesInit.get(inst.getMachineid()-1).addInstance(inst, apps);
			}
		}
	}

	public Application() {
		super();
		init();
	}

	public List<Movement> assign() {
		List<Movement> movement=null;
		movement=new InstanceAssign(apps, machines, insts, mapConstraints,machineType).assign();
		WriteFiles.writeFile(movement,path);
		return movement;
	}
	//从当前状态迁移到目标状态（不可用）
	private void qianyi() {
		Map<Integer,Integer> movement=new HashMap<Integer,Integer>();
		Map<Instance,Integer> assignResult=new HashMap<Instance,Integer>();
		instsInit.sort(new InstanceComparator(apps));
		Collections.reverse(instsInit);
		for(Instance is:insts) {
			assignResult.put(is, is.getMachineid());
		}
		int assigned=0;
		int machinesUnAssigned=0;
		int noNeedAssign=0;
		for(Instance is:instsInit) {
			if(is.getMachineid()!=0) {
				assigned++;
				if(is.getMachineid()==assignResult.get(is))
					noNeedAssign++;
			}
		}
		for(MachineState ms:machinesInit) {
			if(ms.getInsts().size()==0) {
				machinesUnAssigned++;
			}
		}
		System.out.println("初始分配数量"+assigned+" 空置机器数量"+machinesUnAssigned);
		for(int i=0;i<1;i++) {
			for(Instance inst:instsInit) {
				if(inst.getMachineid()==0||inst.getMachineid()==assignResult.get(inst))
					continue;
				MachineState machineState=machinesInit.get(assignResult.get(inst)-1);
				if(machineState.checkApp(mapConstraints)
						&&machineState.checkApp(inst, mapConstraints)
						&&machineState.checkAll(inst, apps)) {
					machinesInit.get(inst.getMachineid()-1).deleteInstance(inst, apps);
					machineState.addInstance(inst, apps);
					movement.put(inst.getInstId(),inst.getMachineid());
				}else {
					for(MachineState ms:machinesInit) {
						if(ms.checkApp(mapConstraints)
								&&ms.checkApp(inst, mapConstraints)
								&&ms.checkAll(inst, apps)) {
							machinesInit.get(inst.getMachineid()-1).deleteInstance(inst, apps);
							ms.addInstance(inst, apps);
							movement.put(inst.getInstId(),inst.getMachineid());
						}
					}
				}
			}
		}
		System.out.println("迁移个数"+movement.size()+"  无需迁移个数"+noNeedAssign);
		for(Instance inst:instsInit) {
			int machineId=assignResult.get(inst);
			if(inst.getMachineid()==0) {
				machinesInit.get(machineId-1).addInstance(inst, apps);
				movement.put(inst.getInstId(), machineId);
			}
		}
		int diff=0;
		for(int i=0;i<insts.size();i++) {
			if(insts.get(i).getMachineid()!=instsInit.get(i).getMachineid()) {
				diff++;
			}
		}
		double score=0;
		for(MachineState ms:machinesInit) {
			score+=ms.scoreCalc();
		}
		System.out.println("差别个数"+diff);
		System.out.println(score);
	}
}
