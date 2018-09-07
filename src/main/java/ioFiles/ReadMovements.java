package ioFiles;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import Assign.Movement;
import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvRow;
/*
 * 读取迁移后列表，初赛时一种思路，没有使用到
 */
public class ReadMovements {
	private static CsvReader cr=new CsvReader();
	private static final int Applength=4;
	private static final int MachineLength=8;
	private static final int instLength=5;
	public static List<Movement> readResult(String path){
		List<Movement> movements=new ArrayList<Movement>();
		File file=new File(path);
		CsvData cd=cr.read(file);
		List<CsvRow> rows=cd.getRows();
		for(CsvRow row:rows) {
			int instId=Integer.parseInt(row.get(0).substring(instLength));
			int machineId=Integer.parseInt(row.get(1).substring(MachineLength));
			movements.add(new Movement(instId, machineId));
		}
		return movements;
	}
}
