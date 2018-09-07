package optimization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import Assign.Movement;
import InputEntities.App;
import InputEntities.Instance;
import Main.Application;
import ioFiles.ReadMovements;
import ioFiles.WriteFiles;
import machineCalc.MachineState;

/*
 * 迁移完毕之后的补充的优化策略
 * 对两台machine，尝试迁移一个实例或者交换一个实例，寻找最优值，重复一定次数
 * 初赛时使用，大约优化100分左右
 */
public class Optimization {
	private String path="src/main/resources/data2/";
	private Application application;
	private List<Movement> movements;
	public Optimization(Application app) {
		super();
		this.application=app;
		movements=ReadMovements.readResult(path+"resultdata.csv");
		for(Movement mv:movements) {
			System.out.println(movements.indexOf(mv));
			int machineId=0;
			Instance insttmp=null;
			for(Instance inst:application.getInsts()) {
				if(inst.getInstId()==mv.getInstId()) {
					machineId=inst.getMachineid();
					insttmp=inst;
					break;
				}
			}
			if(machineId!=0) {
				application.getMachines().get(machineId-1).deleteInstance(insttmp, application.getApps());
			}
			application.getMachines().get(mv.getMachineId()-1).addInstance(insttmp, application.getApps());
		}
		for(MachineState ms:application.getMachines()) {
			ms.scoreCalc();
		}
	}
	public void optimize() {
		List<MachineState> machines=application.getMachines();
		App[] apps=application.getApps();
		Map<Integer,Map<Integer,Integer>> mapConstraints=application.getMapConstraints();
		List<Instance> insts=application.getInsts();
		machines.sort(new Comparator<MachineState>() {

			@Override
			public int compare(MachineState arg0, MachineState arg1) {
				Double v1=arg0.getScore();
				Double v2=arg1.getScore();
				return v1.compareTo(v2);
			}
		});
		Collections.reverse(machines);
		double score=0;
		for(MachineState ms:machines) {
			score+=ms.getScore();
		}
		System.out.println(score);
		int times=10;
		int searchtimes=10;
		for(int k=0;k<times;k++) {
			System.out.println("第"+k+"次尝试交换");
			machines.sort(new Comparator<MachineState>() {

				@Override
				public int compare(MachineState arg0, MachineState arg1) {
					Double v1=arg0.getScore();
					Double v2=arg1.getScore();
					return v1.compareTo(v2);
				}
			});
			Collections.reverse(machines);
			for(int i=0;i<machines.size()/10;i++) {
				if(machines.get(i).getScore()==0)
					continue;
				double value=Double.MAX_VALUE;
				int index=-1;
				for(int t=0;t<searchtimes;t++) {
					double sum=0;
					int num=(int) (Math.random()*(6000));
					if(machines.get(num).getScore()==0)
						continue;
					//					List<Double> cpu1=machines.get(i).getMachine().getCpu();
					//					List<Double> cpu2=machines.get(num).getMachine().getCpu();
					//					double sumCpu1=0;
					//					double sumCpu2=0;
					//					sumCpu1=machines.get(i).getMachine().getMaxCpu();
					//					sumCpu2=machines.get(num).getMachine().getMaxCpu();
					//					for(Double d:cpu1) {
					//						sumCpu1+=d;
					//					}
					//					for(Double d:cpu2) {
					//						sumCpu2+=d;
					//					}
					//					for(int j=0;j<cpu1.size();j++) {
					//						sum+=Math.pow((cpu1.get(j)/sumCpu1-cpu2.get(j)/sumCpu2),2);
					//					}
					//					if(value>sum) {
					//						value=sum;
					//						index=num;
					//					}
					//				}

					if(swapInst(machines.get(i),machines.get(num),mapConstraints,apps))
						break;
				}
			}
			score=0;
			for(MachineState ms:machines) {
				score+=ms.getScore();
			}
			System.out.println(score);
			//WriteFiles.writeFile(movements, path+"resultdata"+(int)score);
		}
		WriteFiles.writeFile(movements, path+"resultdata"+(int)score);
		return ;
	}
	private boolean swapInst(MachineState ms1,MachineState ms2,
			Map<Integer,Map<Integer,Integer>> mapConstraints,App[] apps) {
		boolean result = false;
		double score1=ms1.getScore();
		double score2=ms2.getScore();
		double score=ms1.getScore()+ms2.getScore();
		Instance swapinst1=null;
		Instance swapinst2=null;
		List<Instance> insts1=new ArrayList<Instance>(ms1.getInsts());
		List<Instance> insts2=new ArrayList<Instance>(ms2.getInsts());
		for(Instance inst1:insts1) {
			ms2.addInstance(inst1, apps);
			if(ms2.checkAll(apps)&&ms2.checkApp(mapConstraints)) {
				ms1.deleteInstance(inst1, apps);
				double scoretmp=ms1.scoreCalc()+ms2.scoreCalc();
				if(score>scoretmp) {
					score=scoretmp;
					swapinst1=inst1;
					swapinst2=null;
				}
				for(Instance inst2:insts2) {
					ms1.addInstance(inst2, apps);
					if(ms1.checkAll(apps)&&ms1.checkApp(mapConstraints)) {
						ms2.deleteInstance(inst2, apps);
						scoretmp=ms1.scoreCalc()+ms2.scoreCalc();
						if(score>scoretmp) {
							score=scoretmp;
							swapinst1=inst1;
							swapinst2=inst2;
						}
						ms2.addInstance(inst2, apps);
					}
					ms1.deleteInstance(inst2, apps);
				}
				ms1.addInstance(inst1, apps);
			}
			ms2.deleteInstance(inst1, apps);
		}
		if(swapinst1!=null) {
			ms1.deleteInstance(swapinst1, apps);
			ms2.addInstance(swapinst1, apps);
			movements.add(new Movement(swapinst1.getInstId(), ms2.getMachine().getMachineId()));
			result=true;

			if(swapinst2!=null) {
				ms2.deleteInstance(swapinst2, apps);
				ms1.addInstance(swapinst2, apps);
				movements.add(new Movement(swapinst2.getInstId(), ms1.getMachine().getMachineId()));
			}
		}
		ms1.scoreCalc();
		ms2.scoreCalc();
		if(swapinst1!=null||swapinst2!=null) {
			System.out.println("machine"+ms1.getMachine().getMachineId()+"原来分数"+score1
					+"现在分数"+ms1.getScore()+" "+"machine"+ms2.getMachine().getMachineId()+"原来分数"+score2
					+"现在分数"+ms2.getScore());
		}
		return result;

	}
	public static void main(String[] args) {
		Optimization  op=new Optimization(new Application());
		op.optimize();
	}
}

