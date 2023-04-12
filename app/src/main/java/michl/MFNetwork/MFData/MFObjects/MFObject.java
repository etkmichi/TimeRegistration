package MFNetwork.MFData.MFObjects;
import java.util.concurrent.Semaphore;


public class MFObject{
	
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
	
	public static final String PRINT_BLACK=("\033[0m");
	public static final String PRINT_RED=("\033[31m");
	public static final String PRINT_GREEN=("\033[32m");
	public static final String PRINT_YELLOW=("\033[33m");
	public static final String PRINT_BLUE=("\033[34m");
	public static final String PRINT_MAGENTA=("\033[35m");
	public static final String PRINT_CYAN=("\033[36m");
	public static final String PRINT_WHITE=("\033[37m");
	
	public String printColor="\u001B[40m";
    
	/**
	 * This attribute can be set to mark the index of this object within a data structure.
	 */
	public int volatileIndex=0;

	public static int 
		m_objectsCount=0,
		m_printSetting=1;
	protected int
		m_objectID;
	
	protected String 
		m_className="",
		TAG="CAFEBABE";
	
	private static String 
		m_baseDir="";
	
	protected Semaphore 
		m_mainLock;	

	private static int sleepTime=50;
	
	
	public synchronized static int getSleepTime() {
		return sleepTime;
	}
	
	public synchronized static void setSleepTime(int sleepTime) {
		MFObject.sleepTime = sleepTime;
	}
	
	/**
	 * @return the TAG which is printed into the log
	 */
	public String getPrintTag() {
		return TAG;
	}

	/**
	 * @param set the tag which is printed into the log
	 */
	public void setTAG(String TAG) {
		this.TAG = TAG;
	}

	public MFObject(){
		m_baseDir=System.getProperty("user.dir");
		m_mainLock = new Semaphore(1);
		m_objectsCount++;
		m_objectID=m_objectsCount;
	}
	
	public int getUniqueObjectID(){
		return m_objectID;
	}
	
	public static void setBaseDir(String baseDir){
		m_baseDir=baseDir;
	}
	
	public static String getBaseDir(){
		return m_baseDir;
	}
	
	protected static void goSleep(){
        try{
            Thread.sleep(sleepTime);
        }catch (InterruptedException e){
        	p_debugStaticPrint(e.getMessage());
        }
    }
	
	/**
	 * Sends the executing thread to sleep for timeMS milliseconds.
	 * @param timeMS time to sleep in milliseconds.
	 */
	protected static void goSleep(long timeMS){
        try{
            Thread.sleep(timeMS);
        }catch (InterruptedException e){
        	p_debugStaticPrint(e.getMessage());
        }
    }
	
	protected static boolean lock(Semaphore locker){
    	try{
    		locker.acquire();
    		return true;
    	}catch(InterruptedException e){
    		p_debugStaticPrint(e.getMessage());
    		return false;
    	}
    }
	
	public String getClassName(){
		return m_className;
	}
	
    protected static void release(Semaphore locker){
    	locker.release();
    }
    
    /**
     * This function should be used external! It only ensures 
     * thread safety if it is used for every data external manipulation! 
	 * To prevent deadlocks don't use it class-internal and release it in before every return/break call
	 * @return
     */
    public boolean lock(){
        try{
            m_mainLock.acquire();
        }catch(InterruptedException e){
        	p_debugStaticPrint(e.getMessage());
            return false;
        }
        return true;
    }
    
    public void release(){
        m_mainLock.release();
    }
    
    public void p_debugPrint(String msg){
    	switch(m_printSetting){
    	case m_printSetAndroid:
    		//nop Log.i();
    		break;
    	case m_printSetDesktop:
    		System.out.println(printColor+TAG+" "+ m_className+": "+msg);
    		break;
    	}
    	printColor="";
    }
    
    public void p_errPrint(String msg){
    	switch(m_printSetting){
    	case m_printSetAndroid:
    		//nop Log.i();
    		break;
    	case m_printSetDesktop:
    		System.out.println(PRINT_RED+TAG+" "+ m_className+": "+msg);
    		break;
    	}
    }
    
    public static void p_debugStaticPrint(String msg){
    	switch(m_printSetting){
    	case m_printSetAndroid:
    		//nop Log.i();
    		break;
    	case m_printSetDesktop:
    		System.out.println("MFObject p_debugStaticPrint "+": "+msg);
    		break;
    	}
    }
    
    public void p_releasePrint(String msg){
    	switch(m_printSetting){
    	case m_printSetAndroid:
    		//nop Log.i();
    		break;
    	case m_printSetDesktop:
    		System.out.println(printColor+TAG+" "+ m_className+": "+msg);
    		break;
    	}
    	printColor="";
    }

	public void setPrintColor(String printColor) {
		this.printColor = printColor;
	}
}

