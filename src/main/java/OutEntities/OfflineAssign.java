package OutEntities;
/*
 * 复赛要求的离线任务部署的输出文件格式
 * 格式为：id+machineId+开始时间+分配数量
 */
public class OfflineAssign {
	
	String id;
	int machineId;
	int startTime;
	int numAssigned;
	public OfflineAssign(String id, int machineId, int startTime, int numAssigned) {
		super();
		this.id = id;
		this.machineId = machineId;
		this.startTime = startTime;
		this.numAssigned = numAssigned;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getMachineId() {
		return machineId;
	}
	public void setMachineId(int machineId) {
		this.machineId = machineId;
	}
	public int getStartTime() {
		return startTime;
	}
	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}
	public int getNumAssigned() {
		return numAssigned;
	}
	public void setNumAssigned(int numAssigned) {
		this.numAssigned = numAssigned;
	}
	
}
