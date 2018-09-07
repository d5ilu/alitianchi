package Assign;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import InputEntities.App;
import InputEntities.Instance;
import InputEntities.ResourceProportion;
import OutEntities.OnlineMovement;
import comparator.InstanceComparator;
import machineCalc.MachineState;

public class OnlineTaskAssign {
	private Map<Integer,ResourceProportion> machineType;
	private  App[] apps;
	private  List<MachineState> machines; 
	private  List<Instance> insts;
	private  Map<Integer,Map<Integer,Integer>> mapConstraints;
	private  int [] app_machine;
	public OnlineTaskAssign(Map<Integer, ResourceProportion> machineType, App[] apps, List<MachineState> machines,
			List<Instance> insts, Map<Integer, Map<Integer, Integer>> mapConstraints) {
		super();
		this.machineType = machineType;
		this.apps = apps;
		this.machines = machines;
		this.insts = insts;
		this.mapConstraints = mapConstraints;
	}
	public List<OnlineMovement> assignall(){
		double scoreinit=scoreAll();
		List<OnlineMovement> onlineMovements=new ArrayList<OnlineMovement>();
		int k=2;
		int instNum1=0;
		for(MachineState ms:machines) {
			instNum1+=ms.getInsts().size();
		}
		double value=0.65;
		double step=0.05;
		for(int i=0;i<k;i++) {
			System.out.println(scoreAll());
			value=value-0.12*i;
			step=step-0.01*i;
			onlineMovements.addAll(assign(i+1,value,step));
		}
		reassign(onlineMovements);
		int instNum2=0;
		for(MachineState ms:machines) {
			instNum2+=ms.getInsts().size();
		}
		System.out.println("初始inst数量:"+instNum1+"最后inst数量"+instNum2);
		System.out.println("迁移次数"+onlineMovements.size());
		System.out.println("初始分数："+scoreinit+"迁移后分数："+scoreAll());
		return onlineMovements;

	}
	private double  scoreAll() {
		double score=0;
		for(MachineState ms:machines) {
			score+=ms.scoreCalc();
		}
		return score;
	}
	//实例迁移方式 先对不满足约束的处理，然后处理其他
	public List<OnlineMovement> assign(int movetimes,double value,double step) {
		List<OnlineMovement> onlineMovements=new ArrayList<OnlineMovement>();
		for(MachineState ms:machines) {
			System.out.println("machine"+ms.getMachine().getMachineId()+"约束处理中");
			if(ms.checkApp(mapConstraints))
				continue;
			else {
				List<Instance> instanceToDelete=new ArrayList<Instance>();
				while(ms.unFitInst(mapConstraints)>-1) {
					int appIdtmp=ms.unFitInst(mapConstraints);
					Instance instToDelete=null;
					for(Instance inst:ms.getInsts()) {
						if(inst.getAppId()==appIdtmp) {
							instToDelete=inst;
							break;
						}
					}
					for(MachineState ms2:machines) {
						if(ms2.checkAll(apps)&&ms.checkAll(instToDelete, apps)
								&&ms2.checkApp(mapConstraints)
								&&ms2.checkApp(instToDelete, mapConstraints)) {
							ms2.addInstance(instToDelete, apps);
							ms.deleteInstance(instToDelete, apps);
							instanceToDelete.add(instToDelete);
							onlineMovements.add(
									new OnlineMovement(movetimes, instToDelete.getInstId(),
											ms2.getMachine().getMachineId()));
						}
					}
				}
				//把准备迁移的实例在源主机保留备份
				for(Instance inst:instanceToDelete) {
					ms.addInstance(inst, apps);
				}
			}
		}
		//尝试次数
		int k=1;
		int average=insts.size()/machines.size();
		for(int i=0;i<k;i++) {
			int num=0;
			value=value-step*i;
			for(MachineState ms:machines) {
				List<Instance> instanceToDelete=new ArrayList<Instance>();
				int numOfInst=ms.getInsts().size();
				if(numOfInst<=1)
					continue;
				if(movetimes>=2) {
					Collections.sort(ms.getInsts(),new InstanceComparator(apps));
				}
				System.out.println("第"+movetimes+"次"+"value:"+value+"machine"+ms.getMachine().getMachineId()+"实例迁移中");
				double scoreold=ms.getScore();
				while(!ms.checkCpu(null, apps, 0.5+(value-0.5)*(average+1)/numOfInst)) {
					if(ms.getInsts().size()<i+1)
						break;
					int index=0;
					while(index<numOfInst&&ms.getInsts().get(index).getMachineid()!=ms.getMachine().getMachineId()) {
						index++;
					}
					if(index==numOfInst)
						break;
					Instance is=ms.getInsts().get(index);
					boolean state=false;
					for(MachineState ms1:machines) {
						int numOfInst1=ms1.getInsts().size();
						if(ms1.checkAll(is, apps)&&ms1.checkApp(is, mapConstraints)
								&&ms1.checkCpu(is, apps, 0.5+(value-0.5)*(average+1)/(numOfInst1+1))) {
							ms1.addInstance(is, apps);
							ms.deleteInstance(is, apps);
							instanceToDelete.add(is);
							onlineMovements.add(new OnlineMovement(movetimes, 
									is.getInstId(), ms1.getMachine().getMachineId()));
							//System.out.println("实例"+is.getInstId()+"从machine"+ms.getMachine().getMachineId()
							//		+"迁移到machine"+ms1.getMachine().getMachineId());
							state=true;
							break;
						}
					}
					numOfInst=ms.getInsts().size();
					if(!state)
						break;
				}
				double scorenew=ms.scoreCalc();
				System.out.println("原分数"+scoreold+" 现分数"+scorenew);
				for(Instance is:instanceToDelete) {
					ms.addInstance(is, apps);
					num++;
				}
			}
			if(num<500)
				break;
		}
		//正式开始迁移 1.源主机删除实例2.实例的主机编号改变
		for(OnlineMovement om:onlineMovements) {
			Instance instTodelete=null;
			for(Instance inst:insts) {
				if(inst.getInstId()==om.getInstId()) {
					instTodelete=inst;
					break;
				}
			}
			MachineState ms=machines.get(instTodelete.getMachineid()-1); 
			instTodelete.setMachineid(om.getMachineid());
			ms.deleteInstance(instTodelete, apps);
		}
		return onlineMovements;
	}
	private void reassign(List<OnlineMovement> onlineMovements) {
		int index=6000;
		double value=0.53;
		int movetimes=3;
		boolean state=false;
		for(int i=0;i<3000;i++) {
			index=6000;
			List<Instance> instsOfMachine=machines.get(i).getInsts();
			System.out.println("machine"+i+"正在移动");
			for(int j=0;j<instsOfMachine.size();j++) {
				Instance is=instsOfMachine.get(j);
				while(
						!machines.get(index).checkAll(is, apps)
						||!machines.get(index).checkApp(is, mapConstraints)
						||!machines.get(index).checkCpu(is, apps, value*8/machines.get(index).getInsts().size())) {
					index++;
					if(index==9000)
						break;
				}
				if(index>=9000) {
					state=true;
					break;
				}
				machines.get(i).deleteInstance(is, apps);
				machines.get(index).addInstance(is, apps);
				onlineMovements.add(new OnlineMovement(movetimes, is.getInstId(), index+1));
			}
			if(state) {
				break;
			}
		}
		return;
	}
}
