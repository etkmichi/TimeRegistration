package MFLibrary.MFAndroidGUI.MFDialogs.MFDBEditDialogs;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import MFLibrary.MFAndroidGUI.Abstract.MFAndroidDialog;
import MFNetwork.MFDatabase.MFDatabase.MFDBTable;
import michl.MFAdapters.MFStringListAdapter;

/**
 * This dialog will show sub dialogs in a list. The sub dialogs can be chosen/clicked to edit an entry of a specific table.
 *
 */
public class MFEditDBEntryComposition extends MFAndroidDialog {
    private MFDBTable table;
    private ArrayAdapter listAdapter;
    private ArrayList<String> keyTypes;
    public MFEditDBEntryComposition(Context context, String title, MFDBTable table) {
        super(context, title);
        this.table=table;
        keyTypes=new ArrayList<>();
        listAdapter=new MFStringListAdapter(context,keyTypes);
    }
}
