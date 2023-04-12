package MFLibrary.MFAndroidGUI.Abstract;

import android.content.Context;
import android.content.DialogInterface;

public abstract class MFTwoButtonsDialog extends MFAndroidDialog {
    public MFTwoButtonsDialog(Context context, String title, String leftButton, String rightButton) {
        super(context, title);
        builder.setPositiveButton(leftButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onLeftButtonClicked( dialog,  which);
            }
        });
        builder.setNegativeButton(rightButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onRightButtonClicked( dialog,  which);
            }
        });
    }

    protected abstract void onLeftButtonClicked(DialogInterface dialog, int which);
    protected abstract void onRightButtonClicked(DialogInterface dialog, int which);
}
