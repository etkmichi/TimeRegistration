package michl.timeregistration.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import MFLibrary.MFAndroidGUI.MFDialogs.MFDatePickerButton;
import MFLibrary.MFAndroidGUI.MFDialogs.MFSafetyDialogs.MFInterfaceUnlock;
import MFLibrary.MFAndroidGUI.MFDialogs.MFSafetyDialogs.MFSafetyPWUnlockDialog;
import michl.MFConvesion.MFTimeConverter;
import michl.timeregistration.Database.DBTimeRegData.TRDBAbsenceData;
import michl.timeregistration.Database.DBTimeRegData.TRDBTimeDateEventData;
import michl.timeregistration.Database.DBTimeRegTables.TRDBAbsenceTable;
import michl.timeregistration.Database.DBTimeRegTables.TRDBTimeDateEventTable;
import michl.timeregistration.Database.TRDBHelper;
import michl.timeregistration.R;
import michl.timeregistration.TREmployeeRuntimeData;

public class TRActivityNewAbsence extends AppCompatActivity implements MFInterfaceUnlock {

    private MFDatePickerButton
        startDate,
        stopDate;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        MFSafetyPWUnlockDialog adminUnlock=MainActivity.getAdminUnlockDialog(this,this);
        if(adminUnlock!=null){
            adminUnlock.setUnlocker(this);
            adminUnlock.getBuilder().setCancelable(false);
            adminUnlock.show();
        }else{
            Toast.makeText(this,getResources().getString(R.string.admin_dialog_failed),Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * Init the listeners for the buttons
     */
    private void initOnClickListeners(){
        /*Start button*/
        Button button=findViewById(R.id.absence_btnCancel);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBtnCancelClicked();
            }
        });

        /*Stop button*/
        button=findViewById(R.id.absence_btnAccept);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBtnAcceptClicked();
            }
        });
    }

    private void onBtnCancelClicked(){
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Create database entry for the absence and database entries for the Event table.
     */
    private void onBtnAcceptClicked(){
        TREmployeeRuntimeData rtDat=MainActivity.currentEmployeeRunTimeData;
        if(rtDat==null){
            Toast.makeText(this,"No employee chosen in main menu!",Toast.LENGTH_LONG).show();
            return;
        }

        TRDBAbsenceTable absenceTable=TRDBHelper.instance.getAbsenceTable();
        TRDBAbsenceData absenceData=new TRDBAbsenceData(absenceTable.getNextUID(),absenceTable);
        TRDBTimeDateEventTable eventTable=TRDBHelper.instance.getEventTable();
        TRDBTimeDateEventData eventData=new TRDBTimeDateEventData(eventTable.getNextUID(),eventTable);
        absenceData.k_reason="Not implemented yet! - Krank | Uralub | Fortbildung | Sonstiges";
        absenceData.k_eventID=eventData.k_eventID;
        absenceData.k_color=0x00FF9999;
        absenceData.k_employeeID=rtDat.getEmployeeData().k_employeeID;
        absenceData.k_attestation="Not implemented yet!";
        eventData.k_YYYYMMDD=MFTimeConverter.fromCalendarToYYYYMMDD(startDate.getDate());
        eventData.k_stop=Long.valueOf(MFTimeConverter.fromCalendarToYYYYMMDD(stopDate.getDate()))*10000+Long.valueOf(2359);
        eventData.k_start =(Long.valueOf(eventData.k_YYYYMMDD)*10000)+0000L;
        eventData.k_utcStop=stopDate.getDate().getTimeInMillis();

        boolean ret=true;
        if(TRDBHelper.instance.getEventTable().createData(eventData)<0){
            ret=false;
            Toast.makeText(TRActivityNewAbsence.this,"Couldnt create event entry", Toast.LENGTH_SHORT).show();
        }
        if(TRDBHelper.instance.getAbsenceTable().createData(absenceData)<0){
            ret=false;
            Toast.makeText(TRActivityNewAbsence.this,"Couldnt create attendance entry", Toast.LENGTH_SHORT).show();
        }
        if(ret){
            rtDat.addEvent(eventData);
            rtDat.getListAdapter().notifyDataSetChanged();
            Toast.makeText(TRActivityNewAbsence.this,"Data created successfully", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void unlocked() {
        setContentView(R.layout.activity_new_absence);
        startDate=new MFDatePickerButton(findViewById(R.id.absence_btnStartDate),TRActivityNewAbsence.this);
        stopDate=new MFDatePickerButton(findViewById(R.id.absence_btnEndDate),TRActivityNewAbsence.this);
    }

    @Override
    public void locked() {
        Toast.makeText(this,getResources().getString(R.string.admin_wrong_pw),Toast.LENGTH_SHORT).show();
        finish();
    }
}
