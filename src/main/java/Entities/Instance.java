package Entities;

public class Instance {
	int instId;
	int appId;
	int machineid;
	public Instance(int instId, int appId ,int machineId) {
		super();
		this.instId = instId;
		this.appId = appId;
		this.machineid=machineId;
	}
	public int getMachineid() {
		return machineid;
	}
	public void setMachineid(int machineid) {
		this.machineid = machineid;
	}
	public int getInstId() {
		return instId;
	}
	public void setInstId(int instId) {
		this.instId = instId;
	}
	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	public String toString() {
		return "instId:"+instId+"appId:"+appId+"machineid:"+machineid+"\n";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + appId;
		result = prime * result + instId;
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
		Instance other = (Instance) obj;
		if (appId != other.appId)
			return false;
		if (instId != other.instId)
			return false;
		return true;
	}
}
