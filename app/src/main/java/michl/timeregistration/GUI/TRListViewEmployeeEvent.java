package michl.timeregistration.GUI;

import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import MFLibrary.MFAndroidGUI.MFDialogs.MFComposedDialog;
import MFLibrary.MFAndroidGUI.MFDialogs.MFDBEditDialogs.MFEditDBColorEntry;
import MFLibrary.MFAndroidGUI.MFDialogs.MFDBEditDialogs.MFEditDBDeleteEntry;
import MFLibrary.MFAndroidGUI.MFDialogs.MFDBEditDialogs.MFEditDBTextEntry;
import MFLibrary.MFAndroidGUI.MFDialogs.MFSafetyDialogs.MFInterfaceUnlock;
import MFLibrary.MFAndroidGUI.MFDialogs.MFSafetyDialogs.MFSafetyPWUnlockDialog;
import MFLibrary.MFAndroidGUI.MFListViews.MFListViewMasterSlave;
import michl.timeregistration.Activities.MainActivity;
import michl.timeregistration.Database.DBTimeRegData.TRDBEmployeeData;
import michl.timeregistration.Database.DBTimeRegData.TRDBTimeDateEventData;
import michl.timeregistration.Database.TRDBHelper;
import michl.timeregistration.R;

public class TRListViewEmployeeEvent extends MFListViewMasterSlave implements MFInterfaceUnlock {

    private static View lastEmployeeListItem =null;
    private static int lastEmployeeListItemPosition=-1;

    private TRListAdapterEmployee employeeListAdapter;

    private TRListAdapterEvents eventsListAdapter,emptyEventListAdapter;

    private MainActivity mainActivity;

    private MFEditDBTextEntry
            dial_fNameEdit,
            dial_sNameEdit,
            dial_iconEdit;

    protected TRDBEmployeeData currentData;
    protected View currentView;

    private MFEditDBDeleteEntry dial_deleteEntry;
    private MFEditDBColorEntry
            dial_color;
    private MFComposedDialog clickDial;
    public TRListViewEmployeeEvent(TRListAdapterEmployee employeeAdapter,
                                   ListView masterView,
                                   ListView eventView,
                                   MainActivity context){
        super(employeeAdapter,null,masterView,eventView,context);
        masterView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        /*Create and set an empty event list.*/
        TRDBTimeDateEventData tdEvent=new TRDBTimeDateEventData(-1,TRDBHelper.instance.getEventTable());
        tdEvent.setComposedName(context.getResources().getString(R.string.no_employee));
        ArrayList<TRDBTimeDateEventData> list=new ArrayList<>();
        emptyEventListAdapter=new TRListAdapterEvents(context,list);
        eventsListAdapter=emptyEventListAdapter;
        slave=emptyEventListAdapter;
        slaveView.setAdapter(slave);

        initDialogs();

        clickDial=new MFComposedDialog(context,context.getResources().getString(R.string.edit_employee));
        employeeListAdapter=employeeAdapter;
        mainActivity=context;
    }

    private void initDialogs(){
        DialogInterface.OnDismissListener dismissListener=new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                onEditDismissed();
            }
        };
        dial_fNameEdit = new MFEditDBTextEntry(
                context,getContext().getResources().getString(R.string.new_employee_fname),"FirstName",null);
        dial_fNameEdit.setDissmissListener(dismissListener);

        dial_sNameEdit = new MFEditDBTextEntry(
                context,getContext().getResources().getString(R.string.new_employee_sname),"SecondName",null);
        dial_sNameEdit.setDissmissListener(dismissListener);

        dial_iconEdit = new MFEditDBTextEntry(
                context,getContext().getResources().getString(R.string.new_employee_icon),"Icon",null);
        dial_iconEdit.setDissmissListener(dismissListener);

        dial_color = new MFEditDBColorEntry(
                context,getContext().getResources().getString(R.string.new_employee_color),null,"Color",null);
        dial_color.setDissmissListener(dismissListener);

        dial_deleteEntry = new MFEditDBDeleteEntry(context,
                context.getResources().getString(R.string.db_delete_titel),
                context.getResources().getString(R.string.db_delete_ok),
                context.getResources().getString(R.string.db_delete_cancel),null);
        dial_deleteEntry.setDissmissListener(dismissListener);
    }

    public void addEmployee(TRDBEmployeeData employee){
        employeeListAdapter.addEmployee(employee);
    }

    public void onEditDismissed() {
        //TODO test with many entries
        TRDBHelper.instance.getEmployeeTable().loadFromDatabase();
        employeeListAdapter=new TRListAdapterEmployee(context,
                new ArrayList<>(TRDBHelper.instance.getEmployeeTable().getTableData()),masterView);
        employeeListAdapter.notifyDataSetChanged();
        masterView.setAdapter(employeeListAdapter);
        employeeListAdapter.notifyDataSetChanged();
    }

    public void forceReload(){
        TRDBHelper.instance.getEmployeeTable().loadFromDatabase();
        employeeListAdapter=new TRListAdapterEmployee(context,
                new ArrayList<>(TRDBHelper.instance.getEmployeeTable().getTableData()),masterView);
        employeeListAdapter.notifyDataSetChanged();
        masterView.setAdapter(employeeListAdapter);
        employeeListAdapter.notifyDataSetChanged();
    }

    public void hideEventList(){
        eventsListAdapter=emptyEventListAdapter;
        slave=emptyEventListAdapter;
        slaveView.setAdapter(slave);
    }

    @Override
    public void onMasterListItemClicked(int position, View view, ListView listViewSlave) {
        if(lastEmployeeListItem !=null)
            lastEmployeeListItem.setBackgroundColor(Color.TRANSPARENT);
        unlockTag=f_unlock_tag_onClick;
        lastEmployeeListItem =view;

        eventsListAdapter=emptyEventListAdapter;
        slave=emptyEventListAdapter;
        slaveView.setAdapter(slave);

        mainActivity.saveCurrentRuntimeData();
        mainActivity.setCurrentRuntimeData(null);

        if(lastEmployeeListItemPosition==position){/*Its the same again, lock the view of the employee!*/
            view.setBackgroundColor(Color.TRANSPARENT);
            lastEmployeeListItemPosition=-1;
            return;
        }

        lastEmployeeListItemPosition=position;
        currentData=((TRDBEmployeeData)employeeListAdapter.getItem(position));
        TRSafetyPWUnlockDialog pwDialog=new TRSafetyPWUnlockDialog(context,this,"",currentData);

        view.setBackgroundColor(Color.LTGRAY);
        currentView=view;
        pwDialog.show();
    }


    public final int
            f_unlock_tag_onClick=1,
            f_unlock_tag_onLongClick=2;

    protected int unlockTag=0;
    public void unlocked(){
        switch(unlockTag){
            case f_unlock_tag_onClick:/*A specific employee got unlocked, load the data*/
                mainActivity.setCurrentRuntimeData(
                        currentData.createRuntimeData()
                );
                eventsListAdapter=currentData.getRuntimeData().getListAdapter();
                slave=eventsListAdapter;
                slaveView.setAdapter(slave);
                break;
            case f_unlock_tag_onLongClick:
                showEditMasterItem();
                break;
        }
        unlockTag=0;

    }

    public void locked(){
        Toast.makeText(context,context.getResources().getString(R.string.admin_wrong_pw),Toast.LENGTH_SHORT).show();
        switch(unlockTag){
            case f_unlock_tag_onClick:
                mainActivity.setCurrentRuntimeData(null);
                if(masterView.getSelectedView()!=null)
                    masterView.getSelectedView().setBackgroundColor(Color.TRANSPARENT);
                break;
            case f_unlock_tag_onLongClick:
                break;
        }
        unlockTag=0;
    }

    public void showEditMasterItem(){
        if (currentData != null){
            dial_fNameEdit.setDBData(currentData);
            dial_fNameEdit.setButtonText(getContext().getResources().getString(R.string.new_employee_fname) + " " + currentData.k_firstName);
            dial_fNameEdit.setTitle(getContext().getResources().getString(R.string.new_employee_fname) + " " + currentData.k_firstName);

            dial_sNameEdit.setDBData(currentData);
            dial_sNameEdit.setButtonText(getContext().getResources().getString(R.string.new_employee_sname) + " " + currentData.k_secondName);
            dial_sNameEdit.setTitle(getContext().getResources().getString(R.string.new_employee_sname) + " " + currentData.k_secondName);

            dial_iconEdit.setDBData(currentData);
            dial_iconEdit.setButtonText(getContext().getResources().getString(R.string.new_employee_icon) + " " + currentData.k_icon);
            dial_iconEdit.setTitle(getContext().getResources().getString(R.string.new_employee_icon) + " " + currentData.k_icon);

            dial_color.setDBData(currentData);

            dial_deleteEntry.setDBData(currentData);
            dial_deleteEntry.setButtonText(context.getResources().getString(R.string.db_delete_titel) + ": \n" + currentData.k_firstName + "\n" +
                    currentData.k_secondName);
            dial_deleteEntry.setTitle(context.getResources().getString(R.string.db_delete_titel) + " " + currentData.k_firstName);

            clickDial.clearDialogs();
            clickDial.addDialog(dial_fNameEdit);
            clickDial.addDialog(dial_sNameEdit);
            clickDial.addDialog(dial_iconEdit);
            clickDial.addDialog(dial_color);
            clickDial.addDialog(dial_deleteEntry);
            clickDial.show();
        }else{
            Toast.makeText(context,"No database entry found for this position!",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method marks the currently chosen master item.
     * @param view
     * @param firstVisibleItem
     * @param visibleItemCount
     * @param totalItemCount
     */
    @Override
    public void onMasterScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(mainActivity==null || mainActivity.getCurrentEmployeeRunTimeData()==null){
            if(lastEmployeeListItem!=null)
                lastEmployeeListItem.setBackgroundColor(Color.TRANSPARENT);
            return;
        }
        if( lastEmployeeListItemPosition>=firstVisibleItem &&
                lastEmployeeListItemPosition<(firstVisibleItem+visibleItemCount))
        {/*If the item is within the bounds*/
            lastEmployeeListItem.setBackgroundColor(Color.LTGRAY);
        }else{/*Item is not set or outside the boundaries*/
            if(lastEmployeeListItem!=null){
                lastEmployeeListItem.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    @Override
    public boolean onMasterItemLongClicked(AdapterView<?> parent, View view, int position, long id) {
        currentData = ((TRDBEmployeeData)employeeListAdapter.getItem(position));
        MFSafetyPWUnlockDialog adminUnlock=MainActivity.getAdminUnlockDialog(context,this);
        unlockTag=f_unlock_tag_onLongClick;
        dial_color.setColorizedView(view);
        if(adminUnlock!=null)
            adminUnlock.show();
        return true;
    }

    @Override
    public boolean onSlaveItemLongClicked(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }
}
