package michl.timeregistration.Activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import MFLibrary.MFAndroidGUI.MFDialogs.MFSafetyDialogs.MFInterfaceUnlock;
import MFLibrary.MFAndroidGUI.MFDialogs.MFSafetyDialogs.MFSafetyPWUnlockDialog;
import michl.timeregistration.R;

public class TRActivitySettings extends AppCompatActivity implements MFInterfaceUnlock {
    protected int REQUEST_DB_PATH=0xCAFE1C01;
    protected String TAG="CAFEBABE";
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
    private void initOnClickListeners(){

    }

    public void onBtnDBPathClicked(){
        Intent openDoc=new Intent(Intent.ACTION_OPEN_DOCUMENT);
        //openDoc.setType(");
        startActivityForResult(openDoc,REQUEST_DB_PATH);
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent resultData){
        if(requestCode==REQUEST_DB_PATH && resultCode == Activity.RESULT_OK){
            Uri path=null;
            if(resultData!=null){
                path=resultData.getData();
                Log.i(TAG,"Uri; "+path.toString());
            }
        }
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

    @Override
    public void unlocked() {
        setContentView(R.layout.activity_settings);
        initOnClickListeners();
    }

    @Override
    public void locked() {
        Toast.makeText(this,getResources().getString(R.string.admin_wrong_pw),Toast.LENGTH_SHORT).show();
        finish();
    }
}
