package machineCalc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Entities.App;
import Entities.Instance;
import Entities.Machine;

public class MachineState implements Comparable<MachineState>{
	private Machine machine;
	private double score;
	private List<Instance> insts;
	private Map<Integer,Integer> appNum;
	private int type;
	public MachineState(Machine machine,int size) {
		super();
		this.machine = machine;
		insts=new ArrayList<Instance>();
		appNum=new HashMap<Integer,Integer>();
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
		if(insts.size()==0)
			return score;
		double a=10;
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
	public boolean checkCpu(Instance inst,App[] apps,double value) {
		int len=machine.getCpu().size();
		double maxvalue=0;
		double averagevalue=0;
		double sum=0;
		int num=0;
		int num1=0;
		double[] portion =new double[len];
		for(int i=0;i<len;i++) {
			portion[i]=(machine.getCpu().get(i)+apps[inst.getAppId()].getCpu().get(i))/machine.getMaxCpu();
			sum+=portion[i];
			if(maxvalue<portion[i])
				maxvalue=portion[i];
			if(portion[i]>value)
				num++;
			if(portion[i]>value+0.1)
				num1++;
		}
		averagevalue=sum/len;
		if(num>len/10||num1>len/80)
			return false;
		else
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

	public String toString() {
		double Mem=0;
		for(Double d:machine.getMemory()) {
			if(Mem<d)
				Mem=d;
		}
		return machine.toString()+" "+insts.size()+"分数:"+score+" 剩余硬盘"+(machine.getMaxDisk()-machine.getDisk())
				+" 剩余mem"+(machine.getMaxmemory()-Mem)+"\n";
	}

	@Override
	public int compareTo(MachineState arg0) {
		Double double1=machine.getMaxCpu()+machine.getMaxmemory()+machine.getMaxDisk();
		Double double2=arg0.machine.getMaxCpu()+arg0.machine.getMaxDisk()+arg0.machine.getMaxmemory();
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
