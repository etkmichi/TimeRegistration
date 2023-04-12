package MFLibrary.MFAndroidGUI.MFListViews;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import MFNetwork.MFData.MFObjects.MFObject;

/**
 * This class provides a master slave like list handling. It will wrap around existent objects.
 * It will set the following callbacks which must be implemented with the abstract methods of this class:
 * setOnScrollListener - onScroll(...) -> OnMasterScroll()
 * setOnItemLongClickListener -> onMasterItemLongClicked
 * setOnItemClickListener -> onMasterListItemClicked
 *
 */
public abstract class MFListViewMasterSlave extends MFObject {

    protected ArrayAdapter master;
    protected ArrayAdapter slave;
    protected ListView masterView;
    protected ListView slaveView;
    protected Context context;

    public MFListViewMasterSlave(ArrayAdapter master, ArrayAdapter slave, ListView masterView, ListView slaveView, Context context){
        this.master=master;
        this.slave=slave;
        this.masterView=masterView;
        this.slaveView=slaveView;
        this.context=context;

        if(masterView!=null)initMasterView();
        if(slaveView!=null)initSlaveView();
    }

    private void initMasterView(){
        masterView.setAdapter(master);
        masterView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onMasterListItemClicked(
                        position,view,
                        slaveView
                );
            }
        });

        masterView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                onMasterScroll(view,firstVisibleItem,visibleItemCount,totalItemCount);
            }
        });

        masterView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return onMasterItemLongClicked(parent,view,position,id);
            }
        });
    }

    private void initSlaveView(){
        slaveView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return onSlaveItemLongClicked(parent,view,position,id);
            }
        });
    }
    public Context getContext(){
        return context;
    }
    public abstract void onMasterListItemClicked(int position, View view, ListView listViewSlave);
    public abstract void onMasterScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount);
    public abstract boolean onMasterItemLongClicked(AdapterView<?> parent, View view, int position, long id);

    /**
     * This method will be called, if an item of the slave view was long clicked.
     * @param parent
     * @param view
     * @param position
     * @param id
     * @return
     */
    public abstract boolean onSlaveItemLongClicked(AdapterView<?> parent, View view, int position, long id);
    /*TODO implement database list with methods like
    public abstract void addMasterItem(MFDBData masterItem);*/


}
