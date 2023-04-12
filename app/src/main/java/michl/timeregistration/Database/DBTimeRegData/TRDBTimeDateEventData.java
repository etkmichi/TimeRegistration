package michl.timeregistration.Database.DBTimeRegData;

import java.util.concurrent.Semaphore;

import MFNetwork.MFDatabase.MFDatabase.MFDBData;
import MFNetwork.MFDatabase.MFDatabase.MFDBTable;

public class TRDBTimeDateEventData extends MFDBData {
    public int
    k_eventID=-1,
    k_YYYYMMDD=20181201,
    k_pauseStart=0,
    k_pauseStop=0,
    k_pauseSpan=0;
    public Long
        k_utcStart= 0L,
        k_utcStop=0L,
        k_utcPauseStart= 0L,
        k_utcPauseStop=0L,
        k_start=0L,
        k_stop=0L;


    private Semaphore
            lockData=new Semaphore(1);

    public TRDBTimeDateEventData(int id, MFDBTable table){
        super(table);
        k_eventID=id;
        m_className="TRDBEmployeeData";
    }

    @Override
    public int getUID() {
        return k_eventID;
    }

    @Override
    public void updateData() {
        lock(lockData);
        m_commaSeperatedValues=
                k_eventID   +","+k_YYYYMMDD+","+
                k_start     +","+k_stop+","+
                k_pauseStart+","+ k_pauseStop+","+
                k_pauseSpan +","+k_utcStart +","+
                k_utcStop   +","+ k_utcPauseStart +","+
                k_utcPauseStop;
        m_composedName="Date: "+k_YYYYMMDD+
                        " Start: "+k_start+" Stop: "+k_stop+" Pause: "+k_pauseSpan;
        release(lockData);
    }

    @Override
    public String getCommaSaperatedData() {
        updateData();
        return m_commaSeperatedValues;
    }
}
