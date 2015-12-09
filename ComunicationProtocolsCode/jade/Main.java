package jade;

import messages.AgentMessage;
import messages.RequestJobMessage;

public class Main {
	
	public static void main(String[] args) {
		
		Job j1 = new Job("Transport", "50->Wood->WarehouseOne");
		Job j2 = new Job("Make/Give", "20->Chair");
		
		RequestJobMessage rjm1 = new RequestJobMessage("AgentBob", j1);
		System.out.println(rjm1.toString());
		
		RequestJobMessage rjm2 = new RequestJobMessage("AgentJohn", j2);
		System.out.println(rjm2.toString());
	}
}
