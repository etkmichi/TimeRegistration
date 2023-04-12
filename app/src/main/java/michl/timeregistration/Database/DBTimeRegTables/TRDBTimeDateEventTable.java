package michl.timeregistration.Database.DBTimeRegTables;

import java.sql.ResultSet;
import java.sql.SQLException;

import MFNetwork.MFDatabase.MFDatabase.MFDBData;
import MFNetwork.MFDatabase.MFDatabase.MFDBTable;
import MFNetwork.MFDatabase.MFDatabase.MFDatabaseHelper;
import MFNetwork.MFDatabase.MFDatabase.MFSQLFinals;
import michl.timeregistration.Database.DBTimeRegData.TRDBTimeDateEventData;

public class TRDBTimeDateEventTable extends MFDBTable {
    public TRDBTimeDateEventTable(MFDatabaseHelper dbHelper) {
        super(dbHelper, "TimeDateEvent", "EventID");
        m_className="TRDBTimeDateEventTable";
        addKey("EventID",MFSQLFinals.KEY_TYPE_PRIMARY_INT);
        addKey("YYYYMMDD",MFSQLFinals.KEY_TYPE_INT);
        addKey("Start",MFSQLFinals.KEY_TYPE_INT);
        addKey("Stop",MFSQLFinals.KEY_TYPE_INT);
        addKey("PauseStart",MFSQLFinals.KEY_TYPE_INT);
        addKey("PauseStop",MFSQLFinals.KEY_TYPE_INT);
        addKey("PauseSpan",MFSQLFinals.KEY_TYPE_INT);
        addKey("UTCStart",MFSQLFinals.KEY_TYPE_INT);
        addKey("UTCStop",MFSQLFinals.KEY_TYPE_INT);
        addKey("UTCPauseStart",MFSQLFinals.KEY_TYPE_INT);
        addKey("UTCPauseStop",MFSQLFinals.KEY_TYPE_INT);
        genCommaSeperatedKeys();
    }

    @Override
    public MFDBData readRowData(ResultSet result) throws SQLException {
        TRDBTimeDateEventData event=new TRDBTimeDateEventData(-1,this);
        event.k_eventID=result.getInt("EventID");
        event.k_YYYYMMDD=result.getInt("YYYYMMDD");
        event.k_start=result.getLong("Start");
        event.k_stop=result.getLong("Stop");
        event.k_pauseStart=result.getInt("PauseStart");
        event.k_pauseStop=result.getInt("PauseStop");
        event.k_pauseSpan=result.getInt("PauseSpan");
        event.k_utcStart=result.getLong("UTCStart");
        event.k_utcStop=result.getLong("UTCStop");
        event.k_utcPauseStart=result.getLong("UTCPauseStart");
        event.k_utcPauseStop=result.getLong("UTCPauseStop");
        event.updateData();
        return event;
    }

    @Override
    public void createReferencesCommands() {

    }
}
