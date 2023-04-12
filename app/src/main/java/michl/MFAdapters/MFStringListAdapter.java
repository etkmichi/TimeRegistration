package michl.MFAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * String adapter for lacy programmers.
 */
public class MFStringListAdapter extends ArrayAdapter<String>{
    protected ArrayList<String> listStrings=new ArrayList<>();
    protected Context context;
    public MFStringListAdapter(@NonNull Context context,ArrayList<String> listStrings) {
        super(context,-1,listStrings);
        this.context=context;
    }

    public int addString(String text){
        listStrings.add(text);
        return  listStrings.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(listStrings.size()>getCount()){
            clear();
            addAll(listStrings);
        }
        MFStringListAdapter.ViewHolder viewHolder;
        String val=getItem(position);
        if(convertView==null){
            viewHolder=new MFStringListAdapter.ViewHolder();
            convertView=new TextView(context);
            if(val!=null)
                viewHolder.text=val;
            else
                viewHolder.text="-No valid entry-";
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (MFStringListAdapter.ViewHolder)convertView.getTag();
        }
        if(val==null || viewHolder==null)
            return convertView;
        ((TextView)convertView).setText("\n#"+val+"\n");
        return convertView;
    }

    private static class ViewHolder{
        String text;
    }
}
