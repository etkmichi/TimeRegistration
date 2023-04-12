package MFLibrary.MFAndroidGUI.MFDialogs.MFDBEditDialogs;

import android.content.Context;
import android.widget.Toast;

import MFLibrary.MFAndroidGUI.Abstract.MFTextInputDialog;
import MFNetwork.MFDatabase.MFDatabase.MFDBData;

public class MFEditDBTextEntry extends MFTextInputDialog {
    MFDBData dbData;
    String keyName;
    public MFEditDBTextEntry(Context context, String title,String keyName, MFDBData data) {
        super(context, title);
        this.dbData=data;
        this.keyName=keyName;
    }

    public void onAcceptClicked(){
        if(dbData==null){
            Toast.makeText(context,"dbData==null",Toast.LENGTH_LONG).show();
        }
        String newValue=editText.getText().toString();
        if(!dbData.updateDatabase(keyName,newValue)){
            Toast.makeText(context,"Couldnt update database!!!!!",Toast.LENGTH_LONG).show();
        }
    }
    public void setDBData(MFDBData dbData) {
        this.dbData=dbData;
    }



}
