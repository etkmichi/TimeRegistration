package MFNetwork.MFThreadSystem.MFThreads;

import MFNetwork.MFData.MFObjects.MFObject;

public class XMFInterruptSleepTest extends MFObject {

	public static void main(String[] args) {
		//Interrupts the Thread before going to sleep() -> Interrupted flag is set, does sleep() ask for interrupter()?
		Thread.currentThread().interrupt();
		try {
			p_debugStaticPrint("\nsleep1 - testing sleep after interrupt");
			Thread.sleep(2000);
			p_debugStaticPrint("sleep ran through and interrupterd() was ignored");
		} catch (InterruptedException e) {
			p_debugStaticPrint("sleep was interrupted before sleep() was called");
		}
		
		try {
			p_debugStaticPrint("\nsleep2 - testing for interrupted flag");
			Thread.sleep(2000);
			p_debugStaticPrint("interrupted flag was cleared!");
		} catch (InterruptedException e) {
			p_debugStaticPrint("sleep interrupted flag wasnt cleared!");
		}
		
		Thread.currentThread().interrupt();
		try {
			Thread.interrupted();
			p_debugStaticPrint("\nsleep3 - clearing interrupted flag before sleep call");
			Thread.sleep(2000);
			p_debugStaticPrint("sleep ran through and interrupterd() was cleared");
		} catch (InterruptedException e) {
			p_debugStaticPrint("sleep was interrupted before sleep() was called");
		}
	}

}
