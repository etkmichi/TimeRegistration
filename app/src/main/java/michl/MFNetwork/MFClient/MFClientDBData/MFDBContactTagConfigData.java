package MFNetwork.MFClient.MFClientDBData;

import MFNetwork.MFDatabase.MFDatabase.MFDBData;
import MFNetwork.MFDatabase.MFDatabase.MFDBTable;

public class MFDBContactTagConfigData extends MFDBData {

	public int 
		contactTagConfigID=-1,
		contactID=-1,
		tagID=-1;
	
	public MFDBContactTagConfigData(MFDBTable table) {
		super(table);
	}

	@Override
	public int getUID() {
		return contactTagConfigID;
	}

	@Override
	public String getCommaSaperatedData() {
		return "";
	}

	@Override
	public void updateData() {
	}

}
