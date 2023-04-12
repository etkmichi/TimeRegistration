package MFLibrary.MFAndroidGUI.MFDialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import MFLibrary.MFAndroidGUI.MFAndroidGUIObject;
import michl.timeregistration.R;

public class MFFileDialog extends MFAndroidGUIObject {

    Context context;
    AlertDialog.Builder builder;
    MFFileListAdapter filesAdapter;
    ArrayList<File> fileList;
    File[] files;
    public MFFileDialog(Context context,String titel,String startPath){
        this.context=context;
        builder = new AlertDialog.Builder(context);
        builder.setTitle(titel);
        File start=new File(startPath);

        files = context.getExternalCacheDirs();
        fileList=new ArrayList<>();
        fileList.add(start.getParentFile());
        for(int i=0;i<files.length;i++){
            fileList.add(files[i]);
        }
        files = context.getExternalFilesDirs(null);
        for(int i=0;i<files.length;i++){
            fileList.add(files[i]);
        }
        files = context.getExternalMediaDirs();
        for(int i=0;i<files.length;i++){
            fileList.add(files[i]);
        }
        files = context.getObbDirs();
        for(int i=0;i<files.length;i++){
            fileList.add(files[i]);
        }
        filesAdapter=new MFFileListAdapter(context, fileList);
        builder.setAdapter(filesAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onFileClicked(which);
            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    /**
     * Creates the dialog and returns it.
     * @return
     */
    public Dialog getDialog() {
        Dialog dialog=builder.create();
        ((AlertDialog) dialog).getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        return dialog;
    }

    /**
     * Creates the dialog and shows it.
     */
    public void show(){
        int api_level=Build.VERSION.SDK_INT;
        switch (api_level){
            case 21://Android 5.0
                break;

            case 22:
                break;

            case 27:
                break;
            default:
                Dialog dialog=builder.create();

                ((AlertDialog) dialog).getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        onFileClicked(position);
                    }
                });
                dialog.show();
                break;
        }
    }

    public void onFileClicked(int which) {
        if(which==0){
            File start=fileList.get(0);
            if(!start.canRead() || !start.exists()){
                Toast.makeText(context,R.string.no_read_access,Toast.LENGTH_LONG).show();
                return;
            }
            File parent=start.getParentFile();
            fileList.clear();
            if(parent!=null)
                fileList.add(parent);
            try{
                files=start.listFiles();
            }catch (Exception e){
                e.printStackTrace();
            }
            if(files!=null)
                for(int i=0;i<files.length;i++){
                    fileList.add(files[i]);
                }
            filesAdapter.notifyDataSetChanged();
        }else{
            if(fileList.get(which).isDirectory()){
                /*update list*/
            }else{

            }
        }
    }

    public class MFFileListAdapter extends ArrayAdapter<File> {
        public MFFileListAdapter(Context context, ArrayList<File> listObjects){
            super(context, R.layout.row_item,listObjects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            ViewHolder viewHolder;
            if(convertView==null){
                viewHolder=new ViewHolder();
                convertView=new TextView(getContext());

                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)convertView.getTag();
            }
            File file=getItem(position);

            if(file!=null){
                String fileName=getItem(position).toString();
                viewHolder.fileName=fileName;
                viewHolder.file=file;
            }else{
                viewHolder.fileName="--Sth went wrong--";
            }
            ((TextView)convertView).setText("\n#"+viewHolder.fileName+"\n");
            return convertView;
        }

        private class ViewHolder{
            String fileName;
            File file;
        }
    }

}
