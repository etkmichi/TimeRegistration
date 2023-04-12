package MFNetwork.MFClient.MFClientDBData;

import MFNetwork.MFDatabase.MFDatabase.MFDBData;
import MFNetwork.MFDatabase.MFDatabase.MFDBTable;

public class MFDBTransferConfigData extends MFDBData  {
	public int
		transferConfigID=-1,
		contactID=-1;
	public String
		sendingProcedure="standard";
	public MFDBData
		m_contact=null;
	public MFDBTransferConfigData(MFDBTable table) {
		super(table);
		m_className="MFDBTransferConfigData";
	}
	
	@Override
	public int getUID() {
		// TODO Auto-generated method stub
		return transferConfigID;
	}
	
	@Override
	public String getCommaSaperatedData() {
		m_commaSeperatedValues=transferConfigID+","+sendingProcedure;
		return null;
	}

	@Override
	public void updateData() {
		m_commaSeperatedValues=transferConfigID+","+sendingProcedure;
		m_composedName=sendingProcedure;
	}
}
