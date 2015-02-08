package ola.com.eventtracker.event;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by ABHIJEET on 06-02-2015.
 */
public class EventHandler {

    Context context;
    Uri eventsUri;

    public EventHandler(Context mContext){
        context = mContext;

        if (android.os.Build.VERSION.SDK_INT <= 7) {

            eventsUri = Uri.parse("content://calendar/events");
        }
        else {

            eventsUri = Uri.parse("content://com.android.calendar/events");
        }
    }

    public static final String[] eventsProjection = {
            CalendarContract.Events._ID,
            CalendarContract.Events.CALENDAR_ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DESCRIPTION,
            CalendarContract.Events.EVENT_LOCATION,
            CalendarContract.Events.EVENT_TIMEZONE,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND,
       //     CalendarContract.Events.EVENT_COLOR,
       //     CalendarContract.Events.ALL_DAY
    };

    public static final int FALSE_ID = 1;



    public boolean hasEventAlarm(Map<String, String> map) {
        try{
            String str = map.get(CalendarContract.Events.HAS_ALARM);
            if(str.equals("1")){
                return true;
            }else{
                return false;
            }
        }catch(Exception e){
            return false;
        }
    }
    public long getEventId(Map<String, String> map) {
        try{
            String str = map.get(CalendarContract.Events._ID);
            long id = Long.valueOf(str);
            return id;
        }catch(Exception e){
            return FALSE_ID;
        }
    }


    public Map<String, String> selectEvent(long event_id) {
        try{
            ContentResolver cr = this.context.getContentResolver();

            int colCount = eventsProjection.length;
            Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, event_id);
            Cursor c = cr.query(uri, eventsProjection, null, null, null);
            //	cursor
            if(c.moveToFirst()){
                Map<String, Integer> indexMap = new HashMap<String, Integer>();
                //	index
                for(int i=0;i<colCount;i++){
                    indexMap.put(eventsProjection[i], c.getColumnIndex(eventsProjection[i]));
                }
                //	cursor item
                do{
                    Map<String, String> map = new HashMap<String, String>();
                    for(int i=0;i<colCount;i++){
                        String str = c.getString((Integer)indexMap.get(eventsProjection[i]));
                        map.put(eventsProjection[i], str);
                    }
                    return map;
                }while(c.moveToNext());
            }else{
                return null;	//	clear
            }
        }catch(Exception e){
            return null;
        }
    }
    public List<Map<String, String>> selectEvents(String timezone, Calendar start, Calendar end) {
        try{
            ContentResolver cr = this.context.getContentResolver();

            int colCount = eventsProjection.length;
            String selection = CalendarContract.Events.DTSTART + ">=? AND " + CalendarContract.Events.DTEND + "<=?";
            String[] selectionArgs = new String[]{
                    String.valueOf(start.getTimeInMillis()), String.valueOf(end.getTimeInMillis())
            };
            Cursor c = cr.query(CalendarContract.Events.CONTENT_URI,
                    eventsProjection, selection, selectionArgs, null);
            //			eventsProjection, null, null, null);
            //	cursor
            List<Map<String, String>> list = new ArrayList<Map<String, String>>();
            if(c.moveToFirst()){
                Map<String, Integer> indexMap = new HashMap<String, Integer>();
                //	index
                for(int i=0;i<colCount;i++){
                    indexMap.put(eventsProjection[i], c.getColumnIndex(eventsProjection[i]));
                }
                //	cursor item
                do{
                    Map<String, String> map = new HashMap<String, String>();
                    for(int i=0;i<colCount;i++){
                        String str = c.getString((Integer)indexMap.get(eventsProjection[i]));
                        map.put(eventsProjection[i], str);
                    }
                    list.add(map);
                }while(c.moveToNext());
                return list;
            }else{
                Toast.makeText(this.context,"No events found",Toast.LENGTH_SHORT).show();
                return null;	//	clear


            }
        }catch(Exception e){
            Toast.makeText(this.context,"Exception "+e.getMessage(),Toast.LENGTH_SHORT).show();
            return null;
        }
    }
    public List<Map<String, String>> selectEvents(String title) {
        try{
            ContentResolver cr = this.context.getContentResolver();

            int colCount = eventsProjection.length;
            String selection = CalendarContract.Events.TITLE + "=?";
            String[] selectionArgs = new String[]{
                    title
            };
            Cursor c = cr.query(CalendarContract.Events.CONTENT_URI,
                    eventsProjection, selection, selectionArgs, null);
            //			eventsProjection, null, null, null);
            //	cursor
            List<Map<String, String>> list = new ArrayList<Map<String, String>>();
            if(c.moveToFirst()){
                Map<String, Integer> indexMap = new HashMap<String, Integer>();
                //	index
                for(int i=0;i<colCount;i++){
                    indexMap.put(eventsProjection[i], c.getColumnIndex(eventsProjection[i]));
                }
                //	cursor item
                do{
                    Map<String, String> map = new HashMap<String, String>();
                    for(int i=0;i<colCount;i++){
                        String str = c.getString((Integer)indexMap.get(eventsProjection[i]));
                        map.put(eventsProjection[i], str);
                    }
                    list.add(map);
                }while(c.moveToNext());
                return list;
            }else{
                return null;	//	clear
            }
        }catch(Exception e){
            return null;
        }
    }
}
