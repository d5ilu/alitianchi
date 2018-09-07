package InputEntities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * 机器类，描述机器的情况
 * 包括的属性为：cpu、memory、disk、M、PM、的最大值和当前值
 */
public class Machine {

	int machineId;
	List<Float> cpu;
	List<Float> memory;
	int disk;
	int P;
	int M;
	int PM;
	float maxCpu;
	float maxmemory;
	int maxDisk;
	int maxP;
	int maxM;
	int maxPM;
	public Machine(int machineId, float maxCpu, float maxmemory, int maxDisk, int maxP, int maxM, int maxPM) {
		super();
		this.machineId = machineId;
		this.maxCpu = maxCpu;
		this.maxmemory = maxmemory;
		this.maxDisk = maxDisk;
		this.maxP = maxP;
		this.maxM = maxM;
		this.maxPM = maxPM;
		cpu=Arrays.asList(new Float[98*15]);
		memory=Arrays.asList(new Float[98*15]);
		for(int i=0;i<cpu.size();i++) {
			cpu.set(i, 0F);
			memory.set(i, 0F);
		}
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
	public int getMachineId() {
		return machineId;
	}
	public void setMachineId(int machineId) {
		this.machineId = machineId;
	}
	public float getMaxCpu() {
		return maxCpu;
	}
	public void setMaxCpu(float maxCpu) {
		this.maxCpu = maxCpu;
	}
	public float getMaxmemory() {
		return maxmemory;
	}
	public void setMaxmemory(float maxmemory) {
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
