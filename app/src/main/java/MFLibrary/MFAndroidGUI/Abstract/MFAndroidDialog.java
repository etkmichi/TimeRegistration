package MFLibrary.MFAndroidGUI.Abstract;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import MFLibrary.MFAndroidGUI.MFAndroidGUIObject;

/**
 * This class provides some functionality for handling android dialogs.
 */
public abstract class MFAndroidDialog extends MFAndroidGUIObject {

    protected String
            btnText,
            title;

    protected Context
            context;

    protected AlertDialog.Builder
            builder;

    protected AlertDialog
            dialog=null;

    public MFAndroidDialog(Context context,String title){
        this.context=context;
        btnText=title;
        this.title=title;
        builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
    }

    public String getButtonText(){
        return btnText;
    }

    /**
     * If this dialog will be opened by a button, this method can be used to change the button name.
     * @param btnText
     */
    public void setButtonText(String btnText){
        this.btnText=btnText;
    }

    public void setTitle(String title){
        builder.setTitle(title);
        this.title=title;
    }

    /**
     * Creates the dialog and returns it.
     * @return
     */
    public Dialog getDialog() {
        if(dialog==null)
            dialog=builder.create();
        return dialog;
    }

    public AlertDialog.Builder getBuilder(){
        return builder;
    }


    protected void beforeCreation(){}

    /**
     * Creates the dialog and shows it.
     */
    public void show(){
        beforeCreation();
        dialog=builder.create();
        builder.show();
    }

    /**
     * Sets a listener which will be called if the dialog gets dismissed/closed.
     * @param dismissListener
     */
    public void setDissmissListener(DialogInterface.OnDismissListener dismissListener){
        builder.setOnDismissListener(dismissListener);
    }
}
