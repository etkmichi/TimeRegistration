package michl.timeregistration.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import MFLibrary.MFAndroidGUI.MFDialogs.MFCreatePasswordDialog;
import MFLibrary.MFAndroidGUI.MFDialogs.MFFileDialog;
import MFLibrary.MFAndroidGUI.MFDialogs.MFSafetyDialogs.MFInterfaceUnlock;
import MFLibrary.MFAndroidGUI.MFDialogs.MFSafetyDialogs.MFSafetyPWUnlockDialog;
import MFNetwork.MFData.MFObjects.MFObject;
import MFNetwork.MFDatabase.MFDatabase.MFDBData;
import michl.MFConvesion.MFTimeConverter;
import michl.timeregistration.Database.DBTimeRegData.TRDBAttendanceData;
import michl.timeregistration.Database.DBTimeRegData.TRDBEmployeeData;
import michl.timeregistration.Database.DBTimeRegData.TRDBTimeDateEventData;
import michl.timeregistration.Database.TRDBHelper;
import michl.timeregistration.GUI.TRListAdapterEmployee;
import michl.timeregistration.GUI.TRListViewEmployeeEvent;
import michl.timeregistration.R;
import michl.timeregistration.TREmployeeRuntimeData;
import michl.timeregistration.TRTasks.TRTaskInitDB;

public class MainActivity extends AppCompatActivity {
    public final static int f_request_updateEmployeeList =0x00007001;
    private static TRDBHelper
            databaseHelper;

    private static Long
            currentUTCStart,
            currentUTCStop;

    private static int
            currentPauseSpan=0,
            currentYYYYMMDD=20190101;

    private static Calendar
            currentTimeStart=null,
            currentTimeStop=null,
            currentTimePause,
            currentCalendar=null,
            currentDate;

    private static String
            currentString="";

    private Button
            currentButton;

    public static TREmployeeRuntimeData
            currentEmployeeRunTimeData=null;

    private static TimePickerDialog.OnTimeSetListener
            timeSetListener;

    private static TimePickerDialog
            timePicker;

    private static DatePickerDialog.OnDateSetListener
            dateSetListener;

    private static DatePickerDialog
            datePicker;

    private TRListViewEmployeeEvent
            mainList;

    private static MFSafetyPWUnlockDialog
            adminUnlock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MFObject.setBaseDir(getFilesDir().getPath());
        String path=getFilesDir().getPath();
        String name="TimeRegistrationDatabase.sql";

        setContentView(R.layout.activity_main);

        currentTimePause=Calendar.getInstance();
        currentTimePause.set(Calendar.HOUR_OF_DAY,0);
        currentTimePause.set(Calendar.MINUTE,0);
        currentTimePause.set(Calendar.SECOND,0);
        currentTimePause.set(Calendar.MILLISECOND,0);
        currentDate=Calendar.getInstance();
        currentDate.set(Calendar.SECOND,0);
        currentDate.set(Calendar.MILLISECOND,0);
        currentYYYYMMDD=
                (currentDate.get(Calendar.YEAR)*10000+
                (currentDate.get(Calendar.MONTH)+1)*100+
                currentDate.get(Calendar.DAY_OF_MONTH));



        if(databaseHelper==null){
            databaseHelper=new TRDBHelper(path,name,MainActivity.this);
            /*The task TRTaskInitDB will init the db and some resources of the MainActivity!*/
            databaseHelper.addTask(new TRTaskInitDB(MainActivity.this,databaseHelper));
            databaseHelper.start();
            initDialogs();
            initOnClickListeners();
        }else{
            this.setDatabaseHelper(databaseHelper);
            this.initLists();
            initDialogs();
            initOnClickListeners();
            this.enableDBRelatedButtons(true);
        }

        File adminPw=new File(getFilesDir().getPath(),"administration.pw");

        //TODO if(!adminPw.exists()){
            initAdministration();
            initBackupFolder();
        //}
        //TODO Create dialogs for vertical and horizontal view. If OnCreate is called, use the already created resources.
        DisplayMetrics metrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height=metrics.heightPixels;
        int width = metrics.widthPixels;
        MFObject.p_debugStaticPrint("Height="+height+" Width="+width);
    }



    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        switch (resultCode){
            case f_request_updateEmployeeList:
                TRDBEmployeeData employee =TRActivityNewEmployee.getCreatedEmployee();
                if(employee!=null && mainList!=null){
                    mainList.addEmployee(employee);
                    mainList.forceReload();
                }
                break;
            default:
                super.onActivityResult(requestCode,resultCode,data);
        }
    }

    private void initAdministration() {
        MFCreatePasswordDialog dialog=new MFCreatePasswordDialog(this,
                getResources().getString(R.string.admin_pass_input),getFilesDir().getPath(),"administration.pw");
        dialog.getBuilder().setCancelable(false);
        dialog.show();
    }

    private void initBackupFolder() {
        File backupDirFile = new File("/backup/michl.timereg");
        if (!backupDirFile.mkdirs()) {
            Toast.makeText(this, R.string.backup_no_access, Toast.LENGTH_SHORT);
            MFFileDialog backupDir = new MFFileDialog
                    (this, getResources().getString(R.string.backup_directory), "/backup/michl.timereg");
            backupDir.show();
        }
    }

    public static MFSafetyPWUnlockDialog getAdminUnlockDialog(Context context, MFInterfaceUnlock unlocker){
        adminUnlock=new MFSafetyPWUnlockDialog(context,unlocker,
                context.getResources().getString(R.string.admin_pw_verification),"",
                context.getFilesDir().getPath(),"administration.pw");
        return adminUnlock;
    }

    private void initDialogs() {
        timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                MainActivity.this.onTimeSet(view,hourOfDay,minute);
            }
        };
        timePicker=new TimePickerDialog(MainActivity.this, timeSetListener,0,0,true);

        dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                MainActivity.this.onDateSet(view,year,month,dayOfMonth);
            }
        };
        datePicker=new DatePickerDialog(MainActivity.this,dateSetListener,
                currentDate.get(Calendar.YEAR),
                (currentDate.get(Calendar.MONTH)+1),
                currentDate.get(Calendar.DAY_OF_MONTH));

    }

    /**
     * Call back for date dialog of button date
     * @param view
     * @param year
     * @param month
     * @param dayOfMonth
     */
    private void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        currentDate.set(Calendar.YEAR,year);
        currentDate.set(Calendar.MONTH,month);
        currentDate.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        currentYYYYMMDD=(year*10000+(month+1)*100+dayOfMonth);
        Button date=findViewById(R.id.main_btnDate);
        date.setText(getResources().getString(R.string.main_btn_date)+MFTimeConverter.fromIntYYYYMMDDToString(currentYYYYMMDD));
    }

    /**
     * Initializes the lists for the list views
     */
    public void initLists(){
        ListView listViewEvents=findViewById(R.id.listTimeRecord);
        ListView listViewEmployee=findViewById(R.id.listEmployee);
        TRListAdapterEmployee employeeListAdapter=new TRListAdapterEmployee(
                MainActivity.this,
                new ArrayList<MFDBData>(databaseHelper.getEmployeeTable().getTableData()),
                listViewEvents);

        mainList=new TRListViewEmployeeEvent(employeeListAdapter,listViewEmployee,listViewEvents,MainActivity.this);
    }

    /**
     * Sets the currentEmp... field and loads all values for the gui.
     * @param currentEmployeeRunTimeData
     */
    public void setCurrentRuntimeData(TREmployeeRuntimeData currentEmployeeRunTimeData){
        this.currentEmployeeRunTimeData=currentEmployeeRunTimeData;
        loadEmployeeRuntimeDate(currentEmployeeRunTimeData);
    }

    public TREmployeeRuntimeData getCurrentEmployeeRunTimeData(){
        return currentEmployeeRunTimeData;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.new_employee:
                intentNewEmployee();
                return true;
            case R.id.absence:
                intentAbsence();
                return true;
            case R.id.export:
                intentExport();
                return true;
            case R.id.help:
                intentHelp();
                return true;
            case R.id.setting:
                inentSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Starts the help activity
     */
    public void intentHelp(){
        Intent intent=new Intent(MainActivity.this,TRActivityHelp.class);
        startActivity(intent);
    }

    /**
     * Starts the settings activity
     */
    public void inentSettings(){
        Intent intent=new Intent(MainActivity.this,TRActivitySettings.class);
        startActivity(intent);
    }

    /**
     * Starts the export activity
     */
    public void intentExport(){
        Intent intent=new Intent(MainActivity.this,TRActivityExport.class);
        startActivity(intent);
    }


    public void saveCurrentRuntimeData() {
        if(currentEmployeeRunTimeData!=null){
            /*Set all currently used data to the currentEmployeeRunTimeData and set the values of gui elements to default*/
            currentEmployeeRunTimeData.currentPauseSpan=currentPauseSpan;
            currentEmployeeRunTimeData.currentTimePause=currentTimePause;
            currentEmployeeRunTimeData.currentTimeStart=currentTimeStart;
            currentEmployeeRunTimeData.currentTimeStop=currentTimeStop;
            currentEmployeeRunTimeData.currentUTCStart=currentUTCStart;
            currentEmployeeRunTimeData.currentUTCStop=currentUTCStop;
            currentEmployeeRunTimeData.currentDate=currentDate;
            currentEmployeeRunTimeData.currentYYYYMMDD=currentYYYYMMDD;
        }
    }

    public void enableDBRelatedButtons(boolean enable){
        Button button=findViewById(R.id.main_btnAccept);
        button.setEnabled(enable);
    }

    /**
     * loads the data of stored in rtDat and sets up the gui fields
     * @param rtDat
     */
    public void loadEmployeeRuntimeDate(TREmployeeRuntimeData rtDat){
        if(rtDat!=null){
            /*Set all default values to the  ones stored in currentEmp... (if existing) */
            currentPauseSpan=rtDat.currentPauseSpan;
            currentTimePause=rtDat.currentTimePause;
            currentTimeStart=rtDat.currentTimeStart;
            currentTimeStop=rtDat.currentTimeStop;
            currentUTCStart=rtDat.currentUTCStart;
            currentUTCStop=rtDat.currentUTCStop;
            currentDate=rtDat.currentDate;
            currentYYYYMMDD=rtDat.currentYYYYMMDD;

            Button button=findViewById(R.id.main_btnPause);
            int hour=0,minute=0;
            if(currentTimePause!=null){
                hour=currentTimePause.get(Calendar.HOUR_OF_DAY);
                minute=currentTimePause.get(Calendar.MINUTE);
                button.setText(getResources().getString(R.string.main_btn_pause)+MFTimeConverter.fromIntHHMMToString(hour*100+minute));
            }else{
                button.setText(getResources().getString(R.string.main_btn_pause));
            }

            if(currentDate!=null){
                Button date=findViewById(R.id.main_btnDate);
                date.setText(getResources().getString(R.string.main_btn_date)+MFTimeConverter.fromIntYYYYMMDDToString(currentYYYYMMDD));
                datePicker.updateDate(currentDate.get(Calendar.YEAR),currentDate.get(Calendar.MONTH)+1,
                        currentDate.get(Calendar.DAY_OF_MONTH));
            }else {
                currentDate=Calendar.getInstance();
                currentDate.set(Calendar.SECOND,0);
                currentDate.set(Calendar.MILLISECOND,0);
                currentYYYYMMDD=(currentDate.get(Calendar.YEAR)*10000+
                        (currentDate.get(Calendar.MONTH)+1)*100+
                        currentDate.get(Calendar.DAY_OF_MONTH));
                Button date=findViewById(R.id.main_btnDate);
                date.setText(getResources().getString(R.string.main_btn_date)+MFTimeConverter.fromIntYYYYMMDDToString(currentYYYYMMDD));
            }

            button=findViewById(R.id.main_btnStart);
            if(currentTimeStart!=null){
                hour=currentTimeStart.get(Calendar.HOUR_OF_DAY);
                minute=currentTimeStart.get(Calendar.MINUTE);
                button.setText(getResources().getString(R.string.main_btn_start)+MFTimeConverter.fromIntHHMMToString(hour*100+minute));
            }else{
                button.setText(getResources().getString(R.string.main_btn_start));
            }

            button=findViewById(R.id.main_btnStop);
            if(currentTimeStop!=null){
                hour=currentTimeStop.get(Calendar.HOUR_OF_DAY);
                minute=currentTimeStop.get(Calendar.MINUTE);
                button.setText(getResources().getString(R.string.main_btn_stop)+MFTimeConverter.fromIntHHMMToString(hour*100+minute));
            }else{
                button.setText(getResources().getString(R.string.main_btn_stop));
            }
        }
    }

    /**
     * Init the listeners for the buttons
     */
    private void initOnClickListeners(){
        /*Start button*/
        Button button=findViewById(R.id.main_btnStart);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBtnStartClicked();
            }
        });
        button.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v){
                onBtnStartLongClicked();
                return true;
            }
        });

        /*Stop button*/
        button=findViewById(R.id.main_btnStop);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBtnStopClicked();
            }
        });

        button.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v){
                onBtnStopLongClicked();
                return true;
            }
        });

        /*Accept/Ok button*/
        button=findViewById(R.id.main_btnAccept);
        button.setEnabled(false);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBtnOkClicked();
            }
        });

        /*Pause button*/
        button=findViewById(R.id.main_btnPause);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBtnPauseClicked();
            }
        });
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onBtnPauseLongClicked();
                return true;
            }
        });

        /*Date button*/
        button=findViewById(R.id.main_btnDate);
        button.setText(getResources().getString(R.string.main_btn_date)+
                MFTimeConverter.fromIntYYYYMMDDToString(currentYYYYMMDD));
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                MainActivity.this.onBtnDateClicked();
            }
        });
    }

    private void onBtnDateClicked() {
        if(currentDate!=null)
        datePicker.updateDate(
                currentDate.get(Calendar.YEAR),
                currentDate.get(Calendar.MONTH),
                currentDate.get(Calendar.DAY_OF_MONTH));
        datePicker.show();
    }

    /**
     *
     * Open new employee dialog/activity
     */
    public void intentNewEmployee(){
        Intent intent=new Intent(MainActivity.this,TRActivityNewEmployee.class);
        startActivityForResult(intent,0);
    }

    /**
     * Set start time
     */
    public void onBtnStartClicked(){
        currentButton=findViewById(R.id.main_btnStart);
        currentTimeStart =Calendar.getInstance();
        currentTimeStart.set(Calendar.SECOND,0);
        currentTimeStart.set(Calendar.MILLISECOND,0);
        currentString=getResources().getString(R.string.main_btn_start);

        int hour= currentTimeStart.get(Calendar.HOUR_OF_DAY);
        int minute= currentTimeStart.get(Calendar.MINUTE);
        currentCalendar=currentTimeStart;
        /*The onTimeSet callback is set in the onCreate method (onTimeSet(...))*/
        timePicker.updateTime(hour,minute);
        timePicker.setTitle(R.string.time_titel);
        timePicker.show();
    }

    /**
     * Set stop time
     */
    public void onBtnStopClicked(){
        currentButton=findViewById(R.id.main_btnStop);
        currentTimeStop = Calendar.getInstance();
        currentTimeStop.set(Calendar.SECOND,0);
        currentTimeStop.set(Calendar.MILLISECOND,0);
        currentString=getResources().getString(R.string.main_btn_stop);
        int hour= currentTimeStop.get(Calendar.HOUR_OF_DAY);
        int minute= currentTimeStop.get(Calendar.MINUTE);
        currentCalendar=currentTimeStop;
        timePicker.updateTime(hour,minute);
        timePicker.setTitle(R.string.time_titel);
        timePicker.show();
    }

    /**
     * Open new employee dialog/activity
     */
    public void onBtnPauseClicked(){
        currentButton=findViewById(R.id.main_btnPause);
        currentString=getResources().getString(R.string.main_btn_pause);
        currentTimePause=Calendar.getInstance();
        currentTimePause.set(Calendar.SECOND,0);
        currentTimePause.set(Calendar.MILLISECOND,0);
        currentCalendar=currentTimePause;

        timePicker.updateTime(0,0);
        timePicker.setTitle(R.string.time_titel);
        timePicker.show();
    }

    /**
     *
     * @param view
     * @param hourOfDay
     * @param minute
     */
    public void onTimeSet(TimePicker view,int hourOfDay,int minute){
        if(currentButton==null || currentString==null)
            return;
        currentString+=MFTimeConverter.fromIntHHMMToString(hourOfDay*100+minute);
        currentButton.setText(currentString);
        if(currentCalendar!=null){
            currentCalendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
            currentCalendar.set(Calendar.MINUTE,minute);
        }else
        {
            Toast.makeText(MainActivity.this,"Sth went wrong currentCalendar==null!", Toast.LENGTH_LONG);
        }
        currentCalendar=null;
        currentButton=null;
        currentString="";

    }

    public void onBtnStartLongClicked(){
        currentButton=findViewById(R.id.main_btnStart);
        currentUTCStart=System.currentTimeMillis();
        currentTimeStart =Calendar.getInstance();
        currentTimeStart.set(Calendar.SECOND,0);
        currentTimeStart.set(Calendar.MILLISECOND,0);
        int val=currentTimeStart.get(Calendar.HOUR_OF_DAY)*100+currentTimeStart.get(Calendar.MINUTE);
        String start=getResources().getString(R.string.main_btn_start)+MFTimeConverter.fromIntHHMMToString(val);
        currentButton.setText(start);
    }

    public void onBtnStopLongClicked(){
        Button button=findViewById(R.id.main_btnStop);
        currentUTCStop=System.currentTimeMillis();
        currentTimeStop =Calendar.getInstance();
        currentTimeStop.set(Calendar.SECOND,0);
        currentTimeStop.set(Calendar.MILLISECOND,0);
        int val=currentTimeStop.get(Calendar.HOUR_OF_DAY)*100+currentTimeStop.get(Calendar.MINUTE);
        String stop=getResources().getString(R.string.main_btn_stop)+MFTimeConverter.fromIntHHMMToString(val);
        button.setText(stop);
    }

    public void onBtnPauseLongClicked(){
        Button button=findViewById(R.id.main_btnPause);
        currentPauseSpan=0;
        currentTimePause =Calendar.getInstance();
        currentTimePause.set(Calendar.SECOND,0);
        currentTimePause.set(Calendar.MILLISECOND,0);
        currentTimePause.set(Calendar.HOUR_OF_DAY,0);
        currentTimePause.set(Calendar.MINUTE,0);
        currentTimePause.set(Calendar.SECOND,0);
        int val=currentTimePause.get(Calendar.HOUR_OF_DAY)*100+currentTimePause.get(Calendar.MINUTE);
        String pause=getResources().getString(R.string.main_btn_pause)+MFTimeConverter.fromIntHHMMToString(val);
        button.setText(pause);
    }

    /**
     * Create database entries for attendance and the related event.
     */
    public void onBtnOkClicked(){

        if(currentTimeStart==null || currentEmployeeRunTimeData==null){
            Toast.makeText(MainActivity.this,R.string.main_err_accept_1, Toast.LENGTH_LONG).show();
            return;
        }
        TRDBEmployeeData currentEmployee=currentEmployeeRunTimeData.getEmployeeData();

        if(currentTimeStop==null){
            Toast.makeText(MainActivity.this,R.string.main_auto_set_stop_time, Toast.LENGTH_LONG).show();
            return;
        }

        if(currentTimePause==null){
            Toast.makeText(MainActivity.this,R.string.main_auto_set_pause_time, Toast.LENGTH_LONG).show();
            return;
        }

        /*Create db attendance*/
        TRDBAttendanceData nAttendance=new TRDBAttendanceData(databaseHelper.getAttendanceTable().getNextUID(),
                databaseHelper.getAttendanceTable());
        nAttendance.k_eventID=databaseHelper.getEventTable().getNextUID();
        nAttendance.k_employeeID=currentEmployee.k_employeeID;
        nAttendance.k_color=Color.LTGRAY;

        /*Create db event*/
        TRDBTimeDateEventData nEntry=new TRDBTimeDateEventData(nAttendance.k_eventID,databaseHelper.getEventTable());
        nEntry.k_stop= Long.valueOf(currentYYYYMMDD)*10000+Long.valueOf(currentTimeStop.get(Calendar.HOUR_OF_DAY)*100+ currentTimeStop.get(Calendar.MINUTE));
        nEntry.k_start= Long.valueOf(currentYYYYMMDD)*10000+Long.valueOf(currentTimeStart.get(Calendar.HOUR_OF_DAY)*100+ currentTimeStart.get(Calendar.MINUTE));
        nEntry.k_utcStart=currentTimeStart.getTimeInMillis();
        nEntry.k_utcStop=currentTimeStop.getTimeInMillis();
        nEntry.k_pauseSpan=currentTimePause.get(Calendar.HOUR_OF_DAY)*100+currentTimePause.get(Calendar.MINUTE);
        nEntry.k_pauseStart=0;
        nEntry.k_pauseStop=0;
        nEntry.k_utcPauseStart=0L;
        nEntry.k_utcPauseStop=0L;
        nEntry.k_YYYYMMDD=currentYYYYMMDD;

        boolean ret=true;
        if(databaseHelper.getEventTable().createData(nEntry)<0){
            ret=false;
            databaseHelper.p_releasePrint("Couldnt create event entry in MainActivity.onBtnOkClicked()");
            Toast.makeText(MainActivity.this,R.string.main_err_accept_2, Toast.LENGTH_LONG).show();
        }
        if(databaseHelper.getAttendanceTable().createData(nAttendance)<0){
            ret=false;
            databaseHelper.p_releasePrint("Couldnt create attendance entry in MainActivity.onBtnOkClicked()");
            Toast.makeText(MainActivity.this,R.string.main_err_accept_3, Toast.LENGTH_LONG).show();
        }
        if(ret){
            currentEmployee.getRuntimeData().addEvent(nEntry);
            currentEmployee.getRuntimeData().getListAdapter().notifyDataSetChanged();
            Toast.makeText(MainActivity.this,R.string.main_accept_ok, Toast.LENGTH_SHORT).show();
        }
        //TODO in failure create new database to store the data! Inform user if no access permission!
    }

    public void intentAbsence(){
        Intent intent=new Intent(MainActivity.this,TRActivityNewAbsence.class);
        startActivity(intent);
    }

    public ListView getEventListView() {
        return (ListView)findViewById(R.id.listTimeRecord);
    }

    public void setDatabaseHelper(TRDBHelper databaseHelper) {
        if(databaseHelper!=this.databaseHelper){
            this.databaseHelper.exit();
            this.databaseHelper=databaseHelper;
        }
    }
}
