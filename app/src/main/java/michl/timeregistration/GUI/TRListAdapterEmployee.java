package michl.timeregistration.GUI;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import MFNetwork.MFDatabase.MFDatabase.MFDBData;
import michl.timeregistration.Database.DBTimeRegData.TRDBEmployeeData;
import michl.timeregistration.R;

public class TRListAdapterEmployee extends ArrayAdapter<MFDBData> {

    private ArrayList<MFDBData> listObjects;
    private Context context;
    private int lastPosition=-1;
    private TRDBEmployeeData currentEmployee;
    private ListView employeeEventList;
    public TRListAdapterEmployee(Context context, ArrayList<MFDBData> listObjects, ListView employeeEventList) {
        super(context, R.layout.row_item, listObjects);
        this.context=context;
        this.listObjects=listObjects;
        this.employeeEventList=employeeEventList;
    }

    @Override
    public View getView(int position,View convertView, ViewGroup parent){
        // Get the data item for this position
        TRDBEmployeeData employee = (TRDBEmployeeData)getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.textEmployeeName = (TextView) convertView.findViewById(R.id.textEmployeeName);
            viewHolder.imageEmployee = (ImageView) convertView.findViewById(R.id.imageEmployee);
            viewHolder.layout = (ConstraintLayout) convertView.findViewById(R.id.row_item_employee_background);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        lastPosition = position;
        Icon icon=employee.getIcon();
        viewHolder.textEmployeeName.setLines(2);
        viewHolder.textEmployeeName.setText(employee.k_firstName+"\n"+employee.k_secondName);//
        viewHolder.textEmployeeName.setTextColor(Color.BLACK);
        viewHolder.imageEmployee.setImageIcon(icon);
        viewHolder.layout.setBackgroundColor(employee.k_color);
        // Return the completed view to render on screen
        return convertView;
    }

    public void addEmployee(TRDBEmployeeData employeeData){
        listObjects.add(employeeData);
        notifyDataSetChanged();
    }

    public TRDBEmployeeData getCurrentEmployee(){
        return currentEmployee;
    }

    private static class ViewHolder{
        TextView textEmployeeName;
        ImageView imageEmployee;
        ConstraintLayout layout;
    }
}
