package jade;

import jade.core.behaviours.Behaviour;
import jade.core.Agent;


public class Job {
	
	public String jobType;
	public String jobInfo;
	private int num;
	
	public Job(String jobType, String jobInfo) {
		this.jobType = jobType;
		this.jobInfo = jobInfo;
		this.num = 10;
	}	
	
	public boolean work(Behaviour b){
		if(num != 0)
		{
			System.out.println("Estou a trabalhar: " + jobType + "->" + jobInfo);
			num--;
			b.block(5000);
			return false;
		}
		else return true;
	}
}
