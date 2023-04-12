package MFLibrary.MFAndroidGUI.MFDialogs;

import android.app.Dialog;
import android.content.Context;
import android.widget.Toast;

import java.io.File;

import MFLibrary.MFAndroidGUI.Abstract.MFAndroidDialog;
import MFLibrary.MFAndroidGUI.Abstract.MFTextInputDialog;
import MFLibrary.MFSafety.MFSafetyCodings;
import MFNetwork.MFData.MFObjects.MFObject;
import michl.timeregistration.R;

/**
 * This dialog shows a text input dialog for creating a password. It will show an repeat password input dialog
 * to ensure wrong signs. A hash of the password will be stored in the application folder after calling writePasswordFile().
 */
public class MFCreatePasswordDialog extends MFAndroidDialog {
    private String
            password="",
            passRepeat="",
            fileName="",
            filePath="";

    private int signCount=3;
    public MFCreatePasswordDialog(Context context, String title,String filePath,String fileName) {
        super(context, title);
        if(fileName==null || fileName.replaceAll(" ","").equals("")){
            this.fileName="passwordFile.pw";
        }else{
            this.fileName=fileName;
        }
        if(filePath==null || filePath.replaceAll(" ","").equals("")){
            this.filePath=MFObject.getBaseDir();
        }else{
            this.filePath=filePath;
        }
    }

    public void setPassRepeat(String repeat){
        passRepeat=repeat;
    }

    public void setPassword(String password){
        this.password=password;
    }

    public void setMinSignCount(int signCount){
        this.signCount=signCount;
    }

    public String getPassword(){
        return password;
    }

    @Override
    /**
     * Shows the first out of two dialogs. The first dialog is asking for the password. The second will ask for pw repeat.
     */
    public void show(){
        File file[] = File.listRoots();
        for(int i=0;i<file.length;i++)
            System.out.println(file[i]);
        File adminFile=new File(MFObject.getBaseDir(),fileName);
        if(!adminFile.exists()){
        }
        MFTextInputDialog adminPWInputDialog=new MFTextInputDialog(
                context,
                title) {
            @Override
            public void onAcceptClicked() {
                String password=editText.getText().toString();
                if(checkPassword(password)) {
                    setPassword(password);
                    showRepeatDialog();
                }
                else{
                    /*Input not valid, show this dialog again!*/
                    MFCreatePasswordDialog.this.show();
                }
            }
        };

        Toast.makeText(context,context.getResources().getString(R.string.admin_pass),Toast.LENGTH_LONG).show();
        Dialog dialog=adminPWInputDialog.getDialog();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        adminPWInputDialog.show();

    }

    public void showRepeatDialog(){
        Toast.makeText(context,context.getResources().getString(R.string.admin_pass),Toast.LENGTH_LONG).show();
        MFTextInputDialog adminPWRepeat=new MFTextInputDialog(
                context,
                context.getResources().getString(R.string.password_repeat)) {
            @Override
            public void onAcceptClicked() {
                setPassRepeat(editText.getText().toString());
                if(checkRepeatPW()) {
                    MFSafetyCodings.createPasswordFile(filePath,fileName,getPassword());
                }else{
                    MFCreatePasswordDialog.this.show();
                }
            }
        };
        adminPWRepeat.getDialog().setCancelable(false);
        adminPWRepeat.getDialog().setCanceledOnTouchOutside(false);
        adminPWRepeat.show();
    }


    public boolean checkPassword(String password){
        if(password.length()<signCount)
            return false;
        return true;
    }

    public boolean checkRepeatPW(){
        if(password.equals(passRepeat)){
            return true;
        }
        return false;
    }
}
