package MFNetwork.MFClient.MFClientDBTables;

import java.sql.ResultSet;
import java.sql.SQLException;

import MFNetwork.MFClient.MFClientDBData.MFDBTransferServerData;
import MFNetwork.MFDatabase.MFDatabase.MFDBData;
import MFNetwork.MFDatabase.MFDatabase.MFDBTable;
import MFNetwork.MFDatabase.MFDatabase.MFDatabaseHelper;
import MFNetwork.MFDatabase.MFDatabase.MFSQLFinals;

public class MFDBTransferServerTable extends MFDBTable {

	public MFDBTransferServerTable(MFDatabaseHelper dbHelper) {
		super(dbHelper, "TransferServer", "TransferServerID");
		addKey("TransferServerID", MFSQLFinals.KEY_TYPE_PRIMARY_INT);
		addKey("ServerID", MFSQLFinals.KEY_TYPE_INT);
		addKey("TransferConfigID", MFSQLFinals.KEY_TYPE_INT);
		genCommaSeperatedKeys();
	}
	public boolean addTransferServer(MFDBTransferServerData transferServer){
		boolean done=false;
		lock();
		done=m_vecData.add(transferServer);
		release();
		return done;
	}
	@Override
	public MFDBData readRowData(ResultSet result) throws SQLException {
		MFDBTransferServerData transferServer=new MFDBTransferServerData(this);
		transferServer.transferServerID=result.getInt("TransferServerID");
		transferServer.transferConfigID=result.getInt("TransferConfigID");
		transferServer.serverID=result.getInt("ServerID");
		return transferServer;
	}
	@Override
	public void createReferencesCommands() {
		m_dbHelper.addCMDCreateReference("TransferServer","ServerID", "Server", "ServerID");
		m_dbHelper.addCMDCreateReference("TransferServer","TransferConfigID", "TransferConfig", "TransferConfigID");
	}

}
