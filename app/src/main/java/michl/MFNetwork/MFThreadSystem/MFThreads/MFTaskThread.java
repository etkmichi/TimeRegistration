package MFNetwork.MFThreadSystem.MFThreads;

import java.util.Vector;
import java.util.concurrent.Semaphore;

import MFNetwork.MFData.MFObjects.MFThreadObject;
import MFNetwork.MFThreadSystem.MFTasks.MFTask;

public abstract class MFTaskThread extends MFThreadObject {
	protected Vector<MFTask>
		m_vecTaskQ;
	/**
	 * TODO implement static vector with all MFTaskThread to exit all at once if a program shuts down.
	 */
	protected Semaphore 
		lockTaskQ;
	
	protected int 
		m_sleepTimeIdle=150,
		m_sleepTimePause=50;
	
	protected long
		m_initTimestamp=0,
		m_startTimestamp=0,
		m_stopTimestamp=0,
		m_processingTime=0,
		lifeSpan=0;

	private long endOfLife;
	
	public MFTaskThread(){
		m_initTimestamp=System.currentTimeMillis();
		className="MFTaskThread";
		m_vecTaskQ = new Vector<MFTask>();
		lockTaskQ 		= new Semaphore(1);
	}
	
	/**
	 * The task will be added to the task queue and will be dispatched while the thread is running.
	 * @param task to execute
	 */
	public void addTask(MFTask task){
		lock(lockTaskQ);
		m_vecTaskQ.add(task);
		task.setTaskThread(this);
		release(lockTaskQ);
		
	}
	public void addTask(MFTask task,int pos){
		lock(lockTaskQ);
		if(pos>m_vecTaskQ.size() || pos<0)
			addTask(task);
		else
			m_vecTaskQ.insertElementAt(task, pos);
		release(lockTaskQ);
		
	}
	
	public void clearTasks() {
		lock(lockTaskQ);
		m_vecTaskQ.clear();
		release(lockTaskQ);
	}
	
	public MFTask removeTask(int index) {
		MFTask task = null;
		lock(lockTaskQ);
		if(m_vecTaskQ.size()>index) {
			task=m_vecTaskQ.remove(index);			
		}
		release(lockTaskQ);
		return task;
	}
	
	public void run(){
		m_startTimestamp=System.currentTimeMillis();
		endOfLife=m_startTimestamp+lifeSpan;
		m_started=true;
		p_debugPrint("- is running");
		MFTask task;
		while(!m_stop){
			/*Execute tasks*/
			lock(lockTaskQ);
			if(!m_vecTaskQ.isEmpty()){
				task=m_vecTaskQ.remove(0);
				release(lockTaskQ);/*release must be as close to lock() as possible!!!*/
				task.execute();
				goSleep(m_sleepTimePause);
			}else{
				/*No task? Go sleep!*/
				release(lockTaskQ);
				goSleep(m_sleepTimeIdle);
			}
			
			/*Check if lifespan is exceeded*/
			if(lifeSpan>0) {
				if(endOfLife<System.currentTimeMillis()) {
					m_stop=true;
					break;
				}
			}
		}
		postWork();
		m_stopTimestamp=System.currentTimeMillis();
		m_processingTime = m_stopTimestamp-m_startTimestamp;
		p_debugPrint("Processing time for thread ("+Thread.currentThread().getName()+") ="+m_processingTime+" milliseconds");
		p_debugPrint("Lifetime for thread = "+(m_stopTimestamp-m_initTimestamp)+" milliseconds");
		m_stopped=true;
	}
	
	/**
	 * If the thread didn't start, it will be executed for about timeMS. The execution is dependend of the tasks return, it
	 * can't interrupt a task while its executing. If the thread is already executing, it will life for about timeMS longer.
	 * @param timeMS is the approximated time to life from start or if the thread is already executing, its the approximated
	 * time since setLifeSpan() was called.
	 */
	public void setLifeSpan(int timeMS) {
		endOfLife=System.currentTimeMillis()+lifeSpan;
		lifeSpan=timeMS;
	}
	
	/**
	 * Waits till the thread reached the end of run or till waitTimeoutMS is exceeded.
	 * @param waitTimeoutMS maximum time in milliseconds to wait
	 * @param waitSleepTime time in milliseconds to sleep while staiing in blocking loop
	 * @return true if thread reached end. False if timeout was passed. It is possible that thread didnt start!
	 */
	public boolean waitForStopped(int waitTimeoutMS,int waitSleepTime) {
		if(!m_started)
			p_releasePrint("waitForStopped(...) - Thread wasn't started yet!"); 
		int timeout=waitTimeoutMS;
		while(!m_stopped) {
			goSleep(waitSleepTime);
			timeout-=waitSleepTime;
			if(timeout<=0)
				return false;
		}
		return true;
	}
	protected abstract void postWork();
	
	public void setSleepTimeIdle(int timeMS){
		lock();
		m_sleepTimeIdle=timeMS;
		release();
	}
	public void setSleepTimePause(int timeMS){
		lock();
		m_sleepTimePause=timeMS;
		release();
	}
}
