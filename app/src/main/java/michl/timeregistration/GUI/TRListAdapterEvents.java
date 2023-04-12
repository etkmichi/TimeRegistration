package michl.timeregistration.GUI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import michl.MFConvesion.MFTimeConverter;
import michl.timeregistration.Database.DBTimeRegData.TRDBAbsenceData;
import michl.timeregistration.Database.DBTimeRegData.TRDBAttendanceData;
import michl.timeregistration.Database.DBTimeRegData.TRDBTimeDateEventData;
import michl.timeregistration.Database.TRDBHelper;
import michl.timeregistration.R;

public class TRListAdapterEvents extends ArrayAdapter<TRDBTimeDateEventData> {
    private Context context;
    private View.OnClickListener deleteOnClickListener;
    private ArrayList<TRDBTimeDateEventData> list;
    public TRListAdapterEvents(Context context, ArrayList<TRDBTimeDateEventData> listObjects) {
        super(context, R.layout.time_view_row_item, listObjects);
        deleteOnClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnDeleteClicked(v);
            }
        };
        this.list=listObjects;
        this.context=context;
    }

    @Override
    public View getView(int position,View convertView, ViewGroup parent){

        if(list.size()<=position){/*sth s wrong use the dummy*/
            Toast.makeText(context,"position out of bounce in time event list items",Toast.LENGTH_LONG).show();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView=inflater.inflate(R.layout.row_item, parent, false);
            return convertView;
        }
        TRDBTimeDateEventData event = (TRDBTimeDateEventData)getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        TRListAdapterEvents.ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {
            viewHolder = new TRListAdapterEvents.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.time_view_row_item, parent, false);
            viewHolder.date = (TextView) convertView.findViewById(R.id.time_item_date);
            viewHolder.start = (TextView) convertView.findViewById(R.id.time_item_start);
            viewHolder.stop = (TextView) convertView.findViewById(R.id.time_item_stop);
            viewHolder.pause = (TextView) convertView.findViewById(R.id.time_item_pause);
            viewHolder.delete= (ImageView) convertView.findViewById(R.id.time_item_delete);
            viewHolder.delete.setOnClickListener(deleteOnClickListener);
            /*set the user data pointer (setTag(...))*/
        } else {
            /*get the already set user pointer*/
            viewHolder = (TRListAdapterEvents.ViewHolder) convertView.getTag();
        }


        if(event==null){
            if(list.size()-1>=position)
                list.remove(position);
            return convertView;
        }
        if(viewHolder==null)
            return convertView;
        convertView.setTag(viewHolder);
        viewHolder.delete.setTag(event);

        viewHolder.date.setText
                (context.getResources().getString(R.string.list_btn_date)
                        +MFTimeConverter.fromIntYYYYMMDDToStringYYMMDD(event.k_YYYYMMDD));//context.getResources().getString(R.string.list_btn_date)+"\n"+
        viewHolder.date.setBackgroundColor(context.getResources().getColor(R.color.invisible_dark,null));

        viewHolder.start.setText(context.getResources().getString(R.string.list_btn_start)+
                MFTimeConverter.fromLongYYYYMMDDHHMMToHHMMString(event.k_start));//+"\n"+
        viewHolder.start.setBackgroundColor(context.getResources().getColor(R.color.invisible_dark,null));

        viewHolder.stop.setText(context.getResources().getString(R.string.list_btn_stop)+MFTimeConverter.fromLongYYYYMMDDHHMMToHHMMString(event.k_stop));//context.getResources().getString(R.string.list_btn_stop)+"\nertdfg"+
        viewHolder.stop.setBackgroundColor(context.getResources().getColor(R.color.invisible_dark,null));

        viewHolder.pause.setText(context.getResources().getString(R.string.list_btn_pause)+MFTimeConverter.fromIntHHMMToString(event.k_pauseSpan));//context.getResources().getString(R.string.list_btn_pause)+"\nfg"+
        viewHolder.pause.setBackgroundColor(context.getResources().getColor(R.color.invisible_dark,null));
        return convertView;
    }

    private static class ViewHolder{
        TextView date,start,stop,pause;
        ImageView delete;
        TRDBTimeDateEventData event;
        TRDBAttendanceData attendance;
        TRDBAbsenceData absence;
    }

    private void onBtnDeleteClicked(View view){
        TRDBTimeDateEventData currentEvent=(TRDBTimeDateEventData)view.getTag();
        if(currentEvent==null){
            Toast.makeText(context,"Current item not set, cant find entry to delete!",Toast.LENGTH_LONG).show();
            return;
        }

        TRDBHelper helper=(TRDBHelper)currentEvent.getTable().getDBHelper();
        helper.deleteRow(helper.getAttendanceTable().getTableName(),"EventID",Integer.toString(currentEvent.k_eventID));
        helper.deleteRow(helper.getAbsenceTable().getTableName(),"EventID",Integer.toString(currentEvent.k_eventID));
        helper.deleteRow(
                currentEvent.getTable().getTableName(),
                currentEvent.getTable().getPrimaryKeyName(),
                Integer.toString(currentEvent.k_eventID));
        super.remove(currentEvent);
        this.notifyDataSetChanged();

//        Toast.makeText(context, currentEvent.getTable().getTableName()+" "+
//                currentEvent.getTable().getPrimaryKeyName()+" "+
//                Integer.toString(currentEvent.k_eventID),Toast.LENGTH_LONG).show();

    }
}
