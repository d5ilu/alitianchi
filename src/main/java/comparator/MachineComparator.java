package comparator;

import java.util.Comparator;

import machineCalc.MachineState;

public class MachineComparator implements Comparator<MachineState>{

	@Override
	public int compare(MachineState arg0, MachineState arg1) {
		double v1=15D;
		double v2=7D;
		double v3=1D;
		Double value0=v1*arg0.getMachine().getMaxCpu()+v2*arg0.getMachine().getMaxDisk()+v3*arg0.getMachine().getMaxmemory();
		Double value1=v1*arg1.getMachine().getMaxCpu()+v2*arg1.getMachine().getMaxDisk()+v3*arg1.getMachine().getMaxmemory();
		return value0.compareTo(value1);
	}

}
