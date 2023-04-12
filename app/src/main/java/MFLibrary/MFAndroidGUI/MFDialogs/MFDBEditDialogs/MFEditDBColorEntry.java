package MFLibrary.MFAndroidGUI.MFDialogs.MFDBEditDialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import MFLibrary.MFAndroidGUI.MFDialogs.MFColorListDialog;
import MFNetwork.MFDatabase.MFDatabase.MFDBData;

public class MFEditDBColorEntry extends MFColorListDialog {

    protected MFDBData dbData;
    protected String keyName="";

    public MFEditDBColorEntry(Context context, String title, int[] colors,String keyName, MFDBData data) {
        super(context, title, colors);
        this.dbData=data;
        this.keyName=keyName;
        btnText=title;
        builder.setAdapter(new MFColorListAdapter(context,colorArray), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onColorClicked(dialog,which);
            }
        });
    }

    public void onColorClicked(DialogInterface dialog, int which){
        //TODO update database entry!
        if(which<colorArray.size())
            colorCode=colorArray.get(which);
        if(colorizeView!=null)
            colorizeView.setBackgroundColor(colorCode);

        if(dbData==null){
            Toast.makeText(context,"dbData==null",Toast.LENGTH_LONG).show();
        }
        String newValue=Integer.toString(colorCode);
        if(!dbData.updateDatabase(keyName,newValue)){
            Toast.makeText(context,"Couldnt update database!!!!!",Toast.LENGTH_LONG).show();
        }
    }

    public void setDBData(MFDBData dbData) {
        this.dbData=dbData;
    }
}
