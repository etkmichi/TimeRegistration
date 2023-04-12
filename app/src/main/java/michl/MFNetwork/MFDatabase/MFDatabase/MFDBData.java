package MFNetwork.MFDatabase.MFDatabase;

import MFNetwork.MFData.MFObjects.MFObject;

/**
 * 
 * @author michl
 * To change a key or sth else of the database table some functions must be
 * adjusted.<br>
 * See:<br>
 * subclasses and functions of {@link MFDBData} (for changes of keys or values)
 * subclasses and functions of {@link MFDBTable} (for changes of keys or values)
 * subclasses and functions of {@link MFDatabaseHelper} (just for new tables)
 */
public abstract class MFDBData extends MFObject {
	/**
	 * index must not be set, it will be set, when the data is added to a list. 
	 * It is used to determine the position of data in a list
	 */
	public int 
		m_tableIndex=-1;
	
	public int 
		m_dbDataUID;
	
	public static int 
		m_dbDataCounter=0;/*This counter is unique for every MFDBData object.*/
	
	protected String 
		m_composedName="",
		m_commaSeperatedValues="",
		m_commaSeperatedKeys="";
	
	/**
	 * The notification can be used in the composedName field to inform a user.
	 */
	public String 
		m_notification="";
	
	protected MFDBTable
		table;

	public boolean 
		isWritten=false;
	
	public MFDBData(MFDBTable table){
		this.table=table;
		m_dbDataCounter++;
		m_commaSeperatedKeys=table.getCommaSeperatedKeys();
		m_dbDataUID=m_dbDataCounter;
		m_composedName=""+m_dbDataUID;
	}
		
	public MFDBTable getTable() {
		return table;
	}

	public void setTable(MFDBTable table) {
		this.table = table;
	}

	public int getDBGlobalUID(){
		return m_dbDataUID;
	}
	
	public void setDBGlobalUID(int dbUID){
		m_dbDataUID=dbUID;
	}
	
	/**
	 * The implementation of this method should return the data UID written into the table.
	 * @return Table UID of the data.
	 */
	public abstract int getUID();
	
	/**
	 * Some field of the table are public. This function has to be implemented, if
	 * a change of the public fields affect some protected or private fields. This function
	 * updates data.
	 */
	public abstract void updateData();

	/**
	 * Writes/updates the data to/in the table
	 * @return
	 */
	public boolean updateDatabase(String keyName,String newValue){
		if(table==null || table.getDBHelper()==null){
			p_debugPrint("no valid table or database helper!");
			return false;
		}
		return table.getDBHelper().updateData(getTable(),getUID(),keyName,newValue);
	}

	public String toString() {
		return getComposedName();
	}

	public String getComposedName(){
		return m_composedName;
	}
	public void setComposedName(String name){
		m_composedName=name;
	}
	
	/**
	 * Prints the keys and values.
	 */
	public void printKeysNValues() {
		updateData();
		String vecKeys[]=m_commaSeperatedKeys.split(",");
		String vecData[]=m_commaSeperatedValues.split(",");
		int size=vecKeys.length;
		if(size>vecData.length)
			size=vecData.length;
		
		p_releasePrint("MFDBData KeyCount/DataCount: "+vecKeys.length+"/"+vecData.length);
		for(int i=0;i<size;i++) {
			if(i%2==0)
				printColor=PRINT_CYAN_BACKGROUND;
			else
				printColor=PRINT_GREEN_BACKGROUND;
			p_debugPrint("\nKey"+i+":"+vecKeys[i]+"\nData="+vecData[i]);
		}
	}
	
	/**
	 * 
	 * @return a string whith the comma seperated values of the object. The values must be in the same order as the keys of the
	 * associated table.
	 */
	public abstract String getCommaSaperatedData();

	/**
	 * If the data was loaded by a MFDBTable, the tableIndex field will be set.
	 * @return
	 */
	public int getTableIndex() {
		return m_tableIndex;
	}

}
