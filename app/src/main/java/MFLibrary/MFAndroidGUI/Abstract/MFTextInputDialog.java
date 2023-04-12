package MFLibrary.MFAndroidGUI.Abstract;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

public abstract class MFTextInputDialog extends MFAndroidDialog {
    protected EditText editText;
    public MFTextInputDialog(Context context, String title) {
        super(context, title);
    }

    public abstract void onAcceptClicked();

    @Override
    protected void beforeCreation() {
        editText=new EditText(context);
        builder.setView(editText);
        builder.setTitle(title);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onAcceptClicked();
            }
        });
    }

    public EditText getEditText(){
        return editText;
    }

    public void setOnAcceptClickedListener(DialogInterface.OnClickListener listener){
        builder.setPositiveButton("Ok",listener);
    }
}
