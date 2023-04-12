package MFNetwork.MFClient.MFClientDBTables;

import java.sql.ResultSet;
import java.sql.SQLException;

import MFNetwork.MFClient.MFClientDBData.MFDBContactTagConfigData;
import MFNetwork.MFDatabase.MFDatabase.MFDBData;
import MFNetwork.MFDatabase.MFDatabase.MFDBTable;
import MFNetwork.MFDatabase.MFDatabase.MFDatabaseHelper;
import MFNetwork.MFDatabase.MFDatabase.MFSQLFinals;

public class MFDBContactTagConfigTable extends MFDBTable {

	public MFDBContactTagConfigTable(MFDatabaseHelper dbHelper) {
		super(dbHelper, "ContactTagConfig","ContactTagConfigID");
		addKey("ContactTagConfigID", MFSQLFinals.KEY_TYPE_PRIMARY_INT);
		addKey("ContactID", MFSQLFinals.KEY_TYPE_INT);
		addKey("TagID", MFSQLFinals.KEY_TYPE_INT);
	}

	@Override
	public MFDBData readRowData(ResultSet result) throws SQLException {
		MFDBContactTagConfigData data=new MFDBContactTagConfigData(this);
		data.contactTagConfigID=result.getInt("ContactTagConfigID");
		data.contactID=result.getInt("ContactID");
		data.tagID=result.getInt("TagID");
		return data;
	}

	@Override
	public void createReferencesCommands() {
		m_dbHelper.addCMDCreateReference("ContactTagConfig","ContactID", "Contact", "ContactID");
		m_dbHelper.addCMDCreateReference("ContactTagConfig","TagID", "Tag", "TagID");
	}

}
