package OutEntities;

/*
 * 复赛要求的在线任务迁移格式
 * 格式为：迁移轮数+实例id+machineId
 */
public class OnlineMovement {
	int movetimes; //迁移轮数
	int instId;
	int machineId;
	public OnlineMovement(int movetimes, int instId, int machineId) {
		super();
		this.movetimes = movetimes;
		this.instId = instId;
		this.machineId = machineId;
	}
	public int getMovetimes() {
		return movetimes;
	}
	public void setMovetimes(int movetimes) {
		this.movetimes = movetimes;
	}
	public int getInstId() {
		return instId;
	}
	public void setInstid(int instid) {
		this.instId = instid;
	}
	public int getMachineid() {
		return machineId;
	}
	public void setMachineid(int machineid) {
		this.machineId = machineid;
	}
	
}
