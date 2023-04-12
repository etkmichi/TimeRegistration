package MFNetwork.MFClient.MFClientDBTables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import MFNetwork.MFClient.MFClientDBData.MFDBMessageData;
import MFNetwork.MFDatabase.MFDBTasks.MFDBWriteDataTk;
import MFNetwork.MFDatabase.MFDatabase.MFDBData;
import MFNetwork.MFDatabase.MFDatabase.MFDBTable;
import MFNetwork.MFDatabase.MFDatabase.MFDatabaseHelper;
import MFNetwork.MFDatabase.MFDatabase.MFSQLFinals;

public class MFDBMessageTable extends MFDBTable{

	/**
	 * Generates a MFDBMessageTable for storing messages.<br>
	 * Keys: <br>MessageID:Int<br>ContactID:Int<br>ClientID:Int<br>
	 * AutorID:Int<br>Date:Int<br>Time:Int<br>Message:String<br>MessageType:String<br>TransmissionStatus,MFSQLFinals.KEY_TYPE_INT)
	 * @param dbHelper
	 */
	public MFDBMessageTable(MFDatabaseHelper dbHelper) {
		super(dbHelper, "Message", "MessageID");
		m_className="MFDBMessageTable";
		addKey(	 "MessageID",MFSQLFinals.KEY_TYPE_PRIMARY_INT);
		addKey(  "ContactID",MFSQLFinals.KEY_TYPE_INT);
		addKey(  "ClientID",MFSQLFinals.KEY_TYPE_INT);
		addKey(  "AutorID",MFSQLFinals.KEY_TYPE_INT);
		addKey(  "Date",MFSQLFinals.KEY_TYPE_INT);
		addKey(  "Time",MFSQLFinals.KEY_TYPE_INT);
		addKey(  "Message",MFSQLFinals.KEY_TYPE_TEXT);
		addKey(  "MessageType",MFSQLFinals.KEY_TYPE_TEXT);
		addKey(  "TransmissionStatus",MFSQLFinals.KEY_TYPE_INT);
		genCommaSeperatedKeys();
	}

	@Override
	public MFDBData readRowData(ResultSet result) throws SQLException {
		MFDBMessageData message=new MFDBMessageData(this);
		message.messageID		=result.getInt("MessageID");
		message.contactID		=result.getInt("ContactID");
		message.clientID		=result.getInt("ClientID");
		message.autorID			=result.getInt("AutorID");
		message.date			=result.getInt("Date");
		message.time			=result.getInt("Time");
		message.message			=result.getString("Message");
		message.messageType		=result.getString("MessageType");
		message.transmissionStatus=result.getInt("TransmissionStatus");
		message.updateData();
		return message;
	}

	@Override
	public void createReferencesCommands() {
		m_dbHelper.addCMDCreateReference("Message","ContactID", "Contact", "ContactID");
		m_dbHelper.addCMDCreateReference("Message","ClientID", "ClientAccount", "ClientID");
	}
	
	/**
	 * This function writes a message to the database with a separated thread. The time and date are in
	 * following integer format <br>Date:YYYYMMDD Time HHMMSS | Date as int:20180824   Time as int:100032 (10:00:32).
	 * <br> The UID is obtained from the table. TODO implement a static time/date class to easily convert this format to
	 * String or sth else.
	 * @param message
	 * @param contactID
	 * @param clientID
	 * @param authorID sender id (contactID or clientID)
	 * @param msgType
	 * @return
	 */
	public MFDBMessageData writeMessage(String message,int contactID,int clientID,int authorID,String msgType) {
		MFDBMessageData msg=new MFDBMessageData(this.getNextUID(), this);
		//TODO get system data time etc and write all data to the message.
		msg.clientID=clientID;
		msg.autorID=authorID;
		msg.contactID=contactID;
		LocalDateTime now=LocalDateTime.now();
		
		int date=now.getYear()*10000;
		date+=now.getMonthValue()*100;
		date+=now.getDayOfMonth();
		msg.date=date;
		
		int time=now.getHour()*10000;
		time+=(now.getMinute()*100);
		time+=(now.getSecond());
		msg.time=time;
		
		msg.message=message;
		msg.messageType=msgType;
		msg.messageID=getNextUID();
		
		msg.updateData();
		
		m_dbHelper.addTask(new MFDBWriteDataTk(msg));
		return msg;
	}
	
	/**
	 * This function creates a message out of the data and writes it to the database. The time and date are in
	 * following integer format <br>Date:YYYYMMDD Time HHMMSS | Date as int:20180824   Time as int:100032 (10:00:32).
	 * <br> The UID is obtained from the table.
	 * @param message
	 * @param contactID
	 * @param clientID
	 * @param authorID sender id (contactID or clientID)
	 * @param msgType
	 * @return
	 */
	public MFDBMessageData createMessageData(String message,int contactID,int clientID,int authorID,String msgType) {
		MFDBMessageData msg=new MFDBMessageData(this.getNextUID(), this);
		//TODO get system data time etc and write all data to the message.
		msg.clientID=clientID;
		msg.autorID=authorID;
		msg.contactID=contactID;LocalDateTime now=LocalDateTime.now();
		
		int date=now.getYear()*10000;
		date+=now.getMonthValue()*100;
		date+=now.getDayOfMonth();
		msg.date=date;
		
		int time=now.getHour()*10000;
		time+=(now.getMinute()*100);
		time+=(now.getSecond());
		msg.time=time;
		
		msg.message=message;
		msg.messageType=msgType;
		msg.messageID=getNextUID();
		
		msg.updateData();
		
		return msg;
	}

}
