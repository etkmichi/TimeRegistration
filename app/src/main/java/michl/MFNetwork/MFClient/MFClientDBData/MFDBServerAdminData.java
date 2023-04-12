package MFNetwork.MFClient.MFClientDBData;

import MFNetwork.MFDatabase.MFDatabase.MFDBData;
import MFNetwork.MFDatabase.MFDatabase.MFDBTable;

public class MFDBServerAdminData extends MFDBData  {
	public int
		serverAdminID=-1;
	public String
		firstName="",
		secondName="",
		alias="",
		emailAddress="";
	
	public MFDBServerAdminData(MFDBTable table){
		super(table);
		m_className="MFDBServerAdmin";
	}

	@Override
	public int getUID() {
		return serverAdminID;
	}

	@Override
	public String getCommaSaperatedData() {
		return m_commaSeperatedValues;
	}

	@Override
	public void updateData() {
		m_commaSeperatedValues=serverAdminID+","+firstName+","+secondName+","+alias+","+emailAddress;
	}
}
