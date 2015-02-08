package ola.com.eventtracker.event;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import ola.com.eventtracker.R;

/**
 * Created by ABHIJEET on 06-02-2015.
 */
public class EventAdapter extends ArrayAdapter<EventModel> {


    public EventAdapter(Context context, int resource) {
        super(context, resource);
    }

    public EventAdapter(Context context, int resource, EventModel[] objects) {
        super(context, resource, objects);
    }

    public EventAdapter(Context context,List<EventModel> objects) {
        super(context, R.layout.event_row, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        EventModel eventModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.event_row, parent, false);
            viewHolder.date = (TextView) convertView.findViewById(R.id.vote_points);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.tagline = (TextView) convertView.findViewById(R.id.tag_line);
            viewHolder.count= (TextView) convertView.findViewById(R.id.comment_count);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(eventModel.getStart()));
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        // Populate the data into the template view using the data object
        viewHolder.date.setText(mDay+"");
        viewHolder.title.setText(eventModel.getTitle());
        viewHolder.tagline.setText(eventModel.getDescription());
        viewHolder.count.setText(eventModel.getLocation());

        return convertView;
    }

    private static class ViewHolder {
        TextView date;
        TextView title;
        TextView tagline;
        TextView count;
    }


}
