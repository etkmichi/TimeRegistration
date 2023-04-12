package MFNetwork.MFClient.MFClientDBTables;

import java.sql.ResultSet;
import java.sql.SQLException;

import MFNetwork.MFClient.MFClientDBData.MFDBServerAdminData;
import MFNetwork.MFDatabase.MFDatabase.MFDBData;
import MFNetwork.MFDatabase.MFDatabase.MFDBTable;
import MFNetwork.MFDatabase.MFDatabase.MFDatabaseHelper;
import MFNetwork.MFDatabase.MFDatabase.MFSQLFinals;

public class MFDBServerAdminTable extends MFDBTable {

	public MFDBServerAdminTable(MFDatabaseHelper dbHelper) {
		super(dbHelper, "ServerAdmin", "ServerAdminID");
		addKey("ServerAdminID", MFSQLFinals.KEY_TYPE_PRIMARY_INT);
		addKey("FirstName", MFSQLFinals.KEY_TYPE_TEXT);
		addKey("SecondName", MFSQLFinals.KEY_TYPE_TEXT);
		addKey("Alias", MFSQLFinals.KEY_TYPE_TEXT);
		addKey("EMailAddress", MFSQLFinals.KEY_TYPE_TEXT);
		genCommaSeperatedKeys();
		// TODO Auto-generated constructor stub
	}

	@Override
	public MFDBData readRowData(ResultSet result) throws SQLException {
		MFDBServerAdminData admin=new MFDBServerAdminData(this);
		admin.serverAdminID=result.getInt("ServerAdminID");
		admin.firstName=result.getString("FirstName");
		admin.secondName=result.getString("SecondName");
		admin.alias=result.getString("Alias");
		admin.emailAddress=result.getString("EMailAddress");
		return admin;
	}

	@Override
	public void createReferencesCommands() {
		return;
	}

}
