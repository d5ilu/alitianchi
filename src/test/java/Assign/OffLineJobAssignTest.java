package Assign;

import static org.junit.Assert.*;

import org.junit.Test;

import Main.Application;

public class OffLineJobAssignTest {

	@Test
	public void test() {
		Application application=new Application();
		OffLineJobAssign assign=new OffLineJobAssign(application.getMachines(),
				application.getJobs());
		assign.jobSort();
		return;
	}

}
