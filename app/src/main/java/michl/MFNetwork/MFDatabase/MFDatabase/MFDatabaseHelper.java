package MFNetwork.MFDatabase.MFDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.concurrent.Semaphore;
import java.util.regex.Matcher;

import MFNetwork.MFThreadSystem.MFThreads.MFTaskThread;

//TODO generate function to write the database en/decrypted to the disc.
public class MFDatabaseHelper extends MFTaskThread {
	//TODO It could be more performant if the String.replace methods are replaced with hard-coded methods
	protected String 
		m_dbDir="",
		m_dbName="",
		m_cmdLine="",
		m_nameCurrentTable="";
	
	protected boolean 
		m_DBExists=false;
	
	protected Connection 
		m_connection;
	
	protected Statement 
		m_statement;
	
	protected ResultSet
		m_resultSet;
	
	//TODO change to MFDBTable
	protected Vector<String> 
		m_vecTables;
	
	protected Semaphore
		m_lockSQLOperation=new Semaphore(1);
	
	/**
	 * 
	 * @param dbDir
	 * @param dbName
	 */
	public MFDatabaseHelper(String dbDir, String dbName){
		m_dbName=dbName;

		m_dbDir=dbDir;
		if(!m_dbDir.endsWith("/")){
			m_dbDir+="/";
		}
		
		try{
			Class.forName("org.sqldroid.SQLDroidDriver");
		}catch(ClassNotFoundException e){
			p_releasePrint("Constructor: Class.forName(org.sqlite.JDBC): "+e.getMessage());
		}
		
		m_vecTables=new Vector<String>();
		className = "MFDatabaseHelper";
	}
	
	/**
	 * Connects the database with the specified db-name.
	 * @return if it was successfully connected
	 */
	public boolean connectDB(){
		if(m_dbName.isEmpty() || m_dbDir.isEmpty()){
			m_DBExists=false;
			return false;
		}
		if(m_DBExists){
			p_releasePrint(className+" connectDB() - was already called before! This method will return without reconnecting!");
			return true;
		}
		lock(m_lockSQLOperation);
		try{
			String sqlString="jdbc:sqldroid:"+m_dbDir+m_dbName;
			p_debugPrint("Connecting DB: "+sqlString);
			m_connection = DriverManager.getConnection(sqlString);
		}catch(SQLException e){
			p_releasePrint("MFClientDatabaseHelper(String dbDir) - cant establish database connection: "+e.getMessage());
			m_DBExists=false;
			release(m_lockSQLOperation);
			return false;
		}
		m_DBExists=true;
		release(m_lockSQLOperation);
		return true;
	}
	
	/**
	 * 
	 * @return if true if the connection wasn't closed.
	 */
	public boolean isConnected() {
		if(m_connection==null)
			return false;
		try {
			return !m_connection.isClosed();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			p_releasePrint(e.getMessage());
			return false;
		}
	}
	
	/**
	 * 
	 * @return the name of the database
	 */
	public String getDBName() {
		return m_dbName;
	}
	
	/**
	 * prints the SQLCommand which was set up.
	 */
	public void printCommand(){
		p_releasePrint("SQL Command:\n"+m_cmdLine+"\n");
	}
	
	/**
	 * prints the command as it would be used in command line
	 */
	public void printFinalCommand(){
		String[] cmds=m_cmdLine.split(";");
		String nextCMD="";
		for(int i=0;i<cmds.length-1;i++){
			if(!cmds[i].isEmpty()){
				
				nextCMD=cmds[i];
				if(i==0)
					nextCMD="\n"+nextCMD;
				nextCMD=nextCMD.substring(1, nextCMD.length());
				/*Cut off the replacement string*/
				nextCMD=nextCMD.split(MFSQLFinals.replaceNextReplacement)[0];
				
				/*Remove the last two char's*/
				nextCMD=nextCMD.substring(0, nextCMD.length()-2)+"\n);\n";
				p_releasePrint("\n---------------------------SQL Command "+(i+1)+"----------------------------\n"+nextCMD+
						"---------------------------------------------------------------------------------\n\n");
			}
		}
	}
	
	/**
	 * executes the set up commands.
	 * @return
	 */
	public boolean executeCommand(){
		connectDB();
		if(!m_DBExists){
			p_releasePrint("no database connected");
			return false;
		}
		String[] cmds=m_cmdLine.split(";");
		String nextCMD="";
		if(lock(m_lockSQLOperation));else return false;
		for(int i=0;i<cmds.length-1;i++){
			if(!cmds[i].isEmpty()){
				nextCMD=cmds[i];
				
				/*Cut off the replacement string*/
				nextCMD=nextCMD.split(MFSQLFinals.replaceNextReplacement)[0];
				
				/*Remove the last two char's*/
				nextCMD=nextCMD.substring(0, nextCMD.length()-2)+"\n);\n";
				//p_releasePrint("Executing SQL-Command"+(i+1)+":"+nextCMD);
				try{
					m_statement=m_connection.createStatement();
					m_statement.execute(nextCMD);
				}catch(SQLException e){
					p_errorPrint("SQLException - "+ e.getMessage());
				}
			}
		}
		release(m_lockSQLOperation);
		m_cmdLine="";
		return true;//TODO return false for some exceptions
	}
	
	/**
	 * Executes the cmd string
	 * @param cmd string with SQLite command
	 * @return
	 */
	public boolean executeCommand(String cmd){
		if(!m_DBExists){
			p_releasePrint("no database connected");
			return false;
		}
		String[] cmds=cmd.split(";");
		p_debugPrint("Executing command: "+cmd);
		lock(m_lockSQLOperation);
		try{
			for(int i=0;i<cmds.length;i++){
				m_statement=m_connection.createStatement();
				m_statement.execute(cmds[i]+";");
			}
		}catch(SQLException e){
			p_debugPrint("SQLException - executeCommand(String) failed"+ e.getMessage());
			release(m_lockSQLOperation);
			return false;
		}
		release(m_lockSQLOperation);
		return true;
	}
	
	/**
	 * Sets the dir of the database. In this dir the db file will be created or opened
	 * @param dbDir path to the database
	 */
	public void setDBDir(String dbDir){
		m_dbDir=dbDir;
		if(!m_dbName.isEmpty() && !m_dbDir.isEmpty())
			connectDB();
	}
	
	/**
	 * sets the name of the db. The db will be opened or created with this name
	 * @param dbName
	 */
	public void setDBName(String dbName){
		m_dbName=dbName;
		if(!m_dbName.isEmpty() && !m_dbDir.isEmpty())
			connectDB();
	}
	
	/**
	 * This function appends the primary key statement onto two keys. Not tested!
	 * @param key1
	 * @param key2
	 */
	public void addCMDAppendPrimaryKeysStatement(String key1,String key2){
		/*TODO testing*/
		String newLine=new String(MFSQLFinals.append2PrimaryKeyStatement);/*Template for SQLite reference to a foreign primary key*/
		newLine=newLine.replaceFirst(MFSQLFinals.replaceKey,key1);/*Replacement of the key name in the table*/
		newLine=newLine.replaceFirst(MFSQLFinals.replaceKey,key2);/*Replacement of the foreign key name*/
		
		addCMDCreateKey(m_nameCurrentTable,newLine);
	}
	
	/**
	 * Creates a table.
	 * @param tableName
	 */
	public void addCMDCreateTable(String tableName){
		/*like "CREATE TABLE IF NOT EXISTS tableName (\n"
		 * if table is available in cmd line it will return*/
		m_nameCurrentTable=tableName;
		if(m_cmdLine.contains("CREATE TABLE IF NOT EXISTS "+tableName+" "))
			return;
		
		String createTableCMD=MFSQLFinals.createTable_withReplace;
		
		/*Insert the table name*/
		createTableCMD=createTableCMD.replaceFirst(MFSQLFinals.replaceTableName, tableName);
		
		/*Make the replacement mark unique for this table. To add a key to this table we cann call replaceAll with the table specific replacement*/
		createTableCMD=createTableCMD.replaceFirst(MFSQLFinals.replaceNextReplacement, MFSQLFinals.replaceNextReplacement+tableName+" ");
		
		/*add the creat table command to the command line*/
		m_cmdLine+=createTableCMD;
		m_vecTables.add(tableName);
	}
	
	/**
	 * Adds a primary key keyName in table tableName
	 * @param tableName name of table
	 * @param keyName name of key
	 */
	public void addCMDCreatePrimaryKeyInteger(String tableName,String keyName){
		addCMDCreateKey(MFSQLFinals.keyPrimary, tableName, keyName);
	}
	
	/**
	 * Adds the primary key statement to the current (last eddited) table.
	 * @param keyName of the key
	 */
	public void addCMDCreatePrimaryKeyInteger(String keyName){
		addCMDCreateKey(MFSQLFinals.keyPrimary, m_nameCurrentTable, keyName);
	}
	
	/**
	 * References the keys
	 * @param tableName
	 * @param keyName
	 * @param foreignTableName
	 * @param foreignKeyName
	 */
	public void addCMDCreateReference(String tableName,String keyName,String foreignTableName,String foreignKeyName){
		/*FOREIGN KEY(keyName) REFERENCES foreignTableName(foreignKeyName)*/
		String newLine=new String(MFSQLFinals.referenceInt);/*Template for SQLite reference to a foreign primary key*/
		newLine=newLine.replaceFirst(MFSQLFinals.replaceKey,keyName);/*Replacement of the key name in the table*/
		newLine=newLine.replaceFirst(MFSQLFinals.replaceForeignKey,foreignKeyName);/*Replacement of the foreign key name*/
		newLine=newLine.replaceFirst(MFSQLFinals.replaceForeignTable,foreignTableName);/*Replacement of table name with foreign key*/
		
		addCMDCreateKey(tableName,newLine);
	}
	
	/**
	 * Creates an integer key
	 * @param tableName
	 * @param keyName
	 */
	public void addCMDCreateKeyInteger(String tableName,String keyName){
		addCMDCreateKey(MFSQLFinals.keyInt, tableName, keyName);
	}
	
	/**
	 * Creates an integer key
	 * @param tableName
	 * @param keyName
	 */
	public void addCMDCreateKeyInteger(String keyName){
		addCMDCreateKey(MFSQLFinals.keyInt, m_nameCurrentTable, keyName);
	}
	
	/**
	 * Creates a text key
	 * @param tableName
	 * @param keyName
	 */
	public void addCMDCreateKeyText(String tableName,String keyName){
		addCMDCreateKey(MFSQLFinals.keyText, tableName, keyName);
	}
	
	/**
	 * Creates a text key
	 * @param tableName
	 * @param keyName
	 */
	public void addCMDCreateKeyText(String keyName){
		addCMDCreateKey(MFSQLFinals.keyText, m_nameCurrentTable, keyName);
	}
	
	/**
	 * Creates an integer key
	 * @param tableName
	 * @param keyName
	 */
	public void addCMDCreateKeyReal(String tableName,String keyName){
		addCMDCreateKey(MFSQLFinals.keyReal, tableName, keyName);
	}
	
	/**
	 * Creates a real key
	 * @param tableName
	 * @param keyName
	 */
	public void addCMDCreateKeyReal(String keyName){
		addCMDCreateKey(MFSQLFinals.keyReal, m_nameCurrentTable, keyName);
	}
	
	/**
	 * data must be formated like this: (" 'StringData' " "123",
	 * @param tableName indentifies a table in the database
	 * @param commaSeperatedkeyList is a string with the keys of the table. It must look like (without "):  "ID,Name,Age"
	 * @param commaSeperatedValues are the values for the given keys. The values have to be compatible to the keys.
	 * @return true if successfully executed the database operation.
	 */
	public boolean execInsertData(String tableName,String commaSeperatedkeyList,String commaSeperatedValues){		
		String cmd=MFSQLFinals.insertData_withReplace.replaceFirst(MFSQLFinals.replaceTableName,tableName);
		commaSeperatedValues=commaSeperatedValues.replaceAll(" '","");
		commaSeperatedValues=commaSeperatedValues.replaceAll("'","");
		commaSeperatedValues="'"+commaSeperatedValues+"'";
		commaSeperatedValues=commaSeperatedValues.replaceAll(",", "','");
		commaSeperatedValues=Matcher.quoteReplacement(commaSeperatedValues);
		p_debugPrint("Keys for Values   : "+commaSeperatedkeyList);
		p_debugPrint("Values to insert  : "+commaSeperatedValues);
		cmd=cmd.replaceFirst(MFSQLFinals.replaceKeyList,commaSeperatedkeyList);
		cmd=cmd.replaceFirst(MFSQLFinals.replaceValues,commaSeperatedValues);
		//p_debugPrint("addCMDInsertData insert command:\n"+cmd+"\n-end of CMD\n");
		return executeCommand(cmd);
	}
	
	/**
	 * Deletes a row in the table matching the given parameters
	 * @param tableName table which should contain the data
	 * @param keyName key name which is used to find the row - for example the data specific unique id "keyID"
	 * @param keyValue key data which is used to find the row - for example with "keyID" it could be "56"
	 * @return true if operation was successfully executed
	 */
	public boolean deleteRow(String tableName,String keyName,String keyValue){
		String cmd=MFSQLFinals.deleteRow_withReplace.replaceAll(MFSQLFinals.replaceTableName, tableName);
		cmd=cmd.replaceFirst(MFSQLFinals.replaceKey, keyName);
		cmd=cmd.replaceFirst(MFSQLFinals.replaceKeyValue, "'"+keyValue+"'");
		return executeCommand(cmd);
	}

	/**
	 * Updates the entry with keyUID as UID Key.
	 * @param table - table which contains the table name and primary key name
	 * @param keyUID - primary key value
	 * @param keyName - name of the key which must be updated
	 * @param newValue - new value for the key
	 * @return
	 */
	public boolean updateData(MFDBTable table,int keyUID,String keyName,String newValue){
		String updateCommand=MFSQLFinals.updateData_withReplace;
		updateCommand=updateCommand.replaceFirst(MFSQLFinals.replaceTableName,table.getTableName());
		updateCommand=updateCommand.replaceFirst(MFSQLFinals.replaceKey,keyName);
		updateCommand=updateCommand.replaceFirst(MFSQLFinals.replaceKeyValue,newValue);
		updateCommand=updateCommand.replaceFirst(MFSQLFinals.replaceKeyID,table.getPrimaryKeyName());
		updateCommand=updateCommand.replaceFirst(MFSQLFinals.replaceKeyIDValue,Integer.toString(keyUID));
		table.loadFromDatabase();
		return executeCommand(updateCommand);
	}
	
	private void addCMDCreateKey(String tableName, String newLine){
		if(!m_cmdLine.contains("CREATE TABLE IF NOT EXISTS "+tableName+" ")){
			p_releasePrint("No table with name "+tableName+" available");
			return;
		}
		if(tableName.contains(" ")){
			p_releasePrint("no space allowed!");
			return;
		}	
		
		String oldOne		= MFSQLFinals.replaceNextReplacement+tableName+" ";
		String newOne	= Matcher.quoteReplacement(newLine)+",\n"+oldOne;
		
		m_cmdLine = m_cmdLine.replaceAll(oldOne,newOne);
	}
	
	private void addCMDCreateKey(String keyType,String tableName,String keyName){
		if(keyName.isEmpty()){
			p_releasePrint("key name is empty");
			return;
		}
		if(!m_cmdLine.contains("CREATE TABLE IF NOT EXISTS "+tableName+" ")){
			p_releasePrint("No table with name "+tableName+" available");
			return;
		}
		if(tableName.contains(" ")){
			p_releasePrint("no space allowed!");
			return;
		}	
		if(keyName.contains(" ")){
			p_releasePrint("no space allowed!");
			return;
		}
		
		/*Extract the table to check if a key already exists*/
		String tableKeys=m_cmdLine.split(" "+tableName+" ")[1].split(";")[0];
		//p_debugPrint("addCMDCreateKey - extracted keys:\n "+tableKeys+"-end extracted keys\n");
		if(tableKeys.contains(" "+keyName+" ")){
			p_releasePrint("addCMDCreateKey - creating key failed, key ("+keyName+") already exists");
			return;
		}
		
		String oldOne		= MFSQLFinals.replaceNextReplacement+tableName+" ";
		String newOne	= " "+keyName+Matcher.quoteReplacement(keyType)+ oldOne;
		
		//p_debugPrint("\n New String:\n "+newOne+"\n Old String: "+oldOne +"\n");
		
		m_cmdLine = m_cmdLine.replaceAll(oldOne,newOne);
	}
	
	/**
	 * It cant be public because it wouldn't be thread safe. Data manipulation must be done with MFDatabaseHelper or a subclass
	 * @param tableName
	 * @return
	 */
	protected ResultSet getTableData(String tableName){
		return selectData(tableName,"*");
	}
	
	/**
	 * TODO
	 * @param tableName
	 * @return
	 */
	protected MFDBTable getTable(String tableName) {
		return null;
	}	
	
	/**
	 * Returns a Java ResultSet with access to the data. The tableName identifies a table in the database. The selector
	 * is a key in the table or a database software specific selector operation like * (all).
	 * @param tableName
	 * @param selector
	 * @return
	 */
	public ResultSet selectData(String tableName,String selector){
		ResultSet ret;
		if(lock(m_lockSQLOperation));else return null;
		if(m_connection==null){
			release(m_lockSQLOperation);
			return null;
		}
		/*select is a pre defined string with the sql select syntax. it contains replacement marks to insert specific data*/
		String select=MFSQLFinals.selectData_withReplace.replaceFirst(MFSQLFinals.replaceSelector,selector);
		select=select.replaceFirst(MFSQLFinals.replaceTableName, tableName);
		try {
			/*SQLite supports only this types*/
			m_statement=m_connection.createStatement(
                    ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY);
			ret=m_statement.executeQuery(select);
			release(m_lockSQLOperation);
			return ret;
		} 
		catch (SQLException e) {
			p_releasePrint("selectData failed: "+e.getMessage());
		}
		release(m_lockSQLOperation);
		return null;
	}
	
	public void setCurrentTable(String tableName){
		m_nameCurrentTable=tableName;
	}
	
	public String getDBDir(){
		return m_dbDir;
	}
	public boolean isExistent(){
		return m_DBExists;
	}
	
	/**
	 * Creates and opens or opens a db file with fileName.
	 * @param fileName
	 * @return
	 */
	public boolean createDatabase(String fileName){
		if(fileName.isEmpty()){
			return false;
		}
		m_dbName=fileName;
		return connectDB();
	}

	public void close(){
		if(m_connection != null){
			try {
				m_connection.close();
			} catch (SQLException e) {
				p_debugPrint(e.getMessage());
			}
			m_connection=null;
		}

		if(m_statement!= null)
		{
			try {
				m_statement.close();
			} catch (SQLException e) {
				p_debugPrint(e.getMessage());
			}
			m_statement=null;
		}

		if(m_resultSet != null){
			try {
				m_resultSet.close();
			} catch (SQLException e) {
				p_debugPrint(e.getMessage());
			}
			m_resultSet=null;
		}
	}

	@Override
	protected void postWork() {
		close();
	}
	
}
