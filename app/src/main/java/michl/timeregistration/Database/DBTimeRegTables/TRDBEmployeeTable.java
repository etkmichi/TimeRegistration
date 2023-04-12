package michl.timeregistration.Database.DBTimeRegTables;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import MFNetwork.MFDatabase.MFDatabase.MFDBData;
import MFNetwork.MFDatabase.MFDatabase.MFDBTable;
import MFNetwork.MFDatabase.MFDatabase.MFDatabaseHelper;
import MFNetwork.MFDatabase.MFDatabase.MFSQLFinals;
import michl.timeregistration.Activities.MainActivity;
import michl.timeregistration.Database.DBTimeRegData.TRDBEmployeeData;
import michl.timeregistration.Database.DBTimeRegData.TRDBTimeDateEventData;

public class TRDBEmployeeTable extends MFDBTable {
    private MainActivity context;
    public TRDBEmployeeTable(MFDatabaseHelper helper, MainActivity context){
        super(helper,"Employee","EmployeeID");
        this.context=context;
        m_className="TRDBEmployeeTable";
        addKey("EmployeeID",MFSQLFinals.KEY_TYPE_PRIMARY_INT);
        addKey("FirstName",MFSQLFinals.KEY_TYPE_TEXT);
        addKey("SecondName",MFSQLFinals.KEY_TYPE_TEXT);
        addKey("Icon",MFSQLFinals.KEY_TYPE_TEXT);
        addKey("Color",MFSQLFinals.KEY_TYPE_INT);
        genCommaSeperatedKeys();
    }


    @Override
    public MFDBData readRowData(ResultSet resultSet) throws SQLException {
        TRDBEmployeeData employee=new TRDBEmployeeData(-1,this,context);
        employee.k_employeeID=resultSet.getInt("EmployeeID");
        employee.k_firstName=resultSet.getString("FirstName");
        employee.k_secondName=resultSet.getString("SecondName");
        employee.k_icon=resultSet.getString("Icon");
        employee.k_color=resultSet.getInt("Color");
        employee.updateData();
        return employee;
    }

    @Override
    public void createReferencesCommands() {

    }

    /**
     * Searches for all events connected to a specific employee and returns the list.
     * @param employeeID
     * @return
     */
    public ArrayList<TRDBTimeDateEventData> getEvents(int employeeID){
        ArrayList<TRDBTimeDateEventData> arrayList=new ArrayList<>();
/*        TRDBAttendanceData attendanceData;
        TRDBAbsenceData absenceData;
        TRDBTimeDateEventData event;
        lock(lockVecData);
        for(int i=0;i<m_vecData.size();i++){
            data=(TRDBAttendanceData)m_vecData.get(i);
            if(data.k_employeeID==employeeID){
                event=(TRDBTimeDateEventData)TRDBHelper.instance.getEventTable().findData(data.k_eventID);
                arrayList.add(event);
            }
        }
        release(lockVecData);*/
        return  arrayList;
    }
}
