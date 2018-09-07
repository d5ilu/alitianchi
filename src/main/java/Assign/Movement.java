package Assign;

/*
 * 输出到csv文件的格式
 * 初赛时使用，复赛事改变输出格式
 */
public class Movement {
	int instId;
	int machineId;
	public Movement(int instId, int machineId) {
		super();
		this.instId = instId;
		this.machineId = machineId;
	}
	public int getInstId() {
		return instId;
	}
	public void setInstId(int instId) {
		this.instId = instId;
	}
	public int getMachineId() {
		return machineId;
	}
	public void setMachineId(int machineId) {
		this.machineId = machineId;
	}
	public String toString() {
		return "实例"+instId+" 迁移到machine"+machineId+'\n';
	}
}
