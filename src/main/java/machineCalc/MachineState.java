package machineCalc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import InputEntities.App;
import InputEntities.Instance;
import InputEntities.Job;
import InputEntities.Machine;
/*
 * machine及其属性类
 * 包含属性：分数，任务列表，离线任务列表，app种类数目，机器类型
 * 几个主要方法：分数计算，判断约束是否满足，
 * 判断cpu占用率是否高于某值，增加、删除任务，增加、删除离线任务
 */
public class MachineState implements Comparable<MachineState>{
	private Machine machine;
	private double score;
	private List<Instance> insts;
	private Map<Job,Integer> offTasks;
	private Map<Integer,Integer> appNum;
	private int type;
	public MachineState(Machine machine,int size) {
		super();
		this.machine = machine;
		insts=new ArrayList<Instance>();
		appNum=new HashMap<Integer,Integer>();
		offTasks=new HashMap<Job,Integer>();
	}

	public List<Instance> getInsts() {
		return insts;
	}

	public void setInsts(List<Instance> insts) {
		this.insts = insts;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public double scoreCalc() {
		int len=machine.getCpu().size();
		score=0;
		if(insts.size()==0&&offTasks.size()==0)
			return score;
		double a=insts.size()+1;
		double b=0.5;
		for(int i=0;i<len;i++) {
			double cpuUse=machine.getCpu().get(i)/machine.getMaxCpu();
			if(cpuUse>b) {
				score+=1+a*(Math.exp(cpuUse-b)-1);
			}else {
				score++;
			}
		}
		score=score/len;
		return score;
	}
	//判断加入实例后是否符合约束
	public boolean checkAll(Instance inst,App[] apps) {
		App app=apps[inst.getAppId()];
		if(machine.getDisk()+app.getDisk()>machine.getMaxDisk())
			return false;
		if(machine.getM()+app.getM()>machine.getMaxM())
			return false;
		if(machine.getP()+app.getP()>machine.getMaxP())
			return false;
		if(machine.getPM()+app.getPM()>machine.getMaxPM())
			return false;
		int len=machine.getMemory().size();
		for(int i=0;i<len;i++) {
			if(machine.getMemory().get(i)+app.getMemory().get(i)>machine.getMaxmemory())
				return false;
		}
		len=machine.getCpu().size();
		for(int i=0;i<len;i++) {
			if(machine.getCpu().get(i)+app.getCpu().get(i)>machine.getMaxCpu())
				return false;
		}
		return true;
	}
	//状态检查
	public boolean checkAll(App[] apps) {
		if(machine.getDisk()>machine.getMaxDisk())
			return false;
		if(machine.getM()>machine.getMaxM())
			return false;
		if(machine.getP()>machine.getMaxP())
			return false;
		if(machine.getPM()>machine.getMaxPM())
			return false;
		int len=machine.getMemory().size();
		for(int i=0;i<len;i++) {
			if(machine.getMemory().get(i)>machine.getMaxmemory())
				return false;
		}
		len=machine.getCpu().size();
		for(int i=0;i<len;i++) {
			if(machine.getCpu().get(i)>machine.getMaxCpu())
				return false;
		}
		return true;
	}
	public boolean checkApp(Instance inst,Map<Integer,Map<Integer,Integer>> mapconstraints) {
		int id=inst.getAppId();
		for(int i:appNum.keySet()) {
			if(!mapconstraints.containsKey(i)||!mapconstraints.get(i).containsKey(id))
				continue;
			int num=0;
			if(appNum.containsKey(id))
				num=appNum.get(id);
			if(id==i) {
				if(mapconstraints.get(i).get(id)+1<=num)
					return false;
			}else {
				if(mapconstraints.get(i).get(id)<=num)
					return false;
			}
		}
		if(mapconstraints.containsKey(id)) {
			for(int i:appNum.keySet()) {
				if(!mapconstraints.get(id).containsKey(i))
					continue;
				if(id==i) {
					if(appNum.get(i)>mapconstraints.get(id).get(i)+1)
						return false;
				}else {
					if(mapconstraints.get(id).get(i)<appNum.get(i))
						return false;
				}
			}
		}
		return true;
	}
	public boolean checkApp(Map<Integer,Map<Integer,Integer>> mapconstraints) {
		for(int i:appNum.keySet()) {
			if(!mapconstraints.containsKey(i))
				continue;
			for(int j:appNum.keySet()) {
				if(!mapconstraints.get(i).containsKey(j))
					continue;
				if(i==j) {
					if(mapconstraints.get(i).get(j)<appNum.get(j)-1)
						return false;
				}else {
					if(mapconstraints.get(i).get(j)<appNum.get(j))
						return false;
				}
			}
		}
		return true;
	}
	//返回一个不满足约束的app
	public int unFitInst(Map<Integer,Map<Integer,Integer>> mapconstraints) {
		for(int i:appNum.keySet()) {
			if(!mapconstraints.containsKey(i))
				continue;
			for(int j:appNum.keySet()) {
				if(!mapconstraints.get(i).containsKey(j))
					continue;
				if(i==j) {
					if(mapconstraints.get(i).get(j)<appNum.get(j)-1)
						return i;
				}else {
					if(mapconstraints.get(i).get(j)<appNum.get(j))
						return i;
				}
			}
		}
		return -1;
	}
	public boolean checkCpu(Instance inst,App[] apps,double value) {
		int len=machine.getCpu().size();
		double maxvalue=0;
		double sum=0;
		int num=0;
		int num1=0;
		double[] portion =new double[len];
		for(int i=0;i<len;i++) {
			if(inst==null) {
				portion[i]=(machine.getCpu().get(i))/machine.getMaxCpu();
			}else {
				portion[i]=(machine.getCpu().get(i)+apps[inst.getAppId()].getCpu().get(i))/machine.getMaxCpu();
			}
			sum+=portion[i];
			if(maxvalue<portion[i])
				maxvalue=portion[i];
			if(portion[i]>value)
				num++;
			if(portion[i]>value+0.05)
				num1++;
		}
		if(num>len/10||num1>len/80)
			return false;
		else
			return true;
	}
	public boolean average(Instance inst,App[] apps,double value) {
		int len=machine.getCpu().size();
		double averagevalue=0;
		double sum=0;
		int numA=0;
		int numa=0;
		double[] portion =new double[len];
		for(int i=0;i<len;i++) {
			if(inst==null) {
				portion[i]=(machine.getCpu().get(i))/machine.getMaxCpu();
			}else {
			portion[i]=(machine.getCpu().get(i)+apps[inst.getAppId()].getCpu().get(i))/machine.getMaxCpu();
			}
			sum+=portion[i];
		}
		averagevalue=sum/len;
		for(int i=0;i<len;i++) {
			if(portion[i]>averagevalue+value)
				numA++;
			if(portion[i]<averagevalue-value)
				numa++;
		}
		if(numA>len/10||numa>len/10)
			return false;
		return true;
	}
	public void addInstance(Instance inst,App[] apps) {
		insts.add(inst);
		if(appNum.containsKey(inst.getAppId())) {
			appNum.put(inst.getAppId(), appNum.get(inst.getAppId())+1);
		}else {
			appNum.put(inst.getAppId(), 1);
		}
		inst.setMachineid(machine.getMachineId());
		App app=apps[inst.getAppId()];
		machine.setDisk(machine.getDisk()+app.getDisk());
		machine.setM(machine.getM()+app.getM());
		machine.setP(machine.getP()+app.getP());
		machine.setPM(machine.getPM()+app.getPM());
		int len=machine.getCpu().size();
		for(int i=0;i<len;i++) {
			machine.getCpu().set(i, machine.getCpu().get(i)+app.getCpu().get(i));
		}
		len=machine.getMemory().size();
		for(int i=0;i<len;i++) {
			machine.getMemory().set(i, machine.getMemory().get(i)+app.getMemory().get(i));
		}
	}
	public void deleteInstance(Instance inst,App[] apps) {
		insts.remove(inst);
		appNum.put(inst.getAppId(), appNum.get(inst.getAppId())-1);
		//inst.setMachineid(0);
		App app=apps[inst.getAppId()];
		machine.setDisk(machine.getDisk()-app.getDisk());
		machine.setM(machine.getM()-app.getM());
		machine.setP(machine.getP()-app.getP());
		machine.setPM(machine.getPM()-app.getPM());
		int len=machine.getCpu().size();
		for(int i=0;i<len;i++) {
			machine.getCpu().set(i, machine.getCpu().get(i)-app.getCpu().get(i));
		}
		len=machine.getMemory().size();
		for(int i=0;i<len;i++) {
			machine.getMemory().set(i, machine.getMemory().get(i)-app.getMemory().get(i));
		}
	}
	public boolean checkCpuOfflineJob(Job job,double value,int instnum,int position) {
		int time=job.getTime();
		value=(value-0.5)*8/insts.size()+0.5;
		int len=machine.getCpu().size();
		int num=0;
		int num1=0;
		double[] portion =new double[len];
		for(int i=position;i<time+position;i++) {
			if(job==null) {
				portion[i]=(machine.getCpu().get(i))/machine.getMaxCpu();
			}else {
				portion[i]=(machine.getCpu().get(i)+job.getCpu()*instnum)/machine.getMaxCpu();
			}
			if(portion[i]>1||
					machine.getMemory().get(i)+job.getMemory()*(instnum+1)>machine.getMaxmemory())
				return false;
			if(portion[i]>value)
				num++;
			if(portion[i]>value+0.05)
				num1++;
		}
		if(num>time/10||num1>time/80)
			return false;
		else
			return true;
	}
	public void addOfflineJob(Job job,int num,int position) {
		Job jobtmp=job.clone();
		jobtmp.setInstnum(num);
		int time=job.getTime();
		for(int i=position;i<position+time;i++) {
			float v=machine.getCpu().get(i);
			machine.getCpu().set(i, v+job.getCpu()*num);
			v=machine.getMemory().get(i);
			machine.getMemory().set(i, job.getMemory()*num+v);
		}
		offTasks.put(jobtmp,position);
	}
	public void deleteOfflineJob(Job job,int num) {
		int position=offTasks.get(job);
		int time=job.getTime();
		for(Job jobtmp:offTasks.keySet()) {
			if(jobtmp.equals(job))
				jobtmp.setInstnum(jobtmp.getInstnum()-num);
		}
		for(int i=position;i<position+time;i++) {
			double v=machine.getCpu().get(i);
			machine.getCpu().set(i, (float) (v-job.getCpu()*num));
		}
	}
	public String toString() {
		double Mem=0;
		for(Float d:machine.getMemory()) {
			if(Mem<d)
				Mem=d;
		}
		return machine.toString()+" "+insts.size()+"分数:"+score+" 剩余硬盘"+(machine.getMaxDisk()-machine.getDisk())
				+" 剩余mem"+(machine.getMaxmemory()-Mem)+"\n";
	}

	@Override
	public int compareTo(MachineState arg0) {
		Float double1=machine.getMaxCpu()+machine.getMaxmemory()+machine.getMaxDisk();
		Float double2=arg0.machine.getMaxCpu()+arg0.machine.getMaxDisk()+arg0.machine.getMaxmemory();
		return double1.compareTo(double2);
	}

	public Machine getMachine() {
		return machine;
	}

	public void setMachine(Machine machine) {
		this.machine = machine;
	}
	public boolean sameMachine(MachineState ms) {
		if(machine.getMaxCpu()!=ms.getMachine().getMaxCpu())
			return false;
		if(machine.getMaxmemory()!=ms.getMachine().getMaxmemory())
			return false;
		if(machine.getMaxDisk()!=ms.getMachine().getMaxDisk())
			return false;
		return true;
	}
}
