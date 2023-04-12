package MFNetwork.MFDatabase.MFDatabase;

import java.util.Vector;

import MFNetwork.MFData.MFObjects.MFObject;

/**
 *
 * TODO documentation of gui type and handling gui types!!
 * GUI Type can be used to create automatically GUI dialogs for changing a key. The kind of the
 * GUI dialog (file manager, raw data input text or number, ...) can be created by using the guess type of
 * a MFDBKey object.
 */
public class MFDBKey extends MFObject {
	public static final int
			f_gui_type_id=-1,
			f_gui_type_text=0,
			f_gui_type_number=1,
			f_gui_type_path=2,
			f_gui_type_color=3,
			f_gui_type_date=4,
			f_gui_type_time=5,
			f_gui_type_icon=6;
	public static int keyCount=0;
	private String m_keyName="";
	private int m_keyType=-1;

	private int m_keyTypeGUI=-1;
	
	protected MFDatabaseHelper
		m_dbHelper;
	
	public static Vector<String>
			guessGUITypeListText=new Vector<>(),
			guessGUITypeListNumber=new Vector<>(),
			guessGUITypeListPath=new Vector<>(),
			guessGUITypeListColor=new Vector<>(),
			guessGUITypeListDate=new Vector<>(),
			guessGUITypeListTime=new Vector<>(),
			guessGUITypeListIcon=new Vector<>(),
			guessGUITypeListID=new Vector<>();

	private boolean isguessInitialiced=false;

	/**
	 * Initialices the static lists for guessing a keyGUIType
	 */
	public void initGuessStrings(){
		guessGUITypeListPath.add("Path");
		guessGUITypeListPath.add("path");
		guessGUITypeListPath.add("file");
		guessGUITypeListPath.add("File");
		guessGUITypeListPath.add("Dir");
		guessGUITypeListPath.add("dir");
		guessGUITypeListPath.add("Directory");
		guessGUITypeListPath.add("directory");

		guessGUITypeListNumber.add("Number");
		guessGUITypeListNumber.add("number");
		guessGUITypeListNumber.add("Value");
		guessGUITypeListNumber.add("value");
		guessGUITypeListNumber.add("sum");
		guessGUITypeListNumber.add("Sum");
		guessGUITypeListNumber.add("Amount");
		guessGUITypeListNumber.add("amount");
		guessGUITypeListNumber.add("Worth");
		guessGUITypeListNumber.add("worth");
		guessGUITypeListNumber.add("Price");
		guessGUITypeListNumber.add("price");

		guessGUITypeListText.add("Name");
		guessGUITypeListText.add("name");
		guessGUITypeListText.add("comment");
		guessGUITypeListText.add("Comment");
		guessGUITypeListText.add("Info");
		guessGUITypeListText.add("info");
		guessGUITypeListText.add("Reason");
		guessGUITypeListText.add("reason");//TODO finish it
		isguessInitialiced=true;
	}

	public MFDBKey(MFDatabaseHelper dbHelper,String keyName,int keyType){
		keyCount++;
		m_dbHelper=dbHelper;
		m_keyName=keyName;
		m_keyType=keyType;
		if(!isguessInitialiced)
			initGuessStrings();
		m_keyTypeGUI=guessTypeForGUI(keyName);

	}

	public int guessTypeForGUI(String keyName){
		int ret=-1;
		if(keyName.contains("ID")){
			return  f_gui_type_id;
		}
		/*If there is sth like "path" in the keyName, it should be a path gui type*/
		for(String guess:guessGUITypeListPath){
			if(keyName.contains(guess)){
				return f_gui_type_path;
			}
		}

		for(String guess:guessGUITypeListIcon){
			if(keyName.contains(guess)){
				return f_gui_type_icon;
			}
		}

		/*Prior to time because YYYYMMDDHHMM would be date not time*/
		for(String guess:guessGUITypeListDate){
			if(keyName.contains(guess)){
				return f_gui_type_date;
			}
		}

		for(String guess:guessGUITypeListTime){
			if(keyName.contains(guess)){
				return f_gui_type_time;
			}
		}

		for(String guess:guessGUITypeListText){
			if(keyName.contains(guess)){
				return f_gui_type_text;
			}
		}

		for(String guess:guessGUITypeListColor){
			if(keyName.contains(guess)){
				return f_gui_type_text;
			}
		}

		for(String guess:guessGUITypeListID){
			if(keyName.contains(guess)){
				return f_gui_type_text;
			}
		}
		return ret;
	}

	public int getKeyTypeForGUI(){
		return m_keyTypeGUI;
	}

	public void setKeyTypeForGUI(int guiTypeKey){
		m_keyTypeGUI=guiTypeKey;
	}
	
	public void createKey(String tableName){
		switch(m_keyType){
		case MFSQLFinals.KEY_TYPE_PRIMARY_INT:
			m_dbHelper.addCMDCreatePrimaryKeyInteger(tableName, m_keyName);
			break;
		case MFSQLFinals.KEY_TYPE_INT:
			m_dbHelper.addCMDCreateKeyInteger(tableName, m_keyName);
			break;
		case MFSQLFinals.KEY_TYPE_TEXT:
			m_dbHelper.addCMDCreateKeyText(tableName,m_keyName);
			break;
		default:
			p_releasePrint("invalid key type in MFDBKey(... ,int keyType)");
			break;
		}
	}
	public String getKeyName(){
		return m_keyName;
	}
	public int getKeyType(){
		return m_keyType;
	}


}
