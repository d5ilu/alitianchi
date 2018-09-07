package InputEntities;

import java.util.List;
/*
 * 离线任务，包含几个属性：id，cpu占用，内存占用，实例个数，需要持续时间
 */
public class Job implements Cloneable{
	String id;
	float cpu;
	float memory;
	int instnum;
	int time;
	List<String >pre;
	public Job(String id, float cpu, float memory,  int instnum, int time, List<String> pre) {
		super();
		this.id = id;
		this.cpu = cpu;
		this.memory = memory;
		this.instnum = instnum;
		this.time = time;
		this.pre = pre;
	}
	@Override
	public Job clone() {
		try {
			return (Job) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public float getCpu() {
		return cpu;
	}
	public void setCpu(float cpu) {
		this.cpu = cpu;
	}
	public float getMemory() {
		return memory;
	}
	public void setMemory(float memory) {
		this.memory = memory;
	}
	public int getInstnum() {
		return instnum;
	}
	public void setInstnum(int instnum) {
		this.instnum = instnum;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public List<String> getPre() {
		return pre;
	}
	public void setPre(List<String> pre) {
		this.pre = pre;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(cpu);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		temp = Double.doubleToLongBits(memory);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + time;
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
		Job other = (Job) obj;
		if (Double.doubleToLongBits(cpu) != Double.doubleToLongBits(other.cpu))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (Double.doubleToLongBits(memory) != Double.doubleToLongBits(other.memory))
			return false;
		if (time != other.time)
			return false;
		return true;
	}
	public String toString() {
		return id;
	}
}
