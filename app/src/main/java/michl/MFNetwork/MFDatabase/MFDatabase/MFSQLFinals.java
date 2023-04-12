package MFNetwork.MFDatabase.MFDatabase;

public class MFSQLFinals {
	public static final String 
		createTable_beforeName="CREATE TABLE IF NOT EXISTS ",
		createTable_afterName=" (\n",
		referenceInt ="FOREIGN KEY(--KeyName--) REFERENCES --ForeignTable--(--ForeignKey--)",
		append2PrimaryKeyStatement="PRIMARY KEY(--KeyName--,--KeyName--)",
		append1PrimaryKeyStatement="PRIMARY KEY(--KeyName--)",
		keyPrimary=" integer NOT NULL PRIMARY KEY,\n",
		keyText=" text,\n",
		keyInt=" integer,\n",
		keyReal=" real,\n",
		createTable_closeLine="EndMark text\n);\n",
		createTable_customCloseLine="EndMark text\n);\n";
	public static final String/*Creating CMD with replacement strings*/
		createTable_withReplace="CREATE TABLE IF NOT EXISTS --TableName-- (\n" +
			"--NextReplacement--"+"\n"+
				");\n";
	public static final  String 
		insertData_1_start="INSERT INTO ",
		insertData_2_0_keyListStart=" (",
		insertData_2_1_keyListSeperator=",",
		insertData_2_X_keyListEnd=") ",
		insertData_3_0_valuesStart="VALUES (",
		insertData_3_1_valuesSeperator=", ",
		insertData_3_X_valuesEnd=")",
		insertData_withReplace="INSERT INTO --TableName-- (--KeyList--) VALUES (--Values--);";
	public static final String
		replaceTableName="--TableName--",
		replaceNextReplacement="--NextReplacement--",
		replaceKeyList="--KeyList--",
		replaceValues="--Values--",
		replaceSelector="--Selector--",
		replaceForeignTable="--ForeignTable--",
		replaceForeignKey="--ForeignKey--",
		replaceKey="--KeyName--",
		replaceKeyValue="--KeyValue--",
		replaceKeyID="--KeyID--",
		replaceKeyIDValue="--KeyIDValue--";
	
	public static final String
		selectData_withReplace="SELECT --Selector-- FROM --TableName--;",
		deleteRow_withReplace="DELETE FROM --TableName-- WHERE --KeyName-- = --KeyValue--",
		updateData_withReplace="UPDATE --TableName-- SET --KeyName-- = '--KeyValue--' WHERE --KeyID-- = --KeyIDValue--";
	public static final int
		KEY_TYPE_PRIMARY_INT=1,
		KEY_TYPE_INT=2,
		KEY_TYPE_TEXT=3;
	
		
}
