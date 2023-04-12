package michl.timeregistration.GUI;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;

import MFLibrary.MFAndroidGUI.MFDialogs.MFSafetyDialogs.MFInterfaceUnlock;
import MFLibrary.MFAndroidGUI.MFDialogs.MFSafetyDialogs.MFSafetyPWUnlockDialog;
import MFLibrary.MFSafety.MFSafetyCodings;
import michl.timeregistration.Database.DBTimeRegData.TRDBEmployeeData;

public class TRSafetyPWUnlockDialog extends MFSafetyPWUnlockDialog {

    /**
     *
     * @param context
     * @param data
     */
    public TRSafetyPWUnlockDialog(Context context, MFInterfaceUnlock unlocker, String password, TRDBEmployeeData data) {
        super(context, unlocker,"Password for "+data.k_secondName+" "+data.k_firstName
                , password,
                context.getApplicationInfo().dataDir+"/users/"+
                        data.getUID()+"/",//data.k_secondName+"_"+data.k_firstName+"_"+ -> if name is changed, folder is wrong!
                data.getUID()+".pw");
    }

    /**
     * Creates a password file and stores the sha256 hash in it.
     * @param context
     * @param data
     * @param password
     * @return
     */
    public static boolean createPasswordFile(Context context,TRDBEmployeeData data,String password){
        String filePath=context.getApplicationInfo().dataDir+"/users/"+
                data.getUID()+"/";
        String fileName=data.getUID()+".pw";
        File pFile=new File(filePath,fileName);
        boolean dirCreated=(new File(filePath)).mkdirs();

        if(!dirCreated){
            p_debugStaticPrint("Couldn't create directory!");
            return false;
        }

        if(!pFile.exists()){
            try {
                pFile.createNewFile();
                FileOutputStream out =new FileOutputStream(pFile,true);
                out.write(MFSafetyCodings.getSha256Hash(password.getBytes()));
            } catch (Exception e) {
                p_debugStaticPrint("MFSafetyPWDialog constructor, cant create password file"+e.getMessage());
                p_debugStaticPrint("MFSafetyPWDialog - writeHashToPWFile(String hash,String title): "+e.getMessage());
                return false;
            }
        }

        return true;
    }
}
