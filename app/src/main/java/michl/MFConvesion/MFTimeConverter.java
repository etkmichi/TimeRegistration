package michl.MFConvesion;

import java.util.Calendar;

public class MFTimeConverter {

    /**
     * Converts an interger value that presents hour and minute to a string with hh:mm
     * Examples: 101 (1h 1min) -> 01:01 ; 1 (1min) -> 00:01 ; 1111555 -> 11115:55
     * @param hhmm
     * @return
     */
    public static String fromIntHHMMToString(int hhmm){
        String ret=""+Integer.toString(hhmm);
        if(ret.length()==1){
            ret="00:0"+ret;
            return ret;
        }
        if(ret.length()==2){
            ret="00:"+ret;
            return ret;
        }
        if(ret.length()==3){
            ret="0"+ret.substring(0,1)+":"+ret.substring(1,3);
            return ret;
        }
        ret=(ret.substring(0,ret.length()-2)+":"+ret.substring(ret.length()-2,ret.length()));
        return ret;
    }

    /**
     * Converts an interger value that presents hour and minute to a string with hh:mm
     * Examples: 101 (1h 1min) -> 01:01 ; 1 (1min) -> 00:01 ; 1111555 -> 11115:55
     * @param YYYYMMDDHHMM date time formated like 201901011201 Which is the 1st Januray 2019 12:01pm
     * @return
     */
    public static String fromLongYYYYMMDDHHMMToHHMMString(Long YYYYMMDDHHMM){
        String ret=""+Long.toString(YYYYMMDDHHMM);
        if(ret.length()<4){
            ret="0000"+ret;
        }
        ret=(ret.substring(ret.length()-4,ret.length()-2)+":"+ret.substring(ret.length()-2,ret.length()));
        return ret;
    }

    public static String fromIntYYYYMMDDToStringYYMMDD(int YYYYMMDD){
        String ret=""+Integer.toString(YYYYMMDD);
        ret=    ret.substring(2,ret.length()-4)+"."+
                ret.substring(ret.length()-4,ret.length()-2)+"."+
                ret.substring(ret.length()-2,ret.length());
        return ret;
    }

    /**
     * Converts to string yyyy.mm.dd
     * @param YYYYMMDD
     * @return
     */
    public static String fromIntYYYYMMDDToString(int YYYYMMDD){
        String ret=""+Integer.toString(YYYYMMDD);
        ret=    ret.substring(0,ret.length()-4)+"."+
                ret.substring(ret.length()-4,ret.length()-2)+"."+
                ret.substring(ret.length()-2,ret.length());
        return ret;
    }

    public static int fromCalendarToYYYYMMDD(Calendar calendar){
        return calendar.get(Calendar.YEAR)*10000+calendar.get(Calendar.MONTH)*100+calendar.get(Calendar.DAY_OF_MONTH);
    }

}
