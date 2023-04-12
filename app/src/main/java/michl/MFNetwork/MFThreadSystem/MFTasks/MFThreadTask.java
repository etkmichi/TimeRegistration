package MFNetwork.MFThreadSystem.MFTasks;

import MFNetwork.MFData.MFObjects.MFThreadObject;

public abstract class MFThreadTask extends MFThreadObject {
	public static int m_threadTaskCounter=0;
	
	/*Every subclass of task has its own value/name*/
	public int m_taskValue=0;
	public String m_taskName="";
	
	/*do work with undo pattern*/
	public abstract void doWork();
	public abstract  void undoWork();
}
