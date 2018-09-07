package InputEntities;

/*
 * app之间的冲突 
 * 冲突形式为：同一个machine中，第一种应用存在的情况下第二种应用最多的个数
 */
public class AppInterference {
	int appId1;
	int appId2;
	int num;
	public AppInterference(int appId1, int appId2, int num) {
		super();
		this.appId1 = appId1;
		this.appId2 = appId2;
		this.num = num;
	}
	public String toString() {
		return "appId1:"+appId1+" appId2:"+appId2+" num:"+num+"\n";
	}
	public int getAppId1() {
		return appId1;
	}
	public void setAppId1(int appId1) {
		this.appId1 = appId1;
	}
	public int getAppId2() {
		return appId2;
	}
	public void setAppId2(int appId2) {
		this.appId2 = appId2;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
} 
