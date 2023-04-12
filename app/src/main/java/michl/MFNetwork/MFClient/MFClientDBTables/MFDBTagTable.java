package MFNetwork.MFClient.MFClientDBTables;

import java.sql.ResultSet;
import java.sql.SQLException;

import MFNetwork.MFClient.MFClientDBData.MFDBTagData;
import MFNetwork.MFDatabase.MFDatabase.MFDBData;
import MFNetwork.MFDatabase.MFDatabase.MFDBTable;
import MFNetwork.MFDatabase.MFDatabase.MFDatabaseHelper;
import MFNetwork.MFDatabase.MFDatabase.MFSQLFinals;

public class MFDBTagTable extends MFDBTable {

	public MFDBTagTable(MFDatabaseHelper dbHelper){
		super(dbHelper, "Tag", "TagID");
		addKey("TagID", MFSQLFinals.KEY_TYPE_PRIMARY_INT);
		addKey("Tag", MFSQLFinals.KEY_TYPE_TEXT);
		genCommaSeperatedKeys();
	}

	@Override
	public MFDBData readRowData(ResultSet result) throws SQLException {
		MFDBTagData tag=new MFDBTagData(this);
		tag.tagID=result.getInt("TagID");
		tag.tag=result.getString("Tag");
		return tag;
	}

	@Override
	public void createReferencesCommands() {
		return;		
	}

}
