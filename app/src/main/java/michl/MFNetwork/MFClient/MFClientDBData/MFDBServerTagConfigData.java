package MFNetwork.MFClient.MFClientDBData;

import MFNetwork.MFDatabase.MFDatabase.MFDBData;
import MFNetwork.MFDatabase.MFDatabase.MFDBTable;

public class MFDBServerTagConfigData extends MFDBData {
	public int
		serverTagConfigID=-1,
		serverID=-1,
		tagID=-1;
	public MFDBServerTagConfigData(MFDBTable table) {
		super(table);
		m_className="MFDBServerTagConfigData";
	}

	@Override
	public int getUID() {
		// TODO Auto-generated method stub
		return serverTagConfigID;
	}

	@Override
	public String getCommaSaperatedData() {
		return "";
	}

	@Override
	public void updateData() {
		// TODO Auto-generated method stub
		
	}

}
