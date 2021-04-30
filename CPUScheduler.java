import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class CPUScheduler {
	static ArrayList<Job> list = new ArrayList<Job>();

	private static class Job implements Comparable<Job> {

		private String pid;
		private int arrival_time;
		private int remaining_burst_time;
		private int burst_time;
		private static int numJobs;
		private int response_time;
		private int tat;
		private int wt;
		private boolean first_time;

		public Job(String pid, int arrival_time, int burst_time) {

			this.pid = pid;
			this.arrival_time = arrival_time;
			this.remaining_burst_time = burst_time;
			this.burst_time = burst_time;
			this.first_time = true;
			numJobs++;

		}

		public boolean getFirst_time() {
			return first_time;
		}

		public int getResponse_time() {
			return response_time;
		}

		public int getBurst_time() {
			return burst_time;
		}

		public String getPid() {
			return pid;
		}

		public int getArrival_time() {
			return arrival_time;
		}

		public int getRemainingBurst_time() {
			return remaining_burst_time;
		}

		public static int getNumJobs() {
			return numJobs;
		}

		public int getTat() {
			return tat;
		}

		public int getWt() {
			return wt;
		}

		public void setFirst_time(boolean v) {
			this.first_time = v;
		}

		public void setResponse_time(int v) {
			this.response_time = v;
		}

		public void setPid(String pid) {
			this.pid = pid;
		}

		public void setArrival_time(int arrival_time) {
			this.arrival_time = arrival_time;
		}

		public void setRemainingBurst_time(int burst_time) {
			this.remaining_burst_time = burst_time;
		}

		public void setTat(int tat) {
			this.tat = tat;
		}

		public void setWt(int wt) {
			this.wt = wt;
		}

		@Override
		public int compareTo(Job o) {

			Job job_n = (Job) o;
			if (this.arrival_time > job_n.arrival_time) {
				return 1;
			} else if (this.arrival_time < job_n.arrival_time)
				return -1;

			else
				return 0;

		}

	}

	/**
	 * For FCFS, all the process in the list_fcfs are sorted based on their arrival
	 * order. All the process are executed based on their arrival order. If the
	 * arrival of a process is greater than the current time (completion time of
	 * previous process), the CPU is idle. For FCFS, Process turn around time =
	 * process completion time - arrival time, Process waiting time = Process
	 * turn around time - process CPU burst time, Process response time = first time
	 * process gets the CPU - arrival time
	 */
	public static void FCFS(int num_process) {

		String[] Process_id = new String[list.size()];
		int[] Arr_time = new int[list.size()];
		int[] Cpu_Burst_time = new int[list.size()];
		int index = 0;
		ArrayList<Job> list_fcfs = new ArrayList<Job>(list);

		// System.out.println("SORTED BASED ON ARRIVAL TIME:");

		Collections.sort(list_fcfs);

		for (int i = 0; i < list_fcfs.size(); i++) {

			// System.out.println("Process = " + list_fcfs.get(i).getPid() + " ,
			// arrival_time = "
			// + list_fcfs.get(i).getArrival_time() + " , burst_time = " +
			// list_fcfs.get(i).getBurst_time());
			Process_id[index] = list_fcfs.get(i).getPid();
			Arr_time[index] = list_fcfs.get(i).getArrival_time();
			Cpu_Burst_time[index] = list_fcfs.get(i).getBurst_time();
			index++;
		}

		// System.out.println("\nSCHEDULING: ");

		int[] completion_time = new int[Process_id.length];
		int[] waiting_time = new int[Process_id.length];
		int[] turn_aroundtime = new int[Process_id.length];
		int[] response_time = new int[Process_id.length];

		double avg_waiting_time = 0, avg_turnaround_time = 0, avg_response_time = 0;
		int time;
		int cpu_idle_time = 0;

		if (Arr_time[0] == 0) { // process arrive at time 0
			completion_time[0] = Cpu_Burst_time[0];
			time = 0;
			response_time[0] = time;

		} else { // process did not arrive at time 0

			int cpu_start_idle_time = cpu_idle_time;
			while (Arr_time[0] != cpu_idle_time) {

				cpu_idle_time++;
			}

			// System.out.println("CPU idle from time " + cpu_start_idle_time + " to " +
			// cpu_idle_time);
			System.out.println("[" + cpu_start_idle_time + " - " + cpu_idle_time + "]" + "  CPU idle");
			completion_time[0] = cpu_idle_time + Cpu_Burst_time[0];
			time = cpu_idle_time;
			response_time[0] = time - Arr_time[0];

		}

		while (time != completion_time[0]) {
			time++;
		}

		// System.out.println("Process " + Process_id[0] + " is executing from time "
		// +cpu_idle_time+ " to " + completion_time[0]);
		System.out.println("[" + cpu_idle_time + " - " + completion_time[0] + "]  " + Process_id[0] + " running");

		time = completion_time[0];

		for (int i = 1; i < Process_id.length; i++) {

			int idle_time = 0;

			if (completion_time[i - 1] < Arr_time[i]) {

				idle_time = completion_time[i - 1];

				int idle_start_time = idle_time;

				while (idle_time != Arr_time[i]) {

					idle_time++;
					time++;
				}
				// System.out.println("CPU idle from time " + idle_start_time + " to " +
				// idle_time);
				System.out.println("[" + idle_start_time + " - " + idle_time + "]  " + "CPU idle");
			}

			if (idle_time > 0) { // idle time exists
				completion_time[i] = Arr_time[i] + Cpu_Burst_time[i];

			} else {// no cpu idle time
				completion_time[i] = completion_time[i - 1] + Cpu_Burst_time[i];
				time = completion_time[i - 1];

			}

			response_time[i] = time - Arr_time[i];
			int pro_start_time = time;

			while (time != completion_time[i]) {
				time++;
			}

			// System.out.println("Process " + Process_id[i] + " is executing from time "
			// +pro_start_time + " to "+ completion_time[i]);
			System.out.println("[" + pro_start_time + " - " + completion_time[i] + "]  " + Process_id[i] + " running");
		}

		System.out.println();

		for (int i = 0; i < Process_id.length; i++) {
			turn_aroundtime[i] = completion_time[i] - Arr_time[i];
			waiting_time[i] = turn_aroundtime[i] - Cpu_Burst_time[i];
			// response_time[i] = waiting_time[i];
		}

		for (int i = 0; i < Process_id.length; i++) {
			avg_response_time += response_time[i];
			avg_waiting_time += waiting_time[i];
			avg_turnaround_time += turn_aroundtime[i];

		}

		System.out.println("Turnaround times: ");
		for (int i = 0; i < Process_id.length; i++) {

			System.out.println(Process_id[i] + " = " + turn_aroundtime[i]);
		}

		System.out.println();

		System.out.println("Wait times: ");
		for (int i = 0; i < Process_id.length; i++) {

			System.out.println(Process_id[i] + " = " + waiting_time[i]);
		}

		System.out.println();

		System.out.println("Response times: ");
		for (int i = 0; i < Process_id.length; i++) {

			System.out.println(Process_id[i] + " = " + response_time[i]);
		}

		System.out.println();

		avg_turnaround_time = avg_turnaround_time / num_process;
		System.out.printf("Average turn around time: %.2f\n", avg_turnaround_time);

		avg_waiting_time = avg_waiting_time / num_process;
		System.out.printf("Average waiting time: %.2f\n", avg_waiting_time);

		avg_response_time = avg_response_time / num_process;
		System.out.printf("Average response time: %.2f\n", avg_response_time);

	}

	/**
	 * For RR, the process in list_rr are sorted based on their arrival order. As
	 * current time progresses, processes are added to the queue. The first time
	 * array is used to keep track of which processes have executed on the CPU for
	 * the first time so that we can calculate the response time ( first time
	 * process gets the CPU - arrival time). If the queue is empty and the process
	 * is not the last remaining process to get the CPU, calculate the CPU idle time
	 * until another process is arrived. Once the process is arrived, its added to
	 * the queue. If a process has remainingCPU burst of 0 and it has finished
	 * executing (flag = 1), process turn around and waiting time is calculated.
	 * Process turn around time = current time - arrival time and Process waiting
	 * time = process turn around time - process CPU burst time.
	 *
	 **/
	public static void RR(int num_process, int time_quantum) {

		// Scanner sc = new Scanner(System.in);
		// int time_quantum;

		// System.out.println("ENTER QUANTUM FOR RR SCHEDULING: ");

		// time_quantum = sc.nextInt();

		ArrayList<Job> list_rr = new ArrayList<Job>(list);
		Collections.sort(list_rr);

		int current_time = 0, remain = num_process;
		int flag = 0;
		int cpu_time = 0;
		boolean first_time[] = new boolean[num_process];

		for (int i = 0; i < num_process; i++) {
			first_time[i] = true;
		}

		cpu_time = current_time;
		while (current_time != list_rr.get(0).getArrival_time()) {
			// System.out.println("CPU idle at time "+time);
			current_time++;
		}
		if (cpu_time != current_time) {
			// System.out.println("CPU idle from " + cpu_time + " to " + current_time);
			System.out.println("[" + cpu_time + " - " + current_time + "]" + "  CPU idle");
		}

		Queue<Job> queue = new LinkedList<Job>();

		queue.add(list_rr.get(0));
		Job job = null;

		while (remain > 0) {

			if (!queue.isEmpty()) {

				job = queue.poll();
			}

			if (job != null && job.getRemainingBurst_time() <= time_quantum && job.getRemainingBurst_time() > 0) {

				int st_time = current_time;

				if (job.getFirst_time() == true) {
					job.setResponse_time(current_time - job.getArrival_time());
					job.setFirst_time(false);

				}

				current_time += job.getRemainingBurst_time();
				job.setRemainingBurst_time(0);
				flag = 1;

				// System.out.println(job.getPid() + " running " + st_time + " - " +
				// current_time);
				System.out.println("[" + st_time + " - " + current_time + "]  " + job.getPid() + " running");
				for (int i = 0; i < list_rr.size(); i++) {

					if (!queue.contains((list_rr).get(i)) && list_rr.get(i).getArrival_time() <= current_time
							&& list_rr.get(i).getRemainingBurst_time() > 0) {
						queue.add(list_rr.get(i));
						// System.out.println(list_rr.get(i).getPid());
					}

				}

			} else if (job != null && job.getRemainingBurst_time() > 0) {
				int start_time = current_time;
				if (job.getFirst_time() == true) {
					job.setResponse_time(current_time - job.getArrival_time());
					job.setFirst_time(false);

				}
				int remBurst_time = job.getRemainingBurst_time() - time_quantum;
				job.setRemainingBurst_time(remBurst_time);
				current_time += time_quantum;

				// System.out.println(job.getPid() + " running " + start_time + " - " +
				// current_time);
				System.out.println("[" + start_time + " - " + current_time + "]  " + job.getPid() + " running");

				for (int i = 0; i < list_rr.size(); i++) {
					if (list_rr.get(i).getPid().equals(job.getPid())) {
						// System.out.println("here for "+job.getPid());
						continue;
					}

					if (!queue.contains((list_rr).get(i)) && list_rr.get(i).getArrival_time() <= current_time
							&& list_rr.get(i).getRemainingBurst_time() > 0) {
						queue.add(list_rr.get(i));
						// System.out.println(list_rr.get(i).getPid());
					}

				}
				if (job.getRemainingBurst_time() > 0) {
					queue.add(job);
					// System.out.println("Aftr: "+job.getPid());
				}

			}
			if (flag == 1 && job.getRemainingBurst_time() == 0) {

				remain--;
				job.setTat(current_time - job.getArrival_time());
				job.setWt(job.getTat() - job.getBurst_time());
				flag = 0;

			}

			if (queue.isEmpty() && remain >= 1) {

				int cpu_idle_start_time = current_time;
				int cpu_idle_finish_time = 0;
				for (int i = 0; i < list_rr.size(); i++) {
					if (list_rr.get(i).getArrival_time() > current_time) {
						cpu_idle_finish_time = list_rr.get(i).getArrival_time();
						break;
					}
				}

				while (current_time != cpu_idle_finish_time) {
					current_time++;
				}
				// System.out.println("CPU is idle " + cpu_idle_start_time + " - " +
				// current_time);
				System.out.println("[" + cpu_idle_start_time + " - " + current_time + "]" + "  CPU idle");

				for (int i = 0; i < list_rr.size(); i++) {
					if (cpu_idle_finish_time == list_rr.get(i).getArrival_time()) {
						queue.add(list.get(i));
					}
				}
			}

		}
		// sort the list based on process id(pid)
		Collections.sort(list_rr, Comparator.comparing(Job::getPid));

		System.out.println();
		System.out.println("Turnaround times:");
		for (int i = 0; i < list_rr.size(); i++) {
			System.out.println(list_rr.get(i).getPid() + " = " + list_rr.get(i).getTat());
		}

		System.out.println();
		System.out.println("Wait times:");
		for (int i = 0; i < list_rr.size(); i++) {
			System.out.println(list_rr.get(i).getPid() + " = " + list_rr.get(i).getWt());
		}

		System.out.println();
		System.out.println("Response times:");
		for (int i = 0; i < list_rr.size(); i++) {
			System.out.println(list_rr.get(i).getPid() + " = " + list_rr.get(i).getResponse_time());
		}

		double avg_response_time = 0, avg_tat_time = 0, avg_waiting_time = 0;

		for (int i = 0; i < num_process; i++) {

			avg_response_time += list_rr.get(i).getResponse_time();
			avg_tat_time += list_rr.get(i).getTat();
			avg_waiting_time += list_rr.get(i).getWt();

		}

		avg_response_time = avg_response_time / num_process;
		avg_tat_time = avg_tat_time / num_process;
		avg_waiting_time = avg_waiting_time / num_process;

		System.out.println();

		System.out.printf("Average turn around time: %.2f\n", avg_tat_time);
		System.out.printf("Average wait time: %.2f\n", avg_waiting_time);
		System.out.printf("Average response time: %.2f\n", avg_response_time);

	}

	public static void main(String[] args) {

		String fileName = args[0];
		int time_quantum = Integer.parseInt(args[1]);

		 System.out.println(fileName);
		// System.out.println(time_quantum);

		try {
			FileInputStream fstream = new FileInputStream(fileName);
			Scanner input = new Scanner(fstream);
			String line;
			Scanner info;
			String pid;
			int arrival_time, burst_time;

			System.out.println("************************************************************************************");
			System.out.println("                          " + "Reading Input From File" + "                             ");
			System.out.println("************************************************************************************");
			while (input.hasNext()) {
				line = input.nextLine();
				info = new Scanner(line);
				pid = info.next(); // PID
				arrival_time = Integer.parseInt(info.next()); // Arrival Time
				burst_time = Integer.parseInt(info.next()); // CPU Burst
				System.out.println("pid:" + pid);
				System.out.println("arrival time:" + arrival_time);
				System.out.println("burst time:" + burst_time);
				System.out.println();

				Job job = new Job(pid, arrival_time, burst_time);
				list.add(job);

			}

			int numProcess = Job.getNumJobs();
			System.out.println("************************************************************************************");
			System.out.println("                        " + "Cpu Scheduling Simulation" + "                             ");
			System.out.println("************************************************************************************");

			System.out.println();

			System.out.println("************************************************************************************");
			System.out.println("                       " + "Firt Come First Served Scheduling" + "                     ");
			System.out.println("************************************************************************************");
			FCFS(numProcess);
			System.out.println();

			System.out.println("************************************************************************************");
			System.out.println("                           " + "Round Robin Scheduling" + "                             ");
			System.out.println("************************************************************************************");

			RR(numProcess, time_quantum);

			System.out.println("************************************************************************************");
			System.out.println("                           " + "Project done by Ala Sobhan" + "                         ");
			System.out.println("************************************************************************************");

		} catch (FileNotFoundException e) {
			System.out.println("FILE NOT FOUND!");
		}

	}

}
