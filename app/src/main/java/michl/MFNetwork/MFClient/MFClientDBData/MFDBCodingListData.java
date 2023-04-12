package MFNetwork.MFClient.MFClientDBData;

import MFNetwork.MFDatabase.MFDatabase.MFDBData;
import MFNetwork.MFDatabase.MFDatabase.MFDBTable;

public class MFDBCodingListData extends MFDBData {
	
	public int
		codingListID=-1,
		clientID=-1,
		codingID=-1;
	
	public MFDBCodingListData(MFDBTable table) {
		super(table);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getUID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getCommaSaperatedData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateData() {
		// TODO Auto-generated method stub
		
	}

}
