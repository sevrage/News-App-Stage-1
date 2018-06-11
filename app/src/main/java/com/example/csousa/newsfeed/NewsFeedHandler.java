package com.example.csousa.newsfeed;

import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class NewsFeedHandler {
    /** Tag for log messages */
    private static final String LOG_TAG = NewsFeedHandler.class.getName();

    private NewsFeedHandler() {
    }

    public static List<NewsFeedData> fetchNewsFeeds(String requestUrl) {
        HttpHandler sh = new HttpHandler();

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = sh.getServiceJSON(requestUrl);

        // Extract relevant fields from the JSON response and create a list of {@link NewsFeedData}s
        List<NewsFeedData> newsFeeds = extractNewsFeedFromJson(jsonResponse);

        // Return the list of {@link NewsFeedData}s
        return newsFeeds;
    }

    /**
     * Return a list of {@link NewsFeedData} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<NewsFeedData> extractNewsFeedFromJson(String newsFeedJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsFeedJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding newsFeeds to
        List<NewsFeedData> newsFeeds = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(newsFeedJSON);
            JSONObject response = baseJsonResponse.getJSONObject("response");

            // Extract the JSONArray associated with the key called "results",
            // which represents a list of features (or newsFeeds).
            JSONArray newsFeedsArray = response.getJSONArray("results");

            // For each newsFeed in the newsFeedsArray, create an {@link NewsFeedData} object
            for (int i = 0; i < newsFeedsArray.length(); i++) {

                // Get a single newsFeed at position i within the list of newsFeeds
                JSONObject currentNewsFeed = newsFeedsArray.getJSONObject(i);

                // Extract the JSONArray associated with the key called "tags",
                // which contains contributor info.
                JSONArray tagsArray = currentNewsFeed.getJSONArray("tags");

                // Extract values for respective keys
                String id = currentNewsFeed.getString("id");
                String type = currentNewsFeed.getString("type");
                String sectionId = currentNewsFeed.getString("sectionId");
                String sectionName = currentNewsFeed.getString("sectionName");
                String webPublicationDate = currentNewsFeed.getString("webPublicationDate");
                String webTitle = currentNewsFeed.getString("webTitle");
                String webUrl = currentNewsFeed.getString("webUrl");

                String author = null;
                if (webTitle.contains("|") == true){
                    //save author in case there is no show-tags=contributor in url
                    author = webTitle.substring((webTitle.lastIndexOf("|") + 1)).trim();

                    //remove author text from title description
                    webTitle = webTitle.substring(0, webTitle.lastIndexOf("|")).trim();
                }

                //if show-tags=contributor is enable and filled, use it to get author name
                if (tagsArray.length() > 0) {
                    JSONObject tags = tagsArray.getJSONObject(0);
                    author = tags.getString("webTitle");
                }

                newsFeeds.add(new NewsFeedData(id, type, sectionId, sectionName,
                        webPublicationDate, webTitle, webUrl, author));

            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the newsFeed JSON results", e);
        }

        // Return the list of newsFeeds
        return newsFeeds;
    }

}
