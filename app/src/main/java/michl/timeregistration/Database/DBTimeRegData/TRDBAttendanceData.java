package michl.timeregistration.Database.DBTimeRegData;

import java.util.concurrent.Semaphore;

import MFNetwork.MFDatabase.MFDatabase.MFDBData;
import MFNetwork.MFDatabase.MFDatabase.MFDBTable;

public class TRDBAttendanceData extends MFDBData {

    public int
    k_attendanceID=-1,
    k_color=0,
    k_eventID=-1,
    k_employeeID=-1;
    private Semaphore
            lockData=new Semaphore(1);

    public TRDBAttendanceData(int id, MFDBTable table){
        super(table);
        k_attendanceID=id;
        m_composedName="Attendance";
        m_className="TRDBEmployeeData";
    }

    @Override
    public int getUID() {
        return k_attendanceID;
    }

    @Override
    public void updateData() {
        lock(lockData);
        m_commaSeperatedValues=
                k_attendanceID+","+k_employeeID+","+k_eventID+","+k_color;
        m_composedName="Attendance: k_attendanceID+\",\"+k_employeeID+\",\"+k_eventID+\",\"+k_color"+k_attendanceID+","+k_employeeID+","+k_eventID+","+k_color;
        release(lockData);
    }

    @Override
    public String getCommaSaperatedData() {
        updateData();
        return m_commaSeperatedValues;
    }
}
