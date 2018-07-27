package comparator;

import java.util.Comparator;

import Entities.App;
import Entities.Instance;

public class InstanceComparator implements Comparator<Instance>{
	private App[] apps;

	public InstanceComparator(App[] apps) {
		super();
		this.apps = apps;
	}

	@Override
	public int compare(Instance arg0, Instance arg1) {
		return new AppComparator().compare(apps[arg0.getAppId()], apps[arg1.getAppId()]);
	}

}
