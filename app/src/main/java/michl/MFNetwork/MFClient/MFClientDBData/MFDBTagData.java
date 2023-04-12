package MFNetwork.MFClient.MFClientDBData;

import MFNetwork.MFDatabase.MFDatabase.MFDBData;
import MFNetwork.MFDatabase.MFDatabase.MFDBTable;

public class MFDBTagData extends MFDBData {

	public int
		tagID=-1;
	public String
		tag="";
	public MFDBTagData(MFDBTable table) {
		super(table);
		m_className="MFDBTagData";
	}

	@Override
	public int getUID() {
		return tagID;
	}

	@Override
	public String getCommaSaperatedData() {
		m_commaSeperatedValues=tagID+","+tag;
		return m_commaSeperatedValues;
	}

	@Override
	public void updateData() {
		m_commaSeperatedValues=tagID+","+tag;		
	}

}
