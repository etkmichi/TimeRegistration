package michl.timeregistration.Database.DBTimeRegData;

import android.graphics.Color;
import android.graphics.drawable.Icon;

import java.util.concurrent.Semaphore;

import MFNetwork.MFDatabase.MFDatabase.MFDBData;
import michl.timeregistration.Activities.MainActivity;
import michl.timeregistration.Database.DBTimeRegTables.TRDBEmployeeTable;
import michl.timeregistration.Database.TRDBHelper;
import michl.timeregistration.R;
import michl.timeregistration.TREmployeeRuntimeData;

public class TRDBEmployeeData extends MFDBData {
    private static MainActivity context;
    public int
    k_employeeID=-1,
    k_color=0;
    public String
            k_firstName="",
            k_secondName="",
            k_icon="standard";
    private TREmployeeRuntimeData runtimeData=null;
    private Semaphore
            lockData=new Semaphore(1);

    public TRDBEmployeeData(int id, TRDBEmployeeTable table, MainActivity context){
        super(table);
        k_employeeID=id;
        m_className="TRDBEmployeeData";

        if(context!=null)
            this.context=context;
    }

    @Override
    public int getUID() {
        return k_employeeID;
    }

    @Override
    public void updateData() {
        lock(lockData);
        m_commaSeperatedValues=
                k_employeeID+","+k_firstName+","+k_secondName+","+k_icon+","+k_color;
        m_composedName=
                k_firstName+" "+k_secondName;
        release(lockData);
    }

    @Override
    public String getCommaSaperatedData() {
        lock(lockData);
        m_commaSeperatedValues=
                k_employeeID+","+k_firstName+","+k_secondName+","+k_icon+","+k_color;
        release(lockData);
        return m_commaSeperatedValues;
    }

    public Icon getIcon(){
        //TODO implement with drawable resources
        Icon ret;
        try{
            ret=android.graphics.drawable.Icon.createWithResource(context, R.drawable.icon_test);
            //Icon.createWithContentUri(getBaseDir()+"/icons/"+k_icon);

        }catch(Exception e){
            p_debugPrint("couldnt find icon!! Path/name:"+getBaseDir()+"/icons/"+k_icon);
            ret=android.graphics.drawable.Icon.createWithResource(context, R.drawable.icon_test);
        }
        return ret;
    }

    /**
     * @return the TREmployeeRuntimeData connected to the employee.
     */
    public TREmployeeRuntimeData getRuntimeData(){
        return runtimeData;
    }

    /**
     * Creates the runtime data if not already existent. Stores the returned value on a private field.
     * @return the created or already created {@link TREmployeeRuntimeData}
     */
    public TREmployeeRuntimeData createRuntimeData(){
        if(runtimeData==null){
            p_debugPrint("creating runtime data for employee");
            runtimeData=new TREmployeeRuntimeData(
                    context,
                    this,
                    ((TRDBHelper)table.getDBHelper()).getAttendanceTable(),context.getEventListView());
        }
        return runtimeData;
    }

    public Color getBackgroundColor(){
        Color ret=Color.valueOf(k_color);
        return ret;
    }
}
