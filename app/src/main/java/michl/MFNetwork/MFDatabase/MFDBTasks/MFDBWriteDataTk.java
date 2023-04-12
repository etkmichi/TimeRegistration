package MFNetwork.MFDatabase.MFDBTasks;

import MFNetwork.MFDatabase.MFDatabase.MFDBData;
import MFNetwork.MFDatabase.MFDatabase.MFDBTable;
import MFNetwork.MFThreadSystem.MFTasks.MFTask;

/**
 * This task adds the given data to the database/table. Normally it is executed by
 * the database helper thread.
 * @author michl
 *
 */
public class MFDBWriteDataTk extends MFTask {
	
	protected MFDBData data;
	
	protected MFDBTable table;
	
	/**
	 * This task adds the given data to the database/table. Normally it is executed by
	 * the database helper thread.
	 * @param data is a derived class of MFDBData and represents the entry to the database. All data should be set before this task
	 * will execute the insert command.
	 */
	public MFDBWriteDataTk(MFDBData data) {
		m_className="MFDBWriteDataTk";
		this.data=data;
	}
	
	@Override
	public boolean doWork() {
		data.updateData();
		this.table=data.getTable();
		boolean ret=table.insertData(data);
		if(!ret) {
			p_errPrint("\nMFCDBAddDataTask inserting data failed. Data:");
			data.printKeysNValues();
		}
		data.isWritten=true;
		return ret;
	}

	@Override
	protected boolean undoWork() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public MFDBData getData() {
		return data;
	}

}
