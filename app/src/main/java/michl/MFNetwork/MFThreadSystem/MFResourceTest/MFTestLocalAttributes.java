package MFNetwork.MFThreadSystem.MFResourceTest;

import MFNetwork.MFThreadSystem.MFTasks.MFTask;
import MFNetwork.MFThreadSystem.MFThreads.MFEasyTaskThread;

/**
 * This Class is used to test the behavior of multi threading.
 * @author michl
 *
 */
public class MFTestLocalAttributes {
	public static MFMethodProvider provider;
	public static void main(String[] args) {
		//Test:
		/**
		 * Two threads will access the localAttributeTest() method:
		 * Goal: See if both threads use the same local1 attribute and the same string attribute.
		 * Expection: The local attributes are allocate when the method is called. For every call new
		 * attributes will be allocated and therefor they are seperated attributes.
		 */
		provider=new MFMethodProvider();
		MFEasyTaskThread thread1=new MFEasyTaskThread();
		MFEasyTaskThread thread2=new MFEasyTaskThread();
		MFLocalsTestTask task1=new MFLocalsTestTask(provider);
		MFLocalsTestTask task2=new MFLocalsTestTask(provider);
		thread1.addTask(task1);
		thread2.addTask(task1);

		thread1.start();
		thread2.start();
	}
	
	

}
/**
 * This task calls a specific function during doWork() method. 
 * Goal: Call from two different threads with two tasks on the same method and watch if the local attributes
 * in the method are the same.
 * @author michl
 *
 */
class MFLocalsTestTask extends MFTask {
	MFMethodProvider methodProvider;
	public MFLocalsTestTask(MFMethodProvider methodProvider) {
		this.methodProvider=methodProvider;
	}
	
	@Override
	public boolean doWork() {
		methodProvider.localAttributeTest();
		return true;
	}

	@Override
	protected boolean undoWork() {
		// TODO Auto-generated method stub
		return false;
	}
}

class MFMethodProvider{
	public MFMethodProvider() {
		
	}
	public static int i=0;
	boolean local1=false;
	public void localAttributeTest() {
		
		String string=Thread.currentThread().getName();
		if(local1) {
			local1=true;
			while(local1==true)
			{
				System.out.println(Thread.currentThread().getName()+"thread local1:"+local1+" "+string+" "+i);
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				i--;
			}
		}else {
			local1=true;
			while(local1==true)
			{
				System.out.println(Thread.currentThread().getName()+"thread local1:"+local1+" "+string+" "+i);
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				i++;
			}
		}
	}
}
