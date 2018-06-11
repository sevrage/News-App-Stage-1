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

import butterknife.BindView;
import butterknife.ButterKnife;

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
    public View getView(int position, View listItemView, ViewGroup parent) {
        // if listItemView is null, then inflate a new list item layout.

        ViewHolder holder;
        if (listItemView != null) {
            holder = (ViewHolder) listItemView.getTag();
        } else {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_news_feed, parent, false);
            holder = new ViewHolder(listItemView);
            listItemView.setTag(holder);
        }

        // Find the newsFeed at the given position in the list of newsFeeds
        NewsFeedData currentNewsFeed = getItem(position);

        // Set section name value
        String sectionName = currentNewsFeed.getSectionName();
        holder.sectionName.setText(sectionName);

        // Set news title value
        String title = currentNewsFeed.getWebTitle();
        holder.title.setText(title);

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
        String formattedDate = formatDate(dateObject);
        holder.date.setText(formattedDate);

        // Set publication time value
        String formattedTime = formatTime(dateObject);
        holder.time.setText(formattedTime);

        // Set author value
        String author = currentNewsFeed.getContributor();
        holder.author.setText(author);

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

    static class ViewHolder {
        @BindView(R.id.section_name) TextView sectionName;
        @BindView(R.id.title) TextView title;
        @BindView(R.id.date) TextView date;
        @BindView(R.id.time) TextView time;
        @BindView(R.id.author) TextView author;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
