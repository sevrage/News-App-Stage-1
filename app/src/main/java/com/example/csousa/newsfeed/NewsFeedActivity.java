package com.example.csousa.newsfeed;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsFeedActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsFeedData>> {
    /** Tag for log messages */
    private static final String LOG_TAG = NewsFeedActivity.class.getName();

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    private static final String GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search?q=mars&tag=science/space&format=json&show-tags=contributor&order-by=newest&api-key=test";

    /**
     * Constant value for the newsFeed loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int NEWSFEED_LOADER_ID = 1;

    /** Adapter for the list of newsFeeds */
    private NewsFeedAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        // Find a reference to the {@link ListView} in the layout
        ListView newsFeedListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        newsFeedListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of newsFeeds as input
        mAdapter = new NewsFeedAdapter(this, new ArrayList<NewsFeedData>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsFeedListView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected newsFeed.
        newsFeedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current newsFeed that was clicked on
                NewsFeedData currentNewsFeed = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsFeedUri = Uri.parse(currentNewsFeed.getWebUrl());

                // Create a new intent to view the newsFeed URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsFeedUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });



        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(NEWSFEED_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet);
        }
    }

    @Override
    public Loader<List<NewsFeedData>> onCreateLoader(int id, Bundle bundle) {
        // Create a new loader for the given URL
        return new NewsFeedLoader(this, GUARDIAN_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsFeedData>> loader, List<NewsFeedData> newsfeeds) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No news found."
        mEmptyStateTextView.setText(R.string.no_news);

        // Clear the adapter of previous newsFeed data
        mAdapter.clear();

        // If there is a valid list of {@link NewsFeedData}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (newsfeeds != null && !newsfeeds.isEmpty()) {
            mAdapter.addAll(newsfeeds);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsFeedData>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
}
