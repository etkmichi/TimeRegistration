package MFNetwork.MFDatabase.MFDatabase;

import java.util.Vector;

import MFNetwork.MFCom.MFComDatabase.MFComDatabase;


/*Database tester*/
public class TestingDB {

	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException {
		String m_dbDir="/home/michl/JavaProjects/dbtest/";
		String m_dbName="test2.sqlite";
		MFComDatabase m_helper=new MFComDatabase(m_dbDir,m_dbName);
		Vector<String> keyList=new Vector<String>();
		keyList.add("ClientID integer PRIMARY KEY");
		keyList.add("ServerIP text NOT NULL");
		keyList.add("Port integer");
		keyList.add("Client integer");
		keyList.add("Client2 integer");
		
		m_helper.addCMDCreateTable("ClientData");
		m_helper.printCommand();
		
		m_helper.addCMDCreateTable("Another");
		m_helper.printCommand();
		
		m_helper.addCMDCreateKeyInteger("Another", "KeyInAnotherTable");
		m_helper.addCMDCreateKeyText("Another", "TextAnotherTable");

		m_helper.addCMDCreateKeyReal("Another", "RealAnotherTable");
		m_helper.addCMDCreateKeyReal("ClientData", "RealAnotherTable");
		m_helper.addCMDCreateKeyInteger("ClientData", "KeyInClientDataTable");
		m_helper.printCommand();
		
		//m_helper.executeCommand();
	}

}
