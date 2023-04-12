package MFNetwork.MFClient.MFClientDBData;

import MFNetwork.MFDatabase.MFDatabase.MFDBData;
import MFNetwork.MFDatabase.MFDatabase.MFDBTable;

public class MFDBMessageData extends MFDBData {
	
	public int
		messageID=-1,
		contactID=-1,
		autorID=-1,
		clientID=-1,
		date=20190101,
		time=2359,
		transmissionStatus=-1;//TODO write little doc about status flags
	/**
	 *TransmissionStatus flags: 
	 */
	public static final int
		flag_status_send=0x1,
	 	flag_status_received=0x10;
	
	/*TODO write lib for date translation*/
	public String
		dateString="20190101",
		timeString="2359",
		message="",
		messageType="text";
	
	public MFDBMessageData(int messageID,MFDBTable table) {
		super(table);
		this.messageID=messageID;
	}
	
	public MFDBMessageData(MFDBTable table) {
		super(table);
	}

	@Override
	public int getUID() {
		return messageID;
	}

	@Override
	/**
	* Keys: MessageID:Int ContactID:Int	ClientID:Int AutorID:Int Date:Int Time:Int Message:String MessageType:String
	*/
	public String getCommaSaperatedData() {
		return m_commaSeperatedValues;
	}

	@Override
	public void updateData() {
		m_commaSeperatedValues=
				 messageID+","
				+contactID+","
				+clientID+","
				+autorID+","
				+date+","
				+time+","
				+message+","
				+messageType+","
				+transmissionStatus;
		m_composedName=date+" "+time+" "+transmissionStatus+" \n"+message;
	}
}
