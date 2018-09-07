package InputEntities;

import java.util.List;

/*
 * 在线任务应用，包含id，cpu和memory的 98*15 点的占用，
 * 以及硬盘占用，以及P，M，PM三种重要资源的占用
 * 
 */
public class App {
	int appId;
	List<Float> cpu;
	List<Float> memory;
	int disk;
	int P;
	int M;
	int PM;
	double cpumax;
	double memorymax;
	public App(int appId, List<Float> cpu, List<Float> memory, int disk, int p, int m, int pM) {
		super();
		this.appId = appId;
		this.cpu = cpu;
		this.memory = memory;
		this.disk = disk;
		P = p;
		M = m;
		PM = pM;
		cpumax=0;
		memorymax=0;
		for(Float d:cpu) {
			cpumax=Math.max(cpumax, d);
		}
		for(Float d:memory) {
			memorymax=Math.max(cpumax, d);
		}
	}
	public double cpuSqure() {
		double averagevalue=0.0d;
		double sum=0.0;
		for(Float d:cpu) {
			sum+=d;
		}
		averagevalue=sum/cpu.size();
		double cpuSqure=0.0;
		for(Float d:cpu) {
			cpuSqure=Math.pow(d-averagevalue, 2);
		}
		return cpuSqure;
	}
	public String toString() {
		return "appid:"+appId +" "+cpu.size()+" "+memory.size()+"\n";
	}
	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	public List<Float> getCpu() {
		return cpu;
	}
	public void setCpu(List<Float> cpu) {
		this.cpu = cpu;
	}
	public List<Float> getMemory() {
		return memory;
	}
	public void setMemory(List<Float> memory) {
		this.memory = memory;
	}
	public int getDisk() {
		return disk;
	}
	public void setDisk(int disk) {
		this.disk = disk;
	}
	public int getP() {
		return P;
	}
	public void setP(int p) {
		P = p;
	}
	public int getM() {
		return M;
	}
	public void setM(int m) {
		M = m;
	}
	public int getPM() {
		return PM;
	}
	public void setPM(int pM) {
		PM = pM;
	}
	public double getCpumax() {
		return cpumax;
	}
	public void setCpumax(double cpumax) {
		this.cpumax = cpumax;
	}
	public double getMemorymax() {
		return memorymax;
	}
	public void setMemorymax(double memorymax) {
		this.memorymax = memorymax;
	}
}
