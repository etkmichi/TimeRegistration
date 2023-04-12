package MFNetwork.MFClient.MFClientDBData;

import MFNetwork.MFDatabase.MFDatabase.MFDBData;
import MFNetwork.MFDatabase.MFDatabase.MFDBTable;

public class MFDBClientAccountData extends MFDBData {
	public int
		clientAccountID=-1;
	public String
		accountName="",
		firstName="",
		secondName="",
		alias="",
		password="";
	public MFDBClientAccountData(MFDBTable table) {
		super(table);
		m_className="MFDBClientAccountData";
	}
	@Override
	public int getUID() {
		// TODO Auto-generated method stub
		return clientAccountID;
	}
	@Override
	public String getCommaSaperatedData() {
		return m_commaSeperatedValues;
	}
	
	/**
	 * Updates the comma separated data
	 */
	@Override
	public void updateData() {
		m_commaSeperatedValues=clientAccountID+","+accountName+","+firstName+","+secondName+","+alias+","+password;
	}

}
