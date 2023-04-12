package MFLibrary.MFAndroidGUI.MFDialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import MFLibrary.MFAndroidGUI.Abstract.MFAndroidDialog;

//TODO long click on a color -> automatic creation of different shades of the color and update list with the created shades
/**
 * Creates an AlertDialog with colors given by the array in the constructor. If colors array==null it
 * will set some hardcoded colors.
 */
public class MFColorListDialog extends MFAndroidDialog {

    protected ArrayList<Integer>
            colorArray;

    protected int
            colorCode=Color.TRANSPARENT;

    protected View
            colorizeView;

    public MFColorListDialog(Context context, String title, int[] colors){
        super(context,title);

        colorArray=new ArrayList<>();
        if(colors==null){
            colorArray.add(0xFFFF0000);
            colorArray.add(0xFF550000);
            colorArray.add(0xFFFF5555);
            colorArray.add(0xFF00FF00);
            colorArray.add(0xFF005500);
            colorArray.add(0xFFAAFFAA);
            colorArray.add(0xFF0000FF);
            colorArray.add(0xFF000055);
            colorArray.add(0xFF5555FF);
            colorArray.add(0xFFFFFF00);
            colorArray.add(0xFF888800);
            colorArray.add(0xFFFFFFAA);
            colorArray.add(0xFFAAFF00);
            colorArray.add(0xFFFF5F00);
            colorArray.add(0xFFAA11FF);
            colorArray.add(0xFFAA88FF);
            colorArray.add(0xFFFF22AA);
            colorArray.add(0xFF00FFFF);
            colorArray.add(0xFF999999);
            colorArray.add(0xFF555555);
            colorArray.add(0xFFCCCCCC);
        }else{
            for(int i=0;i<colors.length;i++){
                colorArray.add(colors[i]);
            }
        }

        builder.setAdapter(new MFColorListAdapter(context,colorArray), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onColorClicked(dialog, which);
            }
        });
    }

    public void onColorClicked(DialogInterface dialog,int which){
        if(which<colorArray.size())
            colorCode=colorArray.get(which);
        if(colorizeView!=null)
            colorizeView.setBackgroundColor(colorCode);
    }

    public int getColor(){
        return colorCode;
    }


    /**
     * The view given as parameter will be edited (setBackground(Color)) when the dialog closes.
     * @param v
     */
    public void setColorizedView(View v){
        colorizeView=v;
    }

    protected class MFColorListAdapter extends ArrayAdapter<Integer>{
        public MFColorListAdapter(Context context, ArrayList<Integer> listObjects){
            super(context,-1,listObjects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            ViewHolder viewHolder;
            Integer intVal=getItem(position);
            if(convertView==null){
                viewHolder=new ViewHolder();
                convertView=new TextView(context);
                if(intVal!=null)
                    viewHolder.color=Color.valueOf(intVal);
                else
                    viewHolder.color=Color.valueOf(Color.LTGRAY);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)convertView.getTag();
            }
            if(intVal==null || viewHolder==null)
                return convertView;
            convertView.setBackgroundColor(intVal);
            ((TextView)convertView).setText("\n#"+Integer.toHexString(intVal)+"\n");
            return convertView;
        }
    }

    protected static class ViewHolder{
        Color color;
    }
}
