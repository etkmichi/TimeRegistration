package michl.timeregistration.Database.DBTimeRegTables;

import java.sql.ResultSet;
import java.sql.SQLException;

import MFNetwork.MFDatabase.MFDatabase.MFDBData;
import MFNetwork.MFDatabase.MFDatabase.MFDBTable;
import MFNetwork.MFDatabase.MFDatabase.MFDatabaseHelper;
import MFNetwork.MFDatabase.MFDatabase.MFSQLFinals;
import michl.timeregistration.Database.DBTimeRegData.TRDBAbsenceData;


public class TRDBAbsenceTable extends MFDBTable {
    /**
     * Sets the default parameters.
     * <p><strong>
     * Subclass should implement following in its constructor (with its own design):<p><code>
     * super(helper,"Employee","EmployeeID");
     * m_className="TRDBEmployee";//for debug use
     * addKey("EmployeeID",MFSQLFinals.KEY_TYPE_PRIMARY_INT);//add a primary key
     * addKey("FirstName",MFSQLFinals.KEY_TYPE_TEXT);//add other key
     * addKey("SecondName",MFSQLFinals.KEY_TYPE_TEXT);//add other key
     * addKey("Icon",MFSQLFinals.KEY_TYPE_TEXT);//add other key
     * addKey("Color",MFSQLFinals.KEY_TYPE_PRIMARY_INT);//add other key
     * genCommaSeperatedKeys();//generate a string for database access
     * </code><p></strong>
     *
     * @param dbHelper       Subclass of MFDatabaseHelper. Will be used to read/write to a db-file
     * @param tableName      String for table name which will be created in the database file.
     * @param primaryKeyName name of the primary key in the table.
     */
    public TRDBAbsenceTable(MFDatabaseHelper dbHelper) {
        super(dbHelper, "Absence", "AbsenceID");
        m_className="TRDBAbsenceTable";
        addKey("AbsenceID",MFSQLFinals.KEY_TYPE_PRIMARY_INT);
        addKey("EmployeeID",MFSQLFinals.KEY_TYPE_INT);
        addKey("EventID",MFSQLFinals.KEY_TYPE_INT);
        addKey("Reason",MFSQLFinals.KEY_TYPE_TEXT);
        addKey("Attestation",MFSQLFinals.KEY_TYPE_TEXT);
        addKey("Color",MFSQLFinals.KEY_TYPE_INT);
        genCommaSeperatedKeys();
    }

    @Override
    public MFDBData readRowData(ResultSet result) throws SQLException {
        TRDBAbsenceData absence=new TRDBAbsenceData(-1,this);
        absence.k_absenceID=result.getInt("AbsenceID");
        absence.k_employeeID=result.getInt("EmployeeID");
        absence.k_eventID=result.getInt("EventID");
        absence.k_reason=result.getString("Reason");
        absence.k_reason=result.getString("Attestation");
        absence.k_color=result.getInt("Color");
        absence.updateData();
        return absence;
    }

    @Override
    public void createReferencesCommands() {

    }
}
