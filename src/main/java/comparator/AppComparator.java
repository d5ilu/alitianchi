package comparator;

import java.util.Comparator;

import Entities.App;

public class AppComparator implements Comparator<App> {

	@Override
	public int compare(App arg0, App arg1) {
		//return compare1(arg0,arg1);
		return compare2(arg0,arg1);
	}
	private int compare1(App arg0, App arg1) {
		double v1=50D;
		double v2=20D;
		double v3=6D;
		Double value1=v1*arg0.getCpumax()+v2*arg0.getMemorymax()+v3*arg0.getDisk();
		Double value2=v1*arg1.getCpumax()+v2*arg1.getMemorymax()+v3*arg1.getDisk();
		return value1.compareTo(value2);
	}
	private int compare2(App arg0,App arg1) {
		double v1=500D;
		double v2=10D; 
		double v3=10D;
		Double value1=Math.max(v1*arg0.getCpumax(),Math.max(v2*arg0.getMemorymax(), v3*arg0.getDisk()));
		Double value2=Math.max(v1*arg1.getCpumax(),Math.max(v2*arg1.getMemorymax(), v3*arg1.getDisk()));
		return value1.compareTo(value2);
	}
}
