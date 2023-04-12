package michl.timeregistration.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import MFLibrary.MFAndroidGUI.MFDialogs.MFColorListDialog;
import MFLibrary.MFAndroidGUI.MFDialogs.MFSafetyDialogs.MFInterfaceUnlock;
import MFLibrary.MFAndroidGUI.MFDialogs.MFSafetyDialogs.MFSafetyPWUnlockDialog;
import michl.timeregistration.Database.DBTimeRegData.TRDBEmployeeData;
import michl.timeregistration.Database.DBTimeRegTables.TRDBEmployeeTable;
import michl.timeregistration.Database.TRDBHelper;
import michl.timeregistration.GUI.TRSafetyPWUnlockDialog;
import michl.timeregistration.R;


public class TRActivityNewEmployee extends AppCompatActivity implements MFInterfaceUnlock {
    private static TRDBEmployeeData latestCreatedEmployee=null;
    MFColorListDialog colorList;

    /**
     *
     * @return Returns the created employee. If no employee was created by dialog or something failed during creation, it will
     * return null.
     */
    public static TRDBEmployeeData getCreatedEmployee() {
        return latestCreatedEmployee;
    }

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

    @Override
    public void unlocked() {
        setContentView(R.layout.activity_new_employee);
        int[] colors=getResources().getIntArray(R.array.colorItems);
        colorList=new MFColorListDialog(TRActivityNewEmployee.this,"Choose Color",colors);
        initOnClickListeners();
    }

    @Override
    public void locked() {
        Toast.makeText(this,getResources().getString(R.string.admin_wrong_pw),Toast.LENGTH_SHORT).show();
        finish();
    }

    private void initOnClickListeners(){
        Button btn=findViewById(R.id.newEmployeeBtnOk);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBtnOkClicked();
            }
        });

        btn=findViewById(R.id.newEmployeeBtnCancel);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBtnCancelClicked();
            }
        });

        btn=findViewById(R.id.btnEmployeeColor);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBtnColorClicked(v);
            }

        });
    }

    private void onBtnColorClicked(View v) {
        colorList.setColorizedView(v);
        colorList.getDialog().show();
    }

    public void onBtnOkClicked(){
        String err      ="";
        String name     =((EditText)findViewById(R.id.employ_editTextName)).getText().toString();
        String scName   =((EditText)findViewById(R.id.employ_editTextScName)).getText().toString();
        String icon     =((EditText)findViewById(R.id.employ_icon)).getText().toString();
        String name2    =name.replaceAll(" ","");
        String scName2  =scName.replaceAll(" ","");
        String pw       =((EditText)findViewById(R.id.textEditPW1)).getText().toString();
        String pw2       =((EditText)findViewById(R.id.textEditPW2)).getText().toString();
        int color       =colorList.getColor();

        /*create database entry*/
        TRDBHelper dbHelper=TRDBHelper.instance;
        if(dbHelper==null){
            err="Database wasn't initialized!\n";
            Toast.makeText(this,err,Toast.LENGTH_LONG).show();
            return;
        }

        /*check input values*/
        if(name2.isEmpty() || scName2.isEmpty()){
            err="names must not be empty!!!!!!!!\n";
            Toast.makeText(this,err,Toast.LENGTH_LONG).show();
            return;
        }
        if(pw.isEmpty() || !pw.equals(pw2)){
            err="names must not be empty!!!!!!!!\n";
            Toast.makeText(this,err,Toast.LENGTH_LONG).show();
            return;
        }
        TRDBEmployeeTable table =dbHelper.getEmployeeTable();

        TRDBEmployeeData data = new TRDBEmployeeData(table.getNextUID(),table,null);
        data.k_icon=icon;
        data.k_firstName=name;
        data.k_secondName=scName;
        data.k_color=color;

        if(-1!=table.createData(data)){
            latestCreatedEmployee=data;
        }else{
            latestCreatedEmployee=null;
        }

        TRSafetyPWUnlockDialog.createPasswordFile (this,data,pw);

        /*go back to main activity*/
        setResult(MainActivity.f_request_updateEmployeeList);

        finish();
    }

    public void onBtnCancelClicked(){
        /*go back to main activity*/
        latestCreatedEmployee=null;
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

}
