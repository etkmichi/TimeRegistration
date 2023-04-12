package MFNetwork.MFData.MFObjects;
import java.util.concurrent.Semaphore;
/*This Class is used for multithreading. It ensures an easy handling of thread clases*/

public class MFThreadObject extends Thread{
   
	protected volatile boolean 
    	m_stop = false,
    	m_stopped = false,
    	m_started = false;
    
	/*this values describe the print (debug/release) settings for different systems*/
    public static final int
    	m_printSetAndroid = 0,
    	m_printSetDesktop = 1;
    
    public static final String PRINT_BLACK_BACKGROUND = "\u001B[40m";
	public static final String PRINT_RED_BACKGROUND = "\u001B[41m";
	public static final String PRINT_GREEN_BACKGROUND = "\u001B[42m";
	public static final String PRINT_YELLOW_BACKGROUND = "\u001B[43m";
	public static final String PRINT_BLUE_BACKGROUND = "\u001B[44m";
	public static final String PRINT_PURPLE_BACKGROUND = "\u001B[45m";
	public static final String PRINT_CYAN_BACKGROUND = "\u001B[46m";
	public static final String PRINT_WHITE_BACKGROUND = "\u001B[47m";
	
	public static final String PRINT_BLACK=("\033[0m BLACK");
	public static final String PRINT_RED=("\033[31m RED");
	public static final String PRINT_GREEN=("\033[32m GREEN");
	public static final String PRINT_YELLOW=("\033[33m YELLOW");
	public static final String PRINT_BLUE=("\033[34m BLUE");
	public static final String PRINT_MAGENTA=("\033[35m MAGENTA");
	public static final String PRINT_CYAN=("\033[36m CYAN");
	public static final String PRINT_WHITE=("\033[37m WHITE");
    
	public String printColor="\u001B[40m";
	
    /*this value will be set to one of the m_printSet* values to ensure prints on different systems.
     * To print messages use p_debugPrint(String msg) or p_releasePrint(String msg)*/
    protected static int
    	m_printSetting=m_printSetDesktop;
    
    protected int 
	    index=-1,
	    m_sleep=300;
    
	private static String 
		m_baseDir="";
    
	static int m_threadObjectsCount=0;
	
	protected String 
			className="",
			TAG="CAFEBABE";
	
	protected Semaphore 
		m_semaphore;
	
	public MFThreadObject(){
		m_semaphore = new Semaphore(1);
		m_threadObjectsCount++;
	}
	
	public static void setBaseDir(String baseDir){
		m_baseDir=baseDir;
	}
	
	public static String getBaseDir(){
		return m_baseDir;
	}
	
	public String getClassName() {
		return className;
	}
    
    public int getIndex() {
		return index;
	}

    /**
     * The index can be set to keep a track of the object
     * @param index of a list
     */
	public void setIndex(int index) {
		this.index = index;
		
	}
	
	/**
	 * The executing thread will sleep.
	 * @param timeMS
	 */
	protected void goSleep(long timeMS){
        try{
			Thread.sleep(timeMS);
        }catch (InterruptedException e){
        	p_debugPrint("sleep(timeMS) was interrupted");
        }
    }
	
	/**
	 * The executing thread will sleep.
	 * @param timeMS
	 */
	protected void goSleep(){
        try{
        	Thread.sleep(m_sleep);
        }catch (InterruptedException e){
        	p_debugPrint("sleep(timeMS) was interrupted");
        }
    }
	
	/**
	 * This method will acquire one permit of a semaphore. It is only testet for a mutex (semaphore with single permit).
	 * Depending on the lock() design following is advisable:
	 * Call the release(locker) function as soon as possible!!! 
	 * Take/Remove a ressource which must be locked from public access (pe vector) and move
	 * it to a local variable (to ensure no access from outside). Then release the semaphore and work
	 * with the variable. If necessary one release() in every branch of switch, if,...!!!
	 * @param locker
	 * @return
	 */
	protected static boolean lock(Semaphore locker){
    	try{//TODO clear the interrupted flag. Is it necessary?
    		locker.acquire();
    		return true;
    	}catch(InterruptedException e){
    		p_releasePrint("\n\nLock was interrupted before lock or while acquire(): "+e.getMessage()+"\n\n");
    		return false;
    	}
    }
	
	/**
	 * The release of a semaphore must be as close as possible to  the lock(Semaphore) function.
	 * @param semaphore(1)/mutex which must be released.
	 */
    protected static void release(Semaphore locker){
    	locker.release();
    }
    
    /**
	 * This method will lock a MFObjects semaphore. It is only testet for a mutex (semaphore with single permit).
	 * Depending on the lock-design following is advisable:
	 * Call the release(locker) method as soon as possible!!! 
	 * Take/Remove a resource which must be locked from public access (pe vector) and move
	 * it to a local variable (to ensure no access from outside). Then release the semaphore and work
	 * with the variable. If necessary one release() in every branch of switch, if,... and before every return!!!
	 * @param locker
	 * @return
	 */
	protected static boolean lock(MFObject locker){
		return locker.lock();
    }
	
	/**
	 * The release of a MFObjects semaphore must be as close as possible to  the lock(MFObject) method.
	 * @param MFObject - containing a semaphore(1) which must be released.
	 */
    protected static void release(MFObject locker){
    	locker.release();
    }
    
    protected boolean lock(){
        try{
            m_semaphore.acquire();
        }catch(InterruptedException e){
            return false;
        }
        return true;
    }
    protected void release(){
        m_semaphore.release();
    }
    
    public void p_debugPrint(String msg){
    	switch(m_printSetting){
    	case m_printSetAndroid:
    		//nop Log.i();
    		break;
    	case m_printSetDesktop:
    		System.out.println(printColor+TAG+" "+ className+": "+msg);
    		break;
    	}
    	printColor=PRINT_BLACK;
    }
    
    public void setPrintColor(String PRINT_COLOR) {
    	printColor=PRINT_COLOR;
    }
    
    public static void p_releasePrint(String msg){
    	String TAG="release";
    	String className="";
    	switch(m_printSetting){
    	case m_printSetAndroid:
    		break;
    	case m_printSetDesktop:
    		System.out.println(TAG+" "+ className+": "+msg);
    		break;
    	}
    }
    
    public void p_errorPrint(String msg){
    	switch(m_printSetting){
    	case m_printSetAndroid:
    		//nop Log.i();
    		break;
    	case m_printSetDesktop:
    		System.out.println(PRINT_RED+TAG+" "+ className+" Error: "+msg);
    		break;
    	}
    }
    
    //TODO make same as MFObject...
    public void p_errPrint(String msg){
    	switch(m_printSetting){
    	case m_printSetAndroid:
    		//nop Log.i();
    		break;
    	case m_printSetDesktop:
    		System.out.println(PRINT_RED+TAG+" "+ className+": "+msg);
    		break;
    	}
    }
    
    /**
     * will stop the thread after the current work has been done
     * */
    public void exit(){
		m_stop = true;
    }
    
    /**
     * Used for polling the stop flag. A task may ask for the stop flag to cancel an operation.
     * @return true if the thread shall be stopped.
     */
    public boolean checkForStop() {
    	return m_stop;
    }
    
    /**
     * Returns true if the thread reached the end of its run() funtion.
     * @return
     */
    public boolean isStopped(){
        return m_stopped;
    }
    
    /**
     * Returns true if the thread stepped into the run() function.
     * @return
     */
    public boolean isStarted(){
    	return m_started;
    }
}

