package MFLibrary.MFAndroidGUI.MFDialogs;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;

import MFLibrary.MFAndroidGUI.MFAndroidGUIObject;
import michl.MFConvesion.MFTimeConverter;

/**
 * This class can be used to wrap around an existent button in an android activity.
 * It will create different listeners and dialogs for this button:
 * - {@link android.view.View.OnClickListener} -> Opens {@link android.app.DatePickerDialog}
 * - {@link android.view.View.OnLongClickListener} -> Sets the date to today
 */
public class MFDatePickerButton extends MFAndroidGUIObject {
    private static DatePickerDialog.OnDateSetListener
            dateSetListener;
    private static DatePickerDialog
            datePicker;
    private Context context;

    private Calendar currentDate;

    private static int
            currentYYYYMMDD=20190101;

    boolean isShowingDate=false;
    private Button button;
    private String buttonText;

    public MFDatePickerButton(Button button, Context context){
        this.context=context;
        currentDate=Calendar.getInstance();
        currentDate.set(Calendar.SECOND,0);
        currentDate.set(Calendar.MILLISECOND,0);
        this.button=button;
        buttonText=""+button.getText();
        dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                MFDatePickerButton.this.onDateSet(view,year,month,dayOfMonth);
            }
        };
        datePicker=new DatePickerDialog(context,dateSetListener,
                currentDate.get(Calendar.YEAR),
                (currentDate.get(Calendar.MONTH)+1),
                currentDate.get(Calendar.DAY_OF_MONTH));

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBtnClicked();
            }
        });
        button.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v){
                onBtnLongClicked();
                return true;
            }
        });
    }

    private void onBtnClicked() {
        if(currentDate!=null)
            datePicker.updateDate(
                    currentDate.get(Calendar.YEAR),
                    currentDate.get(Calendar.MONTH),
                    currentDate.get(Calendar.DAY_OF_MONTH));
        datePicker.show();
    }

    private void onBtnLongClicked() {
        currentDate=Calendar.getInstance();
        currentDate.set(Calendar.SECOND,0);
        currentDate.set(Calendar.MILLISECOND,0);
        currentYYYYMMDD=
                currentDate.get(Calendar.YEAR)*10000+
                (currentDate.get(Calendar.MONTH)+1)*100+
                currentDate.get(Calendar.DAY_OF_MONTH);

        if(isShowingDate)
            button.setText(buttonText+MFTimeConverter.fromIntYYYYMMDDToString(currentYYYYMMDD));

        if(currentDate!=null)
            datePicker.updateDate(
                    currentDate.get(Calendar.YEAR),
                    currentDate.get(Calendar.MONTH),
                    currentDate.get(Calendar.DAY_OF_MONTH));
    }


    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
        currentDate.set(Calendar.YEAR,year);
        currentDate.set(Calendar.MONTH,month);
        currentDate.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        currentYYYYMMDD=(year*10000+(month+1)*100+dayOfMonth);
        if(isShowingDate)
            button.setText(buttonText+MFTimeConverter.fromIntYYYYMMDDToString(currentYYYYMMDD));
    }


    /**
     * Sets the date for the dialog
     * @param currentDate
     */
    public void setCurrentDate(Calendar currentDate){
        this.currentDate=currentDate;
        datePicker.updateDate(
                currentDate.get(Calendar.YEAR),
                (currentDate.get(Calendar.MONTH)+1),
                currentDate.get(Calendar.DAY_OF_MONTH));
    }

    public void setButtonShowDateText(boolean showDate){
        isShowingDate=showDate;
    }

    public Calendar getDate(){
        return currentDate;
    }

}
