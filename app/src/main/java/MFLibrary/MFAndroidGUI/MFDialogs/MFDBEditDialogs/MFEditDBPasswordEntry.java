package MFLibrary.MFAndroidGUI.MFDialogs.MFDBEditDialogs;

import android.content.Context;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import MFLibrary.MFAndroidGUI.Abstract.MFTextInputDialog;
import MFNetwork.MFDatabase.MFDatabase.MFDBData;
//TODO testing!
public class MFEditDBPasswordEntry extends MFTextInputDialog {
    MFDBData dbData;
    String keyName;

    public MFEditDBPasswordEntry(Context context, String title,String keyName, MFDBData data)  {
        super(context, title);
        m_className="MFEditDBPasswordEntry";
        this.dbData=data;
        this.keyName=keyName;
    }

    @Override
    public void onAcceptClicked() {
        if(dbData==null){
            Toast.makeText(context,"dbData==null",Toast.LENGTH_LONG).show();
        }
        String newValue=createHash(editText.getText().toString());
        if(!dbData.updateDatabase(keyName,newValue)){
            Toast.makeText(context,"Couldnt update database!!!!!",Toast.LENGTH_LONG).show();
        }
    }

    public void setDBData(MFDBData dbData) {
        this.dbData=dbData;
    }

    private String createHash(String password){
        String hash="";
        try {
            MessageDigest digest=MessageDigest.getInstance("SHA-256");
            byte[] values=digest.digest(password.getBytes());
            for(byte iterator :values){
                /*The string will be filled with the values of each byte and not the corresponding char of a specific byte*/
                hash+=Byte.toString(iterator);
            }
        } catch (NoSuchAlgorithmException e) {
            p_errPrint("createHash(String password)"+e.getMessage());
        }

        p_debugPrint("Create hash: \ndata="+password+"\nhash="+hash);
        return hash;
    }
}
