package michl.timeregistration.Activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import MFLibrary.MFAndroidGUI.MFDialogs.MFSafetyDialogs.MFInterfaceUnlock;
import MFLibrary.MFAndroidGUI.MFDialogs.MFSafetyDialogs.MFSafetyPWUnlockDialog;
import michl.RequestCodes;
import michl.timeregistration.Database.TRDBHelper;
import michl.timeregistration.R;

public class TRActivityExport extends AppCompatActivity implements MFInterfaceUnlock {

    public final static int
        f_type_sqlite=0;

    private int
        type=f_type_sqlite;

    private Uri
        exportUri;

    private boolean isUnlocked=false;

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

    public void setButtonsEnabled(boolean disabled){
        Button button;

        button = findViewById(R.id.btnExportPath);
        button.setEnabled(disabled);

        button = findViewById(R.id.btnSettingsChooseDB);
        button.setEnabled(disabled);

        button = findViewById(R.id.btnSettingsPW);
        button.setEnabled(disabled);
    }

    private void initOnClickListeners(){
        Button button;
        button = findViewById(R.id.btnExportPath);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnExportPathClicked();
            }
        });
        button=findViewById(R.id.btnExport);
        button.setEnabled(false);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBtnExportClicked();

            }
        });
    }

    /**
     * Opens the file manager to search for a path.
     */
    public void onBtnExportPathClicked(){
        Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        startActivityForResult(Intent.createChooser(i, "Choose directory"), RequestCodes.f_result_path);
    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Button button;
        button=findViewById(R.id.btnExport);
        switch(requestCode) {
            case RequestCodes.f_result_path:
                if(data!=null) {
                    exportUri=data.getData();
                    TextView text = (TextView) findViewById(R.id.textExportPath);
                    Log.i("Test", "Result URI " + exportUri.getPath());
                    button.setEnabled(true);
                }else{
                    button.setEnabled(false);
                }
                break;
            default:
                button.setEnabled(false);
                super.onActivityResult(requestCode,resultCode,data);
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

    public void onBtnExportClicked(){
        switch (type){
            case f_type_sqlite:
                if(TRDBHelper.instance.export(exportUri,TRDBHelper.instance.getDBName(),this)){
                    Toast.makeText(this,getApplicationContext().getText(R.string.export_success),Toast.LENGTH_SHORT);
                }
                break;
        }
        Button button;
        button=findViewById(R.id.btnExport);
        button.setEnabled(false);
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null,
                null);
        if (cursor != null) {
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return null;
    }

    @Override
    public void unlocked() {
        setContentView(R.layout.activity_export);
        initOnClickListeners();
    }

    @Override
    public void locked() {
        Toast.makeText(this,getResources().getString(R.string.admin_wrong_pw),Toast.LENGTH_SHORT).show();
        finish();
    }
}
