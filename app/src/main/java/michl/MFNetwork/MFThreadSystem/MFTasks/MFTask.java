package MFNetwork.MFThreadSystem.MFTasks;

import java.util.Vector;

import MFNetwork.MFData.MFObjects.MFObject;
import MFNetwork.MFThreadSystem.MFThreads.MFTaskThread;
/*TODO: MFTask erweitern:
 * ->task reihenfolge mit der einstellung, dass jede task abgebrochen wird, wenn eine vorherige fehlschlägt
 * */
/**
 * The task class represents a executable class which can be added to a task thread. 
 * The localField taskString is for monitoring tasks.
 * @author michl
 *
 */
public abstract class MFTask extends MFObject {
	public static int 
	m_taskCounter=0,
	m_taskIDCounter=0;

	/**
	 * If a task needs to add another task, it can use m_dispatcher to add it.
	 * The MFTaskThread should set itself as this.taskThread.
	 * */
	protected MFTaskThread
	taskThread;

	protected Vector<MFTask> 
	m_vecPriorTask=new Vector<MFTask>();

	/*This variable can be polled to stop a loop while executing*/
	protected boolean forceStop=false;
	
	protected boolean 
	m_finished=false,
	m_appendable=true,
	m_dispatchable=true,
	m_isExecuting=false;

	protected int	
	m_maxIndex=-1,
	m_preferredTaskQIndex=0;

	protected int 
	m_minIndex=-1,
	m_taskID=0;

	protected long
	m_initTimestamp=0,
	m_startTimestamp=0,
	m_stopTimestamp=0,
	m_processingTime=0;

	public String 
	m_taskName="MFTask",
	taskString="";

	private int counter=0;

	public static Vector<String>
	m_vecTaskList=new Vector<String>();

	public MFTask(){
		m_initTimestamp=System.currentTimeMillis();
		m_className="MFTask";
		m_taskCounter++;
		m_taskIDCounter++;
		m_taskID=m_taskIDCounter;
	}

	protected boolean preWork(){
		if(!isDispatchable())/*Checks for prior tasks*/
		{
			if((counter%100)==0)
				p_debugPrint("task cant be dispatched yet.");
			counter++;
			taskThread.addTask(this);/*the task was removed for executing, add it to the task thread again.*/
			return false;	
		}
		m_isExecuting=true;
		m_startTimestamp=System.currentTimeMillis();
		return true;
	}

	/**
	 * this function should be used to execute the work. it contains time meassurement and other
	 * convenient functionality. The timestamps can be used for interpretation of the system behavior.
	 * TODO make timestamps accessible. </br>m_initTimestamp,
		</br>m_startTimestamp,
		</br>m_stopTimestamp,
		</br>m_processingTime;
	 */
	public void execute(){
		if(!preWork())
			return;

		if(doWork()){
			m_finished=true;
		}else{
			p_releasePrint("Something went wrong! taskString=\""+taskString+"\"");
			m_finished=false;
		}
		postWork();
	}

	/**
	 * If a task implements polling for the forceStop field, this function can stop
	 * this task.
	 */
	public void forceStop() {
		forceStop=true;
	}
	
	public int getPrefferedIndex(){
		return m_preferredTaskQIndex;
	}

	/*If taskString field was set in the constructor of a sub class, this function will add it to a static vector. the vector can be used for monitoring*/
	public void addTaskStringToList() {
		m_vecTaskList.add(taskString);
	}

	/**
	 * The doWork function will be executed if a task thread is calling execute() on the task.
	 * @return depends on the work to do.
	 */
	public abstract boolean doWork();
	protected abstract boolean undoWork();

	protected void postWork(){
		m_stopTimestamp=System.currentTimeMillis();
		m_processingTime = m_stopTimestamp-m_startTimestamp;
		p_debugPrint("Time: Exec = "+m_processingTime+"ms"+" existence = "+(m_stopTimestamp-m_initTimestamp)+"ms");
		m_taskCounter--;
	}

	public boolean isFinished() {
		return m_finished;
	}

	public boolean isAppendable(){
		return m_appendable;
	}

	public int getMaxTaskQIndex(){
		return m_maxIndex;
	}

	public void setMaxTaskQIndex(int index){
		m_maxIndex=index;
	}

	/**
	 * All prior tasks must be executed (doWork()) before this task. This method can be used to 
	 * generate a execution sequence.
	 * @param task to execute before this task.
	 * */
	public void addPriorTask(MFTask task){
		if(task.getUniqueObjectID()==m_objectID)
			return;
		if(task == this) /*should be the same as above*/
			return;
		if(task.isFinished())
			return;
		m_dispatchable=false;
		m_minIndex=task.getMaxTaskQIndex();
		m_vecPriorTask.add(task);/*TODO implement task circle check*/
	}

	public boolean isDispatchable(){
		if(m_dispatchable)
			return true;
		else{
			for(int i=0;i<m_vecPriorTask.size();i++){
				/*if one of the prior tasks isn't finished, then this task is not dispachable*/
				if(!m_vecPriorTask.get(i).isFinished())
					return false;
			}
			return true;
		}
	}

	public void p_debugPrint(String msg){
		switch(m_printSetting){
		case m_printSetAndroid:
			break;
		case m_printSetDesktop:
			System.out.println(printColor+TAG+" "+ m_taskName+": "+msg);
			break;
		}
		printColor=PRINT_BLACK;
	}

	public void p_releasePrint(String msg){
		switch(m_printSetting){
		case m_printSetAndroid:
			//nop Log.i();
			break;
		case m_printSetDesktop:
			System.out.println(printColor+TAG+" "+ m_taskName+": "+msg);
			break;
		}
		printColor=PRINT_BLACK;
	}

	public void p_functionErrPrint(String functionName,Exception e) {
		/*TODO add color code for console*/
		printColor=PRINT_RED;
		String msg=printColor+TAG+" "+ m_taskName+": "+functionName+" went wrong: ";
		printColor=PRINT_BLACK;
		if(e!=null)
			msg+=e.getMessage();
		else
			msg+="no exception message commited";

		switch(m_printSetting){
		case m_printSetAndroid:
			//nop Log.i();
			break;
		case m_printSetDesktop:/*TODO draw some chars red*/
			System.out.println(msg);
			break;
		}

	}

	public boolean isDispatched(){
		return m_finished;
	}

	public MFTaskThread getTaskThread() {
		return taskThread;
	}

	public void setTaskThread(MFTaskThread taskThread) {
		this.taskThread = taskThread;
	}

}
