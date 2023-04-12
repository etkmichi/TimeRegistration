package MFLibrary.MFAndroidGUI.MFDialogs.MFDBEditDialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import MFLibrary.MFAndroidGUI.Abstract.MFTwoButtonsDialog;
import MFNetwork.MFDatabase.MFDatabase.MFDBData;
import MFNetwork.MFDatabase.MFDatabase.MFDBTable;

public class MFEditDBDeleteEntry extends MFTwoButtonsDialog {
    protected MFDBData data;
    protected MFDBTable table;
    public MFEditDBDeleteEntry(Context context, String title, String leftButton, String rightButton, MFDBData data) {
        super(context, title, leftButton, rightButton);
        this.data=data;
    }

    @Override
    protected void onLeftButtonClicked(DialogInterface dialog, int which) {
        if(data==null)
            return;
        else
            table=data.getTable();

        if(table==null)
            return;

        if(table.removeData(data)){
            Toast.makeText(context,"Data was removed from database.",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,"Error! Data wasn't removed from database! It could be removed already.",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onRightButtonClicked(DialogInterface dialog, int which) {

    }

    public void setDBData(MFDBData dbData) {
        this.data=dbData;
    }
}
