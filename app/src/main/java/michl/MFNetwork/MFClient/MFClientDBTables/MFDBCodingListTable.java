package MFNetwork.MFClient.MFClientDBTables;

import java.sql.ResultSet;
import java.sql.SQLException;

import MFNetwork.MFClient.MFClientDBData.MFDBCodingListData;
import MFNetwork.MFDatabase.MFDatabase.MFDBData;
import MFNetwork.MFDatabase.MFDatabase.MFDBTable;
import MFNetwork.MFDatabase.MFDatabase.MFDatabaseHelper;
import MFNetwork.MFDatabase.MFDatabase.MFSQLFinals;

public class MFDBCodingListTable extends MFDBTable {
	
	public MFDBCodingListTable(MFDatabaseHelper dbHelper) {
		super(dbHelper, "CodingList", "CodingListID");
		addKey("CodingListID", MFSQLFinals.KEY_TYPE_PRIMARY_INT);
		addKey("ClientID", MFSQLFinals.KEY_TYPE_INT);
		addKey("CodingID", MFSQLFinals.KEY_TYPE_INT);
		genCommaSeperatedKeys();
	}

	@Override
	public MFDBData readRowData(ResultSet result) throws SQLException {
		MFDBCodingListData data=new MFDBCodingListData(this);
		data.codingListID=result.getInt("CodingListID");
		data.clientID=result.getInt("ClientID");
		data.codingID=result.getInt("CodingID");
		return null;
	}

	@Override
	public void createReferencesCommands() {
		m_dbHelper.addCMDCreateReference("CodingList","ClientID", "ClientAccount", "ClientID");
		m_dbHelper.addCMDCreateReference("CodingList","CodingID", "Coding", "CodingID");
	}

}
