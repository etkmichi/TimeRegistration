package michl.timeregistration.TRTasks;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import MFNetwork.MFThreadSystem.MFTasks.MFTask;
import michl.timeregistration.Activities.MainActivity;
import michl.timeregistration.Database.TRDBHelper;

public class TRTaskInitDB extends MFTask {
    final public int DB_READY=1,DB_FAILED=2;

    private static MainActivity mainActivity;
    TRDBHelper databaseHelper;
    private static Handler handler=null;

    /**
     *
     * @param mainActivity if not null, it will be used to set a static field of this class. If mainActivity was set already by a
     *                     constructor call, it is possible to create this task with null. During execution this task will
     *                     use the already set {@link MainActivity}
     * @param databaseHelper
     */
    public TRTaskInitDB(MainActivity mainActivity,TRDBHelper databaseHelper){
        if(mainActivity!=null)
            this.mainActivity=mainActivity;
        mainActivity.enableDBRelatedButtons(false);
        this.databaseHelper=databaseHelper;
        if(handler==null)handler=new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg){
                switch (msg.what){
                    case DB_READY:
                        Toast.makeText(mainActivity,"database was loaded!", Toast.LENGTH_LONG).show();
                        mainActivity.setDatabaseHelper(databaseHelper);
                        mainActivity.initLists();
                        mainActivity.enableDBRelatedButtons(true);
                        break;
                    case DB_FAILED:
                        Toast.makeText(mainActivity,"Couldnt load database!", Toast.LENGTH_LONG).show();
                        mainActivity.enableDBRelatedButtons(false);
                        break;
                }
            }
        };
    }
    @Override
    public boolean doWork() {
        boolean ret=true;
        ret&=databaseHelper.createDB();
        ret&=databaseHelper.loadData();
        if(ret){
            handler.obtainMessage(DB_READY).sendToTarget();
        }
        else
            handler.obtainMessage(DB_FAILED).sendToTarget();
        return true;
    }

    @Override
    protected boolean undoWork() {
        return false;
    }
}
