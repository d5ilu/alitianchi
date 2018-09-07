package Assign;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import InputEntities.App;
import InputEntities.Instance;
import InputEntities.ResourceProportion;
import comparator.InstanceComparator;
import machineCalc.MachineState;
/*
 * first fit 算法分配以及迁移实例
 * 初赛时使用，复赛使用其他方法
 */
public class InstanceAssign {
	private Map<Integer,ResourceProportion> machineType;
	private  Map<Integer,Integer> typePosition;
	private  App[] apps;
	private  List<MachineState> machines; 
	private  List<Instance> insts;
	private  Map<Integer,Map<Integer,Integer>> mapConstraints;
	private  int [] app_machine;
	public InstanceAssign(App[] apps, List<MachineState> machines, List<Instance> insts,
			Map<Integer, Map<Integer, Integer>> mapConstraints,Map<Integer,ResourceProportion> machineType) {
		super();
		this.apps = apps;
		this.machines = machines;
		this.insts = insts;
		this.mapConstraints = mapConstraints;
		this.machineType=machineType;
		typePosition=new HashMap<Integer,Integer>();
		int type=1;
		for(MachineState ms:machines) {
			if(ms.getType()==type) {
				typePosition.put(type, machines.indexOf(ms));
				type++;
			}
		}
	}
	public List<Movement> assign() {
		List<Movement> movement=new LinkedList<Movement>();
		sort();
		app_machine=App_machinePair();
		method2(movement);
		int unAssigned=0;
		int assigned=0;
		for(Instance inst:insts) {
			if(inst.getMachineid()==0) {
				System.out.println("实例"+inst.getInstId()+"没有分配");
				unAssigned++;
			}
			else {
				assigned++;
				//System.out.println("实例"+inst.getInstId()+"分配到"+inst.getMachineid());
			}
		}
		System.out.println("已分配数量"+assigned);
		assigned=0;
		for(MachineState ms:machines) {
			assigned+=ms.getInsts().size();
		}
		double score=0.0D;
		for(MachineState ms :machines) {
			ms.scoreCalc();
			score+=ms.getScore();
			System.out.print(ms.toString());
			//System.out.println(ms);
		}
		System.out.println("未分配数量"+unAssigned+"已分配数量"+assigned);
		System.out.println(score);
		return movement;
	}
	public void method1() {
		//方法1 从头插入
		for(int i=0;i<5;i++) { 
			for(Instance inst:insts) {
				if(inst.getMachineid()!=0)
					continue;
				for(MachineState ms :machines) {
					if(ms.checkAll(inst, apps)&&ms.checkCpu(inst, apps,0.5+0.1*i)&&ms.getType()==app_machine[inst.getAppId()]) {
						ms.addInstance(inst, apps);
						inst.setMachineid(ms.getMachine().getMachineId());
						System.out.println("实例"+inst.getInstId()+"分配到"+ms.getMachine().getMachineId());
						break;
					}
				}
				if(inst.getMachineid()==0) {
					for(MachineState ms :machines) {
						if(ms.checkAll(inst, apps)&&ms.checkCpu(inst, apps,0.5+0.1*i)) {
							ms.addInstance(inst, apps);
							inst.setMachineid(ms.getMachine().getMachineId());
							System.out.println("实例"+inst.getInstId()+"分配到"+ms.getMachine().getMachineId());
							break;
						}
					}
				}
				//			if(inst.getMachineid()==0) {
				//				for(MachineState ms :machines) {
				//					if(ms.checkAll(inst, apps)&&ms.checkCpu(inst, apps)){
				//						ms.addInstance(inst, apps);
				//						inst.setMachineid(ms.getMachine().getMachineId());
				//						break;
				//					}
				//				}
				//			}
			}
		}
	}
	//方法2 循环插入 避免同一种应用实例全部在同一个主机
	public void method2(List<Movement> movement) {
		//循环标志
		boolean state=false;
		//选择标志
		boolean state2=false;
		int index1=0;
		int type=machineType.size();
		int[] index2=new int[type+1];
		for(int i=0;i<type;i++) {
			index2[i+1]=typePosition.get(i+1);
		}
		double value=0.6D;
		double disvalue=0.2;
		int liu=900;
		while(index1<insts.size()) {
			Instance inst=insts.get(index1);
			int machineId=inst.getMachineid();
			if(machineId>liu&&index1<1000) {
				System.out.println("实例"+inst.getInstId()+"跳过");
				index1++;
				continue;
			}
			value=0.55*(1+0.1*index1/insts.size());
			disvalue= 0.9*(1-0.2*index1/insts.size());
			int typeOfInst=app_machine[inst.getAppId()];
			if(machineId>liu&&machines.get(machineId-1).getType()==typeOfInst
					&&machines.get(machineId-1).checkAll(apps)
					&&machines.get(machineId-1).checkApp(mapConstraints)
					&&machines.get(machineId-1).checkCpu(null, apps, value)
					&&machines.get(machineId-1).average(null, apps,disvalue)) {
				System.out.println("实例"+inst.getInstId()+"还在"+"machine"+inst.getMachineid()
				+" 当前第"+insts.indexOf(inst)+"个实例");
				index1++;
				continue;
			}
			int position=machines.size();
			if(typePosition.containsKey(typeOfInst+1))
				position=typePosition.get(typeOfInst+1);
			if(index2[typeOfInst]>=position) {
				index2[typeOfInst]=typePosition.get(typeOfInst);
			}
			while(value<1) {
				while(index2[typeOfInst]<machines.size()) {
					if(index2[typeOfInst]<liu) {
						index2[typeOfInst]=liu;
						continue;
					}

					MachineState ms=machines.get(index2[typeOfInst]);
					if(ms.checkApp(mapConstraints)
							&&ms.checkAll(inst, apps)
							&&ms.checkCpu(inst, apps,value)
							&&ms.checkApp(inst, mapConstraints)
							&&ms.average(inst, apps, disvalue)) {
						if(inst.getMachineid()>0)
							machines.get(inst.getMachineid()-1).deleteInstance(inst, apps);
						if(inst.getMachineid()!=ms.getMachine().getMachineId())
							movement.add(new Movement(inst.getInstId(), ms.getMachine().getMachineId()));
						ms.addInstance(inst, apps);
						//inst.setMachineid(ms.getMachine().getMachineId());
						System.out.println("实例"+inst.getInstId()+"分配到"+ms.getMachine().getMachineId()
								+" 当前第"+insts.indexOf(inst)+"个实例");
						index2[typeOfInst]++;
						state2=true;
						break;
					}else {
						index2[typeOfInst]++;
					}
				}
				if(state2) {
					state2=false;
					break;
				}else {
					int position1=machines.size();
					if(index2[typeOfInst]>=position1) {
						index2[typeOfInst]=0;
						if(state==false) {
							state=true;
						}else {
							state=false;
							value=value+0.03;
							disvalue+=0.03;
							if(value>1) {
								index1++;
								break;
							}

						}
					}
				}
			}
			index1++;
		}
	}
	//与方式2类似，实例与机器种类之间关联不考虑
	public void method3(List<Movement> movement) {
		//循环标志
		boolean state=false;
		//选择标志
		boolean state2=false;
		int index1=0;
		int index2=0;
		double value=0.6D;
		double disvalue=0.2;
		int liu=1000;
		while(index1<insts.size()) {
			Instance inst=insts.get(index1);
			int machineId=inst.getMachineid();
			value=0.6*(1+0.1 *index1/insts.size());
			disvalue= 0.2*(1-0.2*index1/insts.size());
			int typeOfInst=app_machine[inst.getAppId()];
			if(machineId>liu&&machines.get(machineId-1).getType()==typeOfInst
					&&machines.get(machineId-1).checkAll(apps)
					&&machines.get(machineId-1).checkApp(mapConstraints)
					&&machines.get(machineId-1).checkCpu(null, apps, 0.6)
					&&machines.get(machineId-1).average(null, apps,disvalue)) {
				System.out.println("实例"+inst.getInstId()+"还在"+"machine"+inst.getMachineid()
				+" 当前第"+insts.indexOf(inst)+"个实例");
				index1++;
				continue;
			}
			
			while(value<1) {
				while(index2<machines.size()) {
					if(index2<liu) {
						index2=liu;
						continue;
					}

					MachineState ms=machines.get(index2);
					if(ms.checkApp(mapConstraints)
							&&ms.checkAll(inst, apps)
							&&ms.checkCpu(inst, apps,value)
							&&ms.checkApp(inst, mapConstraints)
							&&ms.average(inst, apps, disvalue)) {
						if(inst.getMachineid()>0)
							machines.get(inst.getMachineid()-1).deleteInstance(inst, apps);
						if(inst.getMachineid()!=ms.getMachine().getMachineId())
							movement.add(new Movement(inst.getInstId(), ms.getMachine().getMachineId()));
						ms.addInstance(inst, apps);
						//inst.setMachineid(ms.getMachine().getMachineId());
						System.out.println("实例"+inst.getInstId()+"分配到"+ms.getMachine().getMachineId()
								+" 当前第"+insts.indexOf(inst)+"个实例");
						index2++;
						state2=true;
						break;
					}else {
						index2++;
					}
				}
				if(state2) {
					state2=false;
					break;
				}else {
					int position1=machines.size();
					if(index2>=position1) {
						index2=0;
						if(state==false) {
							state=true;
						}else {
							state=false;
							value=value+0.1;
							disvalue+=0.05;
							if(value>1) {
								index1++;
								break;
							}

						}
					}
				}
			}
			index1++;
		}
	}

	public void sort() {
		//		Collections.sort(machines, new Comparator<MachineState>() {
		//
		//			@Override
		//			public int compare(MachineState arg0, MachineState arg1) {
		//
		//				return new MachineComparator().compare(arg0, arg1);
		//			}
		//		});
		//		Collections.reverse(machines);
		Collections.sort(insts,new Comparator<Instance>() {

			@Override
			public int compare(Instance arg0, Instance arg1) {
				// TODO Auto-generated method stub
					return new InstanceComparator(apps).compare(arg0, arg1);
			}

		});
		Collections.reverse(insts);
	}
	private int[] App_machinePair() {
		int len=apps.length;
		int [] res=new int[len];
		for(int i=1;i<len;i++) {
			double sum=apps[i].getCpumax()+apps[i].getMemorymax()+apps[i].getDisk();
			ResourceProportion rp=new ResourceProportion(0, apps[i].getCpumax(), apps[i].getMemorymax(), apps[i].getDisk());
			int index=Integer.MAX_VALUE;
			double value=Double.MAX_VALUE;
			for(Integer j:machineType.keySet()) {
				if(rp.getCpuProportion()>machineType.get(j).getCpuProportion()
						||rp.getMemoryProportion()>machineType.get(j).getMemoryProportion()
						||rp.getDiskProportion()>machineType.get(j).getDiskProportion())
					continue;
				if(value>rp.simi(machineType.get(j))) {
					value=rp.simi(machineType.get(j));
					index=machineType.get(j).getType();
				}
			}
			res[i]=index;
		}
		return res;
	}
}
