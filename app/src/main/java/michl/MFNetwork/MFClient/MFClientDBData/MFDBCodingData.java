package MFNetwork.MFClient.MFClientDBData;

import MFNetwork.MFDatabase.MFDatabase.MFDBData;
import MFNetwork.MFDatabase.MFDatabase.MFDBTable;

public class MFDBCodingData extends MFDBData {
	
	public int 
		codingID,
		passdataOffset,
		counter;
	
	public String
		codingType="standard",
		password="",
		passdataPath="",
		dynamicPass="";
	
	public MFDBCodingData(MFDBTable table) {
		super(table);
		codingID=-1;
		passdataOffset=0;
		counter=0;
	}

	@Override
	public int getUID() {
		return codingID;
	}

	@Override
	public String getCommaSaperatedData() {
		return m_commaSeperatedValues;
	}

	@Override
	public void updateData() {
		m_commaSeperatedValues=codingID+","+codingType+","+password+","
				+passdataPath+","+passdataOffset+","+counter+","+dynamicPass;
	}
	
}
