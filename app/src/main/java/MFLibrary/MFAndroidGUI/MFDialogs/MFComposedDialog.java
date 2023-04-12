package MFLibrary.MFAndroidGUI.MFDialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.Semaphore;

import MFLibrary.MFAndroidGUI.Abstract.MFAndroidDialog;
import michl.MFAdapters.MFStringListAdapter;

public class MFComposedDialog extends MFAndroidDialog {

    private Semaphore
            lockVecDialog;

    private Vector<MFAndroidDialog>
            vecDialogs;

    private ArrayList<String>
            vecDialogStrings;

    public MFComposedDialog(Context context, String title) {
        super(context, title);
        lockVecDialog=new Semaphore(1);
        lock(lockVecDialog);
        vecDialogs=new Vector<>();
        vecDialogStrings =new ArrayList<>();
        release(lockVecDialog);
    }

    /**
     * Adds a {@link MFColorListDialog} as sub dialog and returns its index.
     * @param name
     */
    public int addColorDialog(String name){
        MFColorListDialog colorDialog =new MFColorListDialog(context,name,null);
        lockVecDialog=new Semaphore(1);
        lock(lockVecDialog);
        vecDialogStrings.add(name);
        vecDialogs.add(colorDialog);
        int size=vecDialogs.size();
        release(lockVecDialog);
        return  size;
    }

    public MFAndroidDialog getDialog(int index){
        return vecDialogs.get(index);
    }

    @Override
    public Dialog getDialog(){
        lock(lockVecDialog);
        builder.setAdapter(new MFStringListAdapter(context, vecDialogStrings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onDialogClicked(dialog,which);
            }
        });
        AlertDialog alertDialog=builder.create();
        release(lockVecDialog);
        return alertDialog;
    }

    @Override
    protected void beforeCreation(){
        lock(lockVecDialog);
        builder.setAdapter(new MFStringListAdapter(context, vecDialogStrings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onDialogClicked(dialog,which);
            }
        });
        release(lockVecDialog);
    }

    public void onDialogClicked(DialogInterface dialog,int which){
        lock(lockVecDialog);
        if(which<0 || which>vecDialogs.size()){
            Toast.makeText(context,"No valid entry chosen!",Toast.LENGTH_SHORT);
            return;
        }
        getDialog(which).show();
        release(lockVecDialog);
    }

    /**
     * Adds the dialog to the list adapter and returns its position.
     * @param dialog
     * @return
     */
    public int addDialog(MFAndroidDialog dialog) {
        lock(lockVecDialog);
        vecDialogStrings.add(dialog.getButtonText());
        vecDialogs.add(dialog);
        int size=vecDialogs.size();
        release(lockVecDialog);

        return  size;
    }

    /**
     * Clears all added dialogs.
     */
    public void clearDialogs(){
        lock(lockVecDialog);
        vecDialogs.clear();
        vecDialogStrings.clear();
        release(lockVecDialog);
    }
}
