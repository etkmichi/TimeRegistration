package MFNetwork.MFClient.MFClientDBData;

import java.util.concurrent.Semaphore;
/*
import MFNetwork.MFData.MFObjects.MFContact;*/
import MFNetwork.MFDatabase.MFDatabase.MFDBData;
import MFNetwork.MFDatabase.MFDatabase.MFDBTable;

//TODO make thread safe (no public values)
public class MFDBContactData extends MFDBData{
			
	public int
		m_codingID,
		m_transferConfigID,
		m_contactID,
		m_handshakeIdentifier,
		m_taskFilter,
		m_clientID;
	
	public String
		m_firstName,
		m_secondName,
		m_alias,
		m_iconName,
		m_handshakeData,
		m_dataPath;
	
//	protected ImageIcon
//		m_icon;
	
	public MFDBCodingData 
		m_codingData;
	
	public MFDBTransferConfigData
		m_transConfigData=null;
	
	public static MFDBTransferConfigData
		m_transferConfigStandard=null;
	
	protected String m_iconPath="";

	protected Semaphore lockData=new Semaphore(1);
/*
	protected MFContact contact;*/


	public MFDBContactData(int contactID,MFDBTable table) {
		super(table);
		this.m_contactID=contactID;
		init();
	}
	
	/**
	 * 
	 * @param contactID
	 * @param clientID - used for multiple clients
	 * @param firstName
	 * @param secondName
	 * @param alias	- client specific identification
	 * @param iconName
	 * @param handshakeIdentification - contact sided contactID for identification
	 * @param handshakeData - used to authenticate a contact
	 * @param taskFilter - permissions for the contact
	 * @param dataPath - dataPath for icons, downloads and other stuff.
	 * @param table - Database table representation
	 */
	public MFDBContactData(
			int contactID,
			int clientID,
			String firstName,
			String secondName,
			String alias,
			String iconName,
			int handshakeIdentification,
			String handshakeData,
			int taskFilter,
			String dataPath,
			MFDBTable table) 
	{
		super(table);
		m_contactID=contactID;
		m_clientID=clientID;
		m_firstName=firstName;
		m_secondName=secondName;
		m_alias=alias;
		m_iconName=iconName;
		m_handshakeIdentifier=handshakeIdentification;
		m_handshakeData=handshakeData;
		m_dataPath=dataPath;
		m_taskFilter=taskFilter;
		generateComposedName();
		init();
	}
	
	/**
	 * This constructor can be used to create an empty object or a dummy
	 * @param table
	 */
	public MFDBContactData(MFDBTable table) {
		super(table);
		m_contactID=-1;
		init();
	}
	
	public MFDBTransferConfigData getTransferConfig(){
		return m_transConfigData;
	}
	
	protected void init(){
		/*load/create the data which are needed for a contact (Coding ContactTagConfig TransferConfig*/
		if(m_iconPath!="" && m_iconName!=""){
			/*TODO Load icon*/
		}
	}
	
	/**
	 * 
	 */
	public void generateComposedName() {
		lock(lockData);
		m_composedName=m_contactID+"; ";
		if(m_alias!="")
			m_composedName+=m_alias;
		m_composedName+=" (";
		if(m_firstName!="" )
			m_composedName+=(m_firstName+", ");
		if(m_secondName!="")
			m_composedName+=m_secondName;
		m_composedName+=")";
		release(lockData);
	}

	public void updateData() {
		lock(lockData);
		m_commaSeperatedValues=
				m_contactID+","+m_clientID+","+
				m_transferConfigID +","+m_codingID+","+
				m_firstName+","+m_secondName+","+
				m_alias+","+m_iconName+","+
				m_dataPath+","+	m_handshakeIdentifier+","+
				m_handshakeData+","+m_taskFilter;
		release(lockData);
		generateComposedName();
	}
		
	public void setContactID(int contactID){
		this.m_contactID=contactID;
	}

	@Override
	public int getUID() {
		return m_contactID;
	}
	
	@Override
	public String getCommaSaperatedData() {
		updateData();
		return m_commaSeperatedValues;
	}

/*	public MFContact getContact() {
		return contact;
	}

	public void setContact(MFContact contact) {
		this.contact = contact;
	}*/
	
	

}
