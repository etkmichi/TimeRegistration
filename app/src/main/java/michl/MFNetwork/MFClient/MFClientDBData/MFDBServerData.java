package MFNetwork.MFClient.MFClientDBData;

import MFNetwork.MFDatabase.MFDatabase.MFDBData;
import MFNetwork.MFDatabase.MFDatabase.MFDBTable;

public class MFDBServerData extends MFDBData {
	/**
	 * Integer keys of the data:<br>
	 * <strong>serverID</strong>:Table unique identification<br>
	 * <strong>serverAdminID</strong>: Identifies the server administrator / Index of the ServerAdmin-Table<br>
	 * <strong>clientIdentification</strong>: Server sided identification of the client<br>
	 * <strong>port</strong>: port of server for protocol communication<br>
	 */
	public int 
		serverID=-1,
		serverAdminID=-1,
		clientIdentification=-1,
		port=3000,
		contactID=-1;
	
	public String
		serverName="",
		serverPass="",
		domain="",
		password="";

	
	public MFDBServerData(MFDBTable table){
		super(table);
		m_className="MFDBServerData";
	}

	@Override
	public int getUID() {
		return serverID;
	}

	@Override
	public String getCommaSaperatedData() {
		updateData();
		return m_commaSeperatedValues;
	}

	@Override
	public void updateData() {
		m_commaSeperatedValues=
			serverID+","+
			contactID+","+
			serverAdminID+","+
			port+","+
			serverName+","+
			serverPass+","+
			domain+","+
			clientIdentification+","+
			password;
		m_composedName=serverName;
		
	}
}
