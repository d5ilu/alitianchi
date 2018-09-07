package comparator;

import java.util.Comparator;

import InputEntities.App;

/*
 * 应用排序，三种主要资源不同的权重下的大小排序
 */
public class AppComparator implements Comparator<App> {

	@Override
	public int compare(App arg0, App arg1) {
		//return compare1(arg0,arg1);
		return compare1(arg0,arg1);
	}
	private int compare1(App arg0, App arg1) {
		double v1=500D;
		double v2=20D;
		double v3=6D;
		double cpuav1=0;
		double cpuav2=0;
		for(Float f:arg0.getCpu()) {
			cpuav1+=f;
		}
		for(Float f:arg1.getCpu()) {
			cpuav2+=f;
		}
		cpuav1/=arg0.getCpu().size();
		cpuav2/=arg0.getCpu().size();
		Double value1=v1*cpuav1+v2*arg0.getMemorymax()+v3*arg0.getDisk();
		Double value2=v1*cpuav2+v2*arg1.getMemorymax()+v3*arg1.getDisk();
		return value1.compareTo(value2);
	}
	private int compare2(App arg0,App arg1) {
		double v1=200D;
		double v2=50D; 
		double v3=10D;
		Double value1=Math.max(v1*(arg0.getCpumax()-Math.sqrt(arg0.cpuSqure())),Math.max(v2*arg0.getMemorymax(), v3*arg0.getDisk()));
		Double value2=Math.max(v1*(arg1.getCpumax()-Math.sqrt(arg1.cpuSqure())),Math.max(v2*arg1.getMemorymax(), v3*arg1.getDisk()));
		return value1.compareTo(value2);
	}
}
