package MFNetwork.MFClient.MFClientDBData;

import MFNetwork.MFDatabase.MFDatabase.MFDBData;
import MFNetwork.MFDatabase.MFDatabase.MFDBTable;

public class MFDBIdentificationData extends MFDBData {
	public int
		identServerID,
		contactID,
		serverID,
		identificationCode;
	public String
		dynamicIdentification="";
	public MFDBIdentificationData(MFDBTable table,int identServerID,int contactID, int serverID,int identificationCode,
			String dynamicIdentification) {
		super(table);
		this.identServerID=identServerID;
		this.contactID=contactID;
		this.serverID=serverID;
		this.identificationCode=identificationCode;
		this.dynamicIdentification=dynamicIdentification;
	}

	@Override
	public int getUID() {
		return identServerID;
	}

	@Override
	public String getCommaSaperatedData() {
		return "";
	}

	@Override
	public void updateData() {
		return;
	}

}
