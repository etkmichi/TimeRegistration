package michl.timeregistration;

import android.content.Context;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;

import MFNetwork.MFData.MFObjects.MFObject;
import michl.timeregistration.Database.DBTimeRegData.TRDBEmployeeData;
import michl.timeregistration.Database.DBTimeRegData.TRDBTimeDateEventData;
import michl.timeregistration.Database.DBTimeRegTables.TRDBAttendanceTable;
import michl.timeregistration.Database.DBTimeRegTables.TRDBTimeDateEventTable;
import michl.timeregistration.GUI.TRListAdapterEvents;

/**
 * This class contains the data of an employee during runtime.
 */
public class TREmployeeRuntimeData extends MFObject {
    public int
            currentPauseSpan=0,
            currentYYYYMMDD=20190101;

    public Long
            currentUTCStart,
            currentUTCStop;

    public Calendar
            currentTimeStart=null,
            currentTimeStop=null,
            currentTimePause,
            currentDate;

    private boolean
            isUserLocked=true;

    private TRDBEmployeeData
            employeeData;

    private TRDBAttendanceTable
            attendanceTable;

    private ArrayList<TRDBTimeDateEventData>
            arrayList;
    private TRListAdapterEvents
            employeeEvents;

    private Context
            context;

    private ListView eventList;

    /**
     * This Constructor will create a list adapter containing the events of a specific employee
     * @param employeeData data of the employee which must be handled
     * @param attendanceTable The table will be searched for employee specific attendance entries.
     */
    public TREmployeeRuntimeData(Context context, TRDBEmployeeData employeeData, TRDBAttendanceTable attendanceTable,ListView eventList){
        this.context=context;
        this.employeeData=employeeData;
        this.attendanceTable=attendanceTable;
        arrayList=attendanceTable.getEvents(employeeData.k_employeeID);
        this.eventList=eventList;
        employeeEvents=new TRListAdapterEvents(context,arrayList);
    }

    /**
     * Searches the table {@link TRDBTimeDateEventTable}, specified in the constructor, for new event entries.
     */
    public void updateListAdapter(){
        arrayList=attendanceTable.getEvents(employeeData.k_employeeID);
        employeeEvents=new TRListAdapterEvents(context,arrayList);
    }

    public void addEvent(TRDBTimeDateEventData event){
        arrayList.add(event);
        employeeEvents.notifyDataSetChanged();
    }

    public TRListAdapterEvents getListAdapter(){
        return employeeEvents;
    }

    public TRDBEmployeeData getEmployeeData(){
        return employeeData;
    }
}
/*    public static class TRCurrentEmployeeRuntimeData{
        public static Long
                currentUTCStart,
                currentUTCStop;

        public static int
                currentPauseSpan=0,
                currentYYYYMMDD=20190101;

        public static Calendar
                currentTimeStart=null,
                currentTimeStop=null,
                currentTimePause,
                currentCalendar=null,
                currentDate;
    }*/

