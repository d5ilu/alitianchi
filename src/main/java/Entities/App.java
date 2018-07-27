package Entities;

import java.util.List;

public class App {
	int appId;
	List<Double> cpu;
	List<Double> memory;
	int disk;
	int P;
	int M;
	int PM;
	double cpumax;
	double memorymax;
	public App(int appId, List<Double> cpu, List<Double> memory, int disk, int p, int m, int pM) {
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
		for(Double d:cpu) {
			cpumax=Math.max(cpumax, d);
		}
		for(Double d:memory) {
			memorymax=Math.max(cpumax, d);
		}
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
	public List<Double> getCpu() {
		return cpu;
	}
	public void setCpu(List<Double> cpu) {
		this.cpu = cpu;
	}
	public List<Double> getMemory() {
		return memory;
	}
	public void setMemory(List<Double> memory) {
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
