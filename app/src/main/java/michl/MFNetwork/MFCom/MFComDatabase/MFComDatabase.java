package MFNetwork.MFCom.MFComDatabase;
import MFNetwork.MFClient.MFClientDBData.MFDBCodingData;
import MFNetwork.MFClient.MFClientDBData.MFDBMessageData;
import MFNetwork.MFClient.MFClientDBData.MFDBServerAdminData;
import MFNetwork.MFClient.MFClientDBData.MFDBServerData;
import MFNetwork.MFClient.MFClientDBData.MFDBTransferConfigData;
import MFNetwork.MFClient.MFClientDBData.MFDBTransferServerData;
import MFNetwork.MFClient.MFClientDBTables.MFDBClientAccountTable;
import MFNetwork.MFClient.MFClientDBTables.MFDBCodingListTable;
import MFNetwork.MFClient.MFClientDBTables.MFDBCodingTable;
import MFNetwork.MFClient.MFClientDBTables.MFDBContactTable;
import MFNetwork.MFClient.MFClientDBTables.MFDBContactTagConfigTable;
import MFNetwork.MFClient.MFClientDBTables.MFDBMessageTable;
import MFNetwork.MFClient.MFClientDBTables.MFDBServerAdminTable;
import MFNetwork.MFClient.MFClientDBTables.MFDBServerTable;
import MFNetwork.MFClient.MFClientDBTables.MFDBServerTagConfigTable;
import MFNetwork.MFClient.MFClientDBTables.MFDBTagTable;
import MFNetwork.MFClient.MFClientDBTables.MFDBTransferConfigTable;
import MFNetwork.MFClient.MFClientDBTables.MFDBTransferServerTable;
import MFNetwork.MFDatabase.MFDatabase.MFDatabaseHelper;

/**
 * The MFComDatabase is a MFDatabaseHelper and creates a clients database. It is needed for
	 * access to the database data. It contains the MFDBTable subclasses for the table creation/loading.
	 * For further information about the database structure see the entity relationship diagram.<br>
	 * <strong>Tables:<br></strong>
	 *
	 *<ul>
	 *<li> 	{@link MFDBClientAccountTable}<br>
	 *<li> 	{@link MFDBContactTable}<br>
	 *<li> 	{@link MFDBServerTable}<br>
	 *<li>	{@link MFDBTransferConfigTable}<br>
	 *<li> 	{@link MFDBCodingTable}<br>
	 *<li>	{@link MFDBTransferServerTable}<br>
	 *<li>	{@link MFDBMessageTable}<br>
	 *<li>	{@link MFDBCodingListTable}<br>
	 *<li>	{@link MFDBContactTagConfigTable}<br>
	 *<li> 	{@link MFDBTagTable}<br>
	 *<li> 	{@link MFDBServerTagConfigTable}<br>
	 *<li> 	{@link MFDBServerAdminTable}<br>
	 *</ul>
 */
public class MFComDatabase extends MFDatabaseHelper{
	
	/*Tables of the database*/
	protected MFDBClientAccountTable
		m_tableClientAccount = new MFDBClientAccountTable(this);
	
	protected MFDBContactTable
		m_tableContact=new MFDBContactTable(this);
	
	protected MFDBServerTable
		m_tableServer=new MFDBServerTable(this);
	
	protected MFDBTransferConfigTable
		m_tableTransferConfig=new MFDBTransferConfigTable(this);
	
	protected MFDBCodingTable
		m_tableCoding=new MFDBCodingTable(this);
	
	protected MFDBTransferServerTable
		m_tableTransferServer=new MFDBTransferServerTable(this);
	
	protected MFDBMessageTable
		m_tableMessage=new MFDBMessageTable(this);
	
	protected MFDBCodingListTable
		m_tableCodingList=new MFDBCodingListTable(this);
	
	protected MFDBContactTagConfigTable
		m_tableContactTagConfig = new MFDBContactTagConfigTable(this);
	
	protected MFDBTagTable
		m_tableTag= new MFDBTagTable(this);
	
	protected MFDBServerTagConfigTable
		m_tableServerTagConfig = new MFDBServerTagConfigTable(this);
	
	protected MFDBServerAdminTable
		m_tableServerAdmin = new MFDBServerAdminTable(this);

	//TODO implement userID
	protected int m_userID=-1;
	
	
	

	
	protected static MFDBServerData m_standardServer = null;
	
	/**
	 *
	 * @param dbDir - path to the database (without file name!)
	 * @param dbName - file name of the database file (without path!)
	 * <br><br>
	 * The MFComDatabase is a MFDatabaseHelper and creates the communication database. It is needed for
	 * access to the communication database data. It contains the MFDBTable subclasses for the table creation/loading. It creates
	 * the {@link MFContact} objects which include all the information for communication usage.
	 * For further information about the database structure see the entity relationship diagram.<br>
	 * <strong>Tables:<br></strong>
	 *
	 *<ul>
	 *<li> 	{@link MFDBClientAccountTable}<br>
	 *<li> 	{@link MFDBContactTable}<br>
	 *<li> 	{@link MFDBServerTable}<br>
	 *<li>	{@link MFDBTransferConfigTable}<br>
	 *<li> 	{@link MFDBCodingTable}<br>
	 *<li>	{@link MFDBTransferServerTable}<br>
	 *<li>	{@link MFDBMessageTable}<br>
	 *<li>	{@link MFDBCodingListTable}<br>
	 *<li>	{@link MFDBContactTagConfigTable}<br>
	 *<li> 	{@link MFDBTagTable}<br>
	 *<li> 	{@link MFDBServerTagConfigTable}<br>
	 *<li> 	{@link MFDBServerAdminTable}<br>
	 *</ul>
	 * @author michl
	 */
	public MFComDatabase(String dbDir,String dbName){
		super(dbDir,dbName);
		className="MFComDatabase";
		connectDB();		
	}
	
	public static void setStandardServer(MFDBServerData server){
		m_standardServer = server;
	}
	
	public static MFDBServerData getStandardServer(){
		return m_standardServer;
	}
	
	public MFDBContactTable getContactTable(){
		return m_tableContact;
	}
	
	public MFDBServerTable getServerTable(){
		return m_tableServer;
	}
	
	public MFDBTransferConfigTable getTransferConfigTable(){
		return m_tableTransferConfig;
	}
	
	public MFDBCodingTable getCodingTable(){
		return m_tableCoding;
	}
	
	//TODO::Implement all classes (tables and data)
	public boolean addServer(MFDBServerData serverData){
		int contactID=m_tableServer.getNextUID();
		boolean done=false;
		
		serverData.serverID=contactID;
		
//		done=execInsertData("Contact", "ContactID,FirstName,SecondName,Alias,IconName", 
//													+contactID+","
//													+m_contactData.firstName+","
//													+m_contactData.secondName+","
//													+m_contactData.alias+","
//													+m_contactData.iconName);
		if(!done)
			return false;
		
		//m_contactTable.addContact(m_contactData);
		
		return false;
	}
	
	/*Adds a transfercoding and returns the id of transfercoding*/
	public int addTransferConfig(MFDBTransferConfigData transferConfig){
		m_tableTransferConfig.addData(transferConfig);
		return -1;
	}
	
	public boolean addTransferServer(MFDBTransferServerData serverData){
		return false;
	}
	
	public boolean addServerAdmin(MFDBServerAdminData serverAdmin){
		return false;
	}
	
	public boolean addCoding(MFDBCodingData coding){
		return m_tableCoding.addData(coding);
	}
	
	public boolean addMessage(MFDBMessageData message){
		return false;
	}
	
	public void loadData(){
		m_tableContact.loadFromDatabase();
		m_tableServer.loadFromDatabase();
		m_tableMessage.loadFromDatabase();
	}
	
	/**
	 * Creates the client database. If new tables were added to the database, this function must be updated.
	 * Implemented create calls in this function:<br>
	 * m_tableClientAccount.createTable();<br>
		m_tableServer.createTable();<br>
		m_tableMessage.createTable();<br>
		m_tableContact.createTable();<br>
		m_tableCodingList.createTable();<br>
		m_tableCoding.createTable();<br>
		m_tableContactTagConfig.createTable();<br>
		m_tableTransferConfig.createTable();<br>
		m_tableTransferServer.createTable();<br>
		m_tableTag.createTable();<br>
		m_tableServerTagConfig.createTable();<br>
		m_tableServerAdmin.createTable();<br>
	 */
	public void createClientDB(){
		/*1 ClientAccount table*/
		m_tableClientAccount.createTable();
		/*2 Server table*/
		m_tableServer.createTable();
		/*3 Message table*/
		m_tableMessage.createTable();
		/*4 Contact  table*/
		m_tableContact.createTable();
		/*5 CodingList  table*/
		m_tableCodingList.createTable();
		/*6 Coding  table*/
		m_tableCoding.createTable();
		/*7 ContactTagConfig  table*/
		m_tableContactTagConfig.createTable();
		/*8 TransferConfig  table*/
		m_tableTransferConfig.createTable();
		/*9 TransferServer  table*/
		m_tableTransferServer.createTable();
		/*10 Tag  table*/
		m_tableTag.createTable();
		/*11 ServerTagConfig  table*/
		m_tableServerTagConfig.createTable();
		/*12 ServerAdmin  table*/
		m_tableServerAdmin.createTable();
		
		//this.printFinalCommand();
		this.executeCommand();
	}

	public MFDBMessageTable getMessageTable() {
		return m_tableMessage;
	}

	/**
	 * The database can be set up for specific user. The must be used to identify a user.
	 * @return
	 */
	
	public int getUserID() {
		return m_userID;
	}
}
