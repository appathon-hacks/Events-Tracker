package ola.com.eventtracker.event;

import java.util.Map;

/**
 * Created by ABHIJEET on 06-02-2015.
 */
public class EventModel {

    private String id;
    private String cid;
    private String title;
    private String description;
    private String location;
    private String timezone;
    private String start;
    private String end;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public EventModel(String id, String cid, String title, String description, String location, String timezone, String start, String end) {
        this.id = id;
        this.cid = cid;
        this.title = title;
        this.description = description;
        this.location = location;
        this.timezone = timezone;
        this.start = start;
        this.end = end;
    }

    public EventModel() {
    }

    public EventModel(String[] eventsProjection){
        this(eventsProjection[0],
                eventsProjection[1],
                eventsProjection[2],
                eventsProjection[3],
                eventsProjection[4],
                eventsProjection[5],
                eventsProjection[6],
                eventsProjection[7]
                );
    }

    public EventModel(Map<String,String> event){
        int i=0;
        this.id = event.get(EventHandler.eventsProjection[i++]);
        this.cid = event.get(EventHandler.eventsProjection[i++]);
        this.title = event.get(EventHandler.eventsProjection[i++]);
        this.description = event.get(EventHandler.eventsProjection[i++]);
        this.location = event.get(EventHandler.eventsProjection[i++]);
        this.timezone = event.get(EventHandler.eventsProjection[i++]);
        this.start = event.get(EventHandler.eventsProjection[i++]);
        this.end = event.get(EventHandler.eventsProjection[i++]);
    }

    @Override
    public String toString() {
        return "EventModel{" +
                "location='" + location + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
