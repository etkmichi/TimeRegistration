package MFNetwork.MFClient.MFClientDBData;

import MFNetwork.MFDatabase.MFDatabase.MFDBData;
import MFNetwork.MFDatabase.MFDatabase.MFDBTable;

public class MFDBTransferServerData extends MFDBData  {

	
	public int
		transferServerID,
		serverID,
		transferConfigID;
	protected 
		MFDBServerData m_server=null;
	protected
		MFDBTransferConfigData m_transConfig=null;
	
	public MFDBTransferServerData(MFDBTable table,MFDBTransferConfigData transferConfig, MFDBServerData server) {
		super(table);
		m_className="MFDBTransferServerData";
		m_transConfig=transferConfig;
		m_server=server;
	}
	
	public MFDBTransferServerData(MFDBTable table) {
		super(table);
		m_className="MFDBTransferServerData";
	}
	
	protected MFDBServerData getServerData(){
		return m_server;
	}
	
	protected MFDBTransferConfigData getTransferConfigData(){
		return m_transConfig;
	}
	
	protected void setServerData(MFDBServerData server){
		m_server=server;
	}
	
	protected void setTransferConfigData(MFDBTransferConfigData transferConfig){
		m_transConfig=transferConfig;
	}
	@Override
	public int getUID() {
		return -1;
	}

	@Override
	public String getCommaSaperatedData() {
		m_commaSeperatedValues=transferServerID+","+serverID+","+transferConfigID;
		return null;
	}

	@Override
	public void updateData() {
		m_commaSeperatedValues=transferServerID+","+serverID+","+transferConfigID;		
	}

}
