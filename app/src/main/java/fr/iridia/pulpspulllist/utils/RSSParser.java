package fr.iridia.pulpspulllist.utils;

import android.util.Log;

import java.net.URL;
import java.util.ArrayList;

import fr.iridia.pulpspulllist.data.Feed;
import nl.matshofman.saxrssreader.RssFeed;
import nl.matshofman.saxrssreader.RssItem;
import nl.matshofman.saxrssreader.RssReader;

public class RSSParser {

    public static final String TAG = "RSSParser";

    public static Feed getFeed(String path) {
        Feed pfeed = new Feed();

        try {
            URL url = new URL("file://" + path);
            RssFeed feed = RssReader.read(url);

            pfeed.name = feed.getTitle();
            pfeed.link = feed.getLink();
            //pulps_feed.lastEdited = feed.

            ArrayList<RssItem> rssItems = feed.getRssItems();

            for (RssItem rssItem : rssItems)
                pfeed.addItem(rssItem.getTitle(), rssItem.getDescription());

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        return pfeed;
    }
}
