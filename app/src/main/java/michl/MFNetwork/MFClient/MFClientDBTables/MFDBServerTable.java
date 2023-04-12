package MFNetwork.MFClient.MFClientDBTables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Semaphore;

import MFNetwork.MFClient.MFClientDBData.MFDBServerData;
import MFNetwork.MFDatabase.MFDatabase.MFDBData;
import MFNetwork.MFDatabase.MFDatabase.MFDBTable;
import MFNetwork.MFDatabase.MFDatabase.MFDatabaseHelper;
import MFNetwork.MFDatabase.MFDatabase.MFSQLFinals;

/**
 * Server table:<br>
 * 	<br>("ServerID",MFSQLFinals.KEY_TYPE_PRIMARY_INT);
	<br>("ServerAdminID",MFSQLFinals.KEY_TYPE_INT);
	<br>("Port",MFSQLFinals.KEY_TYPE_INT);
	<br>("ServerName",MFSQLFinals.KEY_TYPE_TEXT);
	<br>("ServerPass",MFSQLFinals.KEY_TYPE_TEXT);//Low authentication
	<br>("Domain",MFSQLFinals.KEY_TYPE_TEXT);
	<br>("ClientIdentification",MFSQLFinals.KEY_TYPE_INT);//Login: Identifies a client
	<br>("Password",MFSQLFinals.KEY_TYPE_TEXT);//Login: password for an identification
 * @author michl
 *
 */
public class MFDBServerTable extends MFDBTable {
	
	protected static MFDBServerData defaultServer=initDefaultServer();
	private static Semaphore lockDefaultServer=new Semaphore(1);

	public MFDBServerTable(MFDatabaseHelper dbHelper) {
		super(dbHelper,"Server","ServerID");
		lock(lockDefaultServer);
		if(defaultServer==null) {
			defaultServer=new MFDBServerData(this);
			defaultServer.domain="127.0.0.1";
		}
		release(lockDefaultServer);
		m_className="MFDBServerTable";
		addKey("ServerID",MFSQLFinals.KEY_TYPE_PRIMARY_INT);
		addKey("ContactID",MFSQLFinals.KEY_TYPE_INT);
		addKey("ServerAdminID",MFSQLFinals.KEY_TYPE_INT);
		addKey("Port",MFSQLFinals.KEY_TYPE_INT);
		addKey("ServerName",MFSQLFinals.KEY_TYPE_TEXT);
		addKey("ServerPass",MFSQLFinals.KEY_TYPE_TEXT);//Low authentication
		addKey("Domain",MFSQLFinals.KEY_TYPE_TEXT);
		addKey("ClientIdentification",MFSQLFinals.KEY_TYPE_INT);//Login: Identifies a client
		addKey("Password",MFSQLFinals.KEY_TYPE_TEXT);//Login: password for an identification
		genCommaSeperatedKeys();
	}

	private static MFDBServerData initDefaultServer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MFDBData readRowData(ResultSet result) throws SQLException {
		MFDBServerData server=new MFDBServerData(this);
		server.serverID=result.getInt("ServerID");
		server.contactID=result.getInt("ContactID");
		server.serverAdminID=result.getInt("ServerAdminID");
		server.port=result.getInt("Port");
		server.serverName=result.getString("ServerName");
		server.serverPass=result.getString("ServerPass");
		server.domain=result.getString("Domain");
		server.clientIdentification=result.getInt("ClientIdentification");
		server.password=result.getString("Password");
		return server;
	}

	@Override
	public void createReferencesCommands() {
		//m_dbHelper.addCMDCreateReference("Server","ServerAdminID", "ServerAdmin", "ServerAdminID");		
	}


	/**
	 * Generates a task which will write the data to the database
	 * @param serverName
	 * @param port
	 * @param serverPass - password for the server
	 * @param domain - domain or IP address (PE: www.sth.de or 81.21.195.79)
	 * @param clientIdentification - unique identification of the contact sth. like a account.
	 * @param password - password the verify contact
	 * @return
	 */
	public MFDBServerData createServerData(
			int contactID,
			String 	domain,
			int 	port,
			String 	serverName,
			String 	serverPass,
			int 	clientIdentification,
			String 	password) 
	{
		MFDBServerData server=new MFDBServerData(this);
		server.serverID=getNextUID();
		server.contactID=contactID;
		server.serverAdminID=-1;
		server.port=port;
		server.serverName=serverName;
		server.serverPass=serverPass;
		server.domain=domain;
		server.clientIdentification=clientIdentification;
		server.password=password;
		return server;
	}
	
	/**
	 * Searches the table for a entry with contactID
	 * This function is a easy way replacement to get a server for communication.
	 * TODO implement MFComAddressManager: Containing all possible com addresses and other things.
	 * @return
	 */
	public MFDBServerData getServer(int contactID) {
		lock(lockVecData);
		for(MFDBData data:m_vecData) {
			if(((MFDBServerData)data).contactID==contactID) {
				release(lockVecData);
				return (MFDBServerData)data;
			}
		}
		release(lockVecData);
		p_releasePrint("Couldn't find server");
		return defaultServer;
	}

}
