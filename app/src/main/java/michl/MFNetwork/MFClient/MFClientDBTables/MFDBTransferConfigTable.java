package MFNetwork.MFClient.MFClientDBTables;

import java.sql.ResultSet;
import java.sql.SQLException;

import MFNetwork.MFClient.MFClientDBData.MFDBTransferConfigData;
import MFNetwork.MFDatabase.MFDatabase.MFDBData;
import MFNetwork.MFDatabase.MFDatabase.MFDBTable;
import MFNetwork.MFDatabase.MFDatabase.MFDatabaseHelper;
import MFNetwork.MFDatabase.MFDatabase.MFSQLFinals;

public class MFDBTransferConfigTable extends MFDBTable {

	public int
		transferConfigID,
		contactID;
	
	public String
		transferType="standard";
	
	public MFDBTransferConfigTable(MFDatabaseHelper dbHelper) {
		super(dbHelper,"TransferConfig","TransferConfigID");
		m_className="MFDBTransferConfigTable";
		addKey("TransferConfigID", MFSQLFinals.KEY_TYPE_PRIMARY_INT);
		addKey("SendingProcedure", MFSQLFinals.KEY_TYPE_TEXT);
		genCommaSeperatedKeys();
	}

	@Override
	public MFDBData readRowData(ResultSet result) throws SQLException {
		MFDBTransferConfigData transferConfig=new MFDBTransferConfigData(this);
		transferConfig.transferConfigID=result.getInt("TransferConfigID");
		transferConfig.sendingProcedure=result.getString("SendingProcedure");
		return transferConfig;
	}
	
	@Override
	public void createReferencesCommands() {
		//m_dbHelper.addCMDCreateReference("TransferConfig","ContactID", "Contact", "ContactID");				
	}

}
