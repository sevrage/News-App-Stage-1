package com.example.csousa.newsfeed;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * An {@link NewsFeedAdapter} knows how to create a list item layout for each newsFeed
 * in the data source (a list of {@link NewsFeedData} objects).
 *
 * These list item layouts will be provided to an adapter view like ListView
 * to be displayed to the user.
 */
public class NewsFeedAdapter extends ArrayAdapter<NewsFeedData> {

    /**
     * Constructs a new {@link NewsFeedAdapter}.
     *
     * @param context of the app
     * @param newsFeeds is the list of newsFeeds, which is the data source of the adapter
     */
    public NewsFeedAdapter(Context context, List<NewsFeedData> newsFeeds) {
        super(context, 0, newsFeeds);
    }

    /**
     * Returns a list item view that displays information about the newsFeed at the given position
     * in the list of newsFeeds.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_news_feed, parent, false);
        }

        // Find the newsfeed at the given position in the list of newsFeeds
        NewsFeedData currentNewsFeed = getItem(position);

        // Set section name value
        TextView sectionNameView = (TextView) listItemView.findViewById(R.id.section_name);
        String sectionName = currentNewsFeed.getSectionName();
        sectionNameView.setText(sectionName);


        // Set news title value
        TextView titleView = (TextView) listItemView.findViewById(R.id.title);
        String title = currentNewsFeed.getWebTitle();
        titleView.setText(title);


        // Process Publication Date to get Date and Time values
        String webPublicationDate = currentNewsFeed.getWebPublicationDate();
        String s = webPublicationDate.replace("Z", "+00:00");
        Date dateObject = null;
        try {
            dateObject = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Set publication date value
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        String formattedDate = formatDate(dateObject);
        dateView.setText(formattedDate);

        // Set publication time value
        TextView timeView = (TextView) listItemView.findViewById(R.id.time);
        String formattedTime = formatTime(dateObject);
        timeView.setText(formattedTime);


        // Set author value
        TextView authorView = (TextView) listItemView.findViewById(R.id.author);
        String author = currentNewsFeed.getContributor();
        authorView.setText(author);


        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }


    /**
     * Return the formatted date string (i.e. "Mar 8, 1999") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
}
