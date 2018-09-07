package com.tianchi.taskassign.readFile;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import InputEntities.App;
import InputEntities.AppInterference;
import InputEntities.Instance;
import InputEntities.Machine;
import ioFiles.ReadFiles;
import machineCalc.MachineState;

public class ReadfilesTest {
	private String path="src/main/resources/data2/";

	@Test
	public void testGetApps() {
		List<App> apps=ReadFiles.readAppFile(path+"app_resources.csv");
		System.out.println(apps);
	}
	@Test
	public void testGetmachines() {
		List<MachineState> machines=ReadFiles.ReadMachinesFile(path+"machine_resources.csv");
		System.out.println(machines);
	}
	@Test
	public void testGetInstances() {
		List<Instance> insts=ReadFiles.readInstanceFile(path+"instance_deploy.csv");
		System.out.println(insts);
	}
	@Test
	public void testgetAppInterference() {
		List<AppInterference> ais=ReadFiles.readInterferenceFile(path+"app_interference.csv");
		System.out.println(ais);
	}
}
