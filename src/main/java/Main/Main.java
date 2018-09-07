package Main;

import java.util.List;

import Assign.Movement;
import OutEntities.OfflineAssign;
import OutEntities.OnlineMovement;
import ioFiles.WriteFiles;
import optimization.Optimization;

public class Main {
	public static void main(String[] args) {
		Application app=new Application();
		List<OnlineMovement> movement=app.assign2();
		List<OfflineAssign> assigns=app.assign3();
		String path="src/main/resources/data/resultdata_a";
		WriteFiles.writeOnlineFile(movement, path);
		WriteFiles.writeOffLineJobs(assigns, path);
		return ;
	}
}
