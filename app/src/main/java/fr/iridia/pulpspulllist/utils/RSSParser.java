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
        Feed pulps_feed = new Feed();

        try {
            URL url = new URL("file://" + path);
            RssFeed feed = RssReader.read(url);

            pulps_feed.name = feed.getTitle();
            pulps_feed.link = feed.getLink();
            //pulps_feed.lastEdited = feed.

            ArrayList<RssItem> rssItems = feed.getRssItems();

            for (RssItem rssItem : rssItems) {
                Log.i("RSS Reader", rssItem.getTitle());
                //if (rssItem.get)

                //pulps_feed.addItem(rssItem.getTitle(), rssItem.getContent());

            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        return pulps_feed;
    }
}
