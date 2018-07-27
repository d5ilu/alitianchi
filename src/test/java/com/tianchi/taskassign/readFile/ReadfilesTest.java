package com.tianchi.taskassign.readFile;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import Entities.App;
import Entities.AppInterference;
import Entities.Instance;
import Entities.Machine;
import ioFiles.ReadFiles;
import machineCalc.MachineState;

public class ReadfilesTest {

	@Test
	public void testGetApps() {
		List<App> apps=ReadFiles.readAppFile();
		System.out.println(apps);
	}
	@Test
	public void testGetmachines() {
		List<MachineState> machines=ReadFiles.ReadMachinesFile();
		System.out.println(machines);
	}
	@Test
	public void testGetInstances() {
		List<Instance> insts=ReadFiles.readInstanceFile();
		System.out.println(insts);
	}
	@Test
	public void testgetAppInterference() {
		List<AppInterference> ais=ReadFiles.readInterferenceFile();
		System.out.println(ais);
	}
}
