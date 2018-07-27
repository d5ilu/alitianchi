package Entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Machine {

	int machineId;
	List<Double> cpu;
	List<Double> memory;
	int disk;
	int P;
	int M;
	int PM;
	double maxCpu;
	double maxmemory;
	int maxDisk;
	int maxP;
	int maxM;
	int maxPM;
	public Machine(int machineId, double maxCpu, double maxmemory, int maxDisk, int maxP, int maxM, int maxPM) {
		super();
		this.machineId = machineId;
		this.maxCpu = maxCpu;
		this.maxmemory = maxmemory;
		this.maxDisk = maxDisk;
		this.maxP = maxP;
		this.maxM = maxM;
		this.maxPM = maxPM;
		cpu=Arrays.asList(new Double[98]);
		memory=Arrays.asList(new Double[98]);
		for(int i=0;i<cpu.size();i++) {
			cpu.set(i, 0D);
			memory.set(i, 0D);
		}
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
	public int getMachineId() {
		return machineId;
	}
	public void setMachineId(int machineId) {
		this.machineId = machineId;
	}
	public double getMaxCpu() {
		return maxCpu;
	}
	public void setMaxCpu(double maxCpu) {
		this.maxCpu = maxCpu;
	}
	public double getMaxmemory() {
		return maxmemory;
	}
	public void setMaxmemory(double maxmemory) {
		this.maxmemory = maxmemory;
	}
	public int getMaxDisk() {
		return maxDisk;
	}
	public void setMaxDisk(int maxDisk) {
		this.maxDisk = maxDisk;
	}
	public int getMaxP() {
		return maxP;
	}
	public void setMaxP(int maxP) {
		this.maxP = maxP;
	}
	public int getMaxM() {
		return maxM;
	}
	public void setMaxM(int maxM) {
		this.maxM = maxM;
	}
	public int getMaxPM() {
		return maxPM;
	}
	public void setMaxPM(int maxPM) {
		this.maxPM = maxPM;
	}
	public String toString() {
		return "machineId:"+machineId;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + machineId;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Machine other = (Machine) obj;
		if (machineId != other.machineId)
			return false;
		return true;
	}
	
}
