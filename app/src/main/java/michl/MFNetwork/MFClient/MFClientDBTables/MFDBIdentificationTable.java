package MFNetwork.MFClient.MFClientDBTables;

import java.sql.ResultSet;
import java.sql.SQLException;

import MFNetwork.MFClient.MFClientDBData.MFDBIdentificationData;
import MFNetwork.MFDatabase.MFDatabase.MFDBData;
import MFNetwork.MFDatabase.MFDatabase.MFDBTable;
import MFNetwork.MFDatabase.MFDatabase.MFDatabaseHelper;
import MFNetwork.MFDatabase.MFDatabase.MFSQLFinals;

public class MFDBIdentificationTable extends MFDBTable {

	public MFDBIdentificationTable(MFDatabaseHelper dbHelper) {
		super(dbHelper, "Identification", "IdentServerID");
		m_className = "MFDBIdentificationTable";
		addKey("IdentServerID", MFSQLFinals.KEY_TYPE_PRIMARY_INT);
		addKey("ContactID", MFSQLFinals.KEY_TYPE_INT);
		addKey("ServerID",MFSQLFinals.KEY_TYPE_INT);
		addKey("IdentificationCode", MFSQLFinals.KEY_TYPE_INT);
		addKey("DynamicIdentification",MFSQLFinals.KEY_TYPE_TEXT);
		genCommaSeperatedKeys();
	}

	@Override
	public MFDBData readRowData(ResultSet result) throws SQLException {
		int ident,contact,server,identificationCode;
		String dynIdent="";
		ident = result.getInt("IdentServerID");
		contact = result.getInt("ContactID");
		server = result.getInt("ServerID");
		identificationCode = result.getInt("IdentificationCode");
		dynIdent = result.getString("DynamicIdentification");
		MFDBIdentificationData data=new MFDBIdentificationData(this,ident,contact,server,identificationCode,dynIdent);
		return data;
	}

	@Override
	public void createReferencesCommands() {
		// TODO Auto-generated method stub

	}

}
