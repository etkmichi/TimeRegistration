package MFNetwork.MFClient.MFClientDBTables;

import java.sql.ResultSet;
import java.sql.SQLException;

import MFNetwork.MFClient.MFClientDBData.MFDBCodingData;
import MFNetwork.MFCom.MFComDatabase.MFComDatabase;
import MFNetwork.MFDatabase.MFDatabase.MFDBData;
import MFNetwork.MFDatabase.MFDatabase.MFDBTable;
import MFNetwork.MFDatabase.MFDatabase.MFSQLFinals;

public class MFDBCodingTable extends MFDBTable {
	
	public MFDBCodingTable(MFComDatabase dbHelper) {
		super(dbHelper,"Coding","CodingID");
		m_className="MFDBCodingTable";
		addKey( "CodingID",MFSQLFinals.KEY_TYPE_PRIMARY_INT);
		addKey(  "CodingType",MFSQLFinals.KEY_TYPE_TEXT);
		addKey(  "Password",MFSQLFinals.KEY_TYPE_TEXT);
		addKey(  "PassdataPath",MFSQLFinals.KEY_TYPE_TEXT);
		addKey(  "PassDataOffset",MFSQLFinals.KEY_TYPE_INT);
		addKey(  "Counter",MFSQLFinals.KEY_TYPE_INT);
		addKey(  "DynamicPass",MFSQLFinals.KEY_TYPE_TEXT);		
		genCommaSeperatedKeys();
	}

	@Override
	public MFDBData readRowData(ResultSet result) throws SQLException {
		MFDBCodingData coding=new MFDBCodingData(this);
		coding.codingID=result.getInt("CodingID");
		coding.passdataOffset=result.getInt("PassdataOffset");
		coding.codingType=result.getString("CodingType");
		coding.password=result.getString("Password");
		coding.passdataPath=result.getString("PassdataPath");
		coding.passdataOffset=result.getInt("PassDataOffset");
		coding.counter=result.getInt("Counter");
		coding.dynamicPass=result.getString("DynamicPass");
		return coding;
	}

	@Override
	public void createReferencesCommands() {
	}
}
