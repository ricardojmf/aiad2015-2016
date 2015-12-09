package messages;
import jade.Job;

public class RequestJobMessage extends AgentMessage{
	
	Job job;

	public RequestJobMessage(String agentName, Job job) {
		super(agentName);
		this.job = job;
	}
	
	public String toString(){
		return messageType.REQUEST_JOB + "-" + agentName + "-" + job.jobType + "-" + job.jobInfo;
	}
}
