package messages;
import jade.Job;

public class RequestJobMessage extends AgentMessage{
	
	Job job;

	public RequestJobMessage(Job job) {
		this.job = job;
	}
	
	public String toString(){
		return messageType.REQUEST_JOB + "-" + job.jobType + "-" + job.jobInfo;
	}
}
