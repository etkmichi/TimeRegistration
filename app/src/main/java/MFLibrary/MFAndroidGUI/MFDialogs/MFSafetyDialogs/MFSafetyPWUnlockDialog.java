package MFLibrary.MFAndroidGUI.MFDialogs.MFSafetyDialogs;

import android.content.Context;

import java.io.File;

import MFLibrary.MFAndroidGUI.Abstract.MFTextInputDialog;
import MFLibrary.MFAndroidGUI.MFDialogs.MFCreatePasswordDialog;
import MFLibrary.MFSafety.MFSafetyCodings;
import MFNetwork.MFData.MFObjects.MFObject;
import michl.timeregistration.R;
//TODO read system values like vendor id or uid of some devices, to create another hash which should be system unique. IF
//the system unique values will be mixed with the input value, the resulting hash will be dependend
//on the system and it will be more safe! -> Protection against overwriting the local pw file...


public class MFSafetyPWUnlockDialog extends MFTextInputDialog {
    private String
            pwFilePath,
            passwordInput="",
            storedPassword="",
            storedHash="",
            pwFileName;

    private File
            pwFile;

    private boolean unlocked=false;
    private MFInterfaceUnlock unlocker;
    /**
     * This constructor opens the given file which should contain the password hash. If the given file doesn't exist,
     * a {@link MFCreatePasswordDialog} will be created and shown.
     * @param context
     * @param title
     * @param password
     * @param pwFilePath
     * @param pwFileName
     */
    public MFSafetyPWUnlockDialog(Context context,MFInterfaceUnlock unlocker, String title, String password,String pwFilePath,String pwFileName) {
        super(context, title);
        if(pwFileName==null)
            this.pwFileName="passwords";
        else
            this.pwFileName=pwFileName;

        if(pwFilePath==null)
            this.pwFilePath=MFObject.getBaseDir();
        else
            this.pwFilePath=pwFilePath;

        if(!this.pwFilePath.endsWith("/"))
            this.pwFilePath+="/";

        this.unlocker=unlocker;
        this.passwordInput=password;

        /* File doesn't exist, ask for pw! */
        if(!checkForFile(this.pwFilePath,this.pwFileName)){
            MFCreatePasswordDialog pwGenDialog=
                    new MFCreatePasswordDialog(context,title+" "+context.getResources().getString(R.string.password_create),
                            this.pwFilePath,this.pwFileName);
            pwGenDialog.show();
        }
    }

    /**
     * Can be used to change the unlocker during runtime.
     * @param unlocker
     */
    public void setUnlocker(MFInterfaceUnlock unlocker){
        this.unlocker=unlocker;
    }

    @Override
    public void onAcceptClicked() {
        if(unlocker==null)
        {
            p_releasePrint("Cant unlock, no unlockable object/interface set!");
            return;
        }
        if(checkInput()){
            unlocker.unlocked();
        }else{
            unlocker.locked();
        }
    }

    /**
     * Checks if the password of the text edit in the dialog matches with the stored hash.
     * @return true if the hash of the input matches the stored hash.
     */
    public boolean checkInput(){
        String pwInput=getEditText().getText().toString();
        byte[] hash=MFSafetyCodings.getSha256Hash(pwInput.getBytes());
        return MFSafetyCodings.checkFileForHash(new File(pwFilePath,pwFileName),hash);
    }

    public boolean checkForFile(String path,String name){
        File pwFile=new File(path,name);
        if(pwFile.exists())
            return true;
        else
            return false;
    }

}
