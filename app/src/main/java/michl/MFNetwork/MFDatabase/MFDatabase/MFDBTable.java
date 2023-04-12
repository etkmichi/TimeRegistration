package MFNetwork.MFDatabase.MFDatabase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.concurrent.Semaphore;

//import MFNetwork.MFData.MFListModels.MFDBTableListModel;
import MFNetwork.MFData.MFObjects.MFObject;
import MFNetwork.MFThreadSystem.MFThreads.MFTaskThread;

/**
 * This class is used for reading, writing and processing a database.
 * To change a key or sth else of the database table some functions must be
 * adjusted.
 * See:<br>
 * subclasses and functions of {@link MFDBData} (for changes of keys or values)
 * subclasses and functions of {@link MFDBTable} (for changes of keys or values)
 * subclasses and functions of {@link MFDatabaseHelper} (just for new tables)
 * @author michl
 *
 */
public abstract class MFDBTable extends MFObject {
	protected String 
		m_tableName="",
		m_primaryKeyName="",
		m_commaSeperatedKeys="";
	
	/**
	 * The default list can be used for gui or sth else*/
/*	protected MFDBTableListModel
		m_defaultList;*/
		
	protected MFDatabaseHelper
		m_dbHelper;
	
	protected Vector<MFDBData>
		m_vecData=new Vector<>();
		
	protected int m_nextID=1;	
	
	/*this vector must be filled with the keys*/
	protected Vector<MFDBKey> m_vecKeys=new Vector<MFDBKey>();
	protected Vector<MFTaskThread> vecNotifyThreads=new Vector<MFTaskThread>();//TODO implement
	
	protected Semaphore
		lockVecData=new Semaphore(1),
		lockNotifyThreads=new Semaphore(1);

	private boolean doUpdate=true;
	
	/**
	 * Sets the default parameters.
	 * <p><strong>
	 * Subclass should implement following in its constructor (with its own design):<p><code>
	 * super(helper,"Employee","EmployeeID");
    	m_className="TRDBEmployee";//for debug use
        addKey("EmployeeID",MFSQLFinals.KEY_TYPE_PRIMARY_INT);//add a primary key
        addKey("FirstName",MFSQLFinals.KEY_TYPE_TEXT);//add other key
        addKey("SecondName",MFSQLFinals.KEY_TYPE_TEXT);//add other key
        addKey("Icon",MFSQLFinals.KEY_TYPE_TEXT);//add other key
        addKey("Color",MFSQLFinals.KEY_TYPE_PRIMARY_INT);//add other key
        genCommaSeperatedKeys();//generate a string for database access
	 * </code><p></strong>
	 * @param dbHelper Subclass of MFDatabaseHelper. Will be used to read/write to a db-file
	 * @param tableName String for table name which will be created in the database file.
	 * @param primaryKeyName name of the primary key in the table.
	 * 
	 */
	public MFDBTable(MFDatabaseHelper dbHelper, String tableName, String primaryKeyName){
		//m_defaultList=new MFDBTableListModel(this);
		m_dbHelper=dbHelper;
		m_primaryKeyName=primaryKeyName;
		m_tableName=tableName;
		m_className="MFDBTable";
	}
	
	//TODO data data which must fit the table else unknown behavior!  
	/**
	 * This will create a new data in the database table with the given data
	 * @param data data which must fit the table else unknown behavior! 
	 * @return -1 if sth failed, else uid
	 */
	public int createData(MFDBData data){
		if(data.getUID()==-1){
			data.m_dbDataUID=getNextUID();
		}
		boolean done=false;
		int index=-1;
		
		if(m_dbHelper==null){
			p_releasePrint("No helper set!");
			return -1;
		}
		data.updateData();
		done=m_dbHelper.execInsertData(m_tableName, 
				getCommaSeperatedKeys(),
				data.getCommaSaperatedData());
		if(!done){
			p_releasePrint("createData(MFDBData data) - Couldn't create table entry");
			return -1;
		}
		
		/*this will add the created contact to this table*/
		addData(data);
		
		/*TODO: The corresponding table data (Coding, TransferConfig) must be created too!
		 * The coding and transferconfig will be set to a standard. if needed users can change it by
		 * an advanced configuration menu*/
		return data.m_dbDataUID;
	}
	/**
	 * This function will generate the comma separated key list out of the key vector. Its
	 * for intern use of subclasses. It should be used in the constructor after the addKey commands
	 * or if a key got added.
	 **/
	protected void genCommaSeperatedKeys(){
		doUpdate=false;
		m_commaSeperatedKeys=m_vecKeys.get(0).getKeyName()+",";
		for(int i=1;i<m_vecKeys.size()-1;i++){
			m_commaSeperatedKeys+=m_vecKeys.get(i).getKeyName()+",";
		}
		m_commaSeperatedKeys+=m_vecKeys.lastElement().getKeyName();
	}
	
	/**
	 * This function is table specific and must be implemented by the specific table class.
	 * It will read the row data an set the table specific fields.
	 * @param result contains a set of results
	 * @return The {@link MFDBData} object which represents the data stored in the specific table.
	 * @throws SQLException
	 */
	public abstract MFDBData readRowData(ResultSet result) throws SQLException;
	
	/**/
	public void createKeyCommands(){
		for(int i=0;i<m_vecKeys.size();i++){
			m_vecKeys.get(i).createKey(m_tableName);
		}
	}

	public Vector<MFDBData> getTableData(){
		return m_vecData;
	}
	public abstract void createReferencesCommands();
	
	
	public void createTable(){
		m_dbHelper.addCMDCreateTable(m_tableName);
		createKeyCommands();
		//createReferencesCommands();
	}
	
	public String getTableName(){
		return m_tableName;
	}
	
	
	public String getCommaSeperatedKeyList(){
		if(doUpdate)
			genCommaSeperatedKeys();
		return m_commaSeperatedKeys;
	}
	
	/**
	 * This method loads all data from a table in the database file to the MFDBTable object. If a table was already loaded,
	 * the inner vector will be cleared and refilled.
	 * @return
	 */
	public boolean loadFromDatabase(){
		p_releasePrint("Selceting all data in table:"+m_tableName);
		ResultSet results=m_dbHelper.selectData(m_tableName, "*");/*m_resultSet contains the data*/
		if(results==null){
			setPrintColor(PRINT_RED);
			p_releasePrint("Couldn't select data from Contact table ResultSet==Null");
			return false;
		}
		lock(lockVecData);
		m_vecData.clear();
		release(lockVecData);
		try {

			while(results.next()){
				MFDBData data=readRowData(results);
				addData(data);
			}
			//results.getStatement().close();
		} catch (SQLException e) {
			p_errPrint(".next() failed - "+e.getMessage());
		}
		return true;
	}
	
	public String getPrimaryKeyName(){
		return m_primaryKeyName;
	}
			
	/**
	 * Adds a key, doesn't update the comma seperated key list.
	 * @param keyName
	 * @param keyType
	 */
	protected void addKey(String keyName,int keyType){
		m_vecKeys.add(new MFDBKey(m_dbHelper,keyName, keyType));
		doUpdate=true;
	}

	public String getUIDKeyName(){
		for(MFDBKey key:m_vecKeys){
			if(key.getKeyType()==MFSQLFinals.KEY_TYPE_PRIMARY_INT){
				return key.getKeyName();
			}
		}
		return null;
	}
	
//	public void createTable(){
//		m_dbHelper.addCMDCreateTable(m_tableName);
//		for(int i=0; i<m_vecKeys.size();i++){
//			m_vecKeys.get(i).createKey(m_tableName);
//		}
//	}
	
	/*this function is used to insert data into the real db.
	 * The data values must be in the same order as the key names.*/
	public boolean execInsertData(Vector<String> dataValues){
		boolean done=false;
		String commaSeperatedValues="";
		if(doUpdate)
			genCommaSeperatedKeys();
		commaSeperatedValues=dataValues.get(0)+",";
		for(int i=1;i<dataValues.size()-1;i++){
			commaSeperatedValues+=dataValues.get(i)+",";
		}
		commaSeperatedValues+=dataValues.lastElement();
		
		done=m_dbHelper.execInsertData(m_tableName, m_commaSeperatedKeys, 
				commaSeperatedValues);
		if(!done)
			return false;
		
		return true;
	}
	
	public boolean execInsertData(String commaSeperatedValues){
		boolean done=false;
		if(doUpdate)
			genCommaSeperatedKeys();
		String commaSeperatedKeyNames=m_commaSeperatedKeys;
		
		done=m_dbHelper.execInsertData(m_tableName, commaSeperatedKeyNames, 
				commaSeperatedValues);
		if(!done)
			return false;
		
		return true;
	}
	
	/**
	 * Appends the data to the tables data vector and inserts the data into the database file.
	 * @param data
	 * @return
	 */
	public boolean insertData(MFDBData data) {
		if(doUpdate)
			genCommaSeperatedKeys();
		addData(data);
		return m_dbHelper.execInsertData(m_tableName, m_commaSeperatedKeys, data.getCommaSaperatedData());
	}
	
	/**
	 * This will search the tables data vector for an entry with uid as table-wide unique identifier (primary key of
	 * the table)
	 * @param uid
	 * @return
	 */
	public MFDBData findData(int uid) {
		lock(lockVecData);
		for(MFDBData iterator:m_vecData) {
			if(iterator.getUID()==uid) {
				release(lockVecData);
				return iterator;
			}
		}
		release(lockVecData);
		return null;
	}
	
	/**
	 * This method will add the data to a vector and a DefaultListModel. The DefaultListModel can
	 * be used for the swing GUI {@link MFDBTable#getListModel()}. The field {@link MFDBData#m_tableIndex} will be
	 * set to the index of this {@link MFDBTable#m_vecData}.
	 * @param data
	 * @return
	 */
	public boolean addData(MFDBData data){
		lock(lockVecData);
		int id=data.getUID();
		if(id>=m_nextID)
			m_nextID=id+1;
		boolean done =m_vecData.add(data);
		if(!done){
			release(lockVecData);
			return false;
		}
		data.m_tableIndex=m_vecData.size()-1;
		/*The default list can be used for gui or sth else*/
		//m_defaultList.addElement(data);
		release(lockVecData);
		return true;
	}

	/**
	 * Removes the data ath index from the database. If the data was removed successfully it will be removed from the intern
	 * vector and this method returns true.
	 * @param index
	 * @return
	 */
	public boolean removeData(int index){
		lock(lockVecData);
		if(index>=m_vecData.size()|| index<0){
			release(lockVecData);
			return false;
		}
		boolean ret=false;
		MFDBData data=m_vecData.get(index);
		ret=m_dbHelper.deleteRow(m_tableName,getPrimaryKeyName(),Integer.toString(data.getUID()));
		if(ret){
			m_vecData.remove(index);
		}
		release(lockVecData);
		return ret;
	}

	public boolean removeData(MFDBData data){
		lock(lockVecData);
		boolean ret=false;
		if(data==null || data.getTable().getPrimaryKeyName()!=getPrimaryKeyName()){
			release(lockVecData);
			return false;
		}
		ret=m_dbHelper.deleteRow(m_tableName,getPrimaryKeyName(),Integer.toString(data.getUID()));
		if(ret){
			m_vecData.remove(data);
		}
		release(lockVecData);
		return ret;
	}
	
	/**
	 * This function searches the table for a MFDBData object which contains the concatenatedName.
	 * Large table will result in large cpu usage!
	 * @param composedName
	 * @return
	 */
	public MFDBData getTableData(String composedName) {
		for(MFDBData data:m_vecData) {
			if(data.getComposedName()==composedName)
				return data;
		}
		p_releasePrint("No composed name (\""+composedName+"\")found within the table.");
		return null;
	}
	
	/**
	 * This function searches the table for a MFDBData object which contains the dbUID.
	 * Large table will result in large cpu usage!
	 * @param dbUID is a unique and DB-global data identifier of the MFDBData object.
	 * @return null or the MFDBData of this table
	 */
	public MFDBData getTableData(int dbUID) {
		for(MFDBData data:m_vecData) {
			if(data.getDBGlobalUID()==dbUID)
				return data;
		}
		return null;
	}
	
	public String getCommaSeperatedKeys(){
		return m_commaSeperatedKeys;
	}
	
	/**
	 * Returns the table entry at index.
	 * @param index to the data in the table
	 * @return MFDBData of the MFDBTable.
	 */	
	public MFDBData get(int index){
		if(index<0 || index>=m_vecData.size()){
			return null;
		}
		return m_vecData.get(index);
	}

	/**
     * returns the next uid for a new entry. The UID will be the highest uid value incremented by 1*/
	public int getNextUID(){
		lock(lockVecData);
		int ret = m_nextID;
		m_nextID++;
		release(lockVecData);
		return ret;
	}
	
/*	*//**
	 * 
	 * @return a DefaultListModel which represents a list of the contacts stored in the database
	 *//*
	public MFDBTableListModel getListModel(){
		return m_defaultList;
	}*/
	
	public MFDatabaseHelper getDBHelper() {
		return m_dbHelper;
	}
	
	/**
	 * 
	 * @return the count of data entries in the table
	 */
	public int size() {
		int ret=-1;
		lock(lockVecData);
		ret=m_vecData.size();
		release(lockVecData);
		return ret;
	}
	
	/**
	 * This method wakes up all waiting threads ({@link MFDBTable#waitForUpdate(MFTaskThread)}
	 */
	public void wakeUp() {
		lock(lockNotifyThreads);
	}
	/**
	 * This function can be called from a thread to wait for an update. it will wake up all sleeping threads.
	 * The threads will sleep till they get interrupted.
	 * @param waitingThread
	 */
	public void waitForUpdate(MFTaskThread waitingThread) {
		lock(lockNotifyThreads);
		try {
			this.vecNotifyThreads.add(waitingThread);
			Thread.sleep(-1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		release(lockNotifyThreads);
	}
}
