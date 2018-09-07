package Assign;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import InputEntities.Job;
import OutEntities.OfflineAssign;
import machineCalc.MachineState;

public class OffLineJobAssign {
	private  List<MachineState> machines; 
	private List<Job> jobs;
	public OffLineJobAssign(List<MachineState> machines, List<Job> jobs) {
		super();
		this.machines = machines;
		this.jobs = jobs;
	}
	public List<OfflineAssign> assign() {
		List<OfflineAssign> offlineAssigns=new ArrayList<OfflineAssign>();
		Map<String, List<String> > mapAfter=new HashMap<String, List<String>>();
		int len=jobs.size();
		for(Job job:jobs) {
			for(String str:job.getPre()) {
				if(mapAfter.containsKey(str)) {
					mapAfter.get(str).add(job.getId());
				}else {
					List<String> list=new ArrayList<String>();
					list.add(job.getId());
					mapAfter.put(str, list);
				}
			}
		}
		//排序，保证前置任务在前
		jobSort();
		//两个方向：1代表从前向后2表示从后向前
		Map<String,Integer> mapDirection=new HashMap<String,Integer>();
		for(int i=0;i<len/3;i++) {
			mapDirection.put(jobs.get(i).getId(), 1);
		}
		for(int i=len*1/3;i<len;i++) {
			mapDirection.put(jobs.get(i).getId(), 2);
		}
		//保存离线任务开始时间
		Map<String,Integer> map=new HashMap<String,Integer>();
		
		int timelength=1470;
		for(Job job:jobs) {
			int position=0;
			if(mapDirection.get(job.getId())==1) {
				for(String id:job.getPre()) {
					position=Math.max(position,map.get(id));
				}
				if(position==0&&!mapAfter.containsKey(job.getId())) {
					position=(int) (Math.random()*timelength/2);
				}
				if(position<0) {
					System.out.println(position);
				}
				map.put(job.getId(),position+job.getTime());
			}else
				continue;
		}
		for(int i=len-1;i>=0;i--) {
			int position=timelength-1;
			if (mapDirection.get(jobs.get(i).getId())==2){
				if(mapAfter.containsKey(jobs.get(i).getId())) {
					for(String str:mapAfter.get(jobs.get(i).getId())) {
						position=Math.min(position, map.get(str));
					}
				}else {
					if(jobs.get(i).getPre().size()==0)
						position=timelength-1-(int) (Math.random()*timelength/2);
				}
				if(position-jobs.get(i).getTime()<0) {
					System.out.println(position);
				}
				map.put(jobs.get(i).getId(), position-jobs.get(i).getTime());
			}else {
				continue;
			}
		}

		double value=0.53;
		for(Job job:jobs) {
			value=0.52;
			int machineindex=0;
			while(machineindex<6000&&machines.get(machineindex).getScore()==0)
				machineindex++;
			int position=-1;
			if(mapDirection.get(job.getId())==1) {
				position=map.get(job.getId())-job.getTime();
			}else {
				position=map.get(job.getId());
			}
			while(job.getInstnum()>0) {
				int num=0;
				while(machines.get(machineindex).checkCpuOfflineJob(job, value,num, position)
						&&num<=job.getInstnum()) {
					num++;
				}
				num--;
				if(num<=0) {
					machineindex++;
					if(machineindex==8000) {
						machineindex=0;
						value+=0.05;
					}
					continue;
				}
				machines.get(machineindex).addOfflineJob(job.clone(), num, position);
				System.out.println(job.getId()+"分配到"+machineindex+"位置"+position+"数量"+num);
				job.setInstnum(job.getInstnum()-num);
				offlineAssigns.add(new OfflineAssign(job.getId(), machines.get(machineindex).getMachine().getMachineId(),
						position, num));
			}
		}
		double score=0;
		for(MachineState ms:machines) {
			score+=ms.scoreCalc();
			System.out.println(ms.getScore());
		}
		System.out.println(score);
		return offlineAssigns;
	}
	public void jobSort() {
		List<Job> list=new ArrayList<Job>();
		int len=jobs.size();
		boolean state=true;
		HashSet<String> set=new HashSet<String>();
		while(list.size()<len) {
			for(int i=0;i<len;i++) {
				if(set.contains(jobs.get(i).getId()))
					continue;
				List<String> pre=jobs.get(i).getPre();
				if(pre.size()>0) {
					for(String str:jobs.get(i).getPre()) {
						if(!str.equals("")&&!set.contains(str)) {
							state=false;
							break;
						}
					}
				}
				if(state==true) {
					list.add(jobs.get(i));
					set.add(jobs.get(i).getId());
				}else
					state=true;
			}
		}
		jobs=list;
	}
}
