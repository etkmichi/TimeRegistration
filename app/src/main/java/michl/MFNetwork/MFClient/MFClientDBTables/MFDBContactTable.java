package MFNetwork.MFClient.MFClientDBTables;

import java.sql.ResultSet;
import java.sql.SQLException;

import MFNetwork.MFClient.MFClientDBData.MFDBContactData;
import MFNetwork.MFDatabase.MFDatabase.MFDBData;
import MFNetwork.MFDatabase.MFDatabase.MFDBTable;
import MFNetwork.MFDatabase.MFDatabase.MFDatabaseHelper;
import MFNetwork.MFDatabase.MFDatabase.MFSQLFinals;


public class MFDBContactTable extends MFDBTable{
	public int m_indexReceiverList=-1;
	protected static MFDBServerTable serverTable;
	public MFDBContactTable(MFDatabaseHelper dbHelper){
		super(dbHelper,"Contact","ContactID");
		m_className="MFDBContactTable";
		addKey("ContactID", MFSQLFinals.KEY_TYPE_PRIMARY_INT);
		addKey("ClientID", MFSQLFinals.KEY_TYPE_INT);
		addKey("TransferConfigID", MFSQLFinals.KEY_TYPE_INT);
		addKey("CodingID", MFSQLFinals.KEY_TYPE_INT);
		addKey("FirstName", MFSQLFinals.KEY_TYPE_TEXT);
		addKey("SecondName", MFSQLFinals.KEY_TYPE_TEXT);
		addKey("Alias", MFSQLFinals.KEY_TYPE_TEXT);
		addKey("IconName", MFSQLFinals.KEY_TYPE_TEXT);
		addKey("DataPath", MFSQLFinals.KEY_TYPE_TEXT);
		addKey("HandshakeIdentifier",MFSQLFinals.KEY_TYPE_INT);
		addKey("HandshakeData",MFSQLFinals.KEY_TYPE_TEXT);
		addKey("TaskFilter", MFSQLFinals.KEY_TYPE_INT);
		genCommaSeperatedKeys();
	}
	
	/**
	 * This will create a new contact in the database with the given data
	 * @param firstName
	 * @param secondName
	 * @param alias - a short name or more easy identification for a contact
	 * @param iconName - refers to an icon in a folder
	 * @return
	 */
	public int createContact(MFDBData data){
		int uid=getNextUID();
		boolean done=false;
		int index=-1;
		
		if(m_dbHelper==null){
			return -1;
		}
		
		done=m_dbHelper.execInsertData(m_tableName, 
				getCommaSeperatedKeys(),
				data.getCommaSaperatedData());
		if(!done){
			p_releasePrint("Couldn't create contact");
			return -1;
		}
		
		/*this will add the created contact to this table*/
		addData(data);
		
		/*TODO: The corresponding table data (Coding, TransferConfig) must be created too!
		 * The coding and transferconfig will be set to a standard. if needed users can change it by
		 * an advanced configuration menu*/
		return uid;
	}

	public MFDBContactData getContact(int index){
		return (MFDBContactData)get(index);
	}	
	
	/**
	 * removes the contact from the database file and the intern data vectors (contact list and data vector).
	 * @param index 
	 **/
	public boolean removeContact(int index){
		lock(lockVecData);
		int keyValue=m_vecData.get(index).getUID();
		
		if(m_dbHelper.deleteRow(m_tableName,m_primaryKeyName, Integer.toString(keyValue))){
			/*remove from contact table. The index is saved in m_vecTableConnec..*/
			m_vecData.remove(index);
			/*remove from contact list*/
			/*m_defaultList.remove(index);*/

			release(lockVecData);
			return true;
		}
		release(lockVecData);
		return false;
	}

	/**
	 * Creates a MFDBContactData
	 */
	public MFDBData readRowData(ResultSet result) throws SQLException{
		MFDBContactData contact=new MFDBContactData(this);
		contact.m_contactID=result.getInt("ContactID");
		contact.m_clientID=result.getInt("ClientID");
		contact.m_transferConfigID=result.getInt("TransferConfigID");
		contact.m_codingID=result.getInt("CodingID");		
		contact.m_firstName=result.getString("FirstName");
		contact.m_secondName=result.getString("SecondName");
		contact.m_alias=result.getString("Alias");
		contact.m_iconName=result.getString("IconName");
		contact.m_dataPath=result.getString("DataPath");
		contact.m_handshakeIdentifier=result.getInt("HandshakeIdentifier");
		contact.m_handshakeData=result.getString("HandshakeData");
		contact.m_taskFilter=result.getInt("TaskFilter");
		contact.updateData();
		return contact;
	}

	@Override
	public void createReferencesCommands() {
		//m_dbHelper.addCMDCreateReference("Contact","CodingID", "Coding", "CodingID");
		//m_dbHelper.addCMDCreateReference("Contact","TransferConfigID", "TransferConfig", "TransferConfigID");		
	}

	public boolean insertData(MFDBContactData data) {
		// TODO Auto-generated method stub
		p_debugPrint("--------------------------------not implemented---------------------------------------");
		return false;
	}

}

