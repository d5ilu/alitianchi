package Entities;

public class ResourceProportion {
	int type;
	double cpuProportion;
	double memoryProportion;
	double diskProportion;
	double sum;
	public ResourceProportion(int type, double cpuProportion, double memoryProportion, double diskProportion) {
		super();
		this.type = type;
		this.cpuProportion = cpuProportion;
		this.memoryProportion = memoryProportion;
		this.diskProportion = diskProportion;
		sum=cpuProportion+memoryProportion+diskProportion;
	}
	public double simi(ResourceProportion rp) {
		return Math.pow(this.cpuProportion/this.sum-rp.cpuProportion/rp.sum, 2)+Math.pow(this.memoryProportion/this.sum-rp.memoryProportion/rp.sum, 2)
				+Math.pow(this.diskProportion/this.sum-rp.diskProportion/rp.sum, 2);
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public double getCpuProportion() {
		return cpuProportion;
	}
	public void setCpuProportion(double cpuProportion) {
		this.cpuProportion = cpuProportion;
	}
	public double getMemoryProportion() {
		return memoryProportion;
	}
	public void setMemoryProportion(double memoryProportion) {
		this.memoryProportion = memoryProportion;
	}
	public double getDiskProportion() {
		return diskProportion;
	}
	public void setDiskProportion(double diskProportion) {
		this.diskProportion = diskProportion;
	}
	public String toString() {
		return ""+cpuProportion+" "+memoryProportion+" "+diskProportion;
	}
}
