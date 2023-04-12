package MFNetwork.MFClient.MFClientDBTables;

import java.sql.ResultSet;
import java.sql.SQLException;

import MFNetwork.MFClient.MFClientDBData.MFDBServerTagConfigData;
import MFNetwork.MFDatabase.MFDatabase.MFDBData;
import MFNetwork.MFDatabase.MFDatabase.MFDBTable;
import MFNetwork.MFDatabase.MFDatabase.MFDatabaseHelper;
import MFNetwork.MFDatabase.MFDatabase.MFSQLFinals;

public class MFDBServerTagConfigTable extends MFDBTable {

	public MFDBServerTagConfigTable(MFDatabaseHelper dbHelper) {
		super(dbHelper, "ServerTagConfig","ServerTagConfigID");
		addKey("ServerTagConfigID",MFSQLFinals.KEY_TYPE_PRIMARY_INT);
		addKey("ServerID",MFSQLFinals.KEY_TYPE_INT);
		addKey("TagID",MFSQLFinals.KEY_TYPE_INT);
		genCommaSeperatedKeys();
	}

	@Override
	public MFDBData readRowData(ResultSet result) throws SQLException {
		MFDBServerTagConfigData data=new MFDBServerTagConfigData(this);
		data.serverTagConfigID=result.getInt("ServerTagConfigID");
		data.serverID=result.getInt("ServerID");
		data.tagID=result.getInt("TagID");
		return data;
	}

	@Override
	public void createReferencesCommands() {
		m_dbHelper.addCMDCreateReference("ServerTagConfig","ServerID", "Server", "ServerID");
		m_dbHelper.addCMDCreateReference("ServerTagConfig","TagID", "Tag", "TagID");
	}

}
