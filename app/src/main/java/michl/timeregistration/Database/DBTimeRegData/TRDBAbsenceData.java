package michl.timeregistration.Database.DBTimeRegData;

import java.util.concurrent.Semaphore;

import MFNetwork.MFDatabase.MFDatabase.MFDBData;
import MFNetwork.MFDatabase.MFDatabase.MFDBTable;

public class TRDBAbsenceData extends MFDBData {

    public int
    k_absenceID=-1,
    k_color=0,
    k_employeeID=-1,
    k_eventID=-1;
    public String
    k_reason="",
    k_attestation="";
    private Semaphore
            lockData=new Semaphore(1);

    public TRDBAbsenceData(int id, MFDBTable table){
        super(table);
        k_absenceID=id;
        m_composedName="Absence";
        m_className="TRDBEmployeeData";
    }

    @Override
    public int getUID() {
        return k_absenceID;
    }

    @Override
    public void updateData() {
        lock(lockData);
        m_commaSeperatedValues=
                k_absenceID+","+k_employeeID+","+k_eventID+","+k_reason+","+k_attestation+","+k_color;
        release(lockData);
    }

    @Override
    public String getCommaSaperatedData() {
        return m_composedName;
    }
}
