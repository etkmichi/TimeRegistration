package MFNetwork.MFClient.MFClientDBTables;

import java.sql.ResultSet;
import java.sql.SQLException;

import MFNetwork.MFClient.MFClientDBData.MFDBClientAccountData;
import MFNetwork.MFDatabase.MFDatabase.MFDBData;
import MFNetwork.MFDatabase.MFDatabase.MFDBTable;
import MFNetwork.MFDatabase.MFDatabase.MFDatabaseHelper;
import MFNetwork.MFDatabase.MFDatabase.MFSQLFinals;

public class MFDBClientAccountTable extends MFDBTable {

	public MFDBClientAccountTable(MFDatabaseHelper dbHelper) {
		super(dbHelper, "ClientAccount", "ClientAccountID");		
		m_className="MFDBClientAccountTable";
		addKey("ClientID",MFSQLFinals.KEY_TYPE_PRIMARY_INT);
		addKey("AccountName",MFSQLFinals.KEY_TYPE_TEXT);
		addKey("FirstName",MFSQLFinals.KEY_TYPE_TEXT);
		addKey("SecondName",MFSQLFinals.KEY_TYPE_TEXT);
		addKey("Alias",MFSQLFinals.KEY_TYPE_TEXT);
		addKey("Password",MFSQLFinals.KEY_TYPE_TEXT);
		genCommaSeperatedKeys();
	}

	@Override
	public MFDBData readRowData(ResultSet result) throws SQLException {
		MFDBClientAccountData account=new MFDBClientAccountData(this);
		account.clientAccountID=result.getInt("ClientID");
		account.accountName=result.getString("AccountName");
		account.firstName=result.getString("FirstName");
		account.secondName=result.getString("SecondName");
		account.alias=result.getString("Alias");
		account.password=result.getString("Password");
		return account;
	}

	@Override
	public void createReferencesCommands() {
		return;
	}
}
