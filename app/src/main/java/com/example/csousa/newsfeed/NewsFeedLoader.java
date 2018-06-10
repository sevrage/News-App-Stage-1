package com.example.csousa.newsfeed;

import android.content.AsyncTaskLoader;
import android.content.Context;
import java.util.List;

public class NewsFeedLoader extends AsyncTaskLoader<List<NewsFeedData>> {
    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link NewsFeedLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public NewsFeedLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<NewsFeedData> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of news.
        List<NewsFeedData> newsFeeds = NewsFeedHandler.fetchNewsFeeds(mUrl);
        return newsFeeds;
    }
}
