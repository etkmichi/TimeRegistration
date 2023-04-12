package michl.timeregistration.Database.DBTimeRegTables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import MFNetwork.MFDatabase.MFDatabase.MFDBData;
import MFNetwork.MFDatabase.MFDatabase.MFDBTable;
import MFNetwork.MFDatabase.MFDatabase.MFSQLFinals;
import michl.timeregistration.Database.DBTimeRegData.TRDBAttendanceData;
import michl.timeregistration.Database.DBTimeRegData.TRDBTimeDateEventData;
import michl.timeregistration.Database.TRDBHelper;

public class TRDBAttendanceTable extends MFDBTable {
    TRDBHelper helper;
    public TRDBAttendanceTable(TRDBHelper helper){
        super(helper,"Attendance","AttendanceID");
        this.helper=helper;
        m_className="TRDBEmployeeTable";
        addKey("AttendanceID",MFSQLFinals.KEY_TYPE_PRIMARY_INT);
        addKey("EmployeeID",MFSQLFinals.KEY_TYPE_INT);
        addKey("EventID",MFSQLFinals.KEY_TYPE_INT);
        addKey("Color",MFSQLFinals.KEY_TYPE_INT);
        genCommaSeperatedKeys();
    }


    @Override
    public MFDBData readRowData(ResultSet resultSet) throws SQLException {
        TRDBAttendanceData attendance=new TRDBAttendanceData(-1,this);
        attendance.k_attendanceID=resultSet.getInt("AttendanceID");
        attendance.k_employeeID=resultSet.getInt("EmployeeID");
        attendance.k_eventID=resultSet.getInt("EventID");
        attendance.k_color=resultSet.getInt("Color");
        attendance.updateData();
        return attendance;
    }

    @Override
    public void createReferencesCommands() {

    }

    public ArrayList<TRDBTimeDateEventData> getEvents(int employeeID){
        ArrayList<TRDBTimeDateEventData> arrayList=new ArrayList<>();
        TRDBAttendanceData data;
        TRDBTimeDateEventData event;
        lock(lockVecData);
        for(int i=0;i<m_vecData.size();i++){
            data=(TRDBAttendanceData)m_vecData.get(i);
            if(data.k_employeeID==employeeID){
                event=(TRDBTimeDateEventData)helper.getEventTable().findData(data.k_eventID);
                arrayList.add(event);
            }
        }
        release(lockVecData);
        return  arrayList;
    }
}
