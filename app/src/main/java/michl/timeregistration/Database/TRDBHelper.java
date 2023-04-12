package michl.timeregistration.Database;

import android.content.Context;
import android.net.Uri;
import android.support.v4.provider.DocumentFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import MFNetwork.MFDatabase.MFDatabase.MFDatabaseHelper;
import michl.timeregistration.Activities.MainActivity;
import michl.timeregistration.Database.DBTimeRegTables.TRDBAbsenceTable;
import michl.timeregistration.Database.DBTimeRegTables.TRDBAttendanceTable;
import michl.timeregistration.Database.DBTimeRegTables.TRDBEmployeeTable;
import michl.timeregistration.Database.DBTimeRegTables.TRDBTimeDateEventTable;

public class TRDBHelper extends MFDatabaseHelper {

    private TRDBEmployeeTable employeeTable;
    private TRDBAttendanceTable attendanceTable=new TRDBAttendanceTable(this);
    private TRDBAbsenceTable absenceTable=new TRDBAbsenceTable(this);
    private TRDBTimeDateEventTable eventTable=new TRDBTimeDateEventTable(this);
    private MainActivity context;
    public static TRDBHelper instance=null;
    public TRDBHelper(String dbDir, String dbName, MainActivity context) {
        super(dbDir, dbName);
        if(context!=null)
            this.context=context;
        employeeTable=new TRDBEmployeeTable(this,this.context);
        className="TRDBHelper";
        connectDB();
        instance=this;
    }

    public TRDBEmployeeTable getEmployeeTable(){
        return employeeTable;
    }

    public TRDBAttendanceTable getAttendanceTable(){
        return attendanceTable;
    }

    public TRDBAbsenceTable getAbsenceTable(){
        return absenceTable;
    }

    public TRDBTimeDateEventTable getEventTable(){
        return eventTable;
    }

    public boolean loadData(){
        boolean ret=true;
        ret&=employeeTable.loadFromDatabase();
        ret&=attendanceTable.loadFromDatabase();
        ret&=absenceTable.loadFromDatabase();
        ret&=eventTable.loadFromDatabase();
        return ret;
    }

    public boolean createDB(){
        employeeTable.createTable();
        attendanceTable.createTable();
        absenceTable.createTable();
        eventTable.createTable();
        return this.executeCommand();
    }

    public boolean export(String dir,String fileName){
        boolean ret=true;
        String dbfilename = m_dbDir+m_dbName;
        File dbfile = new File(dbfilename);
        if(!dir.endsWith("/"));
            dir+="/";
        String backupfilename = dir+fileName;
        File export = new File(backupfilename);
        try {
            if(!export.exists())
                export.createNewFile();
            FileInputStream fis = new FileInputStream(dbfile);
            OutputStream backup = new FileOutputStream(export);

            byte[] buffer = new byte[4096];
            int length;
            while((length = fis.read(buffer)) > 0) {
                backup.write(buffer, 0, length);
            }
            backup.flush();
            backup.close();
            fis.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public boolean export(Uri exportUri, String fileName, Context context){
        boolean ret=true;
        if(exportUri==null)
            return false;
        String dbFileName = m_dbDir+m_dbName;
        File dbFile = new File(dbFileName);
        DocumentFile docFile=DocumentFile.fromTreeUri(context,exportUri);
        DocumentFile output;

        try {
            output = docFile.createFile("*/*",fileName);
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
            return false;
        }
        if(!output.isFile() || !output.canWrite())
            return ret;
        try {
            FileInputStream fis = new FileInputStream(dbFile);

            OutputStream backup = context.getContentResolver().openOutputStream(output.getUri());

            byte[] buffer = new byte[4096];
            int length;
            while( (length = fis.read(buffer)) > 0) {
                backup.write(buffer, 0, length);
            }
            backup.flush();
            backup.close();
            fis.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

}
